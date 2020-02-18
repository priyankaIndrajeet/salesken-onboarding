package services.global;

import javax.ws.rs.core.Response;

import pojos.User;

public interface ResetPassword {
	/**
	 * See
	 * <a href= "https://github.com/ISTARSkills/javacore/wiki/ResetPassword">Reset
	 * Password </a>
	 * 
	 * @return
	 */
	public Response resetPassword(User user);
}
