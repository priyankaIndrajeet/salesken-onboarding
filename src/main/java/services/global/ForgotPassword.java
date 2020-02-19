package services.global;

import javax.ws.rs.core.Response;

import pojos.User;

public interface ForgotPassword {

	 	
	public Response generatePassword(User user);

}
