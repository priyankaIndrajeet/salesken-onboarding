package services.global.impl;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import constants.Role;

import java.lang.reflect.Method;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {
	private final Logger logger = Logger.getLogger(JWTTokenNeededFilter.class.getName());
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		logger.info("#### authorizationHeader : " + authorizationHeader);
		// Check if the HTTP Authorization header is present and formatted correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			logger.severe("#### invalid authorizationHeader : " + authorizationHeader);
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer ".length()).trim();
		try {

			// Validate the token
			PublicKey publicKey = KeySingleton.getInstance().getPublicKey();
			// Jwts.parser().setSigningKey(key).parseClaimsJws(token);

			Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
			Method method = resourceInfo.getResourceMethod();
			if (method != null) {
				// Get allowed permission on method
				JWTTokenNeeded JWTContext = method.getAnnotation(JWTTokenNeeded.class);
				Role[] permissions = JWTContext.Permissions();
				System.out.println("permissions >>>> " + Arrays.asList(permissions));

				if (!Arrays.asList(permissions).contains(Role.NoRights)) {
					// Get Role from jwt
					ArrayList<String> roles = (ArrayList<String>) claims.get("roles");

					boolean isAuthorizedRole = false;

					for (String role : roles) {
						for (Role permission : permissions) {
							if (role.equalsIgnoreCase(permission.name())) {
								isAuthorizedRole = true;

							}
						}
					}
					if (!isAuthorizedRole) {
						throw new Exception("no roles");
					} else {
						requestContext.setProperty("id", claims.getSubject());
					}
				}

			}
			// System.out.println("UserID >>>>>>>>>>> " + claims.getSubject());

			logger.info("#### valid token : " + token);

		} catch (Exception e) {
			// e.printStackTrace();
			logger.severe("#### invalid token : " + token);
			requestContext.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).build());
		}
	}
}