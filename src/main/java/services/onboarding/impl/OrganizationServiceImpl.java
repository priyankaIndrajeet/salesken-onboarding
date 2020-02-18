package services.onboarding.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import javax.ws.rs.core.Response.Status;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.OrganizationDAO;
import db.interfaces.TeamDAO;
import db.interfaces.UserDAO;
import db.postgres.IndustryTypeDAOPG;
import db.postgres.OrganizationDAOPG;
import db.postgres.PincodeDAOPG;
import db.postgres.TeamDAOPG;
import db.postgres.UserDAOPG;
import pojos.Dashboard;
import pojos.IndustryType;
import pojos.Organization;
import pojos.Pincode;
import pojos.SaleskenResponse;
import pojos.Team;
import pojos.User;
import pojos.Users;
import pojos.ValidateResponse;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.OrganizationService;
import validators.impl.PhoneNumberValidator;

@Path("/organization")
public class OrganizationServiceImpl implements OrganizationService {
	@Context
	private ContainerRequestContext req;

	@Override
	@POST
	@Path("v1/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// Add anotation JWTToken
	public Response createOrganization(Organization org) {
		SaleskenResponse response = null;
		try {
			if (org != null) {
				if (org.getName() != null) {
					if (org.getName().trim().length() > 0) {
						OrganizationDAOPG dao = new OrganizationDAOPG();
						org = dao.createOrganization(org);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS, org);
						return Response.status(Status.CREATED).entity(response).build();
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.INVALID_ORG_NAME_PASSED,
								OnboardingResponseMessages.INVALID_ORG_NAME_PASSED);
						return Response.status(Status.BAD_REQUEST).entity(response).build();
					}
				} else {
					// send msg for null name
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_ORG_NAME_PASSED,
							OnboardingResponseMessages.NULL_ORG_NAME_PASSED);
					return Response.status(Status.BAD_REQUEST).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_ORG_OBJ_PASSED,
						OnboardingResponseMessages.NULL_ORG_OBJ_PASSED);
				return Response.status(Status.BAD_REQUEST).entity(response).build();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
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
					OrganizationDAO orgDAO = new OrganizationDAOPG();
					Organization organization = orgDAO.findOrganizationByUserId(userId);
					if (organization != null) {
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(organization);
						return Response.status(200).entity(response).build();

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_ORG_FOR_REQ_USERID,
								OnboardingResponseMessages.NULL_ORG_FOR_REQ_USERID);
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
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response update(Organization org) {
		SaleskenResponse response = null;

		try {
			if (org != null) {
				OrganizationDAO orgDAO = new OrganizationDAOPG();
				Organization organization = orgDAO
						.findOrganizationByUserId(Integer.parseInt(req.getProperty("id").toString()));
				if (org.getId() != null) {
					if (organization.getId().intValue() == org.getId().intValue()) {

						if (org.getName() == null) {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_ORG_NAME_PASSED,
									OnboardingResponseMessages.NULL_ORG_NAME_PASSED);
							return Response.status(200).entity(response).build();
						}
						if (org.getName().trim().length() == 0) {
							response = new SaleskenResponse(OnboardingResponseCodes.INVALID_ORG_NAME_PASSED,
									OnboardingResponseMessages.INVALID_ORG_NAME_PASSED);
							return Response.status(200).entity(response).build();
						}
						if (org.getWebsite().trim().equalsIgnoreCase("https://")) {
							response = new SaleskenResponse(OnboardingResponseCodes.INCORRECT_URL_FOR_ORG_WEBSITE,
									OnboardingResponseMessages.INCORRECT_URL_FOR_ORG_WEBSITE);
							return Response.status(200).entity(response).build();
						}
						if (org.getWebsite().trim().length() == 0) {
							response = new SaleskenResponse(OnboardingResponseCodes.ORG_WEBSITE_IS_NOT_PASSED,
									OnboardingResponseMessages.ORG_WEBSITE_IS_NOT_PASSED);
							return Response.status(200).entity(response).build();
						}
						if (!new OrganizationDAOPG().isValidOrgWebsite(org.getWebsite().trim(), organization.getId())) {
							response = new SaleskenResponse(OnboardingResponseCodes.ORG_WEBSITE_IS_ALREADY_EXIST,
									OnboardingResponseMessages.ORG_WEBSITE_IS_ALREADY_EXIST);
							return Response.status(200).entity(response).build();
						}
						OrganizationDAOPG dao = new OrganizationDAOPG();
						if (org.getBoardlineNumber() != null && !org.getBoardlineNumber().equalsIgnoreCase("")) {
							ValidateResponse validateResponse = new PhoneNumberValidator()
									.validate(org.getBoardlineNumber());
							if (!validateResponse.getIsSuccess()) {
								response = new SaleskenResponse(OnboardingResponseCodes.PHONE_NUMBER_VALIDATION_FAILED,
										OnboardingResponseMessages.PHONE_NUMBER_VALIDATION_FAILED);
								return Response.status(200).entity(response).build();
							} else
								org.setBoardlineNumber(validateResponse.getSuccessMessage());

							org = dao.update(org);
							if (org != null) {
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(org);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.INVALID_ORG_ID_PASSED,
										OnboardingResponseMessages.INVALID_ORG_ID_PASSED);
								return Response.status(200).entity(response).build();
							}
						} else {
							org = dao.update(org);
							if (org != null) {
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(org);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.INVALID_ORG_ID_PASSED,
										OnboardingResponseMessages.INVALID_ORG_ID_PASSED);
								return Response.status(200).entity(response).build();
							}

						}

					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.UNAUTHORIZED_USER,
								OnboardingResponseMessages.UNAUTHORIZED_USER);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_ORG_ID_PASSED,
							OnboardingResponseMessages.NULL_ORG_ID_PASSED);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_ORG_OBJ_PASSED,
						OnboardingResponseMessages.NULL_ORG_OBJ_PASSED);
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
	@Path("/members")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response viewMemebers(@QueryParam("status") String status, @QueryParam("role") String role,
			@QueryParam("search") String search, @QueryParam("limit") String limit,
			@QueryParam("offset") String offset) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					OrganizationDAOPG dao = new OrganizationDAOPG();
					Users users = dao.getMembers(u, status, role, search, limit, offset);
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
	@Path("/view_teams/")
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewTeams() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					OrganizationDAO orgDAO = new OrganizationDAOPG();
					Organization organization = orgDAO.findOrganizationByUserId(u.getId());
					if (organization != null) {
						TeamDAO dao = new TeamDAOPG();
						ArrayList<Team> teams = dao.findbyOrganizationId(organization.getId());
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(teams);
						return Response.status(200).entity(response).build();
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_ORG_FOR_REQ_USERID,
								OnboardingResponseMessages.NULL_ORG_FOR_REQ_USERID);
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
	@Path("/member/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response viewMemeberByUserId(@PathParam("userId") Integer userId) {
		SaleskenResponse response = null;
		try {
			if (userId != null) {
				if (req.getProperty("id") != null) {
					Integer user_Id = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(user_Id);
					if (u != null) {

						User users = userDAO.findUserProfile(userId);
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

	@Override
	@GET
	@Path("/industry_list")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response viewIndustryList() {
		SaleskenResponse response = null;
		try {
			List<IndustryType> teams = new IndustryTypeDAOPG().getIndustryTypes();
			response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS, OnboardingResponseMessages.SUCCESS);
			response.setResponse(teams);
			return Response.status(200).entity(response).build();
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@GET
	@Path("/dashboard_completion")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response dashboardCompletion() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer user_Id = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(user_Id);
				if (u != null) {

					Dashboard dashboard = new OrganizationDAOPG().getDashboardCompletionInfo(user_Id);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(dashboard);
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
	@Path("/location/{pincode}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response LocationFromPin(@PathParam("pincode") Integer pincode) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer user_Id = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(user_Id);
				if (u != null) {
					if (pincode != null) {
						Pincode pinCode = new PincodeDAOPG().findbyPin(pincode);
						if (pinCode != null) {
							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS);
							response.setResponse(pinCode);
							return Response.status(200).entity(response).build();
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.INVALID_PINCODE_IN_ONBOARDING,
									OnboardingResponseMessages.INVALID_PINCODE_IN_ONBOARDING);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_PINCODE_IN_ONBOARDING,
								OnboardingResponseMessages.NULL_PINCODE_IN_ONBOARDING);
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
