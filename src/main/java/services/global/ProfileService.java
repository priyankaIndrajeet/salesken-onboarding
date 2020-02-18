package services.global;

import javax.ws.rs.core.Response;

import pojos.User;

public interface ProfileService {

	public Response viewProfile(User user);

}
