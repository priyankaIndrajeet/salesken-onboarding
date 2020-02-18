package services.global.impl;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.UserDAO;
import db.postgres.UserDAOPG;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.ProfileService;

@Path("/global")
public class ProfileServiceImpl implements ProfileService {
	@Context
	private ContainerRequestContext req;

	@Override
	@GET
	@Path("/profile")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN, Role.SALES_ASSOCIATE, Role.SALES_MANAGER })
	public Response viewProfile(User user) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();

				User u = userDAO.findUserProfile(userId);
				if (u != null) {
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponseCode(200);
					response.setResponse(u);
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

}
