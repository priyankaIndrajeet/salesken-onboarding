package services.onboarding.impl;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.UserDAO;
import db.postgres.PersonaDAOPG;
import db.postgres.SimplePlaybookDAOPG;
import db.postgres.UserDAOPG;
import pojos.Persona;
import pojos.SaleskenResponse;
import pojos.SimplePlaybook;
import pojos.SimplePlaybookNode;
import pojos.User;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.SimplePlaybookService;

@Path("/simple_playbook")
public class SimplePlaybookServiceImpl implements SimplePlaybookService {
	@Context
	private ContainerRequestContext req;

	@Override
	@GET
	@Path("/view")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response viewByUserId() {
		SaleskenResponse response = null;
		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					SimplePlaybook simplePlaybook = new SimplePlaybookDAOPG().viewSimplePlaybookByUserId(userId);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(simplePlaybook);
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

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@Override
	@POST
	@Path("/add_node")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response addNodeInSimpleplaybook(SimplePlaybookNode simplePlaybookNode) {
		SaleskenResponse response = null;
		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					SimplePlaybookNode node = new SimplePlaybookDAOPG().addNode(simplePlaybookNode, userId);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(node);
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

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@Override
	@POST
	@Path("/update_node")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updateNodeInSimpleplaybook(SimplePlaybookNode simplePlaybookNode) {
		SaleskenResponse response = null;
		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (simplePlaybookNode != null) {
						if (simplePlaybookNode.getId() != null) {
							if (simplePlaybookNode.getQuestion() != null
									&& simplePlaybookNode.getQuestion().trim().length() > 0) {
								SimplePlaybookNode node = new SimplePlaybookDAOPG().updateNode(simplePlaybookNode,
										userId);
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(node);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
										OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
									OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
						return Response.status(200).entity(response).build();
					}

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

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@Override
	@POST
	@Path("/delete_node/{node_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteNodeInSimpleplaybook(@PathParam("node_id") Integer nodeId) {
		SaleskenResponse response = null;
		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					if (nodeId != null) {

						Boolean isDeleted = new SimplePlaybookDAOPG().deleteNode(nodeId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(isDeleted);
						return Response.status(200).entity(response).build();

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
						return Response.status(200).entity(response).build();
					}

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

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}
}
