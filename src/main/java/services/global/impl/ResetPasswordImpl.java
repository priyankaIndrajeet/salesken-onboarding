package services.global.impl;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import services.global.ResetPassword;
import strings.StringUtils;

@Path("/global")
public class ResetPasswordImpl implements ResetPassword {
	@Context
	private ContainerRequestContext req;

	@Override
	@POST
	@Path("/reset_password")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN, Role.SALES_ASSOCIATE, Role.SALES_MANAGER })
	public Response resetPassword(User user) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				user.setId(userId);
				UserDAO dao = new UserDAOPG();
				User u = dao.findbyID(userId);

				if (u != null) {
					if (user.getPassword() != null) {
						if (user.getNewPassword() != null) {
							u = dao.findbyEmail(u.getEmail());
							// MD5 Check for password

							String dbPassword = u.getPassword();
							String password = StringUtils.getMd5(user.getPassword());
							String newPassoword = StringUtils.getMd5(user.getNewPassword());

							if (password.equalsIgnoreCase(dbPassword)) {
								if (!u.getPassword().equalsIgnoreCase(newPassoword)) {
									u = dao.updatePassword(user);
									response = new SaleskenResponse(AssociateResponseCodes.SUCCESS,
											AssociateResponseMessages.SUCCESS);
									response.setResponse(u);
								} else {
									response = new SaleskenResponse(
											AssociateResponseCodes.SAME_PASSWORD_AS_OLD_IN_RESETPASSWORD,
											AssociateResponseMessages.SAME_PASSWORD_AS_OLD_IN_RESETPASSWORD);
								}
							} else {

								response = new SaleskenResponse(AssociateResponseCodes.WRONG_OLD_PASSWORD,
										AssociateResponseMessages.WRONG_OLD_PASSWORD);
							}
						} else {

							response = new SaleskenResponse(AssociateResponseCodes.NULL_NEW_PASSWORD,
									AssociateResponseMessages.NULL_NEW_PASSWORD);
						}
					} else {
						response = new SaleskenResponse(AssociateResponseCodes.NULL_OLD_PASSWORD,
								AssociateResponseMessages.NULL_OLD_PASSWORD);

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
