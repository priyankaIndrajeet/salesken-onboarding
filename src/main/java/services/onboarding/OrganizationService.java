package services.onboarding;

import javax.ws.rs.core.Response;

import pojos.Organization;


public interface OrganizationService {
	/**
	 * 
	 * See <a href="https://github.com/ISTARSkills/javacore/wiki/ViewOrganization">Auth
	 * Wiki Entry </a>
	 * **/
	public Response createOrganization(Organization org);
	public Response view();
	public Response update(Organization org);
	public Response viewTeams();
	public Response viewIndustryList();
	public Response viewMemeberByUserId(Integer userId);
	public Response viewMemebers(String status, String role, String pattern, String limit, String offset);
	public Response dashboardCompletion();
	public Response LocationFromPin(Integer pincode);

}
