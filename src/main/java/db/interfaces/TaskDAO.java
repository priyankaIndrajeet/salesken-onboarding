package db.interfaces;

import java.sql.SQLException;
import java.util.List;

import pojos.Task;
import pojos.User;

public interface TaskDAO {
	public List<Task> findIncompleteTaskbyActor(User user, Integer start, Integer limit) throws SQLException;

	/**
	 * This is TaskDAO interface which will perform all DB relative operation on the
	 * user entity.
	 * 
	 * @author Sunil Verma
	 * @param id
	 * @return user
	 * @throws SQLException
	 */
	public List<Task> findTodaysCompletedTaskByActor(User user) throws SQLException;

	public Boolean isValidDate(String date);

	/**
	 * @author Rohit Kumar This method will return list of Task between two valid
	 *         Dates with respect Database
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws SQLException
	 */
	public List<Task> findTasksBetweenDatesByActor(User user, String startDate, String endDate) throws SQLException;;

	public Boolean isNullFieldsTask(Task task);

	/**
	 * This is TaskDAO interface which will perform all Validation task in task
	 * entity
	 * 
	 * @author Sunil Verma
	 * @param task
	 * @return boolean
	 */
	public Boolean isValidFieldsTask(Task task);

	/**
	 * @author Rohit Kumar This method will return Task Object getching it from
	 *         respective database
	 * @param task
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public Task createTask(Task task) throws SQLException;

	/**
	 * @author Rohit kumar his method will return Task of valid user id getching it
	 *         from respective database
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Task findTaskById(Integer id) throws SQLException;
}
