
package db.postgres;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import db.DBUtils;
import db.interfaces.TaskDAO;
import pojos.Task;
import pojos.User;

public class TaskDAOPG implements TaskDAO {
	/**
	 * @author Rajneesh Yadav
	 */
	@Override
	public ArrayList<Task> findIncompleteTaskbyActor(User user, Integer offset, Integer limit) throws SQLException {

		String sql = "SELECT task. ID AS task_id, LEAD .company_name AS company_name, sales_contact_person.\"name\" AS contact_name, sales_contact_person.phone_number AS contact_number,"
				+ " istar_user.mobile AS agent_mobile, task.lead_id AS lead_id, task.task_type, task.created_at, sales_contact_person. ID AS scpid FROM task JOIN LEAD ON LEAD . ID "
				+ "= task.lead_id AND LEAD .status != 'DELETED' LEFT JOIN sales_contact_person ON sales_contact_person. ID = "
				+ "task.sales_contact_id LEFT JOIN istar_user ON istar_user. ID = task.actor WHERE task.actor = "
				+ user.getId() + " AND task.status IN ('INCOMPLETE') AND DATE (task.start_date) "
				+ "<= DATE (now()) ORDER BY task.created_at DESC LIMIT " + limit + " OFFSET " + offset + ";";

		ArrayList<Task> incompTask = new ArrayList<Task>();

		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {

			Task task = new Task();
			task.setId(Integer.parseInt(row.get("task_id")));
			task.setCompanyName(row.get("company_name"));
			task.setContactPersonName(row.get("contact_name"));
			task.setContactPersonNumber(row.get("contact_number"));
			task.setAgentNumber(row.get("agent_mobile"));
			if (row.get("lead_id") != null)
				task.setLeadId(Integer.parseInt(row.get("lead_id")));
			task.setTaskType(row.get("task_type"));
			task.setCreatedAt(row.get("created_at"));
			if (row.get("scpid") != null)
				task.setSalesContactId(Integer.parseInt(row.get("scpid")));

			incompTask.add(task);
		}
		return incompTask;
	}

	/**
	 * @author Sunil Verma
	 *
	 */
	@Override
	public ArrayList<Task> findTodaysCompletedTaskByActor(User user) throws SQLException {
		ArrayList<Task> arrayTask = new ArrayList<Task>();

		String sql = "SELECT task.disposition, task.status, task. ID, CASE WHEN task.call_duration ISNULL THEN '' ELSE TO_CHAR( CAST ( task.call_duration AS VARCHAR ) :: INTERVAL,"
				+ " 'HH24:MI:SS' ) END AS call_duration, task.call_rating, TO_CHAR( task.updated_at, ' hh12:mi AM' ) AS updated_at,"
				+ " CASE WHEN ( sales_contact_person.\"name\" = '' OR sales_contact_person.\"name\" = 'null' ) THEN 'N/A' ELSE"
				+ " sales_contact_person.\"name\" END AS NAME FROM task LEFT OUTER JOIN sales_contact_person ON task.sales_contact_id"
				+ " = sales_contact_person. ID JOIN \"lead\" ON \"lead\". ID = task.lead_id AND \"lead\".status != 'DELETED' WHERE "
				+ "task.actor = " + user.getId()
				+ " AND task.status IN ('COMPLETED', 'INPROGRESS') AND task.updated_at > DATE (now()) AND ( "
				+ "task.task_type = 'SALES_CALL_TASK' OR task.task_type = 'SALES_PRESENTATION_TASK' OR task.task_type = 'SALES_MEETING_TASK' "
				+ ") ORDER BY task.updated_at DESC;";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {

			Task task = new Task();
			task.setId(Integer.parseInt(row.get("id")));
			task.setStatus(row.get("status"));
			task.setDisposition(row.get("disposition"));
			task.setDuration(row.get("call_duration"));
			task.setUpdatedAt(row.get("updated_at"));
			task.setContactPersonName(row.get("name"));
			arrayTask.add(task);

		}

		return arrayTask;

	}

	@Override
	public Boolean isValidDate(String date) {

		String dateFormat = "yyyy-MM-dd";

		DateFormat sdf = new SimpleDateFormat(dateFormat);

		sdf.setLenient(false);

		try {
			sdf.parse(date);

		} catch (ParseException e) {

			return false;
		}

		return true;

	}

	@Override
	public ArrayList<Task> findTasksBetweenDatesByActor(User user, String startDate, String endDate)
			throws SQLException {
		ArrayList<Task> taskBetweenTwoDates = new ArrayList<Task>();

		String sql = "SELECT task. ID, task.stage_id, pipeline_stage.stage_name, task.actor, task.start_date, task.lead_id, sales_contact_person. ID AS client_id, sales_contact_person. NAME AS client_name, LEAD .company_name, user_profile.profile_image, task.task_type, task.status FROM task JOIN LEAD ON LEAD . ID = task.lead_id AND LEAD .status != 'DELETED' JOIN sales_contact_person ON sales_contact_person. ID = task.sales_contact_id JOIN user_profile ON user_profile.user_id = task.actor JOIN pipeline_stage ON pipeline_stage. ID = task.stage_id WHERE"
				+ " task.actor = " + user.getId() + " AND DATE (task.start_date) >= CAST ('" + startDate
				+ "' AS DATE) AND DATE (task.start_date) < CAST ('" + endDate
				+ "' AS DATE) ORDER BY task.start_date DESC;";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			Task task = new Task();
			task.setId(Integer.parseInt(row.get("id")));
			if (row.get("stage_id") != null)
				task.setStageId(Integer.parseInt(row.get("stage_id")));
			if (row.get("client_id") != null)
				task.setSalesContactId(Integer.parseInt(row.get("client_id")));
			if (row.get("actor") != null)
				task.setActor(Integer.parseInt(row.get("actor")));
			task.setProfileImage(row.get("profile_image"));
			task.setStageName(row.get("stage_name"));
			task.setCompanyName(row.get("company_name"));
			task.setTaskType(row.get("task_type"));
			if (row.get("lead_id") != null)
				task.setLeadId(Integer.parseInt(row.get("lead_id")));
			task.setStartDate(row.get("start_date"));
			task.setStatus(row.get("status"));

			taskBetweenTwoDates.add(task);

		}
		return taskBetweenTwoDates;
	}

	@Override
	public Boolean isNullFieldsTask(Task task) {
		if (task.getActor() == null || task.getLeadId() == null || task.getStatus() == null
				|| task.getStartDate() == null || task.getPipelineId() == null || task.getStageId() == null
				|| task.getLeadId() == null || task.getSalesContactId() == null || task.getOwner() == null)
			return false;
		return true;
	}

	@Override
	public Boolean isValidFieldsTask(Task task) {
		if (task.getActor() != null && task.getLeadId() != null && task.getStatus() != null
				&& task.getStartDate() != null && isValidDate(task.getStartDate()))
			return true;
		return false;
	}

	@Override
	public Task createTask(Task task) throws SQLException {
		String sql = "INSERT INTO  task( name, description, priority, owner, actor, status, start_date, end_date, duration_in_hours,\n"
				+ " assignee_group, assignee_member, is_repeatative, followup_date, is_active, tags, created_at, updated_at,\n"
				+ " is_timed_task, follow_up_duration_in_days, task_type, lead_id, call_duration, score, latitude, longitude,\n"
				+ " analytics, call_rating, sales_contact_id, pipeline_id, stage_id, voice_quality, talk_ratio, sentiment, \n"
				+ " special_score, disposition, callsid, direction, cost) VALUES\n"
				+ " ( ?, ?, null, ?,?, ?, ?::Timestamp, ?::Timestamp,\n"
				+ " NULL, NULL, NULL, NULL, NULL, ?, NULL, now(), now(), NULL, NULL,\n"
				+ " ?, ?, NULL, NULL, NULL, NULL, NULL, NULL, ?, ?,?, NULL, NULL, NULL, NULL, NULL, NULL,\n"
				+ " NULL, NULL);\n" + "";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, task.getName());
		data.put(2, task.getDescription());
		data.put(3, task.getOwner());
		data.put(4, task.getActor());
		data.put(5, task.getStatus());
		data.put(6, task.getStartDate());
		data.put(7, task.getEndDate());
		data.put(8, task.getIsActive());
		data.put(9, task.getTaskType());
		data.put(10, task.getLeadId());
		data.put(11, task.getSalesContactId());
		data.put(12, task.getPipelineId());
		data.put(13, task.getStageId());
		Integer taskId = DBUtils.getInstance().updateObject(sql, data);
		TaskDAO dao = new TaskDAOPG();
		task = dao.findTaskById(taskId);

		return task;

	}

	@Override
	public Task findTaskById(Integer id) throws SQLException {
		String sql = "select * from task where id=" + id + "";
		Task task = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			task = new Task();
			if (row.get("id") != null)
				task.setId(Integer.parseInt(row.get("id")));
			task.setName(row.get("name"));
			task.setDescription(row.get("description"));
			if (row.get("owner") != null)
				task.setOwner(Integer.parseInt(row.get("owner")));
			if (row.get("actor") != null)
				task.setActor(Integer.parseInt(row.get("actor")));
			task.setStatus(row.get("status"));
			task.setStartDate(row.get("start_date"));
			task.setStartDate(row.get("end_date"));
			if (row.get("is_active") != null && row.get("is_active").equalsIgnoreCase("t")) {
				task.setIsActive(true);
			} else {
				task.setIsActive(false);
			}
			task.setCreatedAt(row.get("created_at"));
			task.setUpdatedAt(row.get("updated_at"));
			task.setTaskType(row.get("task_type"));
			task.setLeadId(Integer.parseInt(row.get("lead_id")));
			if (row.get("call_duration") != null)
				task.setCallDuration(Integer.parseInt(row.get("call_duration")));
			if (row.get("score") != null)
				task.setScore(Float.parseFloat(row.get("score")));
			task.setAnalytics(row.get("analytics"));
			if (row.get("call_rating") != null)
				task.setCallRating(Float.parseFloat(row.get("call_rating")));
			if (row.get("sales_contact_id") != null)
				task.setSalesContactId(Integer.parseInt(row.get("sales_contact_id")));
			if (row.get("pipeline_id") != null)
				task.setPipelineId(Integer.parseInt(row.get("pipeline_id")));
			if (row.get("stage_id") != null)
				task.setStageId(Integer.parseInt(row.get("stage_id")));
			if (row.get("voice_quality") != null)
				task.setVoiceQuality(Float.parseFloat(row.get("voice_quality")));
			if (row.get("talk_ratio") != null)
				task.setTalkRatio(Float.parseFloat(row.get("talk_ratio")));
			if (row.get("sentiment") != null)
				task.setSentiment(Float.parseFloat(row.get("sentiment")));
			if (row.get("special_score") != null)
				task.setSpecialScore(Float.parseFloat(row.get("special_score")));
			task.setDisposition(row.get("disposition"));
			task.setCallsid(row.get("callsid"));
			task.setDirection(row.get("direction"));
			if (row.get("cost") != null)
				task.setCost(Float.parseFloat(row.get("cost")));

		}
		return task;

	}

}
