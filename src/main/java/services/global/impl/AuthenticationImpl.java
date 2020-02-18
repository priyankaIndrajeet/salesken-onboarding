/**
 * 
 */
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
import constants.Role;
import db.interfaces.UserDAO;
import db.postgres.UserDAOPG;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import pojos.SaleskenResponse;
import pojos.User;
import pojos.User.UserRoleTypes;
import strings.StringUtils;

/**
 * @author Vaibhav Verma
 * 
 */
@Path("/global")

public class AuthenticationImpl implements services.global.Authentication {
	@Context
	private ContainerRequestContext req;

	@Override
	@POST
	@Path("/authenticate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(User user) {
		SaleskenResponse response = null;
		String jws = null;
		User u = null;
		try {
			if (user.getEmail() != null && user.getPassword() != null) {
				UserDAO dao = new UserDAOPG();
				u = dao.findbyEmail(user.getEmail().toLowerCase());
				if (u != null) {
					// MD5 Check for password
					String password = StringUtils.getMd5(user.getPassword());
					if (u.getPassword().trim().equals(password) || u.getPassword().trim().equals(user.getPassword())) {
						if (!u.getIsDeleted()) {
							if (!u.getIsSuspended()) {
								if (u.getIsVerified()) {
									// License validation is remaining
									if (dao.isValidLicense(u)) {
										u.setPassword(null);
										Calendar c = Calendar.getInstance();
										c.setTime(new Date());
										c.add(Calendar.YEAR, 100);
										Date expiryDate = c.getTime(); // This is for 30 days
										jws = Jwts.builder().setSubject(u.getId() + "").setExpiration(expiryDate)
												.claim("roles", u.getRoles())
												.signWith(KeySingleton.getInstance().getKey()).compact();
										response = new SaleskenResponse(AssociateResponseCodes.SUCCESS,
												AssociateResponseMessages.SUCCESS);
										response.setResponse(u);

									} else {
										response = new SaleskenResponse(AssociateResponseCodes.LIC_INVALID_IN_AUTH,
												AssociateResponseMessages.LIC_INVALID_IN_AUTH);
									}
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
						response = new SaleskenResponse(AssociateResponseCodes.WRONG_PASSWORD_IN_AUTH,
								AssociateResponseMessages.WRONG_PASSWORD_IN_AUTH);
					}

				} else {
					response = new SaleskenResponse(AssociateResponseCodes.USERNAME_INVALID_IN_AUTH,
							AssociateResponseMessages.USERNAME_INVALID_IN_AUTH);
				}

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_VALUES_PASSED_IN_AUTH,
						AssociateResponseMessages.NULL_VALUES_PASSED_IN_AUTH);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}

		return Response.status(200).entity(response).header("Authorization", "Bearer " + jws).build();

	}
}
