package services.global.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

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
import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.UserDAO;
import db.postgres.UserDAOPG;
import io.jsonwebtoken.Jwts;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.ForgotPassword;
import strings.StringUtils;
import pojos.User;

@Path("/global")
public class ForgotPasswordImpl implements ForgotPassword {

	

	@Context
	private ContainerRequestContext req;

	@Override
	@POST
	@Path("/generate_password")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN, Role.SALES_ASSOCIATE, Role.SALES_MANAGER })
	public Response generatePassword(User user) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				user.setId(userId);
				UserDAO dao = new UserDAOPG();
				User u = dao.findbyID(userId);

				if (u != null) {
					if (user.getNewPassword() != null) {
						u = dao.findbyEmail(u.getEmail());
						// MD5 Check for password

						String dbPassword = u.getPassword();
						String newpassword = StringUtils.getMd5(user.getNewPassword());
						u = dao.updatePassword(user);
						if(u!=null) {
							response = new SaleskenResponse(AssociateResponseCodes.SUCCESS,
								AssociateResponseMessages.SUCCESS);
							response.setResponse(u);
						}
					} else {

						response = new SaleskenResponse(AssociateResponseCodes.NULL_NEW_PASSWORD,
								AssociateResponseMessages.NULL_NEW_PASSWORD);
					}
				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_RESETPASSWORD,
							AssociateResponseMessages.INVALID_USERID_IN_RESETPASSWORD);
				}

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_RESETPASSWORD,
						AssociateResponseMessages.NULL_USERID_IN_RESETPASSWORD);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}
}
