package services.onboarding;

import javax.ws.rs.core.Response;

import pojos.OrganizationConfiguration;

public interface OrgConfigService {

	public Response createOrganizationConfiguration(OrganizationConfiguration configuration);

	public Response viewOrganizationConfiguration();

	public Response syncDataFromZoho(Integer pipelineId);

}
