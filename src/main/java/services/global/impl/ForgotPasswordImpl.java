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

	@Override
	@POST
	@Path("/forgot_password")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response forgotPassword(User user) {
		SaleskenResponse response = null;
		String jws = null;
		try {
			if (user.getEmail() != null) {
				UserDAO dao = new UserDAOPG();

				User u = dao.findbyEmail(user.getEmail());
				if (u != null) {
					// MD5 Check for password
					String password = StringUtils.getMd5(u.getPassword());
					if (password != null) {
						if (!u.getIsDeleted()) {
							if (!u.getIsSuspended()) {
								if (u.getIsVerified()) {
									// License validation is remaining
									//if (dao.isValidLicense(u)) {
										u.setPassword(null);
										Calendar c = Calendar.getInstance();
										c.setTime(new Date());
										c.add(Calendar.MINUTE, 30);
										Date expiryDate = c.getTime(); // This is for 30 days
										jws = Jwts.builder().setSubject(u.getId() + "").setExpiration(expiryDate)
												.claim("roles", u.getRoles())
												.signWith(KeySingleton.getInstance().getKey()).compact();
										if (dao.SendVerificationMail(u, jws,"reset")) {
											response = new SaleskenResponse(AssociateResponseCodes.SUCCESS,
													AssociateResponseMessages.SUCCESS);
											response.setResponse(u);
										} else {
											response = new SaleskenResponse(
													AssociateResponseCodes.EMAIL_COULD_NOT_BE_SENT,
													AssociateResponseMessages.EMAIL_COULD_NOT_BE_SENT);
										}
									/*
									 * } else { response = new
									 * SaleskenResponse(AssociateResponseCodes.LIC_INVALID_IN_AUTH,
									 * AssociateResponseMessages.LIC_INVALID_IN_AUTH); }
									 */
								} else {
									response = new SaleskenResponse(
											AssociateResponseCodes.USER_ISNOT_VERIFIED_WITH_RQUESTED_ID,
											AssociateResponseMessages.USER_ISNOT_VERIFIED_WITH_RQUESTED_ID);
								}
							} else {
								response = new SaleskenResponse(AssociateResponseCodes.USER_SUSPENDED_WITH_RQUESTED_ID,
										AssociateResponseMessages.USER_SUSPENDED_WITH_RQUESTED_ID);
							}
						} else {
							response = new SaleskenResponse(AssociateResponseCodes.USER_DELETED_WITH_RQUESTED_ID,
									AssociateResponseMessages.USER_DELETED_WITH_RQUESTED_ID);
						}
					} else {
						response = new SaleskenResponse(AssociateResponseCodes.USER_DELETED_WITH_RQUESTED_ID,
								AssociateResponseMessages.USER_DELETED_WITH_RQUESTED_ID);
					}
				} else {
					response = new SaleskenResponse(AssociateResponseCodes.USER_NOT_FOUND_WITH_REQUESTED_EMAIL,
							AssociateResponseMessages.USER_NOT_FOUND_WITH_REQUESTED_EMAIL);
				}
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.EMAIL_IS_NULL_IN_FORGOT_PASSWORD,
						AssociateResponseMessages.EMAIL_IS_NULL_IN_FORGOT_PASSWORD);

			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}

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
