package db.postgres;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import constants.UserStatus;
import db.DBUtils;
import db.interfaces.TeamDAO;
import pojos.Persona;
import pojos.Product;
import pojos.Team;
import pojos.User;
import pojos.Users;
import strings.StringUtils;

public class TeamDAOPG implements TeamDAO {
	@Override
	public Team findbyId(Integer id) throws SQLException {
		String sql = "SELECT istar_group.*, owner_profile. NAME AS owner_name, string_agg (ROLE .role_name, ',') AS role_names, member_profile.user_id AS member_id,"
				+ " member_profile. NAME AS member_name,member_profile.profile_image as member_image,istar_user.email FROM istar_group LEFT JOIN group_user ON istar_group. ID = group_user.qroupid LEFT JOIN istar_user ON "
				+ "istar_user. ID = group_user.userid AND istar_user.is_deleted = FALSE AND istar_user.is_supended = FALSE LEFT JOIN user_profile member_profile "
				+ "ON member_profile.user_id = istar_user. ID LEFT JOIN user_profile owner_profile ON owner_profile.user_id = istar_group. OWNER LEFT JOIN "
				+ "user_role ON user_role.userid = owner_profile.user_id LEFT JOIN ROLE ON ROLE . ID = user_role.roleid WHERE istar_group. ID = "
				+ id + "and istar_group.is_deleted = 'false'"
				+ " GROUP BY istar_group. ID, owner_profile. NAME, member_profile.user_id, member_profile. NAME ,member_profile.profile_image,istar_user.email ";
		Team team = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (team == null) {
				team = new Team();
				team.setId(id);
				team.setName(row.get("name"));
				team.setDescription(row.get("description"));
				team.setOrganizationId(Integer.parseInt(row.get("organization_id")));

				if (row.get("owner") != null) {
					team.setOwnerId(Integer.parseInt(row.get("owner")));
					User owner = new User();
					owner.setId(Integer.parseInt(row.get("owner")));
					owner.setName(row.get("owner_name"));
					if (row.get("role_names") != null) {
						for (String role : row.get("role_names").split(",")) {
							owner.getRoles().add(role);
						}
					}
					team.setOwner(owner);
				}

			}
			if (row.get("member_id") != null) {
				User member = new User();
				member.setId(Integer.parseInt(row.get("member_id")));
				member.setName(row.get("member_name"));
				member.setEmail(row.get("email"));
				member.setProfileImage(row.get("member_image"));
				team.getUsers().add(member);
			}
			if (row.get("persona") != null) {
				ArrayList<Persona> personaList = new ArrayList<Persona>();
				for (String personaId : row.get("persona").split(",")) {
					String personaName = new PersonaDAOPG().getPersonaName(Integer.parseInt(personaId));
					if (personaName.trim().length() > 0) {
						Persona personaData = new Persona();
						personaData.setId(Integer.parseInt(personaId));
						personaData.setName(personaName);
						personaList.add(personaData);
					}
				}
				team.setPersonas(personaList);
			}
			if (row.get("product") != null) {
				ArrayList<Product> productList = new ArrayList<Product>();
				for (String productId : row.get("product").split(",")) {
					String productName = new ProductDAOPG().getProductName(Integer.parseInt(productId));
					if (productName.trim().length() > 0) {
						Product productData = new Product();
						productData.setId(Integer.parseInt(productId));
						productData.setName(productName);
						productList.add(productData);
					}
				}
				team.setProducts(productList);
			}

		}
		return team;
	}

	@Override
	public ArrayList<Team> findbyOrganizationId(Integer organizationID) throws SQLException {
		String sql = "SELECT istar_group. ID, istar_group.description, t1. NAME AS owner_name, string_agg (DISTINCT ROLE .role_name, ',') AS role_names, istar_group. NAME, "
				+ "istar_group. OWNER, group_user.userid AS member_id, t2. NAME AS member_name, t2.profile_image AS member_profile, istar_user.email, "
				+ "string_agg (DISTINCT pipeline_persona.persona_id :: text, ',') as persona, string_agg(DISTINCT pipeline_product.product_id :: text, ',') as product "
				+ "FROM istar_group left join pipeline_team on pipeline_team.team_id = istar_group.id "
				+ "left join pipeline_product on pipeline_product.pipeline_id = pipeline_team.pipeline_id left join pipeline_persona on pipeline_persona.pipeline_id = pipeline_team.pipeline_id "
				+ "LEFT JOIN user_profile t1 ON t1.user_id = istar_group. OWNER  LEFT JOIN group_user ON group_user.qroupid = istar_group. ID LEFT JOIN user_profile t2 ON t2.user_id = group_user.userid "
				+ "LEFT JOIN istar_user ON istar_user. ID = t2.user_id AND istar_user.is_deleted = FALSE AND istar_user.is_supended = FALSE LEFT JOIN user_role ON user_role.userid = t1.user_id "
				+ "LEFT JOIN ROLE ON ROLE . ID = user_role.roleid WHERE organization_id = " + organizationID
				+ " AND group_type = 'SALES_TEAM'  AND istar_group.is_deleted = FALSE "
				+ "GROUP BY istar_group. ID, t1. NAME, istar_group.description, group_user.userid, t2. NAME, t2.profile_image, istar_user.email ORDER BY ID DESC";

		HashMap<Integer, Team> teamMap = new HashMap<Integer, Team>();

		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (teamMap.containsKey(Integer.parseInt(row.get("id")))) {
				Team team = teamMap.get(Integer.parseInt(row.get("id")));
				if (row.get("member_id") != null) {
					User member = new User();
					member.setId(Integer.parseInt(row.get("member_id")));
					member.setName(row.get("member_name"));
					member.setEmail(row.get("email"));
					member.setProfileImage(row.get("member_profile"));
					team.getUsers().add(member);
				}
			} else {

				Team team = new Team();
				team.setId(Integer.parseInt(row.get("id")));
				team.setName(row.get("name"));
				team.setDescription(row.get("description"));

				if (row.get("owner") != null) {
					User owner = new User();
					owner.setId(Integer.parseInt(row.get("owner")));
					owner.setName(row.get("owner_name"));
					if (row.get("role_names") != null) {
						for (String role : row.get("role_names").split(",")) {
							owner.getRoles().add(role);
						}
					}
					team.setOwner(owner);
				}
				if (row.get("member_id") != null) {
					User member = new User();
					member.setId(Integer.parseInt(row.get("member_id")));
					member.setName(row.get("member_name"));
					member.setEmail(row.get("email"));
					member.setProfileImage(row.get("member_profile"));

					team.getUsers().add(member);
				}
				if (row.get("persona") != null) {
					for (String id : row.get("persona").split(",")) {
						String personaName = new PersonaDAOPG().getPersonaName(Integer.parseInt(id));
						if (personaName != null && personaName.trim().length() > 0) {
							Persona personaData = new Persona();
							personaData.setId(Integer.parseInt(id));
							personaData.setName(personaName);
							team.getPersonas().add(personaData);
						}
					}
				}
				if (row.get("product") != null) {
					for (String id : row.get("product").split(",")) {
						String productName = new ProductDAOPG().getProductName(Integer.parseInt(id));
						if (productName != null && productName.trim().length() > 0) {
							Product productData = new Product();
							productData.setId(Integer.parseInt(id));
							productData.setName(productName);
							team.getProducts().add(productData);
						}
					}
				}
				teamMap.put(Integer.parseInt(row.get("id")), team);
			}
		}
		ArrayList<Team> results = new ArrayList<Team>();
		TreeMap<Integer, Team> sorted = new TreeMap<>(teamMap);
		for (Map.Entry<Integer, Team> entry : sorted.entrySet()) {
			results.add(entry.getValue());
		}
		Collections.sort(results, teamCompareator);
		return results;
	}

	public static Comparator<Team> teamCompareator = new Comparator<Team>() {

		public int compare(Team p1, Team p2) {
			// ascending order
			return p1.getId().compareTo(p2.getId());

		}
	};

	@Override
	public Team createTeam(Team team, User user) throws SQLException {
		String sqlTeam = "INSERT INTO istar_group (created_at, name, updated_at, organization_id, description, group_type,group_mode_type,is_deleted ,owner)"
				+ " VALUES ( 'now()',?, 'now()',?, ?, 'SALES_TEAM', 'BOTH', 'f',? );";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		String description = null;
		if (team.getDescription() != null) {
			description = team.getDescription();
		} else {
			description = "No description";
		}
		String organizationId = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				"select * from org_user where userid= " + user.getId()).get(0).get("organizationid");
		data.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(team.getName())));
		data.put(2, Integer.parseInt(organizationId));
		data.put(3, description);
		data.put(4, team.getOwnerId());

		Integer teamId = DBUtils.getInstance().updateObject(sqlTeam, data);

		if (team.getOwnerId() != null) {
			String sqlUserRole = "SELECT * from user_role WHERE userid=" + team.getOwnerId();
			ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sqlUserRole);
			boolean isAlreadyManager = false;
			for (HashMap<String, String> hashMap : result) {
				if (Integer.parseInt(hashMap.get("roleid")) == 13) {
					isAlreadyManager = true;
				}
			}
			if (!isAlreadyManager) {
				String userRoleInsert = "INSERT INTO user_role (userid, roleid) VALUES (?,?);";
				HashMap<Integer, Object> roleData = new HashMap<Integer, Object>();
				roleData.put(1, team.getOwnerId());
				roleData.put(2, 13);
				DBUtils.getInstance().updateObject(userRoleInsert, roleData);
			}

			if (team.getUserIds() != null) {
				for (Integer userID : team.getUserIds()) {
					if (team.getOwnerId().intValue() != userID.intValue()) {
						if (checkManagerUser(team.getOwnerId(), userID, userID, 0)) {
							String deleteGroupUserSql = "DELETE from group_user WHERE userid=?;";
							HashMap<Integer, Object> deleteGroupUserData = new HashMap<Integer, Object>();
							deleteGroupUserData.put(1, userID);
							DBUtils.getInstance().updateObject(deleteGroupUserSql, deleteGroupUserData);

							String groupUserSql = "INSERT INTO group_user (qroupid, userid) VALUES (?,?);";
							HashMap<Integer, Object> groupUserdata = new HashMap<Integer, Object>();
							groupUserdata.put(1, teamId);
							groupUserdata.put(2, userID);
							DBUtils.getInstance().updateObject(groupUserSql, groupUserdata);

							String deleteUserManagerSql = "DELETE from user_manager WHERE user_id=?;";
							HashMap<Integer, Object> deleteUserManagerData = new HashMap<Integer, Object>();
							deleteUserManagerData.put(1, userID);
							DBUtils.getInstance().updateObject(deleteUserManagerSql, deleteUserManagerData);

							String insertUserManagerSql = "INSERT INTO user_manager (user_id, manager_id) VALUES (?,?);";
							HashMap<Integer, Object> insertUserManagerData = new HashMap<Integer, Object>();
							insertUserManagerData.put(1, userID);
							insertUserManagerData.put(2, team.getOwnerId());
							DBUtils.getInstance().updateObject(insertUserManagerSql, insertUserManagerData);
						}
					}
				}
			}
		}
		team = new TeamDAOPG().findbyId(teamId);
		return team;
	}

	@Override
	public Team updateTeam(Team team) throws SQLException {
		String sqlTeam = "UPDATE istar_group SET name=?, updated_at=now(), description=? WHERE id=?";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		String description = null;
		if (team.getDescription() != null) {
			description = team.getDescription();
		} else {
			description = "No description";
		}

		data.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(team.getName())));
		data.put(2, description);
		data.put(3, team.getId());

		DBUtils.getInstance().updateObject(sqlTeam, data);
		removeAllTeamMembers(team.getId());
		addMembersOwner(team);

		team = new TeamDAOPG().findbyId(team.getId());
		return team;
	}

	@Override
	public void createTeamMapping(Team team) throws SQLException {
		HashMap<Integer, Object> processMap = new HashMap<Integer, Object>();
		for (Integer id : team.getProcessIds()) {
			String sql = "select * from pipeline_team where pipeline_id = " + id + " and team_id = " + team.getId()
					+ "";
			ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql);
			if (result.size() == 0) {
				String teamSql = "Insert into pipeline_team (pipeline_id, team_id) values(?,?)";
				processMap = new HashMap<Integer, Object>();
				processMap.put(1, id);
				processMap.put(2, team.getId());
				DBUtils.getInstance().updateObject(teamSql, processMap);
			}
		}

	}

	@Override
	public Team addMembersOwner(Team team) throws SQLException {
		if (team.getOwnerId() == null) {
			String sql = "SELECT * from istar_group WHERE id=" + team.getId();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql)) {
				if (row.get("owner") != null) {
					team.setOwnerId(Integer.parseInt(row.get("owner")));
				}
			}
		}
		if (team.getUserIds() != null) {
			for (Integer userID : team.getUserIds()) {
				try {
					if (team.getOwnerId() != null) {
						if (team.getOwnerId().intValue() != userID.intValue()) {
							if (checkManagerUser(team.getOwnerId(), userID, userID, 0)) {
								String deleteGroupUserSql = "DELETE from group_user WHERE userid=?;";
								HashMap<Integer, Object> deleteGroupUserData = new HashMap<Integer, Object>();
								deleteGroupUserData.put(1, userID);
								DBUtils.getInstance().updateObject(deleteGroupUserSql, deleteGroupUserData);

								String groupUserSql = "INSERT INTO group_user (qroupid, userid) VALUES (?,?)";
								HashMap<Integer, Object> data = new HashMap<Integer, Object>();
								data.put(1, team.getId());
								data.put(2, userID);
								DBUtils.getInstance().updateObject(groupUserSql, data);

								String deleteUserManagerSql = "DELETE from user_manager WHERE user_id=?;";
								HashMap<Integer, Object> deleteUserManagerData = new HashMap<Integer, Object>();
								deleteUserManagerData.put(1, userID);
								DBUtils.getInstance().updateObject(deleteUserManagerSql, deleteUserManagerData);

								String insertUserManagerSql = "INSERT INTO user_manager (user_id, manager_id) VALUES (?,?);";
								HashMap<Integer, Object> insertUserManagerData = new HashMap<Integer, Object>();
								insertUserManagerData.put(1, userID);
								insertUserManagerData.put(2, team.getOwnerId());
								DBUtils.getInstance().updateObject(insertUserManagerSql, insertUserManagerData);
							}
						}
					} else {
						String deleteGroupUserSql = "DELETE from group_user WHERE userid=?;";
						HashMap<Integer, Object> deleteGroupUserData = new HashMap<Integer, Object>();
						deleteGroupUserData.put(1, userID);
						DBUtils.getInstance().updateObject(deleteGroupUserSql, deleteGroupUserData);

						String groupUserSql = "INSERT INTO group_user (qroupid, userid) VALUES (?,?)";
						HashMap<Integer, Object> data = new HashMap<Integer, Object>();
						data.put(1, team.getId());
						data.put(2, userID);
						DBUtils.getInstance().updateObject(groupUserSql, data);
					}
				} catch (SQLException e) {

				}
			}
		}
		if (team.getOwnerId() != null) {

			String updateTeamOwner = "UPDATE istar_group SET owner=? WHERE id=?;";
			HashMap<Integer, Object> data = new HashMap<Integer, Object>();
			data.put(1, team.getOwnerId());
			data.put(2, team.getId());
			DBUtils.getInstance().updateObject(updateTeamOwner, data);
		}
		team = new TeamDAOPG().findbyId(team.getId());
		return team;
	}

	public boolean checkManagerUser(Integer ownerId, Integer tempUserId, Integer userID, Integer count)
			throws SQLException {
		if (count > 20) {
			return false;
		}
		String sql = "SELECT * from user_manager WHERE user_id=" + ownerId;
		ArrayList<HashMap<String, String>> results = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (results.size() > 0) {
			for (HashMap<String, String> row : results) {
				ownerId = Integer.parseInt(row.get("manager_id"));
				tempUserId = Integer.parseInt(row.get("user_id"));
			}
			if (tempUserId == ownerId) {
				return false;
			}
			if (userID.intValue() == ownerId.intValue()) {
				return false;
			} else {
				return checkManagerUser(ownerId, tempUserId, userID, count + 1);
			}
		} else {
			return true;
		}
	}

	@Override
	public Team removeMembers(Team team) throws SQLException {
		if (team.getUserIds() != null) {
			for (Integer userID : team.getUserIds()) {
				String deleteGroupUser = "DELETE from group_user WHERE userid=?";
				HashMap<Integer, Object> deletData = new HashMap<Integer, Object>();
				deletData.put(1, userID);
				DBUtils.getInstance().updateObject(deleteGroupUser, deletData);

				String deleteUserManagerSql = "DELETE from user_manager WHERE user_id=?;";
				HashMap<Integer, Object> deleteUserManagerData = new HashMap<Integer, Object>();
				deleteUserManagerData.put(1, userID);
				DBUtils.getInstance().updateObject(deleteUserManagerSql, deleteUserManagerData);

			}
		}
		team = new TeamDAOPG().findbyId(team.getId());
		return team;
	}

	@Override
	public void removeAllTeamMembers(Integer teamId) throws SQLException {

		String sqlUserManager = "DELETE from user_manager WHERE user_id in (SELECT userid FROM group_user WHERE qroupid=?)";
		HashMap<Integer, Object> deleteUserManagerData = new HashMap<Integer, Object>();
		deleteUserManagerData.put(1, teamId);
		DBUtils.getInstance().updateObject(sqlUserManager, deleteUserManagerData);

		String sql = "DELETE from group_user WHERE qroupid=?";
		HashMap<Integer, Object> deletData = new HashMap<Integer, Object>();
		deletData.put(1, teamId);

		DBUtils.getInstance().updateObject(sql, deletData);

	}

	@Override
	public Boolean deleteTeam(Integer id) throws SQLException {
		try {
			removeAllTeamMembers(id);
			String sqlTeam = "UPDATE istar_group SET is_deleted=? WHERE id=?;";
			HashMap<Integer, Object> data = new HashMap<Integer, Object>();
			data.put(1, true);
			data.put(2, id);
			DBUtils.getInstance().updateObject(sqlTeam, data);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public Users getUsersForTeamCreation(User u, String role, String search, String limit, String offset,
			Integer teamId) throws SQLException {
		if (role == null) {
			role = "";
		}
		if (search == null) {
			search = "";
		}

		String status = UserStatus.Active.name();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sqlUser = "";
		if (limit != null && offset != null) {
			sqlUser = "SELECT org_user.userid AS ID, user_profile. NAME, COUNT (*) OVER () AS full_count, user_profile.profile_image,"
					+ " istar_user.email, istar_user.mobile, license_issued.licensekey, license_issued.expiryon,"
					+ " string_agg ( ROLE .role_name, ',' ORDER BY ROLE . ID ) AS roles, istar_user.designation_id"
					+ " FROM org_user LEFT JOIN user_profile ON user_profile.user_id = org_user.userid LEFT JOIN"
					+ " istar_user ON istar_user. ID = org_user.userid LEFT JOIN license_issued ON istar_user. ID"
					+ " = license_issued.user_id LEFT JOIN user_role ON user_role.userid = istar_user. ID LEFT JOIN ROLE"
					+ " ON user_role.roleid = ROLE . ID WHERE org_user.organizationid IN ( SELECT organizationid FROM org_user WHERE"
					+ " userid = " + u.getId()
					+ " ) AND org_user.userid NOT IN ( SELECT userid FROM group_user WHERE qroupid IN"
					+ " ( SELECT ID FROM istar_group WHERE organization_id IN ( SELECT organizationid FROM org_user WHERE"
					+ " userid = " + u.getId() + " ) AND is_deleted = FALSE ) ) AND ROLE .role_name LIKE '%" + role
					+ "%' AND ( LOWER " + "(user_profile. NAME) LIKE LOWER ('%" + search
					+ "%') OR LOWER (istar_user.email) LIKE LOWER ('%" + search + "%') ) AND "
					+ "istar_user.is_deleted = FALSE GROUP BY org_user.userid, user_profile. NAME, user_profile.profile_image, istar_user.email,"
					+ " istar_user.mobile, license_issued.licensekey, istar_user.designation_id, license_issued.expiryon  limit "
					+ limit + " offset " + offset;
		} else {
			sqlUser = "SELECT org_user.userid AS ID, user_profile. NAME, COUNT (*) OVER () AS full_count, user_profile.profile_image,"
					+ " istar_user.email, istar_user.mobile, license_issued.licensekey, license_issued.expiryon,"
					+ " string_agg ( ROLE .role_name, ',' ORDER BY ROLE . ID ) AS roles, istar_user.designation_id"
					+ " FROM org_user LEFT JOIN user_profile ON user_profile.user_id = org_user.userid LEFT JOIN"
					+ " istar_user ON istar_user. ID = org_user.userid LEFT JOIN license_issued ON istar_user. ID"
					+ " = license_issued.user_id LEFT JOIN user_role ON user_role.userid = istar_user. ID LEFT JOIN ROLE"
					+ " ON user_role.roleid = ROLE . ID WHERE org_user.organizationid IN ( SELECT organizationid FROM org_user WHERE"
					+ " userid = " + u.getId()
					+ " ) AND org_user.userid NOT IN ( SELECT userid FROM group_user WHERE qroupid IN"
					+ " ( SELECT ID FROM istar_group WHERE organization_id IN ( SELECT organizationid FROM org_user WHERE"
					+ " userid = " + u.getId() + " ) AND is_deleted = FALSE ) ) AND ROLE .role_name LIKE '%" + role
					+ "%' AND ( LOWER " + "(user_profile. NAME) LIKE LOWER ('%" + search
					+ "%') OR LOWER (istar_user.email) LIKE LOWER ('%" + search + "%') ) AND "
					+ "istar_user.is_deleted = FALSE GROUP BY org_user.userid, user_profile. NAME, user_profile.profile_image, istar_user.email,"
					+ " istar_user.mobile, license_issued.licensekey, istar_user.designation_id, license_issued.expiryon;";
		}
		HashMap<Integer, User> userMap = new HashMap<Integer, User>();
		Integer total = 0;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sqlUser)) {
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
		Integer teamOwnerId = null;
		if (teamId != null) {
			String teamSql = "SELECT * from istar_group WHERE id=" + teamId;
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), teamSql)) {
				if (row.get("owner") != null) {
					teamOwnerId = Integer.parseInt(row.get("owner"));
				}
			}
		}

		ArrayList<User> users = new ArrayList<User>(userMap.values());
		int itadminCount = 0;
		if (status != null) {
			ArrayList<User> filterUsers = new ArrayList<User>();
			for (User user : users) {
				// if (user.getStatus().equalsIgnoreCase(status.trim())) { //This is for license
				// of the user. Is license is active orr not
				if (user.getRoles().size() == 1 && user.getRoles().contains("IT_ADMIN")) {
					itadminCount++;
				} else {
					if (teamOwnerId != null) {
						if (user.getId().intValue() != teamOwnerId.intValue()) {
							filterUsers.add(user);
						}else {
							itadminCount++;
						}
					} else {
						filterUsers.add(user);
					}
				}
				// }

			}
			users = filterUsers;
		}

		Users userlist = new Users();
		userlist.setUserList(users);
		userlist.setTotal(total - itadminCount);

		return userlist;
	}

	@Override
	public Users getUsersAndManagersForTeamCreation(Integer userId, String role, String search) throws SQLException {
		if (role == null) {
			role = "";
		}
		if (search == null) {
			search = "";
		}
		String status = UserStatus.Active.name();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sqlUser = "SELECT org_user.userid AS ID, user_profile. NAME, COUNT (*) OVER () AS full_count, user_profile.profile_image,"
				+ " istar_user.email, istar_user.mobile, license_issued.licensekey, license_issued.expiryon,"
				+ " string_agg ( ROLE .role_name, ',' ORDER BY ROLE . ID ) AS roles, istar_user.designation_id"
				+ " FROM org_user LEFT JOIN user_profile ON user_profile.user_id = org_user.userid LEFT JOIN"
				+ " istar_user ON istar_user. ID = org_user.userid LEFT JOIN license_issued ON istar_user. ID"
				+ " = license_issued.user_id LEFT JOIN user_role ON user_role.userid = istar_user. ID LEFT JOIN ROLE"
				+ " ON user_role.roleid = ROLE . ID WHERE org_user.organizationid IN ( SELECT organizationid FROM org_user WHERE"
				+ " userid = " + userId
				+ " ) AND org_user.userid NOT IN ( SELECT userid FROM group_user WHERE qroupid IN"
				+ " ( SELECT ID FROM istar_group WHERE organization_id IN ( SELECT organizationid FROM org_user WHERE"
				+ " userid = " + userId + " ) AND is_deleted = FALSE ) ) AND ROLE .role_name LIKE '%" + role
				+ "%' AND ( LOWER " + "(user_profile. NAME) LIKE LOWER ('%" + search
				+ "%') OR LOWER (istar_user.email) LIKE LOWER ('%" + search + "%') ) AND "
				+ "istar_user.is_deleted = FALSE GROUP BY org_user.userid, user_profile. NAME, user_profile.profile_image, istar_user.email,"
				+ " istar_user.mobile, license_issued.licensekey, istar_user.designation_id, license_issued.expiryon";

		HashMap<Integer, User> userMap = new HashMap<Integer, User>();

		userMap = getAllUsers(sqlUser, userMap);

		sqlUser = " select user_profile.user_id AS ID, user_profile. NAME, COUNT (*) OVER () AS full_count, user_profile.profile_image, istar_user.email, "
				+ "istar_user.mobile, license_issued.licensekey, license_issued.expiryon, string_agg ( ROLE .role_name, ',' ORDER BY ROLE . ID ) AS roles, "
				+ "istar_user.designation_id from istar_group left join user_profile on user_profile.user_id = istar_group.owner "
				+ "left join istar_user on istar_user.id = istar_group.owner LEFT JOIN license_issued ON istar_group.owner = license_issued.user_id "
				+ "left join user_role on user_role.userid = istar_group.owner left join role on role.id = user_role.roleid where istar_group.organization_id in "
				+ "(select organizationid from org_user where userid = " + userId
				+ ") and istar_group.is_deleted = FALSE AND ROLE .role_name LIKE '%" + role + "%' AND "
				+ "( LOWER (user_profile. NAME) LIKE LOWER ('%" + search
				+ "%') OR LOWER (istar_user.email) LIKE LOWER ('%" + search + "%') ) AND istar_user.is_deleted = FALSE "
				+ "GROUP BY user_profile.user_id, user_profile. NAME, user_profile.profile_image, istar_user.email, istar_user.mobile, license_issued.licensekey, "
				+ "istar_user.designation_id, license_issued.expiryon  ";

		userMap = getAllUsers(sqlUser, userMap);

		Integer total = userMap.size();
		ArrayList<User> users = new ArrayList<User>(userMap.values());

		if (status != null) {
			ArrayList<User> filterUsers = new ArrayList<User>();
			for (User user : users) {
				// if (user.getStatus().equalsIgnoreCase(status.trim())) {
				filterUsers.add(user);
				// }

			}
			users = filterUsers;
		}

		Users userlist = new Users();
		userlist.setUserList(users);
		userlist.setTotal(total);

		return userlist;
	}

	private HashMap<Integer, User> getAllUsers(String sqlUser, HashMap<Integer, User> userMap)
			throws NumberFormatException, SQLException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sqlUser)) {
			if (!userMap.containsKey(Integer.parseInt(row.get("id")))) {
				User user = new User();
				user.setId(Integer.parseInt(row.get("id")));
				user.setName(row.get("name"));
				user.setEmail(row.get("email"));
				user.setMobile(row.get("mobile"));
				user.setProfileImage(row.get("profile_image"));
				// total = Integer.parseInt(row.get("full_count"));
				if (row.get("roles") != null) {
					for (String role1 : row.get("roles").split(",")) {
						user.getRoles().add(role1);
					}
				}
				// user.setStatus(UserStatus.Inactive.name());
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
		return userMap;
	}

	@Override
	public Team dummyTeamCreation(Integer userId) throws SQLException {
		String sql = "select * from organization where id in (select organizationid from org_user where userid ="
				+ userId + ")";
		ArrayList<HashMap<String, String>> orgData = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		String org_id = orgData.get(0).get("id");
		String emailSuffix = "@dummy.com";
		if (orgData.get(0).get("website") != null && orgData.get(0).get("website").length() > 0
				&& orgData.get(0).get("website").split("\\.").length > 1) {
			String[] split = orgData.get(0).get("website").replaceAll("/", "").split("\\.");
			emailSuffix = "@" + split[split.length - 2] + "." + split[split.length - 1];
		} else if (orgData.get(0).get("name") != null && orgData.get(0).get("name").length() > 0) {
			emailSuffix = "@" + orgData.get(0).get("name").replaceAll(" ", "").toLowerCase() + ".com";
		}
		ArrayList<Integer> userIds = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			String userEmail = "user_" + org_id + "_0" + i + emailSuffix;
			String userSql = "select * from istar_user where email like '%" + userEmail + "%'";
			ArrayList<HashMap<String, String>> userData = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), userSql);
			if (userData.size() > 0) {
				userIds.add(Integer.parseInt(userData.get(0).get("id")));
			}
		}
		Team team = new Team();
		team.setName("Dummy Team");
		team.setDescription("This is a dummy version for team");
		if (userIds.size() == 0) {
			userIds = createDummyTeamUser(userId, Integer.parseInt(org_id), emailSuffix);
			team.setOwnerId(userIds.get(0));
			userIds.remove(0);
			team.setUserIds(userIds);
		} else {

			String updateUserSql = "UPDATE istar_user SET is_verified = true, is_supended = false, is_deleted = false WHERE id = ?";
			String groupUserSql = "DELETE FROM group_user where userid = ?";
			String userManagerSql = "DELETE FROM user_manager where user_id = ?";
			for (Integer id : userIds) {
				HashMap<Integer, Object> data = new HashMap<Integer, Object>();
				data = new HashMap<Integer, Object>();
				data.put(1, id);
				DBUtils.getInstance().updateObject(updateUserSql, data);
				data = new HashMap<Integer, Object>();
				data.put(1, id);
				DBUtils.getInstance().updateObject(groupUserSql, data);
				data = new HashMap<Integer, Object>();
				data.put(1, id);
				DBUtils.getInstance().updateObject(userManagerSql, data);
			}
			team.setOwnerId(userIds.get(0));
			userIds.remove(0);
			team.setUserIds(userIds);
		}
		User user = new User();
		user.setId(userId);
		return createTeam(team, user);
	}

	private ArrayList<Integer> createDummyTeamUser(Integer userId, Integer org_id, String emailSuffix)
			throws SQLException {
		ArrayList<Integer> userIds = new ArrayList<Integer>();
		ArrayList<Integer> roleId = new ArrayList<Integer>();
		User user = new User();
		String userName = "manager";
		user.setName(userName);
		user.setProfileImage("https://storage.googleapis.com/istar-user-images/files/D.png");
		String email = userName;
		email = email + emailSuffix;
		user.setEmail(email);
		user.setMobile("+13017109180");
		roleId.add(13);
		user.setRoleIds(roleId);
		User userData = new UserDAOPG().createUser(user, userId);
		userIds.add(userData.getId());
		for (int i = 1; i < 10; i++) {
			roleId = new ArrayList<Integer>();
			userName = "demo" + i;
			user = new User();
			user.setName(userName);
			user.setProfileImage("https://storage.googleapis.com/istar-user-images/files/D.png");
			email = userName;
			email = email + emailSuffix;
			user.setEmail(email);
			user.setMobile("+13017109180");
			roleId.add(14);
			user.setRoleIds(roleId);
			userData = new UserDAOPG().createUser(user, userId);
			userIds.add(userData.getId());
		}
		return userIds;
	}

	@Override
	public String getTeamName(int teamId) throws SQLException {
		String sql = "select name from istar_group where id = " + teamId + " and is_deleted = false";
		String name = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			name = row.get("name");
		}
		return name;
	}

}
