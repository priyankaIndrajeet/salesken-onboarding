package services.android;

import javax.ws.rs.core.Response;

import pojos.User;

public interface AndroidAsscLeadServices {
	public Response viewLeadsByActor(User user);
}
