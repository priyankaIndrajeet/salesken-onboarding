package services.onboarding;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import pojos.BulkUser;
import pojos.User;

public interface UserService {
	public Response createUser(User user); 
	public Response updateUser(User user);
	public Response bulkUploadUser(InputStream uploadedInputStream, FormDataContentDisposition fileDetail);
	public Response bulkUserSubmit(ArrayList<BulkUser> users);
	public Response getAllRoles();
	public Response deleteUser(Integer userID);
	public Response updateUserProfile(User user);
	public Response deactivateUser(Integer user_id);
	public Response userVelidator(BulkUser user);
	public Response deleteBulkUsers(ArrayList<Integer> userIds);
	public Response createV1User(User user);
	public Response updateV1User(User user) throws IOException, InterruptedException;
	public Response userCreationFieldsData(String designation);
	public Response userUpdationFieldsData(Integer user_id);
 }
