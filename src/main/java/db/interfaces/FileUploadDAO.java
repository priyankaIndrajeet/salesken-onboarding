package db.interfaces;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface FileUploadDAO {
	public String uploadExcelFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail);

	public String uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String fileType);

	public String uploadGenricFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail);
	
	public String uploadGenricFileWithFolderName(InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String folderName,String fileType);

}
