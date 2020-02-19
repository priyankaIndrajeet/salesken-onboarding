package services.onboarding.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.FileUploadDAO;
import db.interfaces.UserDAO;
import db.postgres.FileUploadDAOPG;
import db.postgres.UserDAOPG;
import pojos.BulkUser;
import pojos.SaleskenResponse;
import pojos.User;
import pojos.ValidateResponse;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.UserService;
import validators.impl.PhoneNumberValidator;

@Path("/user")
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
	@Path("/user_verification")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response userVelidator(BulkUser user) {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(new UserDAOPG().userVerification(user));
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

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createUser(User user) {
		SaleskenResponse response = null;
		try {
			if (user != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (user.getName() != null && user.getName().trim().length() > 0) {
						if (user.getName().trim().length() > 0) {
							if (user.getEmail() != null && user.getEmail().trim().length() > 0) {
								if (user.getMobile() != null) {
									if (!userDAO.isUserAlreadyExistWithEmail(user.getEmail())) {
										if (!userDAO.isUserAlreadyExistWithMobile(user.getMobile())) {
											ValidateResponse phoneNumberValidateResponse = new PhoneNumberValidator()
													.validate(user.getMobile());
											if (phoneNumberValidateResponse.getIsSuccess()) {
												user.setMobile(phoneNumberValidateResponse.getSuccessMessage());
												if (u != null) {
													user = userDAO.createUser(user, userId);
													response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
															OnboardingResponseMessages.SUCCESS);
													response.setResponse(user);
													return Response.status(200).entity(response).build();
												} else {
													response = new SaleskenResponse(
															OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
															OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
													return Response.status(200).entity(response).build();
												}
											} else {
												response = new SaleskenResponse(
														OnboardingResponseCodes.PHONE_NUMBER_VALIDATION_FAILED,
														OnboardingResponseMessages.PHONE_NUMBER_VALIDATION_FAILED);
												response.setResponse(phoneNumberValidateResponse);
												return Response.status(200).entity(response).build();
											}
										} else {
											response = new SaleskenResponse(
													OnboardingResponseCodes.USER_ALREADY_EXIST_REQ_MOBILE,
													OnboardingResponseMessages.USER_ALREADY_EXIST_REQ_MOBILE);
											return Response.status(200).entity(response).build();
										}

									} else {
										response = new SaleskenResponse(
												OnboardingResponseCodes.USER_ALREADY_EXIST_REQ_EMAIL,
												OnboardingResponseMessages.USER_ALREADY_EXIST_REQ_EMAIL);
										return Response.status(200).entity(response).build();
									}
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.NULL_MOBILE_IN_USERCREATION,
											OnboardingResponseMessages.NULL_MOBILE_IN_USERCREATION);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.NULL_EMAIL_IN_USERCREATION,
										OnboardingResponseMessages.NULL_EMAIL_IN_USERCREATION);
								return Response.status(200).entity(response).build();
							}
						} else {

							response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USER_NAME,
									OnboardingResponseMessages.INVALID_USER_NAME);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_NAME,
								OnboardingResponseMessages.NULL_USER_NAME);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_IN_USERCREATION,
						OnboardingResponseMessages.NULL_USER_IN_USERCREATION);
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
	public Response updateUser(User user) {
		SaleskenResponse response = null;
		try {
			if (user != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (u.getId() != null) {
							// user.setId(u.getId());
							if (user.getName() != null && user.getName().trim().length() > 0) {
								user = userDAO.updateUser(user);
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(user);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_NAME,
										OnboardingResponseMessages.NULL_USER_NAME);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_ID,
									OnboardingResponseMessages.NULL_USER_ID);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_IN_USERUPDATION,
						OnboardingResponseMessages.NULL_USER_IN_USERUPDATION);
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
	@Path("/update_profile")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN, Role.SALES_ASSOCIATE, Role.SALES_MANAGER })
	public Response updateUserProfile(User user) {
		SaleskenResponse response = null;
		try {
			if (user != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (u.getId() != null) {
							user.setId(u.getId());
							if (user.getName() != null) {
								user = userDAO.updateUserProfile(user);
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(user);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_NAME,
										OnboardingResponseMessages.NULL_USER_NAME);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_ID,
									OnboardingResponseMessages.NULL_USER_ID);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_IN_USERUPDATION,
						OnboardingResponseMessages.NULL_USER_IN_USERUPDATION);
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
	@Path("/roles")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
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

	@GET
	@Path("/delete/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteUser(@PathParam("user_id") Integer userID) {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (userID != null) {
						userDAO.deleteUser(userID);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
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

	@POST
	@Path("/delete_bulk_user")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteBulkUsers(ArrayList<Integer> userIds) {
		SaleskenResponse response = null;

		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (userIds.size() > 0) {
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS, userDAO.deletBulkUsers(userIds));
						return Response.status(200).entity(response).build();
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERIDS_PASSED,
								OnboardingResponseMessages.NULL_USERIDS_PASSED, true);
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
	@Path("/deactivate/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deactivateUser(@PathParam("user_id") Integer user_id) {
		SaleskenResponse response = null;
		try {

			if (user_id != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);

					if (u != null) {
						userDAO.deactivateUser(user_id);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(true);
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
		return Response.status(200).entity(response).build();
	}

	@POST
	@Path("/v1/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createV1User(User user) {
		SaleskenResponse response = null;
		try {
			if (user != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (user.getName() != null && user.getName().trim().length() > 0) {
						if (user.getName().trim().length() > 0) {
							if (user.getEmail() != null && user.getEmail().trim().length() > 0) {
								if (user.getMobile() != null) {
									if (!userDAO.isUserAlreadyExistWithEmail(user.getEmail())) {
										if (!userDAO.isUserAlreadyExistWithMobile(user.getMobile())) {
											ValidateResponse phoneNumberValidateResponse = new PhoneNumberValidator()
													.validate(user.getMobile());
											if (phoneNumberValidateResponse.getIsSuccess()) {
												user.setMobile(phoneNumberValidateResponse.getSuccessMessage());
												if (u != null) {
													user = userDAO.createV1User(user, userId);
													response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
															OnboardingResponseMessages.SUCCESS);
													response.setResponse(user);
													return Response.status(200).entity(response).build();
												} else {
													response = new SaleskenResponse(
															OnboardingResponseCodes.INVALID_USERID_IN_ONBOARDING,
															OnboardingResponseMessages.INVALID_USERID_IN_ONBOARDING);
													return Response.status(200).entity(response).build();
												}
											} else {
												response = new SaleskenResponse(
														OnboardingResponseCodes.PHONE_NUMBER_VALIDATION_FAILED,
														OnboardingResponseMessages.PHONE_NUMBER_VALIDATION_FAILED);
												response.setResponse(phoneNumberValidateResponse);
												return Response.status(200).entity(response).build();
											}
										} else {
											response = new SaleskenResponse(
													OnboardingResponseCodes.USER_ALREADY_EXIST_REQ_MOBILE,
													OnboardingResponseMessages.USER_ALREADY_EXIST_REQ_MOBILE);
											return Response.status(200).entity(response).build();
										}

									} else {
										response = new SaleskenResponse(
												OnboardingResponseCodes.USER_ALREADY_EXIST_REQ_EMAIL,
												OnboardingResponseMessages.USER_ALREADY_EXIST_REQ_EMAIL);
										return Response.status(200).entity(response).build();
									}
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.NULL_MOBILE_IN_USERCREATION,
											OnboardingResponseMessages.NULL_MOBILE_IN_USERCREATION);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.NULL_EMAIL_IN_USERCREATION,
										OnboardingResponseMessages.NULL_EMAIL_IN_USERCREATION);
								return Response.status(200).entity(response).build();
							}
						} else {

							response = new SaleskenResponse(OnboardingResponseCodes.INVALID_USER_NAME,
									OnboardingResponseMessages.INVALID_USER_NAME);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_NAME,
								OnboardingResponseMessages.NULL_USER_NAME);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_USERID_IN_ONBOARDING,
							OnboardingResponseMessages.NULL_USERID_IN_ONBOARDING);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_IN_USERCREATION,
						OnboardingResponseMessages.NULL_USER_IN_USERCREATION);
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
	@Path("/v1/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updateV1User(User user) throws IOException, InterruptedException {
		SaleskenResponse response = null;
		try {
			if (user != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (u.getId() != null) {
							// user.setId(u.getId());
							if (user.getName() != null && user.getName().trim().length() > 0) {
								user = userDAO.updateV1User(user, userId);
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(user);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_NAME,
										OnboardingResponseMessages.NULL_USER_NAME);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_ID,
									OnboardingResponseMessages.NULL_USER_ID);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_USER_IN_USERUPDATION,
						OnboardingResponseMessages.NULL_USER_IN_USERUPDATION);
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
	@Path("/user_creation_field_data/{designation}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response userCreationFieldsData(@PathParam("designation") String designation) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					User user = userDAO.getUserCreationFieldsData(designation, userId);
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

	@Override
	@GET
	@Path("/user_updation_field_data/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response userUpdationFieldsData(@PathParam("user_id") Integer user_id) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					User user = userDAO.getUserUpdationFieldsData(user_id);
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
}
