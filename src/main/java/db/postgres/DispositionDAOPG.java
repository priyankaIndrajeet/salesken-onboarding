package db.postgres;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import constants.AssociateResponseCodes;
import constants.AssociateResponseMessages;
import db.DBUtils;
import db.interfaces.DispositionDAO;
import db.interfaces.TaskDAO;
import pojos.Disposition;
import pojos.Disposition.DispositionType;
import pojos.SaleskenResponse;
import pojos.Task;

public class DispositionDAOPG implements DispositionDAO {

	@Override
	public SaleskenResponse callAnswered(Disposition disposition) throws SQLException {
		SaleskenResponse response = null;

		if (isNullParamsDisposition(disposition)) {

			if (isValidParamsDisposition(disposition)) {
				// Make Disposition as CallAnswered here

				String sql = "UPDATE task SET  call_rating = ?, disposition = ? WHERE id = ?";

				HashMap<Integer, Object> callansweredData = new HashMap<Integer, Object>();

				callansweredData.put(1, disposition.getCallRating());
				callansweredData.put(2, disposition.getDispositionType());
				callansweredData.put(3, disposition.getTaskId());

				DBUtils.getInstance().updateObject(sql, callansweredData);
				Task task = null;
				if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
					task = createFollowUpTask(disposition);
				}
				response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);

				if (task != null) {
					response.setResponse(task);
				}
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.INVALID_PARAMS_DISP_CALLANSWERED, AssociateResponseMessages.INVALID_PARAMS_DISP_CALLANSWERED);

			}

		} else {
			response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_DISP_CALLANSWERED, AssociateResponseMessages.NULL_PARAMS_IN_DISP_CALLANSWERED);

		}
		return response;
	}

	@Override
	public SaleskenResponse noResponse(Disposition disposition) throws SQLException {
		SaleskenResponse response = null;
		if (isNullParamsDisposition(disposition)) {

			if (isValidParamsDisposition(disposition)) {
				Task task = null;
				String updateDisposition = "UPDATE task set disposition=? where id= ? ;";

				HashMap<Integer, Object> updateDispostionTask = new HashMap<Integer, Object>();
				updateDispostionTask.put(1, DispositionType.NoResponse.name());
				updateDispostionTask.put(2, disposition.getTaskId());
				DBUtils.getInstance().updateObject(updateDisposition, updateDispostionTask);
				if (disposition.getIsFollowUp()) {

					task = new DispositionDAOPG().createFollowUpTask(disposition);

				}
				response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);
				if (task != null)
					response.setResponse(task);

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.INVALID_PARAMS_DISP_NORESPONSE, AssociateResponseMessages.INVALID_PARAMS_DISP_NORESPONSE);

			}

		} else {
			response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_DISP_NORESPONSE, AssociateResponseMessages.NULL_PARAMS_IN_DISP_NORESPONSE);

		}
		return response;

	}

	@Override
	public SaleskenResponse voiceMail(Disposition disposition) throws SQLException {
		SaleskenResponse response = null;
		if (isNullParamsDisposition(disposition)) {

			if (isValidParamsDisposition(disposition)) {
				// Make Disposition as voiceMail here
				String updateDisposition = "update task set disposition=?  where id=?;";
				HashMap<Integer, Object> updateDispostionTask = new HashMap<Integer, Object>();
				updateDispostionTask.put(1, DispositionType.VoiceMail.name());
				updateDispostionTask.put(2, disposition.getTaskId());

				DBUtils.getInstance().updateObject(updateDisposition, updateDispostionTask);
				Task task = null;
				if (disposition.getIsFollowUp()) {
					task = new DispositionDAOPG().createFollowUpTask(disposition);

				}
				response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);

				if (task != null) {
					response.setResponse(task);
				}

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.INVALID_PARAMS_DISP_VOICEMAIL, AssociateResponseMessages.INVALID_PARAMS_DISP_VOICEMAIL);

			}

		} else {
			response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_DISP_VOICEMAIL, AssociateResponseMessages.NULL_PARAMS_IN_DISP_VOICEMAIL);

		}
		return response;

	}

	@Override
	public SaleskenResponse dropped(Disposition disposition) throws SQLException {
		SaleskenResponse response = null;
		if (isNullParamsDisposition(disposition)) {
			if (isValidParamsDisposition(disposition)) {
				// Make Disposition as dropped here

				String sql = "UPDATE task SET disposition = ? WHERE id = ?";

				HashMap<Integer, Object> calldropped = new HashMap<Integer, Object>();

				calldropped.put(1, disposition.getDispositionType());
				calldropped.put(2, disposition.getTaskId());
				Task task = null;
				DBUtils.getInstance().updateObject(sql, calldropped);
				if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
					task = createFollowUpTask(disposition);
					System.out.println(task.getId() + ">>>>>>>>>>>>>");
				}

				response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);
				if (task != null) {
					response.setResponse(task);
				}

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.INVALID_PARAMS_DISP_DROPPED, AssociateResponseMessages.INVALID_PARAMS_DISP_DROPPED);

			}

		} else {
			response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_DISP_DROPPED, AssociateResponseMessages.NULL_PARAMS_IN_DISP_DROPPED);

		}
		return response;

	}

	@Override
	public SaleskenResponse wrongNumber(Disposition disposition) throws SQLException {
		SaleskenResponse response = null;
		if (isNullParamsDisposition(disposition)) {

			if (isValidParamsDisposition(disposition)) {

				String updateLead = "UPDATE lead set \"status\"=?  where id in(SELECT lead_id from task where id=?);";
				String updateDisposition = "UPDATE task set disposition=? where id= ? ;";

				HashMap<Integer, Object> updateLeadStatus = new HashMap<Integer, Object>();
				updateLeadStatus.put(1, disposition.getLeadStatus());
				updateLeadStatus.put(2, disposition.getTaskId());
				DBUtils.getInstance().updateObject(updateLead, updateLeadStatus);

				HashMap<Integer, Object> updateDispostionTask = new HashMap<Integer, Object>();
				updateDispostionTask.put(1, DispositionType.WrongNumber.name());
				updateDispostionTask.put(2, disposition.getTaskId());
				DBUtils.getInstance().updateObject(updateDisposition, updateDispostionTask);

				response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.INVALID_PARAMS_DISP_WRONGNUMBER, AssociateResponseMessages.INVALID_PARAMS_DISP_WRONGNUMBER);

			}

		} else {
			response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_DISP_WRONGNUMBER, AssociateResponseMessages.NULL_PARAMS_IN_DISP_WRONGNUMBER);

		}
		return response;

	}

	@Override
	public SaleskenResponse notDisposed(Disposition disposition) throws SQLException {
		SaleskenResponse response = null;
		if (isNullParamsDisposition(disposition)) {

			if (isValidParamsDisposition(disposition)) {

				String updateDisposition = "UPDATE task set disposition='NotDisposed' where id=?;";
				HashMap<Integer, Object> updateDispostionTask = new HashMap<Integer, Object>();
				updateDispostionTask.put(1, disposition.getTaskId());
				DBUtils.getInstance().updateObject(updateDisposition, updateDispostionTask);

				// Make Disposition as wrongPerson here
				response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.INVALID_PARAMS_DISP_WRONGPERSON, AssociateResponseMessages.INVALID_PARAMS_DISP_WRONGPERSON);

			}

		} else {
			response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_DISP_NOTDISPOSED, AssociateResponseMessages.NULL_PARAMS_IN_DISP_NOTDISPOSED);

		}
		return response;

	}

	@Override
	public SaleskenResponse wrongPerson(Disposition disposition) throws SQLException {
		SaleskenResponse response = null;
		if (isNullParamsDisposition(disposition)) {

			if (isValidParamsDisposition(disposition)) {
				// Make Disposition as wrongPerson here
				String updateLead = "UPDATE lead set \"status\"=?  where id in(SELECT lead_id from task where id=?);";

				HashMap<Integer, Object> updateLeadStatus = new HashMap<Integer, Object>();
				updateLeadStatus.put(1, disposition.getLeadStatus());
				updateLeadStatus.put(2, disposition.getTaskId());
				DBUtils.getInstance().updateObject(updateLead, updateLeadStatus);

				String updateDisposition = "UPDATE task set disposition=? where id= ? ;";
				HashMap<Integer, Object> updateDispostionTask = new HashMap<Integer, Object>();
				updateDispostionTask.put(1, DispositionType.WrongPerson.name());
				updateDispostionTask.put(2, disposition.getTaskId());
				DBUtils.getInstance().updateObject(updateDisposition, updateDispostionTask);

				response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);

			} else {
				response = new SaleskenResponse(AssociateResponseCodes.INVALID_PARAMS_DISP_NOTDISPOSED, AssociateResponseMessages.INVALID_PARAMS_DISP_NOTDISPOSED);

			}

		} else {
			response = new SaleskenResponse(AssociateResponseCodes.NULL_PARAMS_IN_DISP_WRONGPERSON, AssociateResponseMessages.NULL_PARAMS_IN_DISP_WRONGPERSON);

		}
		return response;

	}

	@Override
	public Boolean isValidParamsDisposition(Disposition disposition) {

		if (disposition.getDispositionType() != null && disposition.getTaskId() != null) {
			switch (DispositionType.valueOf(disposition.getDispositionType())) {
			case CallAnswered:
				if (disposition.getCallRating() != null && disposition.getNotes() != null) {
					if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
						if (disposition.getFollowupDate() != null && disposition.getFollowupTime() != null && disposition.getFollowpActor() != null && disposition.getFollowupTaskType() != null
								&& isValidDateTime(disposition.getFollowupDate() + " " + disposition.getFollowupTime())) {

							return true;
						} else {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}

			case NoResponse:

				if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
					if (disposition.getFollowupDate() != null && disposition.getFollowupTime() != null && disposition.getFollowpActor() != null && disposition.getFollowupTaskType() != null
							&& isValidDateTime(disposition.getFollowupDate() + " " + disposition.getFollowupTime())) {

						return true;
					} else {
						return false;
					}
				}
				return false;

			case VoiceMail:
				if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
					if (disposition.getFollowupDate() != null && disposition.getFollowupTime() != null && disposition.getFollowpActor() != null && disposition.getFollowupTaskType() != null
							&& isValidDateTime(disposition.getFollowupDate() + " " + disposition.getFollowupTime())) {

						return true;
					} else {
						return false;
					}
				}
				return false;
			case Dropped:
				if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
					if (disposition.getFollowupDate() != null && disposition.getFollowupTime() != null && disposition.getFollowpActor() != null && disposition.getFollowupTaskType() != null
							&& isValidDateTime(disposition.getFollowupDate() + " " + disposition.getFollowupTime())) {

						return true;
					} else {
						return false;
					}
				}
				return false;
			case WrongNumber:
				if (disposition.getLeadStatus() != null)
					return true;
				break;

			case WrongPerson:

				if (disposition.getLeadStatus() != null)
					return true;
				break;

			case NotDisposed:
				return true;

			}

		}

		return false;
	}

	@Override
	public Boolean isNullParamsDisposition(Disposition disposition) {
		if (disposition.getTaskId() != null) {

			switch (DispositionType.valueOf(disposition.getDispositionType())) {
			case CallAnswered:
				if (disposition.getCallRating() != null && disposition.getNotes() != null) {
					if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
						if (disposition.getFollowupDate() != null && disposition.getFollowupTime() != null && disposition.getFollowpActor() != null && disposition.getFollowupTaskType() != null) {
							return true;
						} else {
							return false;
						}
					}
					return true;
				} else {
					return false;
				}

			case NoResponse:

				if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
					if (disposition.getFollowupDate() != null && disposition.getFollowupTime() != null && disposition.getFollowpActor() != null && disposition.getFollowupTaskType() != null)
						return true;
					return false;
				}
				return false;

			case VoiceMail:
				if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
					if (disposition.getFollowupDate() != null && disposition.getFollowupTime() != null && disposition.getFollowpActor() != null && disposition.getFollowupTaskType() != null)
						return true;
					return false;
				}
				return false;

			case Dropped:
				if (disposition.getIsFollowUp() != null && disposition.getIsFollowUp()) {
					if (disposition.getFollowupDate() != null && disposition.getFollowupTime() != null && disposition.getFollowpActor() != null && disposition.getFollowupTaskType() != null)
						return true;
					return false;
				}
				return false;

			case WrongNumber:
				if (disposition.getLeadStatus() != null)
					return true;
				break;

			case WrongPerson:
				if (disposition.getLeadStatus() != null)
					return true;
				break;

			case NotDisposed:
				return true;

			default:
				return false;
			}

		}
		return false;
	}

	@Override
	public Task createFollowUpTask(Disposition disposition) throws SQLException {
		TaskDAO taskdao = new TaskDAOPG();

		Task task = taskdao.findTaskById(disposition.getTaskId());

		String sql = "INSERT INTO \"public\".\"task\"(\"name\", \"description\", \"priority\", \"owner\", \"actor\", "
				+ "\"status\", \"start_date\", \"end_date\", \"duration_in_hours\", \"assignee_group\", \"assignee_member\","
				+ " \"is_repeatative\", \"followup_date\", \"is_active\", \"tags\", \"created_at\", \"updated_at\", \"is_timed_task\", "
				+ "\"follow_up_duration_in_days\", \"task_type\", \"lead_id\", \"call_duration\", \"score\", \"latitude\", \"longitude\", "
				+ "\"analytics\", \"call_rating\", \"sales_contact_id\", \"pipeline_id\", \"stage_id\", \"voice_quality\", \"talk_ratio\","
				+ "\"sentiment\", \"special_score\", \"disposition\", \"callsid\", \"direction\", \"cost\") "
				+ "VALUES (?, NULL, NULL, ?, ?, 'INCOMPLETE', ? :: Timestamp, ? :: Timestamp, NULL, NULL, NULL, NULL, NULL, 't', NULL, 'now()', "
				+ "'now()', NULL, NULL, ?, ?, NULL, NULL, NULL, NULL, NULL, NULL, ?, ?, ?," + "" + " NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);\n" + "";

		HashMap<Integer, Object> taskData = new HashMap<Integer, Object>();
		taskData.put(1, "Follow up for task:" + task.getId());
		taskData.put(2, task.getOwner());
		taskData.put(3, task.getActor());
		taskData.put(4, disposition.getFollowupDate() + " " + disposition.getFollowupTime());
		taskData.put(5, disposition.getFollowupDate() + " " + disposition.getFollowupTime());
		taskData.put(6, disposition.getFollowupTaskType());
		taskData.put(7, task.getLeadId());
		taskData.put(8, task.getSalesContactId());
		taskData.put(9, task.getPipelineId());
		taskData.put(10, task.getStageId());
		Integer taskId = DBUtils.getInstance().updateObject(sql, taskData);

		task = taskdao.findTaskById(taskId);
		return task;

	}

	public static Boolean isValidDateTime(String datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String currentdatetime = formatter.format(date);
		System.out.println(currentdatetime);
		System.out.println(datetime);
		if ((datetime.compareTo(currentdatetime)) > 0)
			return true;

		return false;

	}

}
