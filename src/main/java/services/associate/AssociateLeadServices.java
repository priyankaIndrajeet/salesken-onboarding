package services.associate;

import javax.ws.rs.core.Response;

import pojos.Lead;

public interface AssociateLeadServices {

	/**
	 * 
	 * See <a href= "https://github.com/ISTARSkills/javacore/wiki/AddLead">Add Lead
	 * </a>
	 * 
	 * @param user
	 * @return
	 */
	public Response addLead( Lead lead);

	/**
	 * See <a href= "https://github.com/ISTARSkills/javacore/wiki/ViewLeads">View
	 * Leads </a>
	 * 
	 * @param user
	 * @return
	 */
	public Response	 viewLead(Integer offset, Integer limit);
	
	
}
