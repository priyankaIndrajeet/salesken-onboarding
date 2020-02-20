package ai.salesken.onboarding.service;

import javax.ws.rs.core.Response;

import ai.salesken.onboarding.model.User;

 
public interface Authentication {
	/**
	 * 
	 * See <a href="https://github.com/ISTARSkills/javacore/wiki/Authetication">Auth
	 * Wiki Entry </a>
	 * 
	 * 
	 * @param user
	 * @return SaleskenResponse
	 * @throws NullPointerException
	 */
	public Response authenticate(User user) throws NullPointerException;

}
