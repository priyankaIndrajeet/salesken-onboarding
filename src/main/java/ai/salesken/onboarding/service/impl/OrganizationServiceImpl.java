package ai.salesken.onboarding.service.impl;

import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import ai.salesken.onboarding.service.OrganizationService;

 
@Path("/organization")
public class OrganizationServiceImpl implements OrganizationService {
	@Context
	private ContainerRequestContext req;
   

}
