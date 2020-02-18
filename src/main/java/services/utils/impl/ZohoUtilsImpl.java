package services.utils.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBUtils;
import pojos.ZohoLead;
import services.utils.ZohoUtils;

public class ZohoUtilsImpl implements ZohoUtils {

	@Override
	public Boolean syncData(Integer userID, Integer pipelineId) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select * from organization_configuration where organization_id in (select organizationid from org_user where userid='"
				+ userID + "') and property_name='ZohoAccessToken'";
		String accessToken = null;
		for (HashMap<String, String> hMap : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (hMap.get("updated_at") != null) {
				try {
					Date compareDate = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 1));
					Date updatedAt = sdf.parse(hMap.get("updated_at"));
					 
					if (compareDate.after(updatedAt)) {
						accessToken = hMap.get("property_value");
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (accessToken != null) {
			importZohoData(accessToken, userID, pipelineId);
			return true;// change it later
		} else {
			return false;
		}
	}

	private static void getLeads(String token) {
		try {
			String url = "https://www.zohoapis.in/crm/v2/Leads";

			String res = new URLUtilsImpl().sendGetZoho(url, token);
			System.out.println("res " + res);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void importZohoData(String accessToken, Integer userID, Integer pipelineId) throws SQLException {
		String responseLeads = importLeads(accessToken);
		if (responseLeads != null) {
			JsonObject jsonObject = new JsonParser().parse(responseLeads).getAsJsonObject();
			JsonArray jsonArray = jsonObject.getAsJsonArray("data");
			ZohoLead[] zohoLeads = new Gson().fromJson(new Gson().toJson(jsonArray), ZohoLead[].class);
			for (ZohoLead zohoLead : zohoLeads) {
				System.out.println("zohoLead   " + new Gson().toJson(zohoLead));

				if (zohoLead.getPhone() != null) {
					checkSalesContactPerson(zohoLead, userID);
				}

			}
		}

	}

	private Integer[] checkSalesContactPerson(ZohoLead zohoLead, Integer userID) throws SQLException {
		Integer scpId = null;
		Integer leadId = null;
		String sql = "SELECT sales_contact_person.* FROM sales_contact_person JOIN LEAD ON LEAD . ID = sales_contact_person.lead_id WHERE lead_id IN ( SELECT LEAD . ID FROM LEAD WHERE OWNER IN "
				+ "( SELECT org_user.userid FROM org_user WHERE org_user.organizationid IN "
				+ "( SELECT org_user.organizationid FROM org_user WHERE org_user.userid = " + userID
				+ " ) ) ) AND ( phone_number = '" + zohoLead.getPhone() + "' )"
				+ " AND LEAD .status != 'DELETED' ORDER BY sales_contact_person. ID DESC";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (result.size() > 0) {
			for (HashMap<String, String> hMap : result) {
				scpId = Integer.parseInt(hMap.get("id"));
				leadId = Integer.parseInt(hMap.get("lead_id"));
				leadUpdate(zohoLead, hMap);
				scpUpdate(zohoLead, hMap);
				break;
			}
		} else {
			Integer productId = null;
			String companyName = "Unknown Company";
			String leadStatus = "INPROGRESS";
			if (zohoLead.getCompany() != null) {
				companyName = zohoLead.getCompany();
			}
			if (zohoLead.getLead_Status() != null) {
				leadStatus = zohoLead.getLead_Status();
			}
			String outboundProduct = "SELECT * from organization_configuration WHERE organization_id in (SELECT organizationid from org_user WHERE userid="
					+ userID + ") and property_name='OutboundProduct'";
			ArrayList<HashMap<String, String>> resultOrgConfig = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), outboundProduct);
			if (resultOrgConfig.size() > 0) {
				for (HashMap<String, String> hashMap : resultOrgConfig) {
					if (hashMap.get("property_value") != null) {
						productId = Integer.parseInt(hashMap.get("property_value"));
					}
				}
			}
			String leadUpdateSql = "INSERT INTO public.lead (owner, actor, stage, product_id, lead_source, company_name, address, created_at, updated_at, status, country, state, city, pin_code, reason, value, company_details, company_website, timezone) "
					+ "VALUES (?, ?, ?, ?, 'Lead from Zoho',?, ?, now(), now(), ?, 'US', 'CA ', ' ', '', NULL, NULL, NULL, NULL, NULL);";
			HashMap<Integer, Object> leadInsertData = new HashMap<Integer, Object>();
			leadInsertData.put(1, userID);
			leadInsertData.put(2, null);
			leadInsertData.put(3, "1");
			leadInsertData.put(4, productId);
			leadInsertData.put(5, companyName);
			leadInsertData.put(6, "No Address");
			leadInsertData.put(7, leadStatus);
			leadId = DBUtils.getInstance().updateObject(leadUpdateSql, leadInsertData);
			if (leadId != 0) {
				String insertScpSql = "INSERT INTO public.sales_contact_person ( name, email, phone_number, lead_id, office_phone_number, company_name, city, state, created_at, updated_at, language_pref, country, job_title) "
						+ "VALUES (?, ?, ?,?, NULL, 'NA', '', 'CA', now(), now(), NULL, NULL, 'NA');";
				HashMap<Integer, Object> scpInsertData = new HashMap<Integer, Object>();
				scpInsertData.put(1, zohoLead.getFull_Name());
				scpInsertData.put(2, zohoLead.getEmail());
				scpInsertData.put(3, zohoLead.getPhone());
				scpInsertData.put(4, leadId);

				Integer scpID = DBUtils.getInstance().updateObject(insertScpSql, scpInsertData);

				System.out.println("scpID inserted " + scpID + " for lead id inserted " + leadId);
			}
		}

		return new Integer[] { scpId, leadId };
	}

	private void leadUpdate(ZohoLead zohoLead, HashMap<String, String> hMap) throws SQLException {
		System.out.println("zohoLead>>>> " + new Gson().toJson(zohoLead));
		int count = 1;
		HashMap<Integer, Object> leadUpdateData = new HashMap<Integer, Object>();
		String params = "";
		if (zohoLead.getCompany() != null) {
			params = params + " company_name=?,";
			leadUpdateData.put(count, zohoLead.getCompany());
			count++;
		}
		if (zohoLead.getLead_Status() != null) {
			params = params + " status=? ";
			leadUpdateData.put(count, zohoLead.getLead_Status());
			count++;
		}
		if (params.trim().length() > 0) {
			String leadUpdateSql = "UPDATE public.lead SET " + params + "  WHERE id = ?;";
			leadUpdateData.put(count, Integer.parseInt(hMap.get("lead_id")));
			DBUtils.getInstance().updateObject(leadUpdateSql, leadUpdateData);
		}
	}

	private void scpUpdate(ZohoLead zohoLead, HashMap<String, String> hMap) throws SQLException {
		String scpParams = "";
		int count = 1;
		HashMap<Integer, Object> scpUpdateData = new HashMap<Integer, Object>();
		if (zohoLead.getFull_Name() != null) {
			scpParams = scpParams + "name=?, ";
			scpUpdateData.put(count, zohoLead.getFull_Name());
			count++;
		}
		if (zohoLead.getEmail() != null) {
			scpParams = scpParams + "email=?, ";
			scpUpdateData.put(count, zohoLead.getEmail());
			count++;
		}
		if (scpParams.trim().length() > 0) {
			String scpUpdateSql = "UPDATE public.sales_contact_person SET " + scpParams
					+ "  updated_at=now() WHERE id=?;";
			scpUpdateData.put(count, Integer.parseInt(hMap.get("id")));
			DBUtils.getInstance().updateObject(scpUpdateSql, scpUpdateData);
		}
	}

	private String importLeads(String token) {
		String res = null;
		try {
			String url = "https://www.zohoapis.in/crm/v2/Leads";
			res = new URLUtilsImpl().sendGetZoho(url, token);
			System.out.println("res " + res);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return res;

	}

	public static void main(String[] args) {
		new ZohoUtilsImpl().importLeads("1000.fb826563d7e5d6e5b61372e3a376c71b.67beea84accc362dfb2797de7ebe2c14");
	}

	private Boolean exportLeads(String token) {

		try {
			String url = "https://www.zohoapis.in/crm/v2/Leads";

			String res = new URLUtilsImpl().sendGetZoho(url, token);
			System.out.println("res " + res);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private Boolean importTasks(String token) {
		try {
			String url = "https://www.zohoapis.in/crm/v2/Leads";

			String res = new URLUtilsImpl().sendGetZoho(url, token);
			System.out.println("res " + res);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	private Boolean exportExportTasks(String token) {

		try {
			String url = "https://www.zohoapis.in/crm/v2/Tasks";
			String res = new URLUtilsImpl().sendGetZoho(url, token);
			System.out.println("res " + res);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
