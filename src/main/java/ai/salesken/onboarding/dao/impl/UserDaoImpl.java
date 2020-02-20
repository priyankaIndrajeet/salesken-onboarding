package ai.salesken.onboarding.dao.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import ai.salesken.onboarding.dao.UserDao;
import ai.salesken.onboarding.enums.UserStatus;
import ai.salesken.onboarding.model.User;
import ai.salesken.onboarding.utils.DButils.DBUtils;
 
public class UserDaoImpl implements UserDao {
	@Override
	public User findbyEmail(String email) throws SQLException {
		User user = null;
		String sql = "SELECT istar_user. ID,  istar_user.email, istar_user.mobile, istar_user.auth_token AS token, user_profile.name, sales_manager_profile.language_pref AS langg, user_profile.profile_image AS profileImage, istar_user.password, istar_user.is_verified AS isVerified, istar_user.is_supended AS isSuspended, istar_user.is_deleted isDeleted FROM istar_user, user_profile, sales_manager_profile WHERE istar_user.email='"
				+ email
				+ "' AND user_profile.user_id = istar_user. ID AND sales_manager_profile.user_id = istar_user. ID;";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			user = new User();
			user.setId(Integer.parseInt(row.get("id")));
			user.setEmail(row.get("email"));
			if (row.get("isdeleted").equalsIgnoreCase("t")) {
				user.setIsDeleted(true);
			} else {
				user.setIsDeleted(false);
			}

			if (row.get("issuspended").equalsIgnoreCase("t")) {
				user.setIsSuspended(true);
			} else {
				user.setIsSuspended(false);
			}

			if (row.get("isverified").equalsIgnoreCase("t")) {
				user.setIsVerified(true);
			} else {
				user.setIsVerified(false);
			}
			 
			user.setNumber(row.get("mobile"));
			user.setFirst_name(row.get("name"));
			user.setPassword(row.get("password"));
			user.setRoles(getRoles(Integer.parseInt(row.get("id"))));
			user.setStatus(UserStatus.Inactive.name());
			getLicenseKeys(user);
			// user.setLicenseKeys(getLicenseKeys(Integer.parseInt(row.get("id"))));
		}
		return user;
	}

	@Override
	public Boolean isValidLicense(User user) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select * from license_issued WHERE user_id=" + user.getId();
		Date currentDate = new Date();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (row.get("expiryon") != null) {
				String expireAt = row.get("expiryon");
				Date expireDate = null;
				try {
					expireDate = sdf.parse(expireAt);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (expireDate != null && expireDate.compareTo(currentDate) >= 0) {
					return true;
				}
			}

		}
		return true;
	}
	private ArrayList<String> getRoles(Integer id) throws SQLException {
		ArrayList<String> roles = new ArrayList<String>();
		String sql = "select role.id, role.role_name from user_role, role where role.id=user_role.roleid and userid="
				+ id;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			roles.add(row.get("role_name"));
		}
		return roles;
	}
	private User getLicenseKeys(User user) throws SQLException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "SELECT * from license_issued WHERE user_id=" + user.getId();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			user.getLicenseKeys().add(row.get("licensekey"));
			try {
				if (row.get("expiryon") != null) {
					String expiryStr = row.get("expiryon");
					Date expiry = sdf.parse(expiryStr);
					if (new Date().before(expiry)) {
						user.setStatus(UserStatus.Active.name());
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		return user;
	}

}
