package db.postgres;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import constants.UserStatus;
import db.DBUtils;
import db.interfaces.OrganizationDAO;
import pojos.Dashboard;
import pojos.Organization;
import pojos.Pincode;
import pojos.User;
import pojos.Users;
import strings.StringUtils;

public class OrganizationDAOPG implements OrganizationDAO {

	@Override
	public Organization createOrganization(Organization organization) throws SQLException {
		String sql = "INSERT INTO  organization  " + "( name, industry  " + ", website, address_line_1, address_line_2, socialsite, landmark, pincode_id, contact_phone,profile )" + " VALUES" + "(?,?,?,?,?,?,?,?,?,?)";

		String industry, website, socialSite, address_line_1, address_line_2, landmark, profile = null;
		Integer pincode = null;
		String boardlineNumber = null;

		if (organization.getIndustry() == null) {
			industry = "NOT_SPECIFIED";
		} else {
			industry = organization.getIndustry();

		}

		if (organization.getWebsite() == null) {
			website = "No website provided";
		} else {
			website = organization.getWebsite();

		}

		if (organization.getSocialSite() == null) {
			socialSite = "No social site provided";
		} else {
			socialSite = organization.getSocialSite();

		}

		if (organization.getAddressLine1() == null) {
			address_line_1 = "No address provided";
		} else {
			address_line_1 = organization.getAddressLine1();

		}

		if (organization.getAddressLine2() == null) {
			address_line_2 = "No address line 2 provided";
		} else {
			address_line_2 = organization.getAddressLine2();

		}

		if (organization.getLandmark() == null) {
			landmark = "No landmark provided";
		} else {
			landmark = organization.getLandmark();

		}

		if (organization.getPin() == null) {
		} else {
			pincode = organization.getPin();

		}

		Integer pinCodeId = null;
		String pincodeSql = "select * from pincode where pin = " + pincode;
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), pincodeSql);
		if (result.size() > 0) {
			pinCodeId = Integer.parseInt(result.get(0).get("id"));
		}

		if (organization.getBoardlineNumber() == null) {
		} else {
			boardlineNumber = organization.getBoardlineNumber();
		}
		if (organization.getProfile() == null) {
			profile = "No profile provided";
		} else {
			profile = organization.getProfile();

		}

		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(organization.getName())));
		data.put(2, industry);
		data.put(3, website);
		data.put(4, socialSite);
		data.put(5, address_line_1);
		data.put(6, address_line_2);
		data.put(7, landmark);
		data.put(8, pinCodeId);
		data.put(9, boardlineNumber);
		data.put(10, profile);
		Integer organizationId = DBUtils.getInstance().updateObject(sql, data);

		organization = new OrganizationDAOPG().findOrganizationById(organizationId);
		return organization;
	}

	@Override
	public Organization findOrganizationById(Integer id) throws SQLException {

		String sql = "SELECT * from organization where id=" + id;
		Organization organization = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql)) {
			organization = new Organization();
			organization.setId(id);
			organization.setName(row.get("name"));
			organization.setOrganizationType(row.get("organization_type"));
			organization.setIndustry(row.get("industry"));
			organization.setWebsite(row.get("website"));
			organization.setAddressLine1(row.get("address_line_1"));
			if (row.get("address_line_2") != null)
				organization.setAddressLine2(row.get("address_line_2"));
			if (row.get("socialsite") != null)
				organization.setSocialSite(row.get("socialsite"));
			if (row.get("landmark") != null)
				organization.setLandmark(row.get("landmark"));
			if (row.get("pincode_id") != null) {
				Pincode pincode = new PincodeDAOPG().findbyID(Integer.parseInt(row.get("pincode_id")));
				organization.setPincode(pincode);
			}
			if (row.get("contact_phone") != null) {
				organization.setBoardlineNumber(row.get("contact_phone"));
			}
			if (row.get("profile") != null) {
				organization.setProfile(row.get("profile"));
			}

		}
		return organization;
	}

	@Override
	public Organization findOrganizationByUserId(Integer userId) throws SQLException {

		String sql = "SELECT * from organization WHERE id in (SELECT organizationid from org_user WHERE userid=" + userId + ")";
		Organization organization = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql)) {
			organization = new Organization();
			organization.setId(Integer.parseInt(row.get("id")));
			organization.setName(row.get("name"));
			organization.setOrganizationType(row.get("organization_type"));
			organization.setIndustry(row.get("industry"));
			organization.setWebsite(row.get("website"));
			organization.setAddressLine1(row.get("address_line_1"));
			if (row.get("address_line_2") != null)
				organization.setAddressLine2(row.get("address_line_2"));
			if (row.get("socialsite") != null)
				organization.setSocialSite(row.get("socialsite"));
			if (row.get("landmark") != null)
				organization.setLandmark(row.get("landmark"));
			if (row.get("pincode_id") != null) {
				Pincode pincode = new PincodeDAOPG().findbyID(Integer.parseInt(row.get("pincode_id")));
				organization.setPincode(pincode);
			}
			if (row.get("contact_phone") != null) {
				organization.setBoardlineNumber(row.get("contact_phone"));
			}
			if (row.get("profile") != null) {
				organization.setProfile(row.get("profile"));
			}

		}
		return organization;
	}

	@Override
	public Organization update(Organization organization) throws SQLException {

		String sql = "UPDATE organization " + "SET name = ?,industry=?,website=?,socialsite=? , address_line_1=? , address_line_2=?, landmark=? , pincode_id=? , contact_phone=?::bigint, profile=? " + "WHERE id=?";

		String industry, website, socialSite, address_line_1, address_line_2, landmark, profile = null;
		Integer pincode = null;
		String boardlineNumber = null;

		if (organization.getIndustry() == null) {
			industry = "NOT_SPECIFIED";
		} else {
			industry = organization.getIndustry();

		}

		if (organization.getWebsite() == null) {
			website = "No website provided";
		} else {
			website = organization.getWebsite();

		}

		if (organization.getSocialSite() == null) {
			socialSite = "No social site provided";
		} else {
			socialSite = organization.getSocialSite();

		}

		if (organization.getAddressLine1() == null) {
			address_line_1 = "No address provided";
		} else {
			address_line_1 = organization.getAddressLine1();

		}

		if (organization.getAddressLine2() == null) {
			address_line_2 = "No address line 2 provided";
		} else {
			address_line_2 = organization.getAddressLine2();

		}

		if (organization.getLandmark() == null) {
			landmark = "No landmark provided";
		} else {
			landmark = organization.getLandmark();

		}

		if (organization.getPin() == null) {
		} else {
			pincode = organization.getPin();

		}
		Integer pinCodeId = null;
		String pincodeSql = "select * from pincode where pin = " + pincode;
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), pincodeSql);
		if (result.size() > 0) {
			pinCodeId = Integer.parseInt(result.get(0).get("id"));
		}

		if (organization.getBoardlineNumber() != null && !organization.getBoardlineNumber().equalsIgnoreCase("")) {
			boardlineNumber = organization.getBoardlineNumber();
		} else {

		}

		if (organization.getProfile() == null) {
			profile = "No profile provided";
		} else {
			profile = organization.getProfile();

		}

		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, StringUtils.wordsCapitalize(StringUtils.cleanHTML(organization.getName())));
		data.put(2, industry);
		data.put(3, website);
		data.put(4, socialSite);
		data.put(5, address_line_1);
		data.put(6, address_line_2);
		data.put(7, landmark);
		data.put(8, pinCodeId);
		data.put(9, boardlineNumber);
		data.put(10, profile);
		data.put(11, organization.getId());

		DBUtils.getInstance().updateObject(sql, data);

		organization = new OrganizationDAOPG().findOrganizationById(organization.getId());
		return organization;
	}

	@Override
	public Users getMembers(User u, String status, String role, String search, String limit, String offset) throws SQLException {
		if (role == null) {
			role = "";
		}
		if (search == null) {
			search = "";
		}
		String sqlForOrgMember = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (limit != null && offset != null) {
			sqlForOrgMember = "SELECT org_user.userid AS ID, user_profile.name,COUNT (*) OVER () AS full_count, user_profile.profile_image, istar_user.email, istar_user.mobile, license_issued.licensekey, license_issued.expiryon, " + "string_agg ( ROLE .role_name, ',' ORDER BY ROLE . ID ) AS roles, istar_user.designation_id FROM org_user LEFT JOIN user_profile ON user_profile.user_id = org_user.userid LEFT JOIN istar_user ON istar_user. ID = org_user.userid" + " LEFT JOIN license_issued ON istar_user. ID = license_issued.user_id LEFT JOIN user_role ON user_role.userid = istar_user. ID LEFT JOIN ROLE ON user_role.roleid = ROLE . ID WHERE org_user.organizationid IN " + "( SELECT organizationid FROM org_user WHERE userid = " + u.getId() + " ) and ROLE.role_name like '%" + role
					+ "%' and (lower( user_profile. NAME) LIKE 	lower('%" + search + "%') OR lower(istar_user.email) LIKE lower('%" + search + "%')) AND istar_user.is_deleted = FALSE " + "GROUP BY org_user.userid, user_profile.name, user_profile.profile_image, istar_user.email, istar_user.mobile, license_issued.licensekey, " + "istar_user.designation_id, license_issued.expiryon limit " + limit + " offset " + offset;
		} else {
			sqlForOrgMember = "SELECT org_user.userid AS ID, user_profile.name,COUNT (*) OVER () AS full_count, user_profile.profile_image, istar_user.email, istar_user.mobile, license_issued.licensekey, license_issued.expiryon, " + "string_agg ( ROLE .role_name, ',' ORDER BY ROLE . ID ) AS roles, istar_user.designation_id FROM org_user LEFT JOIN user_profile ON user_profile.user_id = org_user.userid LEFT JOIN istar_user ON istar_user. ID = org_user.userid" + " LEFT JOIN license_issued ON istar_user. ID = license_issued.user_id LEFT JOIN user_role ON user_role.userid = istar_user. ID LEFT JOIN ROLE ON user_role.roleid = ROLE . ID WHERE org_user.organizationid IN " + "( SELECT organizationid FROM org_user WHERE userid = " + u.getId() + " ) and ROLE.role_name like '%" + role
					+ "%' and (lower( user_profile. NAME) LIKE 	lower('%" + search + "%') OR lower(istar_user.email) LIKE lower('%" + search + "%')) AND istar_user.is_deleted = FALSE " + "GROUP BY org_user.userid, user_profile.name, user_profile.profile_image, istar_user.email, istar_user.mobile, license_issued.licensekey, " + "istar_user.designation_id, license_issued.expiryon;";
		}
		HashMap<Integer, User> userMap = new HashMap<Integer, User>();
		Integer total = 0;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sqlForOrgMember)) {
			if (!userMap.containsKey(Integer.parseInt(row.get("id")))) {
				User user = new User();
				user.setId(Integer.parseInt(row.get("id")));
				user.setName(row.get("name"));
				user.setEmail(row.get("email"));
				user.setMobile(row.get("mobile"));
				user.setProfileImage(row.get("profile_image"));
				total = Integer.parseInt(row.get("full_count"));
				if (row.get("roles") != null) {
					for (String role1 : row.get("roles").split(",")) {
						user.getRoles().add(role1);
					}
				}
				user.setStatus(UserStatus.Inactive.name());
				if (row.get("licensekey") != null) {
					try {
						user.getLicenseKeys().add(row.get("licensekey"));
						String expiryStr = row.get("expiryon");
						Date expiry = sdf.parse(expiryStr);
						if (new Date().before(expiry)) {
							user.setStatus(UserStatus.Active.name());
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				userMap.put(Integer.parseInt(row.get("id")), user);
			} else {
				User user = userMap.get(Integer.parseInt(row.get("id")));
				if (row.get("licensekey") != null) {
					try {
						user.getLicenseKeys().add(row.get("licensekey"));
						String expiryStr = row.get("expiryon");
						Date expiry = sdf.parse(expiryStr);
						if (new Date().before(expiry)) {
							user.setStatus(UserStatus.Active.name());
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}

			}

		}
		ArrayList<User> users = new ArrayList<User>(userMap.values());

		if (status != null) {
			ArrayList<User> filterUsers = new ArrayList<User>();
			for (User user : users) {
				if (user.getStatus().equalsIgnoreCase(status.trim())) {
					filterUsers.add(user);
				}

			}
			users = filterUsers;
		}

		Users userlist = new Users();
		userlist.setUserList(users);
		userlist.setTotal(total);

		return userlist;
	}

	@Override
	public Dashboard getDashboardCompletionInfo(Integer user_Id) throws SQLException {
		Dashboard dashboard = new Dashboard();
		Integer count = 0;
		Organization organization = findOrganizationByUserId(user_Id);

		String sql = "SELECT * FROM organization WHERE id =" + organization.getId();
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		data = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (data.size() > 0 && data.get(0).get("name").trim().length() > 0) {
			dashboard.setOrganization(true);

			count++;
		} else {
			dashboard.setOrganization(false);

		}
		dashboard.setIsWizardDone(false);
		if (data.size() > 0 && data.get(0).get("is_wizard_done") != null) {
			if (data.get(0).get("is_wizard_done").equalsIgnoreCase("t")) {
				dashboard.setIsWizardDone(true);
			}

		}
		data = new ArrayList<HashMap<String, String>>();
		sql = "SELECT * FROM istar_user where id in (SELECT userid FROM org_user WHERE organizationid = " + organization.getId() + ") and is_deleted  = false LIMIT 2";
		data = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (data.size() > 1) {
			dashboard.setUser(true);
		} else {
			dashboard.setUser(false);
		}
		data = new ArrayList<HashMap<String, String>>();
		sql = "SELECT * FROM istar_group WHERE organization_id = " + organization.getId() + " and is_deleted = false LIMIT 1";
		data = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (data.size() > 0 && dashboard.getUser()) {
			dashboard.setTeam(true);
			count++;
		} else {
			dashboard.setTeam(false);
		}
		data = new ArrayList<HashMap<String, String>>();
		sql = "SELECT * FROM product WHERE organization_id = " + organization.getId() + " and deleted = false LIMIT 1";
		data = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (data.size() > 0) {
			dashboard.setProduct(true);
			count++;
		} else {
			dashboard.setProduct(false);
		}
		data = new ArrayList<HashMap<String, String>>();
		sql = "SELECT * FROM pipeline WHERE organization_id = " + organization.getId() + " and is_active = true LIMIT 1";
		data = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (data.size() > 0) {
			dashboard.setPipeline(true);
			count++;
		} else {
			dashboard.setPipeline(false);
		}
		data = new ArrayList<HashMap<String, String>>();
		sql = "SELECT * FROM simple_playbook where organization_id =" + organization.getId() + " LIMIT 1";
		data = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (data.size() > 0) {
			dashboard.setSimplePlaybook(true);
		} else {
			dashboard.setSimplePlaybook(false);
		}
		data = new ArrayList<HashMap<String, String>>();
		sql = "SELECT * FROM persona WHERE organization_id =" + organization.getId() + " and is_deleted=false LIMIT 1";
		data = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (data.size() > 0) {
			dashboard.setPersona(true);
			count++;
		} else {
			dashboard.setPersona(false);
		}
		data = new ArrayList<HashMap<String, String>>();
		sql = "SELECT * FROM advanced_playbook WHERE stage_task_id in (SELECT id FROM stage_task WHERE stage_id in (SELECT id FROM pipeline_stage WHERE pipeline_id in (SELECT id FROM pipeline WHERE organization_id = " + organization.getId() + ")))";
		data = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (data.size() > 0 && dashboard.getSimplePlaybook()) {
			dashboard.setAdvancedPlaybook(true);
			count++;
		} else {
			dashboard.setAdvancedPlaybook(false);
		}

		if ((count / 6) == 1) {
			sql = "update organization set is_wizard_done = ? where id = ?";
			HashMap<Integer, Object> org_data = new HashMap<Integer, Object>();
			org_data.put(1, true);
			org_data.put(2, organization.getId());
			DBUtils.getInstance().updateObject(sql, org_data);
			dashboard.setIsWizardDone(true);
		}
		String overPersentStr = new DecimalFormat("##.##").format((float) (count * 100) / 6);
		dashboard.setOverAllpercentage(Float.parseFloat(overPersentStr));

		return dashboard;
	}

	@Override
	public boolean isValidOrgWebsite(String website, Integer org_id) throws SQLException {
		String sql = "select * from organization where website like '%" + website + "%'";
		ArrayList<HashMap<String, String>> organizationList = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		for (HashMap<String, String> row : organizationList) {
			if (row.get("website").trim().equalsIgnoreCase(website.trim())
					&& (org_id == null || Integer.parseInt(row.get("id")) != org_id)) {
				return false;
			}
		}
		return true;
	}
}
