package services.superadmin;

import javax.ws.rs.core.Response;

import pojos.Organization;

public interface SuperAdminOrganizationServices {
	public Response createOrganization(Organization organization);

	public Response editOrganization(Organization organization);

}
