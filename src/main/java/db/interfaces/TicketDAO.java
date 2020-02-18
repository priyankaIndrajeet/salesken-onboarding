/**
 * 
 */
package db.interfaces;

import pojos.Ticket;

/**
 * @author anurag
 *
 */
public interface TicketDAO {
	public Boolean raiseTicket(Ticket ticket);

	/**
	 * This is a TicketDAO Interface which will perform to check for required fields
	 * i.e.- type,description,name,email, Attachment,subject. is null
	 * 
	 * @author Sunil Verma
	 * @param ticket
	 * @return boolean
	 */
	public Boolean isNullFields(Ticket ticket);
}
