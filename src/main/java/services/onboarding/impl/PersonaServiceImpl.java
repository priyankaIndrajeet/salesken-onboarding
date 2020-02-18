package services.onboarding.impl;

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
import db.interfaces.PersonaDAO;
import db.interfaces.UserDAO;
import db.postgres.PersonaDAOPG;
import db.postgres.TeamDAOPG;
import db.postgres.UserDAOPG;
import pojos.Persona;
import pojos.PersonaData;
import pojos.PersonaFields;
import pojos.PersonaMetadata;
import pojos.Product;
import pojos.ProductFeature;
import pojos.SaleskenResponse;
import pojos.Team;
import pojos.User;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.PersonaService;

@Path("/persona")
public class PersonaServiceImpl implements PersonaService {

	@Context
	private ContainerRequestContext req;

	/**
	 * Function to view default meta-data of the persona.
	 */
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
					PersonaDAO dao = new PersonaDAOPG();
					PersonaFields personaFields = dao.getPersonaCreationFields(u);
					// PipelineFields pipelineFields = dao.getProductCreationFields(u);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(personaFields);
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

	/**
	 * Function to view persona details by persona ID.
	 */
	@Override
	@GET
	@Path("/view/{personaid}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response view(@PathParam("personaid") Integer personaId) {
		SaleskenResponse response = null;
		try {
			if (personaId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						Persona persona = new PersonaDAOPG().findById(personaId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(persona);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
						OnboardingResponseMessages.PERSONA_ID_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	/**
	 * Function to view all the personas belonging to an organization.
	 */
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

					ArrayList<Persona> persona = new PersonaDAOPG().viewPersonaByUserId(userId);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(persona);
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

	/**
	 * Function to view mapping of the features of product to the persona data.
	 */
	@Override
	@GET
	@Path("/product_features/{personadata_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response productFeatureInProductData(@PathParam("personadata_id") Integer personaDataId) {
		SaleskenResponse response = null;
		try {
			if (personaDataId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						ArrayList<Product> products = new PersonaDAOPG().personaDataProductFeature(personaDataId,
								userId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(products);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
						OnboardingResponseMessages.PERSONA_ID_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	/**
	 * Function to create Persona and set default persona meta-data for the created
	 * persona.
	 */
	@Override
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createPersona(Persona persona) {
		SaleskenResponse response = null;
		try {
			if (persona != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (persona.getName() != null) {

							if (persona.getName().trim().length() > 0) {
								persona = new PersonaDAOPG().createPersona(persona, userId);
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(persona);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_NAME_IS_INVALID,
										OnboardingResponseMessages.PERSONA_NAME_IS_INVALID);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_NAME_IS_NULL,
									OnboardingResponseMessages.PERSONA_NAME_IS_NULL);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_OBJ_IS_NULL,
						OnboardingResponseMessages.PERSONA_OBJ_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	/**
	 * Function to map persona to processes(pipelines).
	 */
	@POST
	@Path("/persona_mapping")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createMapping(Persona persona) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (persona.getId() != null) {
						if (persona.getProcessIds().size() > 0) {
							PersonaDAOPG dao = new PersonaDAOPG();
							Boolean isSuccess = dao.createPersonaMapping(persona);
							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS, isSuccess);
							return Response.status(200).entity(response).build();
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_PIPELINE_ID_PASSED,
									OnboardingResponseMessages.NULL_PIPELINE_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
								OnboardingResponseMessages.PERSONA_ID_IS_NULL);
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

	/**
	 * Function to update Persona name and image.
	 */
	@Override
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updatePersona(Persona persona) {
		SaleskenResponse response = null;
		try {
			if (persona != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (persona.getId() != null) {
							if (persona.getName() != null) {
								if (persona.getName().trim().length() > 0) {
									persona = new PersonaDAOPG().updatePersona(persona, userId);

									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(persona);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_NAME_IS_INVALID,
											OnboardingResponseMessages.PERSONA_NAME_IS_INVALID);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_NAME_IS_NULL,
										OnboardingResponseMessages.PERSONA_NAME_IS_NULL);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
									OnboardingResponseMessages.PERSONA_ID_IS_NULL);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_OBJ_IS_NULL,
						OnboardingResponseMessages.PERSONA_OBJ_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	/**
	 * Function to create new persona meta-data.
	 */
	@Override
	@POST
	@Path("/create_persona_metadata")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createPersonaMetaData(PersonaMetadata personaMetadata) {
		SaleskenResponse response = null;
		try {
			if (personaMetadata != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (personaMetadata.getPersonaId() != null) {
							if (personaMetadata.getKey() != null) {
								if (personaMetadata.getKey().trim().length() > 0) {
									personaMetadata = new PersonaDAOPG().createPersonaMetaData(personaMetadata);

									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(personaMetadata);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_KEY_IS_INVALID,
											OnboardingResponseMessages.PERSONA_KEY_IS_INVALID);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_KEY_IS_NULL,
										OnboardingResponseMessages.PERSONA_KEY_IS_NULL);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
									OnboardingResponseMessages.PERSONA_ID_IS_NULL);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_META_DATA_IS_NULL,
						OnboardingResponseMessages.PERSONA_META_DATA_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	/**
	 * Function to add persona data to corresponding persona meta-data id.
	 */
	@Override
	@POST
	@Path("/create_personadata")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createPersonaData(PersonaData personaData) {
		SaleskenResponse response = null;
		try {
			if (personaData != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (personaData.getPersonaId() != null) {
							if (personaData.getValue() != null) {
								if (personaData.getMetadataId() != null) {
									if (personaData.getValue().trim().length() > 0) {
										personaData = new PersonaDAOPG().createPersonaData(personaData);

										response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
												OnboardingResponseMessages.SUCCESS);
										response.setResponse(personaData);
										return Response.status(200).entity(response).build();
									} else {
										response = new SaleskenResponse(
												OnboardingResponseCodes.PERSONA_VALUE_IS_INVALID,
												OnboardingResponseMessages.PERSONA_VALUE_IS_INVALID);
										return Response.status(200).entity(response).build();
									}
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_KEY_IS_NULL,
											OnboardingResponseMessages.PERSONA_KEY_IS_NULL);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_VALUE_IS_NULL,
										OnboardingResponseMessages.PERSONA_VALUE_IS_NULL);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
									OnboardingResponseMessages.PERSONA_ID_IS_NULL);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_DATA_IS_NULL,
						OnboardingResponseMessages.PERSONA_DATA_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	/**
	 * Function to update persona meta-data key.
	 */
	@Override
	@POST
	@Path("/update_persona_metadata")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updatePersonaMetaData(PersonaMetadata personaMetadata) {
		SaleskenResponse response = null;
		try {
			if (personaMetadata != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (personaMetadata.getId() != null) {
							if (personaMetadata.getKey() != null) {
								if (personaMetadata.getKey().trim().length() > 0) {
									personaMetadata = new PersonaDAOPG().updatePersonaMetaData(personaMetadata);
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(personaMetadata);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_KEY_IS_INVALID,
											OnboardingResponseMessages.PERSONA_KEY_IS_INVALID);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_KEY_IS_NULL,
										OnboardingResponseMessages.PERSONA_KEY_IS_NULL);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_META_DATA_ID_IS_NULL,
									OnboardingResponseMessages.PERSONA_META_DATA_ID_IS_NULL);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_META_DATA_IS_NULL,
						OnboardingResponseMessages.PERSONA_META_DATA_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	/**
	 * Function to update persona data value.
	 */
	@Override
	@POST
	@Path("/update_personadata")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updatePersonaData(PersonaData personaData) {
		SaleskenResponse response = null;
		try {
			if (personaData != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						if (personaData.getId() != null) {
							if (personaData.getValue() != null) {
								if (personaData.getValue().trim().length() > 0) {
									personaData = new PersonaDAOPG().updatePersonaData(personaData);
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(personaData);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_VALUE_IS_INVALID,
											OnboardingResponseMessages.PERSONA_VALUE_IS_INVALID);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_VALUE_IS_NULL,
										OnboardingResponseMessages.PERSONA_VALUE_IS_NULL);
								return Response.status(200).entity(response).build();
							}

						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_DATA_ID_IS_NULL,
									OnboardingResponseMessages.PERSONA_DATA_ID_IS_NULL);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_DATA_IS_NULL,
						OnboardingResponseMessages.PERSONA_DATA_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	/**
	 * Function to delete persona by persona ID.
	 */
	@Override
	@GET
	@Path("/delete/{personaId}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deletePersona(@PathParam("personaId") Integer personaId) {
		SaleskenResponse response = null;
		try {
			if (personaId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						boolean value = new PersonaDAOPG().deletePersona(personaId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(value);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_ID_IS_NULL,
						OnboardingResponseMessages.PERSONA_ID_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}

	}

	/**
	 * Function to delete persona meta-data by persona meta-data ID. And also delete
	 * the respective persona data's.
	 */
	@Override
	@GET
	@Path("/delete_persona_metadata/{personametadataid}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deletePersonaMetaData(@PathParam("personametadataid") Integer personaMetadatId) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (personaMetadatId != null) {
						boolean value = new PersonaDAOPG().deletePersonaMetaData(personaMetadatId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(value);
						return Response.status(200).entity(response).build();
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_META_DATA_ID_IS_NULL,
								OnboardingResponseMessages.PERSONA_META_DATA_ID_IS_NULL);
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

	/**
	 * Function to delete persona data by persona data ID.
	 */
	@Override
	@GET
	@Path("/delete_personadata/{personadataid}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deletePersonaData(@PathParam("personadataid") Integer personadataid) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (personadataid != null) {
						boolean value = new PersonaDAOPG().deletePersonaData(personadataid);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(value);
						return Response.status(200).entity(response).build();
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_DATA_ID_IS_NULL,
								OnboardingResponseMessages.PERSONA_DATA_ID_IS_NULL);
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

	/**
	 * Function to add mapping of the product feature to the persona data.
	 */
	@Override
	@POST
	@Path("/add_product_feature_mapping")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response addProductFeatureInNeedMapping(ArrayList<ProductFeature> productFeatures) {
		SaleskenResponse response = null;
		try {
			if (productFeatures != null && productFeatures.size() > 0) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						boolean isSuccess = new PersonaDAOPG().addProductFeatureInNeedMapping(productFeatures);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(isSuccess);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PERSONA_DATA_IS_NULL,
						OnboardingResponseMessages.PERSONA_DATA_IS_NULL);
				return Response.status(200).entity(response).build();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}
	/**
	 * Function to view all the personas belonging to an organization.
	 */
	@Override
	@GET
	@Path("/metadata/{metadata_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response metadataDataById(@PathParam("metadata_id") Integer metadatId) {
		SaleskenResponse response = null;
		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {

					PersonaMetadata personaMetadata = new PersonaDAOPG().getPersonaByMetadataId(metadatId);
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(personaMetadata);
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
