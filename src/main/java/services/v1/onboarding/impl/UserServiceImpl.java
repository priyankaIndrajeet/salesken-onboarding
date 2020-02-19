package services.v1.onboarding.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.FileUploadDAO;
import db.interfaces.UserDAO;
import db.postgres.FileUploadDAOPG;
import db.postgres.UserDAOPG;
import io.swagger.annotations.Api;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import pojos.BulkUser;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.impl.JWTTokenNeeded;
import services.v1.onboarding.UserService;

@Path("/user")
@Api("/User Service")
@SwaggerDefinition(basePath = "/salesken-onboarding/api", info = @Info(description = "This application contains all the Salesken Onboarding Wizard Application API's", title = "Salesken Onboarding API", version = ""), consumes = {
		"application/json" }, produces = {
				"application/json" }, externalDocs = @ExternalDocs(value = "Read This For Documentation", url = "https://salesken.atlassian.net/wiki/spaces/TD/pages/128647175/Technical+Spec+-+Onboarding"))
public class UserServiceImpl implements UserService {
	@Context
	private ContainerRequestContext req;

	@POST
	@Path("/bulk_upload")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response bulkUploadUser(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		SaleskenResponse response = null;

		try {
			if (fileDetail != null && uploadedInputStream != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						String fileType = null;
						try {
							String tempPath = "/temp/" + fileDetail.getFileName();
							File file = new File(tempPath);
							fileType = Files.probeContentType(file.toPath());
							Files.deleteIfExists(file.toPath());

						} catch (Exception e) {

						}
						FileUploadDAO fileUploadDAO = new FileUploadDAOPG();
						String url = fileUploadDAO.uploadFile(uploadedInputStream, fileDetail, fileType);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						ArrayList<BulkUser> bulkUsers = new UserDAOPG().getPreviewFromFile(u.getId(), url);
						if (bulkUsers.size() == 0) {
							response.setResponse(true);
						} else {
							response.setResponse(bulkUsers);
						}
						return Response.status(200).entity(response).build();

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_FILE_IN_BULK_UPLOAD,
						OnboardingResponseMessages.NULL_FILE_IN_BULK_UPLOAD);
				return Response.status(200).entity(response).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@POST
	@Path("/bulk_submit")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response bulkUserSubmit(ArrayList<BulkUser> users) {
		SaleskenResponse response = null;
		try {
			if (users != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);

					if (u != null) {
						userDAO.bulkUserCreation(users, userId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
					return Response.status(200).entity(response).build();
				}
			} else {

			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
		return Response.status(200).entity(response).build();
	}

	@Override
	@GET
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllRoles() {
		SaleskenResponse response = null;
		try {
			ArrayList<pojos.Role> roles = new UserDAOPG().getAllRoles();
			response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS, OnboardingResponseMessages.SUCCESS);
			response.setResponse(roles);
			return Response.status(200).entity(response).build();
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

}
