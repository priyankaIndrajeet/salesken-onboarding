package services.associate;

import javax.ws.rs.core.Response;

import pojos.Disposition;
import pojos.Task;

public interface AssociateTaskServices {

	/**
	 * See <a href=
	 * "https://github.com/ISTARSkills/javacore/wiki/IncompleteTask">Incomplete Task
	 * </a>
	 * 
	 * @param user
	 * @return
	 * @throws NullPointerException
	 */

	public Response incompleteTask(Integer start, Integer limit) throws NullPointerException;

	/**
	 * 
	 * See <a href=
	 * "https://github.com/ISTARSkills/javacore/wiki/TodaysCompletedTask">Today's
	 * completed task </a>
	 * 
	 * @param user
	 * @return
	 */
	public Response todayscompletedTask();

	/**
	 * See <a href=
	 * "https://github.com/ISTARSkills/javacore/wiki/GetTaskBetweenTwoDates">Get
	 * Task Between Two Dates </a>
	 * 
	 * @param user
	 * @param date1
	 * @param date2
	 * @return
	 */
	public Response getTaskBetweenTwoDates(String startDate, String endDate);

	/**
	 * 
	 * See <a href= "https://github.com/ISTARSkills/javacore/wiki/CreateTask">Raise
	 * Ticket</a>
	 * 
	 * @return
	 */
	public Response createTask( Task task);

	/**
	 * See <a href=
	 * "https://github.com/ISTARSkills/javacore/wiki/Disposition">Disposition</a>
	 * 
	 * @return
	 */
	public Response disposition(Disposition disposition);

}
