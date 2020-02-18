package services.onboarding.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import constants.OnboardingResponseCodes;
import constants.OnboardingResponseMessages;
import constants.Role;
import db.interfaces.FileUploadDAO;
import db.interfaces.ProductDAO;
import db.interfaces.UserDAO;
import db.postgres.FileUploadDAOPG;
import db.postgres.IndustryTypeDAOPG;
import db.postgres.ProductDAOPG;
import db.postgres.UserDAOPG;
import pojos.Currency;
import pojos.DataPojo;
import pojos.IndustryType;
import pojos.Product;
import pojos.ProductAsset;
import pojos.ProductData;
import pojos.ProductMetadata;
import pojos.SaleskenResponse;
import pojos.User;
import services.global.impl.JWTTokenNeeded;
import services.onboarding.ProductService;

@Path("/product")
public class ProductServiceImpl implements ProductService {
	@Context
	private ContainerRequestContext req;

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
					ProductDAO productDAO = new ProductDAOPG();
					ArrayList<Product> products = productDAO.viewProducts(u);
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

		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}
	}

	@Override
	@GET
	@Path("/currency")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response currency() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					ProductDAO productDAO = new ProductDAOPG();
					ArrayList<Currency> currencies = productDAO.viewCurrency();
					response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
							OnboardingResponseMessages.SUCCESS);
					response.setResponse(currencies);
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
	@Path("/view/{productId}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response view(@PathParam("productId") Integer productId) {
		SaleskenResponse response = null;
		try {
			if (productId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						ProductDAO productDAO = new ProductDAOPG();
						Product product = productDAO.findById(productId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(product);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_ID_PASSED,
						OnboardingResponseMessages.NULL_PRODUCT_ID_PASSED);
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
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createProduct(Product product) {
		SaleskenResponse response = null;

		try {
			if (product != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				ProductDAO productDAO = new ProductDAOPG();

				if (product.getName() != null && product.getName().trim().length() > 0) {
					if (req.getProperty("id") != null) {

						UserDAO userDAO = new UserDAOPG();
						User u = userDAO.findbyID(userId);

						if (u != null) {

							Product p = productDAO.createProduct(product, userId);
							if (p != null) {
								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(p);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(
										OnboardingResponseCodes.PRODUCT_NOT_CREATED_WITH_REQ_OBJ,
										OnboardingResponseMessages.PRODUCT_NOT_CREATED_WITH_REQ_OBJ);
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
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_NAME_PASSED,
							OnboardingResponseMessages.NULL_PRODUCT_NAME_PASSED);
					return Response.status(200).entity(response).build();
				}

			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PRODUCT_OBJ_NULL_IN_CREATE_PRODUCT,
						OnboardingResponseMessages.PRODUCT_OBJ_NULL_IN_CREATE_PRODUCT);
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
	@Path("/product_mapping")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createMapping(Product product) {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO userDAO = new UserDAOPG();
				User u = userDAO.findbyID(userId);
				if (u != null) {
					if (product.getId() != null) {
						if (product.getProcessIds() != null) {
							ProductDAOPG dao = new ProductDAOPG();
							dao.createProductMapping(product);
							response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
									OnboardingResponseMessages.SUCCESS, true);
							return Response.status(200).entity(response).build();
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.NULL_PIPELINE_ID_PASSED,
									OnboardingResponseMessages.NULL_PIPELINE_ID_PASSED);
							return Response.status(200).entity(response).build();
						}
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_ID_PASSED,
								OnboardingResponseMessages.NULL_PRODUCT_ID_PASSED);
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
	public Response updateProduct(Product product) {
		SaleskenResponse response = null;
		try {
			if (product != null) {
				if (product.getId() != null) {
					if (product.getName() != null && product.getName().trim().length() > 0) {
						if (req.getProperty("id") != null) {
							Integer userId = Integer.parseInt(req.getProperty("id").toString());
							UserDAO userDAO = new UserDAOPG();
							User u = userDAO.findbyID(userId);
							ProductDAO productDAO = new ProductDAOPG();

							if (u != null) {

								Product p = productDAO.updateProduct(product);
								if (p != null) {
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(p);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(
											OnboardingResponseCodes.PRODUCT_NOT_CREATED_WITH_REQ_OBJ,
											OnboardingResponseMessages.PRODUCT_NOT_CREATED_WITH_REQ_OBJ);
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
						response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_NAME_PASSED,
								OnboardingResponseMessages.NULL_PRODUCT_NAME_PASSED);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_ID_PASSED,
							OnboardingResponseMessages.NULL_PRODUCT_ID_PASSED);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PRODUCT_OBJ_NULL_IN_CREATE_PRODUCT,
						OnboardingResponseMessages.PRODUCT_OBJ_NULL_IN_CREATE_PRODUCT);
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
	@Path("/add_asset")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response addProductAsset(ProductAsset productAsset) {
		SaleskenResponse response = null;
		try {
			if (productAsset != null) {
				if (productAsset.getProductId() != null) {
					ProductAsset pAsset = new ProductDAOPG().createProductAsset(productAsset);
					if (pAsset != null) {
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(pAsset);
						return Response.status(200).entity(response).build();
					} else {
						response = new SaleskenResponse(OnboardingResponseCodes.PRODUCT_ASSET_NOT_CREATED,
								OnboardingResponseMessages.PRODUCT_ASSET_NOT_CREATED);
						return Response.status(200).entity(response).build();
					}
				} else {
					response = new SaleskenResponse(OnboardingResponseCodes.PRODUCT_ID_NULL_IN_PRODUCT_ASSET,
							OnboardingResponseMessages.PRODUCT_ID_NULL_IN_PRODUCT_ASSET);
					return Response.status(200).entity(response).build();
				}
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PRODUCT_ASSET_OBJ_NULL_IN_CREATE_PRODUCT_ASSET,
						OnboardingResponseMessages.PRODUCT_ASSET_OBJ_NULL_IN_CREATE_PRODUCT_ASSET);
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
	@Path("/delete_asset/{asset_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteProductAsset(@PathParam("asset_id") Integer productAssetId) {
		SaleskenResponse response = null;
		try {
			if (productAssetId != null) {
				Boolean isDeleted = new ProductDAOPG().deleteProductAsset(productAssetId);
				response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS, OnboardingResponseMessages.SUCCESS);
				response.setResponse(isDeleted);
				return Response.status(200).entity(response).build();
			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.PRODUCT_OBJ_NULL_IN_CREATE_PRODUCT,
						OnboardingResponseMessages.PRODUCT_OBJ_NULL_IN_CREATE_PRODUCT);
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
	@Path("/create_product_metadata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createProductMetadata(ProductMetadata productMetadata) {
		SaleskenResponse response = null;

		try {
			if (productMetadata != null) {

				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);

					if (u != null) {
						if (productMetadata.getKey() != null && productMetadata.getKey().trim().length() > 0) {

							if (productMetadata.getProductId() != null) {

								ProductDAO productDAO = new ProductDAOPG();
								ProductMetadata p = productDAO.createProductMetadata(productMetadata);
								if (p != null) {
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(p);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(
											OnboardingResponseCodes.PRODUCTSPECS_NOT_CREATED_WITH_REQ_OBJ,
											OnboardingResponseMessages.PRODUCTSPECS_NOT_CREATED_WITH_REQ_OBJ);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PRODUCT_ID_NULL_IN_PRODUCTSPECS,
										OnboardingResponseMessages.PRODUCT_ID_NULL_IN_PRODUCTSPECS);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PRODUCTSPECS_KEY_NOT_PASSED,
									OnboardingResponseMessages.PRODUCTSPECS_KEY_NOT_PASSED);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS,
						OnboardingResponseMessages.PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS);
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
	@Path("/create_productdata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response createProductData(ProductData productData) {
		SaleskenResponse response = null;

		try {
			if (productData != null) {

				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);

					if (u != null) {
						if (productData.getValue() != null && productData.getValue().trim().length() > 0) {

							if (productData.getProductId() != null) {

								ProductDAO productDAO = new ProductDAOPG();
								ProductData p = productDAO.createProductData(productData);
								if (p != null) {
									response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
											OnboardingResponseMessages.SUCCESS);
									response.setResponse(p);
									return Response.status(200).entity(response).build();
								} else {
									response = new SaleskenResponse(
											OnboardingResponseCodes.PRODUCTSPECS_NOT_CREATED_WITH_REQ_OBJ,
											OnboardingResponseMessages.PRODUCTSPECS_NOT_CREATED_WITH_REQ_OBJ);
									return Response.status(200).entity(response).build();
								}
							} else {
								response = new SaleskenResponse(OnboardingResponseCodes.PRODUCT_ID_NULL_IN_PRODUCTSPECS,
										OnboardingResponseMessages.PRODUCT_ID_NULL_IN_PRODUCTSPECS);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(OnboardingResponseCodes.PRODUCTSPECS_KEY_NOT_PASSED,
									OnboardingResponseMessages.PRODUCTSPECS_KEY_NOT_PASSED);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS,
						OnboardingResponseMessages.PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS);
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
	@Path("/update_metadata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updateMetadata(ProductMetadata productMetadata) {
		SaleskenResponse response = null;

		try {
			if (productMetadata != null) {

				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);

					if (u != null) {

						if (productMetadata.getId() != null) {
							if (productMetadata.getKey() != null && productMetadata.getKey().trim().length() > 0) {
								ProductDAO productDAO = new ProductDAOPG();
								ProductMetadata p = productDAO.updateProductMetadata(productMetadata);

								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(p);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(
										OnboardingResponseCodes.PRODUCTSPECS_KEY_INVALID_IN_PRODUCTSPECS,
										OnboardingResponseMessages.PRODUCTSPECS_KEY_INVALID_IN_PRODUCTSPECS);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(
									OnboardingResponseCodes.PRODUCTSPECS_ID_NULL_IN_PRODUCTSPECS,
									OnboardingResponseMessages.PRODUCTSPECS_ID_NULL_IN_PRODUCTSPECS);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS,
						OnboardingResponseMessages.PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS);
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
	@Path("/update_product_data")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response updateProductData(ProductData productData) {
		SaleskenResponse response = null;

		try {
			if (productData != null) {

				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);

					if (u != null) {
						if (productData.getId() != null) {
							if (productData.getValue() != null && productData.getValue().trim().length() > 0) {
								ProductDAO productDAO = new ProductDAOPG();
								ProductData p = productDAO.updateProductData(productData);

								response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
										OnboardingResponseMessages.SUCCESS);
								response.setResponse(p);
								return Response.status(200).entity(response).build();
							} else {
								response = new SaleskenResponse(
										OnboardingResponseCodes.PRODUCTSPECS_VALUE_INVALID_IN_PRODUCTSPECS,
										OnboardingResponseMessages.PRODUCTSPECS_VALUE_INVALID_IN_PRODUCTSPECS);
								return Response.status(200).entity(response).build();
							}
						} else {
							response = new SaleskenResponse(
									OnboardingResponseCodes.PRODUCTSPECS_ID_NULL_IN_PRODUCTSPECS,
									OnboardingResponseMessages.PRODUCTSPECS_ID_NULL_IN_PRODUCTSPECS);
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
				response = new SaleskenResponse(OnboardingResponseCodes.PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS,
						OnboardingResponseMessages.PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS);
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
	@Path("/product_metadata/{product_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response productSpecification(@PathParam("product_id") Integer productID) {
		SaleskenResponse response = null;
		try {
			if (productID != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						ProductDAO productDAO = new ProductDAOPG();
						ArrayList<ProductMetadata> productSpecifications = productDAO.productMetadata(productID);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
						response.setResponse(productSpecifications);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_ID_PASSED,
						OnboardingResponseMessages.NULL_PRODUCT_ID_PASSED);
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
	@Path("/delete/{product_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteProduct(@PathParam("product_id") Integer productID) {
		SaleskenResponse response = null;
		try {
			if (productID != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {

						ProductDAO productDAO = new ProductDAOPG();
						boolean result = productDAO.productDeletion(productID);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_ID_PASSED,
						OnboardingResponseMessages.NULL_PRODUCT_ID_PASSED);
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
	@Path("/delete_product_data/{product_data_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteProductData(@PathParam("product_data_id") Integer productDataId) {
		SaleskenResponse response = null;
		try {
			if (productDataId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						ProductDAO productDAO = new ProductDAOPG();
						productDAO.deleteProductData(productDataId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_SPECIFICATION_ID_PASSED,
						OnboardingResponseMessages.NULL_PRODUCT_SPECIFICATION_ID_PASSED);
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
	@Path("/delete_meta_data/{metadata_id}/")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	public Response deleteProductMetadata(@PathParam("metadata_id") Integer productMetaDataId) {
		SaleskenResponse response = null;
		try {
			if (productMetaDataId != null) {
				if (req.getProperty("id") != null) {
					Integer userId = Integer.parseInt(req.getProperty("id").toString());
					UserDAO userDAO = new UserDAOPG();
					User u = userDAO.findbyID(userId);
					if (u != null) {
						ProductDAO productDAO = new ProductDAOPG();
						productDAO.deleteProductMetaData(productMetaDataId);
						response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS,
								OnboardingResponseMessages.SUCCESS);
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
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_PRODUCT_SPECIFICATION_ID_PASSED,
						OnboardingResponseMessages.NULL_PRODUCT_SPECIFICATION_ID_PASSED);
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
	@Path("/upload/product")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.IT_ADMIN })
	@Override
	public Response uploadProductImage(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		// TODO Auto-generated method stub
		SaleskenResponse response = null;
		try {
			if (fileDetail != null && uploadedInputStream != null) {

				String fileType = null;
				try {
					String tempPath = "/temp/" + fileDetail.getFileName();
					File file = new File(tempPath);
					fileType = Files.probeContentType(file.toPath());
					Files.deleteIfExists(file.toPath());
				} catch (Exception e) {
					e.printStackTrace();
				}
				FileUploadDAO fileUploadDAO = new FileUploadDAOPG();

				String url = fileUploadDAO.uploadGenricFileWithFolderName(uploadedInputStream, fileDetail, "product",
						fileType);

				DataPojo dataPojo = new DataPojo(url);

				response = new SaleskenResponse(OnboardingResponseCodes.SUCCESS, OnboardingResponseMessages.SUCCESS);
				response.setResponse(dataPojo);
				return Response.status(200).entity(response).build();

			} else {
				response = new SaleskenResponse(OnboardingResponseCodes.NULL_FILE_IN_BULK_UPLOAD,
						OnboardingResponseMessages.NULL_FILE_IN_BULK_UPLOAD);
				return Response.status(200).entity(response).build();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response = new SaleskenResponse(OnboardingResponseCodes.PROBLEM_WITH_DB,
					OnboardingResponseMessages.PROBLEM_WITH_DB);
			return Response.status(200).entity(response).build();
		}

	}
}