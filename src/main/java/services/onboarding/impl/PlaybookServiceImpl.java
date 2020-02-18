package services.onboarding.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.UserDAO;
import db.postgres.PlaybookDAOPG;
import db.postgres.UserDAOPG;
import pojos.AdvancedPlaybook;
import pojos.AdvancedPlaybookNode;
import pojos.Dimension;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.PlaybookService;

@Path("/advanced_playbook")
public class PlaybookServiceImpl implements PlaybookService {

	@Context
	private ContainerRequestContext req;

	@GET
	@Path("/view_stage_task/{stageTaskId}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response viewStageTaskActivity(@PathParam("stageTaskId") Integer stageTaskId) {
		SaleskenResponse response = null;

		try {
			if (stageTaskId != null) {

				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						AdvancedPlaybook advancedPlaybook = new PlaybookDAOPG().getAllActivityofStageTask(stageTaskId);

						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(advancedPlaybook);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_STAGE_TASK_ID_PASSED,
						OnboardingResponseMessages.NULL_STAGE_TASK_ID_PASSED);
				return Response.status(200).entity(response).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@GET
	@Path("/dimensions")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response viewAllDimension() {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					ArrayList<Dimension> taskData = new PlaybookDAOPG().getAllDimension();

					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(taskData);
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

	@GET
	@Path("/view_node/{node_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response viewAllChildsByNodeId(@PathParam("node_id") Integer nodeId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					AdvancedPlaybookNode advancedPlaybookNode = new PlaybookDAOPG().getAllChildByNodeId(nodeId);

					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(advancedPlaybookNode);
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

	@POST
	@Path("/create_node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response createAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybookNode) {
		SaleskenResponse response = null;
		try {
			if (advancedPlaybookNode.getStageTaskId() != null) {
				if (advancedPlaybookNode.getLevel() != null) {
					if (advancedPlaybookNode.getSnippetText().trim() != null) {
						if (req.getProperty("id") != null) {
							Integer userId = Integer.parseInt(req.getProperty("id").toString());
							UserDAO userDAO = new UserDAOPG();
							User u = userDAO.findbyID(userId);
							if (u != null) {

								advancedPlaybookNode = new PlaybookDAOPG()
										.createAdvancedPlaybookNode(advancedPlaybookNode);
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(advancedPlaybookNode);
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
						response = new SaleskenResponse(OnboardingResponseCodes.NODE_VALUE_NULL_PASSED,
								OnboardingResponseMessages.NODE_VALUE_NULL_PASSED);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.STAGE_TASK_LEVEL_IS_NULL,
							OnboardingResponseMessages.STAGE_TASK_LEVEL_IS_NULL);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_STAGE_TASK_ID_PASSED,
						OnboardingResponseMessages.NULL_STAGE_TASK_ID_PASSED);
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
	@Path("/update_node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response updateAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybookNode) {
		SaleskenResponse response = null;
		try {
			if (advancedPlaybookNode.getId() != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						advancedPlaybookNode = new PlaybookDAOPG().updateAdvancedPlaybookNode(advancedPlaybookNode);

						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(advancedPlaybookNode);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_PLAYBOOK_NODE_ID_PASSED,
						OnboardingResponseMessages.NULL_PLAYBOOK_NODE_ID_PASSED);
				return Response.status(200).entity(response).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@GET
	@Path("/delete_node/{nodeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response deleteAdvancedPlaybookNode(@PathParam("nodeId") Integer nodeId) {
		SaleskenResponse response = null;
		try {
			if (nodeId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						Boolean res = new PlaybookDAOPG().deleteAdvancedPlaybookNode(nodeId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(res);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_PLAYBOOK_NODE_ID_PASSED,
						OnboardingResponseMessages.NULL_PLAYBOOK_NODE_ID_PASSED);
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
	@Path("/add_node_child")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response addMappingInAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybook) {
		SaleskenResponse response = null;
		try {
			if (advancedPlaybook.getId() != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						AdvancedPlaybookNode res = new PlaybookDAOPG()
								.addMappingInAdvancedPlaybookNode(advancedPlaybook);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(res);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_PLAYBOOK_NODE_ID_PASSED,
						OnboardingResponseMessages.NULL_PLAYBOOK_NODE_ID_PASSED);
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
	@Path("/update_dimension")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response updateDimension(AdvancedPlaybookNode advancedPlaybook) {
		SaleskenResponse response = null;
		try {
			if (advancedPlaybook.getStageTaskId() != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						Boolean res = new PlaybookDAOPG().updateDimension(advancedPlaybook);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(res);
						return Response.status(Status.OK).entity(response).build();

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
						return Response.status(Status.BAD_REQUEST).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
					return Response.status(Status.BAD_REQUEST).entity(response).build();
				}

			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_STAGE_TASK_ID_PASSED,
						OnboardingResponseMessages.NULL_STAGE_TASK_ID_PASSED);
				return Response.status(Status.BAD_REQUEST).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}

	@GET
	@Path("/delete_stage_playbook/{stageTaskId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response deletePlaybookOfStage(@PathParam("stageTaskId") Integer stageTaskId) {
		SaleskenResponse response = null;
		try {
			if (stageTaskId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						Boolean res = new PlaybookDAOPG().deletePlaybookStageTask(stageTaskId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(res);
						return Response.status(Status.OK).entity(response).build();

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
						return Response.status(Status.BAD_REQUEST).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
					return Response.status(Status.BAD_REQUEST).entity(response).build();
				}

			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_STAGE_TASK_ID_PASSED,
						OnboardingResponseMessages.NULL_STAGE_TASK_ID_PASSED);
				return Response.status(Status.BAD_REQUEST).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}

	@GET
	@Path("/is_salesken_suggest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response isSaleskenSuggest() {
		SaleskenResponse response = null;
		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(true);
					return Response.status(Status.OK).entity(response).build();

				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
					return Response.status(Status.BAD_REQUEST).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
						OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
				return Response.status(Status.BAD_REQUEST).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}

	@POST
	@Path("/add_level")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response addLevel(AdvancedPlaybookNode advancedPlaybookNode) {
		SaleskenResponse response = null;
		try {
			if (advancedPlaybookNode.getStageTaskId() != null) {

				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						Integer level = new PlaybookDAOPG().addLevel(advancedPlaybookNode);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(level);
						return Response.status(Status.OK).entity(response).build();

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
						return Response.status(Status.BAD_REQUEST).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
					return Response.status(Status.BAD_REQUEST).entity(response).build();
				}

			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_STAGE_TASK_ID_PASSED,
						OnboardingResponseMessages.NULL_STAGE_TASK_ID_PASSED);
				return Response.status(Status.BAD_REQUEST).entity(response).build();
			}

		} catch (

		SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}
}
