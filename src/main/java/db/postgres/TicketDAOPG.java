package db.postgres;

import db.interfaces.TicketDAO;
import pojos.Ticket;

public class TicketDAOPG implements TicketDAO {

	@Override
	public Boolean raiseTicket(Ticket ticket) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @author user Sunil Verma------
	 */
	@Override
	public Boolean isNullFields(Ticket ticket) {
		if (ticket.getName() == null || ticket.getEmail() == null || ticket.getType() == null || ticket.getDescription() == null || ticket.getAttachment() == null || ticket.getSubject() == null)
			return true;
		return false;
	}

}
