package db.postgres;

import java.io.InputStream;
import java.util.UUID;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import db.interfaces.FileUploadDAO;

public class FileUploadDAOPG implements FileUploadDAO {

	@Override
	public String uploadExcelFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail) {
		// FileInputStream is = (FileInputStream) uploadedInputStream;
		UUID filename = UUID.randomUUID();
		Storage storage = StorageOptions.getDefaultInstance().getService();
		String bucketName = "istar-user-images";
		String folderName = "files/";
		BlobId blobId = BlobId.of(bucketName, folderName + filename.toString());
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/vnd.ms-excel").build();
		Blob blob = storage.create(blobInfo, uploadedInputStream);
		Acl acl = storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
		// return blob.getMediaLink();
		return "https://storage.googleapis.com/istar-user-images/files/" + filename.toString();
	}

	@Override
	public String uploadFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail, String fileType) {
		UUID filename = UUID.randomUUID();
		Storage storage = StorageOptions.getDefaultInstance().getService();
		String bucketName = "istar-user-images";
		String folderName = "files/";
		BlobId blobId = BlobId.of(bucketName, folderName + filename);
		BlobInfo blobInfo = null;
		if (fileType != null) {
			blobInfo = BlobInfo.newBuilder(blobId).setContentType(fileType).build();
		} else {
			blobInfo = BlobInfo.newBuilder(blobId).build();
		}
		Blob blob = storage.create(blobInfo, uploadedInputStream);
		Acl acl = storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
		blob.getMediaLink();
		return "https://storage.googleapis.com/istar-user-images/files/" + filename.toString();
	}

	@Override
	public String uploadGenricFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail) {
		Storage storage = StorageOptions.getDefaultInstance().getService();
		String bucketName = "istar-user-images";
		String folderName = "genric/";
		BlobId blobId = BlobId.of(bucketName, folderName + fileDetail.getFileName().toString());
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		Blob blob = storage.create(blobInfo, uploadedInputStream);
		Acl acl = storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
		// return blob.getMediaLink();
		return "https://storage.googleapis.com/istar-user-images/files/" + fileDetail.getFileName().toString();
	}
	
	public String uploadGenricFileWithFolderName(InputStream uploadedInputStream, FormDataContentDisposition fileDetail,String folderName,String fileType) {
		String filename = UUID.randomUUID().toString().replace("-", "");
		Storage storage = StorageOptions.getDefaultInstance().getService();
		String bucketName = "istar-user-images";
		folderName = folderName+"/";
		BlobId blobId = BlobId.of(bucketName, folderName + filename);
		BlobInfo blobInfo = null;
		if (fileType != null) {
			blobInfo = BlobInfo.newBuilder(blobId).setContentType(fileType).build();
		} else {
			blobInfo = BlobInfo.newBuilder(blobId).build();
		}
		Blob blob = storage.create(blobInfo, uploadedInputStream);
		Acl acl = storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
		blob.getMediaLink();
		return "https://storage.googleapis.com/istar-user-images/"+folderName + filename.toString();
	}
	
	
	

}
