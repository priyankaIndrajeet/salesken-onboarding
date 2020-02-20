package ai.salesken.onboarding.service;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface UserService {
  	public Response bulkUploadUser(InputStream uploadedInputStream, FormDataContentDisposition fileDetail);
	 
 	 
 }
