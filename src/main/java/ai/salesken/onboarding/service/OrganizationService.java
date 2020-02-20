package ai.salesken.onboarding.service;

import javax.ws.rs.core.Response;

import ai.salesken.onboarding.model.Organization;

public interface OrganizationService {
	public Response createOrganization(Organization organization);
}
