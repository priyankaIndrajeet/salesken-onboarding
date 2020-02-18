package services.associate.impl;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.AssociateResponseCodes;
import constants.AssociateResponseMessages;
import db.interfaces.ProductDAO;
import db.interfaces.UserDAO;
import db.postgres.ProductDAOPG;
import db.postgres.UserDAOPG;
import pojos.Product;
import pojos.SaleskenResponse;
import pojos.User;
import services.associate.AssociateProductServices;
import services.global.impl.JWTTokenNeeded;

@Path("/associate")
public class AssociateProductServicesImpl implements AssociateProductServices {
	@Context
	private ContainerRequestContext req;

	@Override
	@GET
	@Path("/view_product")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response viewProduct() {
		SaleskenResponse response = null;
		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO dao = new UserDAOPG();
				User u = dao.findbyID(userId);
				if (u != null) {
					ProductDAO productDAO = new ProductDAOPG();
					ArrayList<Product> viewProduct = productDAO.viewProducts(u);
					response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);
					response.setResponse(viewProduct);

				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_VIEWPRODUCT,
							AssociateResponseMessages.INVALID_USERID_IN_VIEWPRODUCT);

				}

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_VIEWPRODUCT,
						AssociateResponseMessages.NULL_USERID_IN_VIEWPRODUCT);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}

		return Response.status(200).entity(response).build();
	}

}
