package services.associate;

import javax.ws.rs.core.Response;

import pojos.Ticket;

public interface AssociateRaiseTicketServices {
	/**
	 * See <a href= "https://github.com/ISTARSkills/javacore/wiki/RaiseTicket">Raise
	 * Ticket</a>
	 * 
	 * @param ticket
	 * @return
	 */
	public Response raiseTicket(Ticket ticket);

}
