package services.associate.impl;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import constants.AssociateResponseCodes;
import constants.AssociateResponseMessages;
import db.interfaces.TicketDAO;
import db.interfaces.UserDAO;
import db.postgres.TicketDAOPG;
import db.postgres.UserDAOPG;
import pojos.SaleskenResponse;
import pojos.Ticket;
import pojos.User;
import services.associate.AssociateRaiseTicketServices;
import services.global.impl.JWTTokenNeeded;

@Path("/associate")
public class AssociateRaiseTicketServicesImpl implements AssociateRaiseTicketServices {
	@Context
	private ContainerRequestContext req;

	@Override
	@POST
	@Path("/raise_ticket")
	@Produces(MediaType.APPLICATION_JSON)
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	public Response raiseTicket(Ticket ticket) {
		SaleskenResponse response = null;

		try {
			if (req.getProperty("id") != null) {
				Integer userId = Integer.parseInt(req.getProperty("id").toString());
				UserDAO dao = new UserDAOPG();
				User u = dao.findbyID(userId);
				if (u != null) {
					TicketDAO ticketDAO = new TicketDAOPG();

					if (ticket != null && ticketDAO.isNullFields(ticket)) {
						if (ticketDAO.raiseTicket(ticket)) {
							response = new SaleskenResponse(AssociateResponseCodes.SUCCESS,
									AssociateResponseMessages.SUCCESS);
						}
					} else {
						response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_RAISETICKET,
								AssociateResponseMessages.NULL_PARAMS_IN_RAISETICKET);
					}

				} else {
					response = new SaleskenResponse(AssociateResponseCodes.INVALID_USERID_IN_RAISETICKET,
							AssociateResponseMessages.INVALID_USERID_IN_RAISETICKET);

				}

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.NULL_USERID_IN_RAISETICKET,
						AssociateResponseMessages.NULL_USERID_IN_RAISETICKET);
			}
		} catch (SQLException e) {
			response = new SaleskenResponse(AssociateResponseCodes.PROBLEM_WITH_DB,
					AssociateResponseMessages.PROBLEM_WITH_DB);
		}
		return Response.status(200).entity(response).build();
	}

}
