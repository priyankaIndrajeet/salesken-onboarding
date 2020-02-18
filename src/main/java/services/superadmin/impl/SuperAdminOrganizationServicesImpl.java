package services.superadmin.impl;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.AssociateResponseCodes;
import constants.AssociateResponseMessages;
import constants.SuperAdminResponseCodes;
import constants.SuperAdminResponseMessages;
import db.interfaces.OrganizationDAO;
import db.postgres.OrganizationDAOPG;
import pojos.Organization;
import pojos.SaleskenResponse;
import services.global.impl.JWTTokenNeeded;
import services.superadmin.SuperAdminOrganizationServices;

@Path("/super_admin")
public class SuperAdminOrganizationServicesImpl implements SuperAdminOrganizationServices {

	@Override
	@POST
	@Path("/create_org")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrganization(Organization organization) {

		SaleskenResponse response = null;
		try {
			if (organization != null) {
				OrganizationDAO dao = new OrganizationDAOPG();
				/*-if (dao.isNullFieldsOrganization(organization)) {
					if (dao.isValidFieldsOrganization(organization)) {
						Organization org = dao.createOrganization(organization);
						response = new SaleskenResponse(SuperAdminResponseCodes.SUCCESS,
								SuperAdminResponseMessages.SUCCESS);
						response.setResponse(org);
					} else {
						response = new SaleskenResponse(SuperAdminResponseCodes.INVALID_PARAMS_IN_CREATE_ORG,
								SuperAdminResponseMessages.INVALID_PARAMS_IN_CREATE_ORG);
					}

				} else {
					response = new SaleskenResponse(SuperAdminResponseCodes.NULL_PARAMS_IN_CREATE_ORG,
							SuperAdminResponseMessages.NULL_PARAMS_IN_CREATE_ORG);
				}*/

			} else {
				response = new SaleskenResponse(SuperAdminResponseCodes.NULL_ORG_OBJECT_IN_CREATE_ORG,
						SuperAdminResponseMessages.NULL_ORG_OBJECT_IN_CREATE_ORG);
			}
		} catch ( Exception e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}

	@Override
	public Response editOrganization(Organization organization) {
		return Response.status(404).entity(null).build();
	}

}
