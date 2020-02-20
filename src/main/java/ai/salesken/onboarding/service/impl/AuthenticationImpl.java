/**
 * 
 */
package ai.salesken.onboarding.service.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ai.salesken.onboarding.constants.ResponseCodes;
import ai.salesken.onboarding.constants.ResponseMessages;
import ai.salesken.onboarding.dao.UserDao;
import ai.salesken.onboarding.dao.impl.UserDaoImpl;
import ai.salesken.onboarding.misc.KeySingleton;
import ai.salesken.onboarding.model.SaleskenResponse;
import ai.salesken.onboarding.model.User;
import ai.salesken.onboarding.service.Authentication;
import ai.salesken.onboarding.utils.string.StringUtils;
import io.jsonwebtoken.Jwts;

/**
 * @author Anurag
 *
 */
@Path("/global")

public class AuthenticationImpl implements Authentication {
	@Context
	private ContainerRequestContext req;
	@Inject
	UserDao userDao;

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
				u = userDao.findbyEmail(user.getEmail().toLowerCase());
				if (u != null) {
					// MD5 Check for password
					String password = StringUtils.getMd5(user.getPassword());
					if (u.getPassword().trim().equals(password) || u.getPassword().trim().equals(user.getPassword())) {
						if (!u.getIsDeleted()) {
							if (!u.getIsSuspended()) {
								if (u.getIsVerified()) {
									// License validation is remaining
									if (userDao.isValidLicense(u)) {
										u.setPassword(null);
										Calendar c = Calendar.getInstance();
										c.setTime(new Date());
										c.add(Calendar.YEAR, 100);
										Date expiryDate = c.getTime(); // This is for 30 days
										jws = Jwts.builder().setSubject(u.getId() + "").setExpiration(expiryDate)
												.claim("roles", u.getRoles())
												.signWith(KeySingleton.getInstance().getKey()).compact();
										response = new SaleskenResponse(ResponseCodes.SUCCESS,
												ResponseMessages.SUCCESS);
										response.setResponse(u);

									} else {
										response = new SaleskenResponse(ResponseCodes.LIC_INVALID_IN_AUTH,
												ResponseMessages.LIC_INVALID_IN_AUTH);
									}
								} else {
									response = new SaleskenResponse(ResponseCodes.USER_ISNOT_VERIFIED_WITH_RQUESTED_ID,
											ResponseMessages.USER_ISNOT_VERIFIED_WITH_RQUESTED_ID);
								}

							} else {
								response = new SaleskenResponse(ResponseCodes.USER_SUSPENDED_WITH_RQUESTED_ID,
										ResponseMessages.USER_SUSPENDED_WITH_RQUESTED_ID);
							}

						} else {
							response = new SaleskenResponse(ResponseCodes.USER_DELETED_WITH_RQUESTED_ID,
									ResponseMessages.USER_DELETED_WITH_RQUESTED_ID);
						}

					} else {
						response = new SaleskenResponse(ResponseCodes.WRONG_PASSWORD_IN_AUTH,
								ResponseMessages.WRONG_PASSWORD_IN_AUTH);
					}

				} else {
					response = new SaleskenResponse(ResponseCodes.USERNAME_INVALID_IN_AUTH,
							ResponseMessages.USERNAME_INVALID_IN_AUTH);
				}

			} else {
				response = new SaleskenResponse(ResponseCodes.NULL_VALUES_PASSED_IN_AUTH,
						ResponseMessages.NULL_VALUES_PASSED_IN_AUTH);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(ResponseCodes.PROBLEM_WITH_DB, ResponseMessages.PROBLEM_WITH_DB);
		}

		return Response.status(200).entity(response).header("Authorization", "Bearer " + jws).build();

	}
}
