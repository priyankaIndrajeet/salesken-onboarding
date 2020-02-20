package ai.salesken.onboarding.service.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jvnet.hk2.annotations.Service;

import ai.salesken.onboarding.constants.ResponseCodes;
import ai.salesken.onboarding.constants.ResponseMessages;
import ai.salesken.onboarding.dao.FileUploadDao;
import ai.salesken.onboarding.dao.UserDao;
import ai.salesken.onboarding.model.SaleskenResponse;
import ai.salesken.onboarding.model.User;
import ai.salesken.onboarding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.SwaggerDefinition;

@Path("/user")
@Api("/User Service")
@SwaggerDefinition(externalDocs = @ExternalDocs(value = "Read This For Documentation", url = "https://salesken.atlassian.net/wiki/spaces/TD/pages/128647175/Technical+Spec+-+Onboarding"))
@Service
public class UserServiceImpl implements UserService {
	@Context
	private ContainerRequestContext req;
	@Inject
	UserDao userDao;
	@Inject
	FileUploadDao fileUploadDAO;

	@POST
	@Path("/bulk_upload")
	@Produces(MediaType.APPLICATION_JSON)
	// @JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@ApiOperation(produces = "application/json", value = "Bulk Uplaod", httpMethod = "POST", notes = "<br>This service is used for creating user in bulk")
	@ApiResponses(value = { @ApiResponse(code = 206, message = "Partial Content"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })

	@Override
	public Response bulkUploadUser(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		SaleskenResponse response = null;

		try {
			if (fileDetail != null && uploadedInputStream != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					User u = userDao.findbyID(userId);
					if (u != null) {
						String fileType = null;
						try {
							String tempPath = "/temp/" + fileDetail.getFileName();
							File file = new File(tempPath);
							fileType = Files.probeContentType(file.toPath());
							Files.deleteIfExists(file.toPath());

						} catch (Exception e) {
							e.printStackTrace();
						}

						String url = fileUploadDAO.uploadFile(uploadedInputStream, fileDetail, fileType);
						response = new SaleskenResponse(ResponseCodes.SUCCESS, ResponseMessages.SUCCESS);
						ArrayList<User> bulkUsers = userDao.getPreviewFromFile(u.getId(), url);
						if (bulkUsers.size() == 0) {
							response.setResponse(true);
						} else {
							response.setResponse(bulkUsers);
						}
						return Response.status(200).entity(response).build();

					} else {
						response = new SaleskenResponse(ResponseCodes.INVALID_USERID_IN_ONBOARDING,
								ResponseMessages.INVALID_USERID_IN_ONBOARDING);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(ResponseCodes.NULL_USERID_IN_ONBOARDING,
							ResponseMessages.NULL_USERID_IN_ONBOARDING);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(ResponseCodes.NULL_FILE_IN_BULK_UPLOAD,
						ResponseMessages.NULL_FILE_IN_BULK_UPLOAD);
				return Response.status(200).entity(response).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(ResponseCodes.DB_ERROR, ResponseMessages.DB_EXCEPTION);
			return Response.status(200).entity(response).build();
		}
	}

}
