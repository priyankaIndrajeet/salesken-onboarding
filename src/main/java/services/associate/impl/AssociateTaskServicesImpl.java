package services.associate.impl;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.AssociateResponseCodes;
import constants.AssociateResponseMessages;
import constants.Role;
import db.interfaces.DispositionDAO;
import db.interfaces.TaskDAO;
import db.interfaces.UserDAO;
import db.postgres.DispositionDAOPG;
import db.postgres.TaskDAOPG;
import db.postgres.UserDAOPG;
import pojos.Disposition;
import pojos.Disposition.DispositionType;
import pojos.SaleskenResponse;
import pojos.Task;
import pojos.User;
import services.associate.AssociateTaskServices;
import services.global.impl.JWTTokenNeeded;

@Path("/associate")
public class AssociateTaskServicesImpl implements AssociateTaskServices {
	@Context
	private ContainerRequestContext req;

	@Override
	@GET
	@Path("/incomplete_task/{offset}/{limit}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.SALES_ASSOCIATE })
	public Response incompleteTask(@PathParam("offset") Integer offset, @PathParam("limit") Integer limit) {
		SaleskenResponse response = null;
		if (offset == null) {
			offset = 0;
		}
		if (limit == null) {
			limit = 10;
		}
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO dao = new UserDAOPG();
				User u = dao.findbyID(userId);
				if (u != null) {
					TaskDAO taskdao = new TaskDAOPG();
					List<Task> tasks = taskdao.findIncompleteTaskbyActor(u, offset, limit);
					response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);
					response.setResponse(tasks);
				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_INCOMPLETE_TASK,
							AssociateResponseMessages.INVALID_USERID_INCOMPLTE_TASK);
				}
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_INCOMPLETE_TASK,
						AssociateResponseMessages.NULL_USERID_INCOMPLTE_TASK);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}

	@Override
	@GET
	@Path("/todays_completed_task")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.SALES_ASSOCIATE })
	public Response todayscompletedTask() {

		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO dao = new UserDAOPG();
				User u = dao.findbyID(userId);
				if (u != null) {
					TaskDAO taskDAO = new TaskDAOPG();
					List<Task> todaysCompletedTask = taskDAO.findTodaysCompletedTaskByActor(u);
					response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);
					response.setResponse(todaysCompletedTask);
				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_TODAYCOMPLETED_TASK,
							AssociateResponseMessages.INVALID_USERID_IN_TODAYCOMPLETED_TASK);
				}
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_TODAYCOMPLETED_TASK,
						AssociateResponseMessages.NULL_USERID_IN_TODAYCOMPLETED_TASK);

			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}

	@Override
	@POST
	@Path("/disposition")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.SALES_ASSOCIATE })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response disposition(Disposition disposition) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDao = new UserDAOPG();
				User u = userDao.findbyID(userId);
				if (u != null) {
					if (disposition != null) {
						DispositionDAO dao = new DispositionDAOPG();
						if (disposition.getDispositionType() != null) {

							switch (DispositionType.valueOf(disposition.getDispositionType())) {
							case CallAnswered:
								response = dao.callAnswered(disposition);
								break;
							case NoResponse:
								response = dao.noResponse(disposition);
								break;
							case VoiceMail:
								response = dao.voiceMail(disposition);
								break;
							case Dropped:
								response = dao.dropped(disposition);
								break;
							case WrongNumber:
								response = dao.wrongNumber(disposition);
								break;
							case WrongPerson:
								response = dao.wrongPerson(disposition);
								break;
							case NotDisposed:
								response = dao.notDisposed(disposition);
								break;

							default:
								response = new SaleskenResponse(AssociateResponseCodes.INVALID_DISP_TYPE_IN_DISPOSITION,
										AssociateResponseMessages.INVALID_DISP_TYPE_IN_DISPOSITION);
								break;
							}

						} else {
							response = new SaleskenResponse(AssociateResponseCodes.NULL_DISP_TYPE_IN_DISPOSITION,
									AssociateResponseMessages.NULL_DISP_TYPE_IN_DISPOSITION);
						}
					} else {
						response = new SaleskenResponse(AssociateResponseCodes.NULL_DISP_OBJECT_IN_DISPOSITION,
								AssociateResponseMessages.NULL_DISP_OBJECT_IN_DISPOSITION);
					}
				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_DISPOSITION,
							AssociateResponseMessages.INVALID_USERID_IN_DISPOSITION);
				}
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_DISPOSITION,
						AssociateResponseMessages.NULL_USERID_IN_DISPOSITION);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();

	}

	@Override
	@POST
	@Path("/create_task")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTask(Task task) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					TaskDAO dao = new TaskDAOPG();

					if (dao.isNullFieldsTask(task)) {

						if (dao.isValidFieldsTask(task)) {
							task = dao.createTask(task);
							response = new SaleskenResponse(AssociateResponseCodes.SUCCESS,
									AssociateResponseMessages.SUCCESS);
							response.setResponse(task);

						} else {
							response = new SaleskenResponse(AssociateResponseCodes.INVALID_PARAMS_IN_CREATETASK,
									AssociateResponseMessages.INVALID_PARAMS_IN_CREATETASK);

						}
					} else {
						response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_CREATETASK,
								AssociateResponseMessages.NULL_PARAMS_IN_CREATETASK);
					}

				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_CREATETASK,
							AssociateResponseMessages.INVALID_USERID_IN_CREATETASK);
				}
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_CREATETASK,
						AssociateResponseMessages.NULL_USERID_IN_CREATETASK);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}

	@Override
	@GET
	@Path("/calender_task/{start_date}/{end_date}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.SALES_ASSOCIATE })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTaskBetweenTwoDates(@PathParam("start_date") String startDate,
			@PathParam("end_date") String endDate) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO dao = new UserDAOPG();
				User u = dao.findbyID(userId);

				if (u != null) {
					if (startDate != null && endDate != null) {
						TaskDAO taskDAO = new TaskDAOPG();
						if (taskDAO.isValidDate(startDate)) {
							if (taskDAO.isValidDate(endDate)) {
								List<Task> getTaskBetweenTwoDates = taskDAO.findTasksBetweenDatesByActor(u, startDate,
										endDate);
								response = new SaleskenResponse(AssociateResponseCodes.SUCCESS,
										AssociateResponseMessages.SUCCESS);
								response.setResponse(getTaskBetweenTwoDates);

							} else {
								response = new SaleskenResponse(
										AssociateResponseCodes.INVALID_ENDDATE_IN_GETTASKBETWEENTWODATES,
										AssociateResponseMessages.INVALID_ENDDATE_IN_GETTASKBETWEENTWODATES);

							}
						} else {
							response = new SaleskenResponse(
									AssociateResponseCodes.INVALID_STARTDATE_IN_GETTASKBETWEENTWODATES,
									AssociateResponseMessages.INVALID_STARTDATE_IN_GETTASKBETWEENTWODATES);

						}
					} else {
						response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_GETTASKBETWEENTWODATES,
								AssociateResponseMessages.NULL_PARAMS_IN_GETTASKBETWEENTWODATES);

					}

				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_GETTASKBETWEENTWODATES,
							AssociateResponseMessages.INVALID_USERID_IN_GETTASKBETWEENTWODATES);

				}

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_GETTASKBETWEENTWODATES,
						AssociateResponseMessages.NULL_USERID_IN_GETTASKBETWEENTWODATES);

			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}

}
