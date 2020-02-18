package services.global;

import javax.ws.rs.core.Response;

import pojos.User;

public interface ForgotPassword {

	/**
	 * See
	 * <a href= "https://github.com/ISTARSkills/javacore/wiki/ForgotPassword">Forgot
	 * Password </a>
	 * 
	 * @param email
	 * @return
	 */
	public Response forgotPassword(User user);
	
	public Response generatePassword(User user);

}
