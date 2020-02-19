package services.onboarding.impl;

import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import services.onboarding.OrganizationService;

@Path("/organization")
public class OrganizationServiceImpl implements OrganizationService {
	@Context
	private ContainerRequestContext req;
   

}
