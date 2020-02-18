package services.global.impl;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.AssociateResponseCodes;
import constants.AssociateResponseMessages;
import constants.Role;
import db.interfaces.UserDAO;
import db.postgres.UserDAOPG;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.ProfileUpdate;

@Path("/global")
public class ProfileUpdateImpl implements ProfileUpdate {
	@Context
	private ContainerRequestContext req;
	
	@POST
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN, Role.SALES_ASSOCIATE, Role.SALES_MANAGER })
	@Path("/update_profile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response profileUpdate(User user) {
		SaleskenResponse response = null;
		try {

			if (user != null && req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				user.setId(userId);
				User u = userDAO.updateProfile(user);

				if (u != null) {
					response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);
					response.setResponse(u);
				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_PROFILE_UPDATE,
							AssociateResponseMessages.INVALID_USERID_IN_PROFILE_UPDATE);
				}
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_PROFILE_UPDATE,
						AssociateResponseMessages.NULL_USERID_IN_PROFILE_UPDATE);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();

	}

}
