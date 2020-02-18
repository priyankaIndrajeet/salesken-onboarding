package services.global.impl;

import java.security.PublicKey;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import constants.AssociateResponseCodes;
import constants.AssociateResponseMessages;
import constants.Role;
import db.interfaces.UserDAO;
import db.postgres.UserDAOPG;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.RefreshToken;

@Path("/global")
public class RefreshTokenImpl implements RefreshToken {

	@Context
	private ContainerRequestContext req;

	@Override
	@POST
	@Path("/refresh_token")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.SALES_ASSOCIATE, Role.SALES_MANAGER, Role.IT_ADMIN })
	public Response refreshJwtToken() throws SQLException {
		String jws = null;
		SaleskenResponse saleskenResponse = null;
		String authorizationHeader = req.getHeaderString(HttpHeaders.AUTHORIZATION);
		PublicKey publicKey = KeySingleton.getInstance().getPublicKey();
		// Jwts.parser().setSigningKey(key).parseClaimsJws(token);
		String token = authorizationHeader.substring("Bearer ".length()).trim();
		System.out.println("token >>>>> " + token);

		Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody()
				.setExpiration(new Date(System.currentTimeMillis() - 1000));

		System.out.println(">>>>>>>>>>>>>> " + new Gson().toJson(claims));

		if (req.getProperty("id") != null) {
			Integer userId = Integer.parseInt(req.getProperty("id").toString());

			UserDAO dao = new UserDAOPG();
			User u = dao.findbyID(userId);

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, 30);
			Date expiryDate = c.getTime();
			saleskenResponse = new SaleskenResponse();
			saleskenResponse.setResponseCode(AssociateResponseCodes.SUCCESS);
			saleskenResponse.setResponseMessage(AssociateResponseMessages.SUCCESS);
			jws = Jwts.builder().setSubject(userId + "").setExpiration(expiryDate).claim("roles", u.getRoles())
					.signWith(KeySingleton.getInstance().getKey()).compact();
		}

		return Response.status(200).entity(saleskenResponse).header("Authorization", "Bearer " + jws).build();
	}

}
