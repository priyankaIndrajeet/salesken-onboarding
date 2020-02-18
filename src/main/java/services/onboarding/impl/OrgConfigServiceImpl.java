package services.onboarding.impl;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.OrganizationConfigurationDAO;
import db.interfaces.UserDAO;
import db.postgres.OrganizationConfigurationDAOPG;
import db.postgres.UserDAOPG;
import pojos.OrganizationConfiguration;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.OrgConfigService;
import services.utils.ZohoUtils;
import services.utils.impl.ZohoUtilsImpl;

@Path("/org_config")
public class OrgConfigServiceImpl implements OrgConfigService {

	@Context
	private ContainerRequestContext req;

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createOrganizationConfiguration(OrganizationConfiguration configuration) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				if (configuration != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (configuration.getPropertyName() != null) {
							if (configuration.getPropertyName().trim().length() > 0) {
								if (configuration.getPropertyValue() != null) {
									if (configuration.getPropertyValue().trim().length() > 0) {
										OrganizationConfigurationDAO dao = new OrganizationConfigurationDAOPG();
										configuration = dao.createConfiguration(configuration, userId);
										if (configuration != null) {
											response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
													OnboardingResponseMessages.SUCCESS);
											response.setResponse(configuration);
											return Response.status(200).entity(response).build();
										} else {
											response = new SaleskenResponse(
													OnboardingResponseCodes.ORG_CONFIG_NOT_CREATED,
													OnboardingResponseMessages.ORG_CONFIG_NOT_CREATED);
											return Response.status(200).entity(response).build();
										}
									} else {
										response = new SaleskenResponse(
												OnboardingResponseCodes.INVALID_PROPERTY_VALUE_IN_ORG_CONFIG,
												OnboardingResponseMessages.INVALID_PROPERTY_VALUE_IN_ORG_CONFIG);
										return Response.status(200).entity(response).build();
									}
								} else {
									response = new SaleskenResponse(
											OnboardingResponseCodes.NULL_PROPERTY_VALUE_IN_ORG_CONFIG,
											OnboardingResponseMessages.NULL_PROPERTY_VALUE_IN_ORG_CONFIG);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(
										OnboardingResponseCodes.INVALID_PROPERTY_NAME_IN_ORG_CONFIG,
										OnboardingResponseMessages.INVALID_PROPERTY_NAME_IN_ORG_CONFIG);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_PROPERTY_NAME_IN_ORG_CONFIG,
									OnboardingResponseMessages.NULL_PROPERTY_NAME_IN_ORG_CONFIG);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.ORG_CONFIG_OBJ_NULL,
							OnboardingResponseMessages.ORG_CONFIG_OBJ_NULL);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
						OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
				return Response.status(200).entity(response).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@GET
	@Path("/view")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response viewOrganizationConfiguration() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					OrganizationConfigurationDAO dao = new OrganizationConfigurationDAOPG();
					ArrayList<OrganizationConfiguration> configurations = dao.viewConfiguration(userId);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(configurations);
					return Response.status(200).entity(response).build();

				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
					return Response.status(200).entity(response).build();
				}

			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
						OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
				return Response.status(200).entity(response).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@GET
	@Path("/zohosync_data/{pipeline_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response syncDataFromZoho(@PathParam("pipeline_id") Integer pipelineId) {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer uID = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(uID);
				if (u != null) {
					ZohoUtils zohoUtils = new ZohoUtilsImpl();
					Boolean isSuccess = zohoUtils.syncData(uID, pipelineId);
					if (isSuccess) {
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						return Response.status(200).entity(response).build();
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.ZOHO_TOKEN_INVALID,
								OnboardingResponseMessages.ZOHO_TOKEN_INVALID);
						return Response.status(200).entity(response).build();
					}

				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
						OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

}
