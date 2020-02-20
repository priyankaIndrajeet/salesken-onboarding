package ai.salesken.onboarding.service.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import java.sql.SQLException;
import javax.inject.Inject;
import org.jvnet.hk2.annotations.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ai.salesken.onboarding.constants.ResponseCodes;
import ai.salesken.onboarding.constants.ResponseMessages;
import ai.salesken.onboarding.dao.OrganizationDao;
import ai.salesken.onboarding.enums.Role;
import ai.salesken.onboarding.exception.InvalidFieldsException;
import ai.salesken.onboarding.exception.InvalidPhoneNumberException;
import ai.salesken.onboarding.misc.JWTTokenNeeded;
import ai.salesken.onboarding.model.Organization;
import ai.salesken.onboarding.model.SaleskenResponse;
import ai.salesken.onboarding.model.ValidateResponse;
import ai.salesken.onboarding.service.OrganizationService;
import ai.salesken.onboarding.utils.Validator;

@Path("/v1/onboarding/organizations/")
@Service
@Api(tags = { "Organization" })

public class OrganizationServiceImpl implements OrganizationService {
	@Context
	private ContainerRequestContext req;
	private static final Logger logger = LogManager.getLogger(OrganizationServiceImpl.class);
	@Inject
	OrganizationDao organizationDao;

	@Inject
	Validator Validator;

	@ApiOperation(produces = "application/json", value = "Create Organization", httpMethod = "POST", notes = "<br>This service creates an organization for the requestor")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successful Creation"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	@POST
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response createOrganization(Organization organization) {
		SaleskenResponse response = null;
		SaleskenResponse fieldResponse = null;
		logger.info("Create Organization Request Received for:");
		try {
			if (organization != null) {
				fieldResponse = organizationDao.isValidOrgFields(organization);
				if (fieldResponse.getResponseCode() != null) {
					throw new InvalidFieldsException(
							"FieldsValidationException: " + fieldResponse.getResponseMessage());
				}
				if (organization.getName() != null) {
					if (organization.getName().trim().length() > 0) {
						if (organization.getBoard_number() != null
								&& !organization.getBoard_number().equalsIgnoreCase("")) {
							ValidateResponse validateResponse = Validator
									.PhoneNumberValidator(organization.getBoard_number());
							if (!validateResponse.getIsSuccess()) {
								throw new InvalidPhoneNumberException(
										"PhoneNumberValidationException: Phone number of the organization is not valid. Please correct and submit again.");
							}
						}
						organization = organizationDao.createOrganization(organization);

					} else {
						response = new SaleskenResponse(ResponseCodes.INVALID_PARAMETERS_PASSED,
								ResponseMessages.INVALID_INPUT_ORG_NAME);
						return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
					}

				} else {
					response = new SaleskenResponse(ResponseCodes.NULL_PARAMETERS_PASSED,
							ResponseMessages.NULL_ORG_NAME_PASSED);
					return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(ResponseCodes.NULL_ORG_OBJ_PASSED,
						ResponseMessages.NULL_ORG_OBJ_PASSED);
				return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
			}

		} catch (InvalidFieldsException fe) {
			logger.error("Invalid fields exception for the organization: {}", organization.toString());
			return Response.status(Response.Status.BAD_REQUEST).entity(fieldResponse).build();
		} catch (InvalidPhoneNumberException pe) {
			logger.error("Invalid board line number exception for the organization: {}", organization.toString());
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new SaleskenResponse(ResponseCodes.INVALID_PARAMETERS_PASSED,
							ResponseMessages.INVALID_INPUT_BOARD_LINE_NUMBER))
					.build();
		} catch (SQLException se) {
			logger.error("SQL Exception while creating the organization {} in the DB: {}", organization.toString(),
					se.getErrorCode());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new SaleskenResponse(ResponseCodes.DB_ERROR, ResponseMessages.DB_EXCEPTION)).build();
		} catch (Exception e) {
			logger.error("Unknown Exception while creating the Organization {}", organization.toString());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new SaleskenResponse(ResponseCodes.UNKNOWN_EXCEPTION, ResponseMessages.UNKNOWN_EXCEPTION))
					.build();
		}
		logger.info("Organization Created: {}", organization.toString());
		return Response.status(Response.Status.CREATED)
				.entity(new SaleskenResponse(ResponseCodes.SUCCESS, ResponseMessages.SUCCESS)).build();
	}

}
