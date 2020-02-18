package db.interfaces;

import java.sql.SQLException;

import pojos.Disposition;
import pojos.SaleskenResponse;
import pojos.Task;

public interface DispositionDAO {

	public SaleskenResponse noResponse(Disposition disposition) throws SQLException;

	/**
	 * this method will return SaleskenResponse object getching it from respective
	 * database
	 * 
	 * @param disposition
	 * @return
	 * @throws SQLException
	 */
	public SaleskenResponse voiceMail(Disposition disposition) throws SQLException;

	public SaleskenResponse dropped(Disposition disposition) throws SQLException;

	public SaleskenResponse wrongNumber(Disposition disposition) throws SQLException;

	/**
	 * This method will return SaleskenResponse object getching it from respective
	 * database.
	 * 
	 * @author Sunil Verma
	 * @param disposition
	 * @return response
	 * @throws SQLException
	 */
	public SaleskenResponse notDisposed(Disposition disposition) throws SQLException;

	/**
	 * this method will return SaleskenResponse object getching it from respective
	 * database
	 * 
	 * @param disposition
	 * @return
	 * @throws SQLException
	 */
	public SaleskenResponse wrongPerson(Disposition disposition) throws SQLException;

	public SaleskenResponse callAnswered(Disposition disposition) throws SQLException;

	/**
	 * This is a DispositionDAO Interface which will perform to check for required
	 * fields i.e.-CallAnswered,NoResponse,VoiceMail,Dropped to matching the UserPG
	 * class if valid then return true else return false.
	 * 
	 * @author Sunil Verma
	 * @param disposition
	 * @return boolean
	 */
	public Boolean isValidParamsDisposition(Disposition disposition);

	public Boolean isNullParamsDisposition(Disposition disposition);

	public Task createFollowUpTask(Disposition disposition) throws SQLException;

}
