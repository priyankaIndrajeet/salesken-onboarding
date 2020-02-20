package ai.salesken.onboarding.service.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import ai.salesken.onboarding.model.SaleskenResponse;
import ai.salesken.onboarding.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Path("/user")
@SwaggerDefinition(host = "localhost:8080", basePath = "/salesken-onboarding/api", info = @Info(description = "This application contains all the Salesken Onboarding Wizard Application API's", title = "Salesken Onboarding API", version = "1.6.0"), consumes = {
		"application/json" }, produces = {
				"application/json" }, externalDocs = @ExternalDocs(value = "Read This For Documentation", url = "https://salesken.atlassian.net/wiki/spaces/TD/pages/128647175/Technical+Spec+-+Onboarding"))
@Api("/User Service")

public class UserServiceImpl implements UserService {
	@Context
	private ContainerRequestContext req;

	@POST
	@Path("/bulk_upload")
	@Produces(MediaType.APPLICATION_JSON)
	// @JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@ApiOperation(produces = "application/json", value = "Create Event", httpMethod = "POST", notes = "<br>This service creates an event for the requestor")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successful operation"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	@Override
	public Response bulkUploadUser(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		SaleskenResponse response = null;

		return Response.status(200).entity(response).build();
	}

}
