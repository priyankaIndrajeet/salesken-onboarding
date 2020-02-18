package services.global;

import javax.ws.rs.core.Response;

import pojos.User;

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
