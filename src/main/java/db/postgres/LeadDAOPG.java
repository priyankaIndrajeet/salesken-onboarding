package db.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import db.DBUtils;
import db.interfaces.LeadDAO;
import pojos.Lead;
import pojos.SalesContactPerson;
import pojos.Task;
import pojos.User;

public class LeadDAOPG implements LeadDAO {
	/**
	 * @author user Sunil Verma
	 */
	@Override
	public Boolean isNullParamCreateLead(Lead lead) {
		if (lead.getActor() == null || lead.getCompanyName() == null)
			return false;
		return true;
	}

	@Override
	public Boolean isValidParamCreateLead(Lead lead) throws SQLException {
		if (lead.getActor() != null) {
			String ownerSql = "select manager_id from user_manager where user_id=" + lead.getActor() + " limit 1;";
			ArrayList<HashMap<String, String>> resultOwnerCheck = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), ownerSql);
			if (resultOwnerCheck.size() > 0) {

				String productSql = "select product_id from lead where owner="
						+ resultOwnerCheck.get(0).get("manager_id") + ";";
				ArrayList<HashMap<String, String>> resultProductCheck = DBUtils.getInstance()
						.executeQuery(Thread.currentThread().getStackTrace(), productSql);
				if (lead.getLeadSource() != null && lead.getStatus() != null && (lead.getCompanyName()).length() > 0
						&& (lead.getLeadSource()).length() > 0 && (lead.getStatus()).length() > 0
						&& resultProductCheck.size() > 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * @author Rajneesh Yadav
	 * @throws SQLException
	 */
	public Lead addLeadByActor(Lead lead) throws SQLException {

		String sql = "INSERT INTO \"public\".\"lead\"(\"owner\", \"actor\", \"stage\", \"product_id\", \"lead_source\", "
				+ "\"company_name\", \"address\", \"created_at\", \"updated_at\", \"status\","
				+ " \"country\", \"state\", \"city\", \"pin_code\", \"reason\", \"value\", \"company_details\","
				+ " \"company_website\", \"timezone\")"
				+ " VALUES ( ?, ?, ?,?, ?, ?, ?, now(), now(), ?, ?, ?, ?, ?, NULL, NULL, NULL, NULL, NULL);\n" + "";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, lead.getOwner());
		data.put(2, lead.getActor());
		data.put(3, lead.getStage());
		data.put(4, lead.getProductId());
		data.put(5, lead.getLeadSource());
		data.put(6, lead.getCompanyName());
		data.put(7, lead.getAddress());
		data.put(8, lead.getStatus());
		data.put(9, lead.getCountry());
		data.put(10, lead.getState());
		data.put(11, lead.getCity());
		data.put(12, lead.getPinCode());
		Integer leadId = DBUtils.getInstance().updateObject(sql, data);
		lead = new LeadDAOPG().findLeadById(leadId);
		return lead;
	}

	@Override
	public ArrayList<Lead> findLeadByActor(User user, Integer offset, Integer limit) throws SQLException {

		String sql = "SELECT \"lead\".*,sales_contact_person.id as scp_id, sales_contact_person.name as scp_name,sales_contact_person.email as scp_email, sales_contact_person.phone_number as scp_phone_number, "
				+ "sales_contact_person.office_phone_number as scp_office_phone_number, sales_contact_person.language_pref as scp_language_pref, sales_contact_person.job_title as scp_job_title FROM LEAD LEFT JOIN "
				+ "sales_contact_person ON \"lead\". ID = sales_contact_person.lead_id where LEAD .id in (SELECT DISTINCT lead.id from lead WHERE actor="
				+ user.getId() + " ORDER BY id DESC OFFSET " + offset + " LIMIT " + limit + ")";

		HashMap<Integer, Lead> leadMap = new HashMap<Integer, Lead>();

		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (leadMap.containsKey(Integer.parseInt(row.get("id")))) {
				Lead lead = leadMap.get(Integer.parseInt(row.get("id")));
				if (row.get("scp_id") != null) {
					SalesContactPerson salesContactPerson = new SalesContactPerson();
					salesContactPerson.setId(Integer.parseInt(row.get("scp_id")));
					salesContactPerson.setName(row.get("scp_name"));
					salesContactPerson.setEmail(row.get("scp_email"));
					salesContactPerson.setPhoneNumber(row.get("scp_phone_number"));
					if (row.get("id") != null) {
						salesContactPerson.setLeadId(Integer.parseInt(row.get("id")));
						salesContactPerson.setOfficePhoneNumber(row.get("scp_office_phone_number"));
						salesContactPerson.setCountry(row.get("country"));
						salesContactPerson.setCity(row.get("city"));
						salesContactPerson.setState(row.get("state"));
						salesContactPerson.setLanguagePref(row.get("scp_language_pref"));
						salesContactPerson.setJobTitle(row.get("scp_job_title"));
					}
					lead.getSalesContactPersons().add(salesContactPerson);
				}
				leadMap.put(lead.getId(), lead);
			} else {
				Lead lead = new Lead();
				lead.setId(Integer.parseInt(row.get("id")));
				;
				lead.setOwner(Integer.parseInt(row.get("owner")));
				if (row.get("actor") != null)
					lead.setActor(Integer.parseInt(row.get("actor")));
				if (row.get("product_id") != null)
					lead.setProductId(Integer.parseInt(row.get("product_id")));
				lead.setStage(row.get("stage"));
				lead.setLeadSource(row.get("lead_source"));
				lead.setCompanyName(row.get("company_name"));
				lead.setAddress(row.get("address"));

				lead.setStatus(row.get("status"));
				lead.setCountry(row.get("country"));
				lead.setCity(row.get("city"));
				lead.setPinCode(row.get("pin_code"));
				lead.setReason(row.get("reason"));
				lead.setValue(row.get("value"));
				lead.setCompanyDetails(row.get("company_details"));
				lead.setCompanyWebsite(row.get("company_website"));
				lead.setTimezone(row.get("timezone"));

				if (row.get("scp_id") != null) {
					SalesContactPerson salesContactPerson = new SalesContactPerson();
					salesContactPerson.setId(Integer.parseInt(row.get("scp_id")));
					salesContactPerson.setName(row.get("scp_name"));
					salesContactPerson.setEmail(row.get("scp_email"));
					salesContactPerson.setPhoneNumber(row.get("scp_phone_number"));
					if (row.get("id") != null) {
						salesContactPerson.setLeadId(Integer.parseInt(row.get("id")));
						salesContactPerson.setOfficePhoneNumber(row.get("scp_office_phone_number"));
						salesContactPerson.setCountry(row.get("country"));
						salesContactPerson.setCity(row.get("city"));
						salesContactPerson.setState(row.get("state"));

						salesContactPerson.setLanguagePref(row.get("scp_language_pref"));
						salesContactPerson.setJobTitle(row.get("scp_job_title"));
					}
					lead.getSalesContactPersons().add(salesContactPerson);
				}
				lead.setLastTask(getLastTask(lead.getId()));
				leadMap.put(Integer.parseInt(row.get("id")), lead);
			}
		}

		return new ArrayList<Lead>(leadMap.values());
	}

	private Task getLastTask(Integer leadId) throws SQLException {
		Task task = null;
		String sqlLastTask = "SELECT task. ID, task.lead_id, user_profile.name, pipeline.name AS pipeline_name, user_profile.profile_image AS picture, "
				+ "task.status, TO_CHAR( task.start_date, 'dd/mm/yyyy HH24:MI' ) AS start_date, task.task_type AS task_name"
				+ " FROM task, user_profile, pipeline WHERE task.lead_id = " + leadId
				+ " AND user_profile.user_id = task.actor AND pipeline. ID = task.pipeline_id ORDER BY task. ID DESC;";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlLastTask);
		if (result.size() > 0) {
			HashMap<String, String> row = result.get(0);
			task = new Task();
			task.setId(Integer.parseInt(row.get("id")));
			task.setActorName(row.get("name"));
			task.setPipelineName(row.get("pipeline_name"));
			task.setProfileImage(row.get("picture"));
			task.setStatus(row.get("status"));
			task.setStartDate(row.get("start_date"));
			task.setTaskType(row.get("task_name"));
		}
		return task;
	}

	@Override
	public Lead findLeadById(Integer id) throws SQLException {

		String sql = "select * from lead where id =" + id;
		Lead lead = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			lead = new Lead();
			if (row.get("id") != null)
				lead.setId(Integer.parseInt(row.get("id")));
			lead.setOwner(Integer.parseInt(row.get("owner")));
			if (row.get("actor") != null)
				lead.setActor(Integer.parseInt(row.get("actor")));
			if (row.get("product_id") != null)
				lead.setProductId(Integer.parseInt(row.get("product_id")));
			lead.setStage(row.get("stage"));
			lead.setLeadSource(row.get("lead_source"));
			lead.setCompanyName(row.get("company_name"));
			lead.setAddress(row.get("address"));
			lead.setCreatedAt(row.get("created_at"));
			lead.setUpdatedAt(row.get("updated_at"));
			lead.setStatus(row.get("status"));
			lead.setCountry(row.get("country"));
			lead.setCity(row.get("city"));
			lead.setPinCode(row.get("pin_code"));
			lead.setReason(row.get("reason"));
			lead.setValue(row.get("value"));
			lead.setCompanyDetails(row.get("company_details"));
			lead.setCompanyWebsite(row.get("company_website"));
			lead.setTimezone(row.get("timezone"));

		}

		return lead;
	}
}
