package services.onboarding.impl;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.PipelineDAO;
import db.interfaces.UserDAO;
import db.postgres.OrganizationDAOPG;
import db.postgres.PipelineDAOPG;
import db.postgres.UserDAOPG;
import pojos.LeadSource;
import pojos.Organization;
import pojos.Pipeline;
import pojos.PipelineFields;
import pojos.PipelineStage;
import pojos.PipelineStageTask;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.PipelineService;

@Path("/pipeline")
@JsonInclude(Include.NON_NULL)
public class PipelineServiceImpl implements PipelineService {

	@Context
	private ContainerRequestContext req;

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response create(Pipeline pipeline) {
		SaleskenResponse response = null;
		try {
			if (pipeline != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						PipelineDAO pipelineDAO = new PipelineDAOPG();
						Organization organization = new OrganizationDAOPG().findOrganizationByUserId(userId);
						pipeline = pipelineDAO.pipelineCreation(pipeline, organization.getId());
						if (pipeline != null) {
							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							response.setResponse(pipeline);
							return Response.status(200).entity(response).build();
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_IS_NOT_CREATED,
									OnboardingResponseMessages.PIPELINE_IS_NOT_CREATED);
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
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_IS_NULL,
						OnboardingResponseMessages.PIPELINE_IS_NULL);
				return Response.status(200).entity(response).build();
			}
		} catch (SQLException e) {

			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}

	}

	@Override
	@GET
	@Path("/view")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })

	public Response view() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					PipelineDAO pipelineDAO = new PipelineDAOPG();
					ArrayList<Pipeline> pipelines = pipelineDAO.getPipelineFromUser(userId);
					if (pipelines != null) {
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(pipelines);
						return Response.status(200).entity(response).build();

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NO_PIPELINES,
								OnboardingResponseMessages.NO_PIPELINES);
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

	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response update(Pipeline pipeline) {
		SaleskenResponse response = null;

		try {
			if (pipeline != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (pipeline.getId() != null) {

							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipeline = pipelineDAO.pipelineUpdation(pipeline);
							if (pipeline != null) {
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS, pipeline);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_IS_NOT_UPDATED,
										OnboardingResponseMessages.PIPELINE_IS_NOT_UPDATED);
								return Response.status(200).entity(response).build();
							}

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
									OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_IS_NULL,
						OnboardingResponseMessages.PIPELINE_IS_NULL);
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
	@Path("/create_stage")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createStage(PipelineStage pipelineStage) {
		SaleskenResponse response = null;
		try {
			if (pipelineStage != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (pipelineStage.getPipelineId() != null) {

							if (pipelineStage.getName() != null) {
								if (pipelineStage.getName().trim().length() > 0) {

									PipelineDAO pipelineStageDAO = new PipelineDAOPG();
									pipelineStage = pipelineStageDAO.createpipelinestage(pipelineStage);
									if (pipelineStage != null) {
										response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
												OnboardingResponseMessages.SUCCESS);
										response.setResponse(pipelineStage);
										return Response.status(200).entity(response).build();
									} else {
										response = new SaleskenResponse(
												OnboardingResponseCodes.PIPELINE_STAGE_NOT_CREATED,
												OnboardingResponseMessages.PIPELINE_STAGE_NOT_CREATED);
										return Response.status(200).entity(response).build();
									}

								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_NAME_INVALID,
											OnboardingResponseMessages.PIPELINE_STAGE_NAME_INVALID);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_NAME_IS_NULL,
										OnboardingResponseMessages.PIPELINE_STAGE_NAME_IS_NULL);
								return Response.status(200).entity(response).build();
							}

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
									OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_IS_NULL,
						OnboardingResponseMessages.PIPELINE_STAGE_IS_NULL);
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
	@Path("/create_stage_task")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createStageTask(PipelineStageTask pipelineStageTask) {
		SaleskenResponse response = null;

		try {
			if (pipelineStageTask != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (pipelineStageTask.getTaskType() != null) {
							if (pipelineStageTask.getStageId() != null) {
								PipelineDAO pipelineDAO = new PipelineDAOPG();
								pipelineStageTask = pipelineDAO.createpipelinestagetask(pipelineStageTask);
								if (pipelineStageTask != null) {
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(pipelineStageTask);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.STAGETASK_NOT_CREATED,
											OnboardingResponseMessages.STAGETASK_NOT_CREATED);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(
										OnboardingResponseCodes.STAGEID_NULL_IN_STAGE_TASK_CREATION,
										OnboardingResponseMessages.STAGEID_NULL_IN_STAGE_TASK_CREATION);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_TASK_TYPE_IS_NULL,
									OnboardingResponseMessages.PIPELINE_STAGE_TASK_TYPE_IS_NULL);
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
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_TASK_IS_NULL,
						OnboardingResponseMessages.PIPELINE_STAGE_TASK_IS_NULL);
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
	@Path("/delete_pipeline/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response pipelinedeletion(@PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;

		try {
			if (pipelineId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						PipelineDAO pipelineDAO = new PipelineDAOPG();
						boolean res = pipelineDAO.pipelinedeletion(pipelineId);

						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS, res);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
						OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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
	@Path("/delete_stage/{stageId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response stageDeletion(@PathParam("stageId") Integer stageId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (stageId != null) {
						PipelineDAO pipelineDAO = new PipelineDAOPG();
						boolean res = pipelineDAO.stagedeletion(stageId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS, res);
						return Response.status(200).entity(response).build();

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_STAGE_ID_IS_NULL);
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

	@POST
	@Path("/delete_task/{taskId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response taskdeletion(@PathParam("taskId") Integer taskId) {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					if (taskId != null) {
						PipelineDAO pipelineDAO = new PipelineDAOPG();
						boolean res = pipelineDAO.taskdeletion(taskId);

						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS, res);
						return Response.status(200).entity(response).build();
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.TASK_ID_IS_NULL,
								OnboardingResponseMessages.TASK_ID_IS_NULL);
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
	@GET
	@Path("/{pipeline_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })

	public Response viewPipeline(@PathParam("pipeline_id") Integer pipelineId) {
		SaleskenResponse response = null;
		try {
			if (pipelineId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						PipelineDAO pipelineDAO = new PipelineDAOPG();
						Pipeline pipeline = pipelineDAO.pipelineDetails(pipelineId);
						if (pipeline != null) {
							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							response.setResponse(pipeline);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.INVALID_PIPELINE_ID_PASSED,
									OnboardingResponseMessages.INVALID_PIPELINE_ID_PASSED);
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
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
						OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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
	@Path("/add_team/{teamId}/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response addTeamPipeline(@PathParam("teamId") Integer teamId, @PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineId != null) {
						if (teamId != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipelineDAO.addTeamPipeline(teamId, pipelineId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_ID_PASSED,
									OnboardingResponseMessages.NULL_TEAM_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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

	@POST
	@Path("/delete_team/{teamId}/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteTeamPipeline(@PathParam("teamId") Integer teamId,
			@PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineId != null) {
						if (teamId != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipelineDAO.deleteTeamPipeline(teamId, pipelineId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_ID_PASSED,
									OnboardingResponseMessages.NULL_TEAM_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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

	@POST
	@Path("/add_product/{productId}/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response addProductPipeline(@PathParam("productId") Integer productId,
			@PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineId != null) {
						if (productId != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipelineDAO.addProductPipeline(productId, pipelineId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_ID_PASSED,
									OnboardingResponseMessages.NULL_PRODUCT_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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

	@POST
	@Path("/delete_product/{productId}/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteProductPipeline(@PathParam("productId") Integer productId,
			@PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineId != null) {
						if (productId != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipelineDAO.deleteProductPipeline(productId, pipelineId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_ID_PASSED,
									OnboardingResponseMessages.NULL_PRODUCT_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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

	@POST
	@Path("/add_persona/{personaId}/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response addPersonaPipeline(@PathParam("personaId") Integer personaId,
			@PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineId != null) {
						if (personaId != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipelineDAO.addPersonaPipeline(personaId, pipelineId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
									OnboardingResponseMessages.PERSONA_ID_IS_NULL);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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

	@POST
	@Path("/delete_persona/{personaId}/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deletePersonaPipeline(@PathParam("personaId") Integer personaId,
			@PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineId != null) {
						if (personaId != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipelineDAO.deletePersonaPipeline(personaId, pipelineId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
									OnboardingResponseMessages.PERSONA_ID_IS_NULL);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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

	@POST
	@Path("/update_stage")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updateStage(PipelineStage pipelineStage) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineStage.getPipelineId() != null) {
						if (pipelineStage.getId() != null) {
							if (pipelineStage.getName() != null) {
								if (pipelineStage.getName().trim().length() > 0) {

									PipelineDAO pipelineDAO = new PipelineDAOPG();
									pipelineDAO.updatePipelineStage(pipelineStage);
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(pipelineStage);
									return Response.status(200).entity(response).build();

								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_NAME_INVALID,
											OnboardingResponseMessages.PIPELINE_STAGE_NAME_INVALID);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_NAME_IS_NULL,
										OnboardingResponseMessages.PIPELINE_STAGE_NAME_IS_NULL);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_ID_IS_NULL,
									OnboardingResponseMessages.PIPELINE_STAGE_ID_IS_NULL);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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

	@POST
	@Path("/update_stage_task")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updateStageTask(PipelineStageTask pipelineStageTask) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineStageTask.getId() != null) {
						if (pipelineStageTask.getTaskType() != null) {
							if (pipelineStageTask.getName().trim().length() > 0) {
								PipelineDAO pipelineDAO = new PipelineDAOPG();
								pipelineDAO.updatePipelineStageTask(pipelineStageTask);
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(pipelineStageTask);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(
										OnboardingResponseCodes.PIPELINE_STAGE_TASK_NAME_IS_INVALID,
										OnboardingResponseMessages.PIPELINE_STAGE_TASK_NAME_IS_INVALID);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_TASK_TYPE_IS_NULL,
									OnboardingResponseMessages.PIPELINE_STAGE_TASK_TYPE_IS_NULL);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_TASK_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_STAGE_TASK_ID_IS_NULL);
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
	@GET
	@Path("/creation_fields")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response creationFields() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					PipelineDAO pipelineDAO = new PipelineDAOPG();
					PipelineFields pipelineFields = pipelineDAO.getPipelineCreationFields(u);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(pipelineFields);
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
	@GET
	@Path("/lead_source")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response viewLeadSource() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					PipelineDAO pipelineDAO = new PipelineDAOPG();
					ArrayList<LeadSource> leadSources = pipelineDAO.viewAllLeadSource(u);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(leadSources);
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
	@GET
	@Path("/pipeline_stage/{stage_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response viewPipelineStageByid(@PathParam("stage_id") Integer stageId) {
		SaleskenResponse response = null;
		try {
			if (stageId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						PipelineDAO pipelineDAO = new PipelineDAOPG();
						PipelineStage pipelineStage = pipelineDAO.viewPipelineStage(stageId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(pipelineStage);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_STAGE_ID_IS_NULL,
						OnboardingResponseMessages.PIPELINE_STAGE_ID_IS_NULL);
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
	@Path("/update_order")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updateOrderPipelineStages(Pipeline pipeline) {
		SaleskenResponse response = null;

		try {
			if (pipeline != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (pipeline.getId() != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipeline = pipelineDAO.pipelineStageOrderUpdation(pipeline);
							if (pipeline != null) {
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS, pipeline);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_IS_NOT_UPDATED,
										OnboardingResponseMessages.PIPELINE_IS_NOT_UPDATED);
								return Response.status(200).entity(response).build();
							}

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
									OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_IS_NULL,
						OnboardingResponseMessages.PIPELINE_IS_NULL);
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
	@Path("/add_leadsource/{leadSourceId}/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response addLeadSourcePipeline(@PathParam("leadSourceId") Integer leadSourceID,
			@PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineId != null) {
						if (leadSourceID != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipelineDAO.addLeadSourcePipeline(leadSourceID, pipelineId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_LEAD_SOURCE_ID_PASSED,
									OnboardingResponseMessages.NULL_LEAD_SOURCE_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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

	@POST
	@Path("/delete_leadsource/{leadSourceId}/{pipelineId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteLeadSourcePipeline(@PathParam("leadSourceId") Integer leadSourceId,
			@PathParam("pipelineId") Integer pipelineId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (pipelineId != null) {
						if (leadSourceId != null) {
							PipelineDAO pipelineDAO = new PipelineDAOPG();
							pipelineDAO.deleteLeadSourcePipeline(leadSourceId, pipelineId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_LEAD_SOURCE_ID_PASSED,
									OnboardingResponseMessages.NULL_LEAD_SOURCE_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PIPELINE_ID_IS_NULL,
								OnboardingResponseMessages.PIPELINE_ID_IS_NULL);
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
