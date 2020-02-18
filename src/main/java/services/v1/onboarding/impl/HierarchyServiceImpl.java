package services.v1.onboarding.impl;

import java.sql.SQLException;
import java.util.ArrayList;

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
import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.UserDAO;
import db.postgres.UserDAOPG;
import io.swagger.annotations.Api;
import pojos.Designation;
import pojos.DesignationUser;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.HierarchyService;

@Path("v1/hierarchy")
@Api("/User Service1")

public class HierarchyServiceImpl implements HierarchyService {
	@Context
	private ContainerRequestContext req;

	@GET
	@Path("/managers/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response getManagers(@PathParam("userId") Integer userId) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer uID = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(uID);
				if (u != null) {
					ArrayList<User> managers = userDAO.getManagers(userId);

					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(managers);
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
	@Path("/designations")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response getAllDesignations() {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					ArrayList<Designation> designations = userDAO.getDesignations(userId);

					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(designations);
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
	@Path("/add_designation")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDesignation(Designation designation) {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (designation != null) {
						if (designation.getDesignation() != null && designation.getDesignation().trim().length() > 0) {
							designation = userDAO.addDesignation(designation, userId);

							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							response.setResponse(designation);
							return Response.status(200).entity(response).build();
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
									OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
								OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
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
	@Path("/designation_user/{designation_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response getUserByDesignationWise(@PathParam("designation_id") Integer designationId) {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					ArrayList<User> userList = userDAO.getDesignationsWiseUsers(userId, designationId);

					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(userList);
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
	@Path("/designation_user/{designation_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response getUserDetailByDesignationWise(@PathParam("designation_id") Integer designationId,
			@PathParam("user_id") Integer userID) {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer authuserId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(authuserId);
				if (u != null) {
					User user = userDAO.getDesignationsWiseSingleUser(userID, designationId);

					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(user);
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
	@Path("/delete_owner/{owner_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteOwner(@PathParam("owner_id") Integer ownerId) {
		SaleskenResponse response = null;
		try {
			if (ownerId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						boolean result = userDAO.deleteOwner(ownerId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(result);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_ID,
						OnboardingResponseMessages.NULL_USER_ID);
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
	@Path("/view_all_associate/{userID}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
 	public Response getManagerAssociate(@PathParam("userID") Integer userId) {
		SaleskenResponse response = null;
		try {
			if (userId != null) {
				if (req.getProperty("id") != null) {
					Integer user_Id = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(user_Id);
					if (u != null) {

						ArrayList<DesignationUser> managerAssociates = userDAO
								.getAllAssociateWithManagerByuserId(userId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(managerAssociates);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_MANAGER_ID,
						OnboardingResponseMessages.NULL_MANAGER_ID);
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
	@Path("/assign_owner")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response assignOwner(User user) {
		SaleskenResponse response = null;
		try {
			if (user != null) {
				if (user.getUserIds() != null) {
					if (user.getManagerId() != null) {

						if (req.getProperty("id") != null) {
							Integer userId = Integer.parseInt(req.getProperty("id").toString());
							UserDAO userDAO = new UserDAOPG();
							User u = userDAO.findbyID(userId);
							if (u != null) {
								Boolean res = userDAO.assignOwner(user);
								if (res) {
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS, res);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(
											OnboardingResponseCodes.SELECTED_OWNER_IS_ALREADY_ASSOCIATE_OF_USER,
											OnboardingResponseMessages.SELECTED_OWNER_IS_ALREADY_ASSOCIATE_OF_USER);
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
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_MANAGER_ID,
								OnboardingResponseMessages.NULL_MANAGER_ID);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERIDS,
							OnboardingResponseMessages.NULL_USERIDS);
					return Response.status(200).entity(response).build();
				}
			} else {

				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_IN_ASSIGN_OWNER,
						OnboardingResponseMessages.NULL_USER_IN_ASSIGN_OWNER);
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
	@Path("/update_designation_user")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updateDesignationOfUser(User user) {
		SaleskenResponse response = null;
		try {
			if (user != null) {
				if (user.getUserIds() != null) {
					if (req.getProperty("id") != null) {
						Integer userId = Integer.parseInt(req.getProperty("id").toString());
						UserDAO userDAO = new UserDAOPG();
						User u = userDAO.findbyID(userId);
						if (u != null) {
							Designation res = userDAO.updateDesignationOfUser(user, userId);
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
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERIDS,
							OnboardingResponseMessages.NULL_USERIDS);
					return Response.status(200).entity(response).build();
				}
			} else {

				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_IN_ASSIGN_OWNER,
						OnboardingResponseMessages.NULL_USER_IN_ASSIGN_OWNER);
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
