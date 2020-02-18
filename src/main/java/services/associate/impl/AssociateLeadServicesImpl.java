package services.associate.impl;

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

import constants.AssociateResponseCodes;
import constants.AssociateResponseMessages;
import constants.Role;
import db.interfaces.LeadDAO;
import db.interfaces.UserDAO;
import db.postgres.LeadDAOPG;
import db.postgres.UserDAOPG;
import pojos.Lead;
import pojos.SaleskenResponse;
import pojos.User;
import services.associate.AssociateLeadServices;
import services.global.impl.JWTTokenNeeded;

@Path("/associate")
public class AssociateLeadServicesImpl implements AssociateLeadServices {
	@Context
	private ContainerRequestContext req;

	@Override
	@POST
	@Path("/create_lead")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addLead(Lead lead) {
		SaleskenResponse response = null;
		try {

			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO dao = new UserDAOPG();
				User u = dao.findbyID(userId);
				if (u != null) {
					LeadDAO leadDAO = new LeadDAOPG();
					if (leadDAO.isNullParamCreateLead(lead)) {
						if (leadDAO.isValidParamCreateLead(lead)) {
							lead = leadDAO.addLeadByActor(lead);
							if (lead != null) {
								response = new SaleskenResponse(AssociateResponseCodes.SUCCESS,
										AssociateResponseMessages.SUCCESS);
								response.setResponse(lead);
							} else {
								response = null;
							}
						} else {
							response = new SaleskenResponse(AssociateResponseCodes.INVALID_INPUT_PARAMS_IN_ADDLEAD,
									AssociateResponseMessages.INVALID_INPUT_PARAMS_IN_ADDLEAD);
						}
					} else {
						response = new SaleskenResponse(AssociateResponseCodes.NULL_INPUT_PARAM_IN_ADDLEAD,
								AssociateResponseMessages.NULL_INPUT_PARAM_IN_ADDLEAD);
					}

				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_ADDLEAD,
							AssociateResponseMessages.INVALID_USERID_IN_ADDLEAD);
				}

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_ADDLEAD,
						AssociateResponseMessages.NULL_USERID_IN_ADDLEAD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}

	@Override
	@GET
	@Path("/view_lead/{offset}/{limit}")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded(Permissions = { Role.SALES_ASSOCIATE })
	public Response viewLead(@PathParam("offset") Integer offset, @PathParam("limit") Integer limit) {
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
					LeadDAO leaddao = new LeadDAOPG();
					ArrayList<Lead> alllead = leaddao.findLeadByActor(u, offset, limit);
					response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);
					response.setResponse(alllead);
				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_VIEWLEADS,
							AssociateResponseMessages.INVALID_USERID_IN_VIEWLEADS);
				}
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_VIEWLEADS,
						AssociateResponseMessages.NULL_USERID_IN_VIEWLEADS);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}
}
