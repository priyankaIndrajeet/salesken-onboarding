package services.global;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface GenericFileUpload {
	public String fileUpload(InputStream uploadedInputStream, FormDataContentDisposition fileDetail);
}
