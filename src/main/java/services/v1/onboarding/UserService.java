package services.v1.onboarding;

import java.io.InputStream;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import pojos.BulkUser;

public interface UserService {
	public Response bulkUploadUser(InputStream uploadedInputStream, FormDataContentDisposition fileDetail);

	public Response bulkUserSubmit(ArrayList<BulkUser> users);

	public Response getAllRoles();
}
