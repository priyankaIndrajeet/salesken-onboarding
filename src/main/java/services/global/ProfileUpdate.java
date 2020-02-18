package services.global;

import javax.ws.rs.core.Response;

import pojos.User;

public interface ProfileUpdate {
	/**
	 * See
	 * <a href= "https://github.com/ISTARSkills/javacore/wiki/ProfileUpdate">Profile
	 * Update </a>
	 * 
	 * @param user
	 * @return
	 */
	public Response profileUpdate(User user);
}
