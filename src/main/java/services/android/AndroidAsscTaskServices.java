package services.android;

import javax.ws.rs.core.Response;

import pojos.Disposition;

/**
 * @author Anurag
 *
 */
public interface AndroidAsscTaskServices {
	/**
	 * @param date
	 * @param userId
	 * @param status
	 * @return
	 */
	public Response tasks(String date, Integer userId, String status);

	/**
	 * @param disposition
	 * @return
	 */
	public Response disposition(Disposition disposition);
}
