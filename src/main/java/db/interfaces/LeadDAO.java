package db.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import pojos.Lead;
import pojos.User;

public interface LeadDAO {
	public Boolean isNullParamCreateLead(Lead lead);

	/**
	 * @author Rohit kumar this method will true if all fields of lead is valid with
	 *         respect database otherwise false;
	 * @param lead
	 * @return
	 * @throws SQLException
	 */
	public Boolean isValidParamCreateLead(Lead lead) throws SQLException;

	public Lead addLeadByActor(Lead lead) throws SQLException;

	public ArrayList<Lead> findLeadByActor(User user, Integer offset, Integer limit) throws SQLException;

	Lead findLeadById(Integer id) throws NumberFormatException, SQLException;

}
