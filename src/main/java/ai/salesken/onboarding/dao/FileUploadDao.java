package ai.salesken.onboarding.dao;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface FileUploadDao {
	public String uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String fileType);

}
