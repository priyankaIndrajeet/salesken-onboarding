package services.global.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import db.interfaces.FileUploadDAO;
import db.postgres.FileUploadDAOPG;
import services.global.GenericFileUpload;

@Path("/file")
public class GenericFileUploadImpl implements GenericFileUpload {
	@Override
	@POST
	@Path("/upload")
	@Produces(MediaType.APPLICATION_JSON)
	public String fileUpload(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		String url = null;
		if (fileDetail != null && uploadedInputStream != null) {
			String fileType = null;
			try {
				String tempPath = "/temp/" + fileDetail.getFileName();
				File file = new File(tempPath);
				fileType = Files.probeContentType(file.toPath());
				Files.deleteIfExists(file.toPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			FileUploadDAO fileUploadDAO = new FileUploadDAOPG();
			url = fileUploadDAO.uploadFile(uploadedInputStream, fileDetail, fileType);
		}
		return url;
	}

}
