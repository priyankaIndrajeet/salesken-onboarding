package services.onboarding.impl;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.TeamDAO;
import db.interfaces.UserDAO;
import db.postgres.TeamDAOPG;
import db.postgres.UserDAOPG;
import pojos.SaleskenResponse;
import pojos.Team;
import pojos.User;
import pojos.Users;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.TeamService;

@Path("/team")
public class TeamServiceImpl implements TeamService {
	@Context
	private ContainerRequestContext req;

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createTeam(Team team) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					if (team != null) {
						if (team.getName() != null) {
							if (team.getName().trim().length() > 0) {
								TeamDAOPG dao = new TeamDAOPG();
								team = dao.createTeam(team, u);
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS, team);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.INVALID_NAME_IN_CREATE_TEAM,
										OnboardingResponseMessages.INVALID_NAME_IN_CREATE_TEAM);
								return Response.status(200).entity(response).build();
							}
						} else {

							response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_NAME_PASSED,
									OnboardingResponseMessages.NULL_TEAM_NAME_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.TEAM_OBJ_NULL_IN_CREATE_TEAM,
								OnboardingResponseMessages.TEAM_OBJ_NULL_IN_CREATE_TEAM);
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
	@Path("/team_mapping")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createMapping(Team team) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (team.getId() != null) {
						if (team.getProcessIds() != null) {
							TeamDAOPG dao = new TeamDAOPG();
							dao.createTeamMapping(team);
							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS, true);
							return Response.status(200).entity(response).build();
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_PIPELINE_ID_PASSED,
									OnboardingResponseMessages.NULL_PIPELINE_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_ID_PASSED,
								OnboardingResponseMessages.NULL_TEAM_ID_PASSED);
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
	@Path("/{teamId}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response viewTeam(@PathParam("teamId") Integer teamId) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (teamId != null) {
						TeamDAO teamDAO = new TeamDAOPG();
						Team team = teamDAO.findbyId(teamId);
						if (team != null) {
							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							response.setResponse(team);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.INVALID_TEAM_ID_PASSED,
									OnboardingResponseMessages.INVALID_TEAM_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_ID_PASSED,
								OnboardingResponseMessages.NULL_TEAM_ID_PASSED);
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
	@Path("/add_member_owner")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response addMembersOwnerInTeam(Team team) {

		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					if (team != null) {
						if (team.getId() != null) {
							TeamDAO teamDAO = new TeamDAOPG();
							team = teamDAO.addMembersOwner(team);
							if (team != null) {
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(team);
								return Response.status(200).entity(response).build();

							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.INVALID_TEAM_ID_PASSED,
										OnboardingResponseMessages.INVALID_TEAM_ID_PASSED);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_ID_PASSED,
									OnboardingResponseMessages.NULL_TEAM_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_OBJ_PASSED,
								OnboardingResponseMessages.NULL_TEAM_OBJ_PASSED);
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
	@Path("/remove_members")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response removeMembersInTeam(Team team) {

		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					if (team != null) {
						if (team.getId() != null) {
							TeamDAO teamDAO = new TeamDAOPG();
							team = teamDAO.removeMembers(team);
							if (team != null) {
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(team);
								return Response.status(200).entity(response).build();

							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.INVALID_TEAM_ID_PASSED,
										OnboardingResponseMessages.INVALID_TEAM_ID_PASSED);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_ID_PASSED,
									OnboardingResponseMessages.NULL_TEAM_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_OBJ_PASSED,
								OnboardingResponseMessages.NULL_TEAM_OBJ_PASSED);
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
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response updateTeam(Team team) {

		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					if (team != null) {
						if (team.getId() != null) {
							if (team.getName() != null) {
								if (team.getName().trim().length() > 0) {
									TeamDAOPG dao = new TeamDAOPG();
									team = dao.updateTeam(team);
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS, team);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.INVALID_NAME_IN_CREATE_TEAM,
											OnboardingResponseMessages.INVALID_NAME_IN_CREATE_TEAM);
									return Response.status(200).entity(response).build();
								}
							} else {

								response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_NAME_PASSED,
										OnboardingResponseMessages.NULL_TEAM_NAME_PASSED);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_ID_PASSED,
									OnboardingResponseMessages.NULL_TEAM_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.TEAM_OBJ_NULL_IN_UPDATE_TEAM,
								OnboardingResponseMessages.TEAM_OBJ_NULL_IN_UPDATE_TEAM);
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

	@GET
	@Path("/delete/{team_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response deleteTeam(@PathParam("team_id") Integer teamID) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (teamID != null) {
						TeamDAO teamDAO = new TeamDAOPG();
						Boolean isDeleted = teamDAO.deleteTeam(teamID);
						if (isDeleted) {
							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							return Response.status(200).entity(response).build();

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.TEAM_IS_NOT_DELETED,
									OnboardingResponseMessages.TEAM_IS_NOT_DELETED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_TEAM_ID_PASSED,
								OnboardingResponseMessages.NULL_TEAM_ID_PASSED);
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
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response getUsersForTeamCreation(@QueryParam("role") String role, @QueryParam("search") String search,
			@QueryParam("limit") String limit, @QueryParam("offset") String offset,@QueryParam("team_id")Integer teamId) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					TeamDAO dao = new TeamDAOPG();

					Users users = dao.getUsersForTeamCreation(u, role, search, limit, offset,teamId);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(users);
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
	@Path("/users_managers")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response getUsersAndManagersForTeamCreation(@QueryParam("role") String role,
			@QueryParam("search") String search) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					TeamDAO dao = new TeamDAOPG();

					Users users = dao.getUsersAndManagersForTeamCreation(218915, role, search);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(users);
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
	@Path("/dummy_team")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response dummyTeamCreation() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					TeamDAO teamDAO = new TeamDAOPG();
					Team teamData = teamDAO.dummyTeamCreation(userId);

					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS, OnboardingResponseMessages.SUCCESS,
							teamData);
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

}
