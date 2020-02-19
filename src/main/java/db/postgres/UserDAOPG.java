/**
 * 
 */
package db.postgres;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import constants.UserStatus;
import db.DBProperties;
import db.DBUtils;
import db.interfaces.UserDAO;
import pojos.BulkUser;
import pojos.BulkUserField;
import pojos.Designation;
import pojos.DesignationUser;
import pojos.Organization;
import pojos.Role;
import pojos.SIPCredential;
import pojos.Team;
import pojos.User;
import pojos.ValidateResponse;
import strings.StringUtils;
import validators.impl.PhoneNumberValidator;

/**
 * @author Vaibhav Verma
 *
 */
public class UserDAOPG implements UserDAO {

	@Override
	public User findbyID(Integer id) throws SQLException {
		User user = null;
		String sql = "SELECT istar_user. ID,  istar_user.email, istar_user.mobile, istar_user.auth_token AS token, user_profile.name, sales_manager_profile.language_pref AS langg, user_profile.profile_image AS profileImage, istar_user.password, istar_user.is_verified AS isVerified, istar_user.is_supended AS isSuspended, istar_user.is_deleted isDeleted FROM istar_user, user_profile, sales_manager_profile WHERE istar_user. ID ="
				+ id + " AND user_profile.user_id = istar_user. ID AND sales_manager_profile.user_id = istar_user. ID;";
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
			user.setLanguage(row.get("langg"));
			user.setMobile(row.get("mobile"));
			user.setName(row.get("name"));
			user.setPassword(row.get("password"));
			user.setProfileImage(row.get("profileImage"));
			user.setToken(row.get("token"));
			user.setRoles(getRoles(id));
			user.setStatus(UserStatus.Inactive.name());
			getLicenseKeys(user);
			// user.setLicenseKeys(getLicenseKeys(id));
		}
		return user;
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
			user.setLanguage(row.get("langg"));
			user.setMobile(row.get("mobile"));
			user.setName(row.get("name"));
			user.setPassword(row.get("password"));
			user.setProfileImage(row.get("profileimage"));
			user.setToken(row.get("token"));
			user.setRoles(getRoles(Integer.parseInt(row.get("id"))));
			user.setStatus(UserStatus.Inactive.name());
			getLicenseKeys(user);
			// user.setLicenseKeys(getLicenseKeys(Integer.parseInt(row.get("id"))));
		}
		return user;
	}

	@Override
	public User updateProfile(User user) throws SQLException {
		String updateName = "update user_profile set name=? where user_id=?";
		String updateImage = "update user_profile set profile_image=? where user_id=?";
		String updateLanguage = "update sales_manager_profile set language_pref=? where user_id=?";

		if (user.getName() != null) {
			HashMap<Integer, Object> profilenameData = new HashMap<Integer, Object>();
			profilenameData.put(1, StringUtils.stringCapitalize(user.getName()));
			profilenameData.put(2, user.getId());
			DBUtils.getInstance().updateObject(updateName, profilenameData);
		}
		if (user.getProfileImage() != null) {
			HashMap<Integer, Object> profileImageData = new HashMap<Integer, Object>();
			profileImageData.put(1, user.getProfileImage());
			profileImageData.put(2, user.getId());
			DBUtils.getInstance().updateObject(updateImage, profileImageData);
		}

		if (user.getLanguage() != null) {
			HashMap<Integer, Object> languageData = new HashMap<Integer, Object>();
			languageData.put(1, user.getLanguage());
			languageData.put(2, user.getId());
			DBUtils.getInstance().updateObject(updateLanguage, languageData);
		}

		return findbyID(user.getId());
	}

	@Override
	public User updatePassword(User user) throws SQLException {
		String updateName = "update istar_user set password=? where id=?";
		HashMap<Integer, Object> profilenameData = new HashMap<Integer, Object>();
		String password = StringUtils.getMd5(user.getNewPassword());
		profilenameData.put(1, password);
		profilenameData.put(2, user.getId());
		DBUtils.getInstance().updateObject(updateName, profilenameData);
		return findbyID(user.getId());

	}

	@Override
	public Boolean forgotPassword() throws SQLException {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public ArrayList<User> findbyOrganizationId(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<BulkUser> getPreviewFromFile(Integer id, String filepath) throws SQLException {
		JsonArray resError = new JsonArray();
		JsonArray res = new JsonArray();
		try {
			InputStream input = new URL(filepath).openStream();

			Workbook workbook = WorkbookFactory.create(input);

			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter dataFormatter = new DataFormatter();
			// ("\n\nIterating over Rows and Columns using for-each loop\n");

			int i = 0;
			for (Row row : sheet) {
				Boolean isError = false;
				if (i != 0) {
					JsonObject jsonObject = new JsonObject();

					JsonObject name = new JsonObject();
					JsonObject email = new JsonObject();
					JsonObject mobile = new JsonObject();
					if (row.getCell(0) == null) {
						name.addProperty("value", "");
						name.addProperty("error", true);
						name.addProperty("message", "Name is mandatory");
						jsonObject.add("name", name);
						isError = true;

					}
					if (row.getCell(1) == null) {
						email.addProperty("value", "");
						email.addProperty("error", true);
						email.addProperty("message", "Email is mandatory");
						jsonObject.add("email", email);
						isError = true;

					}
					if (row.getCell(2) == null) {
						mobile.addProperty("value", "");
						mobile.addProperty("error", true);
						mobile.addProperty("message", "Phone is mandatory");
						jsonObject.add("mobile", mobile);
						isError = true;

					}
					String userEmail = "";
					String userMobile = "";
					ArrayList<HashMap<String, String>> userDataList = new ArrayList<HashMap<String, String>>();
					for (Cell cell : row) {
						String cellValue = dataFormatter.formatCellValue(cell);
						if (cell.getColumnIndex() == 1) {
							if (cellValue.trim().length() > 0) {
								userEmail = cellValue;
							}
						}
						if (cell.getColumnIndex() == 2) {
							if (cellValue.trim().length() > 0)
								userMobile = cellValue;
						}
					}
					// System.err.print(userEmail + " " + userMobile);
					String sql = "";
					if (userEmail.trim().length() > 0) {
						if (userMobile.trim().length() > 0) {
							sql = "SELECT * FROM istar_user WHERE email like '%" + userEmail + "%' or mobile like '%"
									+ userMobile + "%' and is_deleted = FALSE";
							userDataList = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
									sql);
						} else {
							sql = "SELECT * FROM istar_user WHERE email like '%" + userEmail
									+ "%' and is_deleted = FALSE";
							userDataList = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
									sql);
						}
					} else {
						sql = "SELECT * FROM istar_user WHERE mobile like '%" + userMobile
								+ "%' and is_deleted = FALSE";
						userDataList = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
					}

					if (userDataList.size() == 0) {
						for (Cell cell : row) {
							String cellValue = dataFormatter.formatCellValue(cell);
							if (!cellValue.trim().equalsIgnoreCase("")) {
								if (cell.getColumnIndex() == 0) {
									if (cellValue.length() > 2) {

										if (Pattern.matches(".*\\d.*", cellValue)) {
											name.addProperty("value", cellValue);
											name.addProperty("error", true);
											name.addProperty("message", "Name is not valid");
											isError = true;
										} else {
											name.addProperty("value", StringUtils.stringCapitalize(cellValue));
											name.addProperty("error", false);
											name.addProperty("message", "Looks Good");
										}

									} else {
										name.addProperty("value", cellValue);
										name.addProperty("error", true);
										name.addProperty("message", "Name should be 3 alphabets or more");
										isError = true;
									}
									jsonObject.add("name", name);
								} else if (cell.getColumnIndex() == 1) {

									if (cellValue.length() > 0) {
										if (Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z0-9]{2,6}$",
												Pattern.CASE_INSENSITIVE).matcher(cellValue).find()) {
											email.addProperty("value", cellValue);
											email.addProperty("error", false);
											email.addProperty("message", "Looks Good");

										} else {
											email.addProperty("value", cellValue);
											email.addProperty("error", true);
											email.addProperty("message", "Invalid email address");
											isError = true;
										}
									} else {
										email.addProperty("value", cellValue);
										email.addProperty("error", true);
										email.addProperty("message", "Email is mandatory");
										isError = true;
									}
									jsonObject.add("email", email);

								} else if (cell.getColumnIndex() == 2) {

									String mobileNumber = cellValue.trim().replaceAll(" ", "");
									// System.err.println(mobileNumber);
									ValidateResponse validate = new PhoneNumberValidator().validate(mobileNumber);
									if (validate.getIsSuccess()) {

										mobile.addProperty("value", validate.getSuccessMessage());
										mobile.addProperty("error", false);
										mobile.addProperty("message", "Looks Good");

									} else {
										mobile.addProperty("value", mobileNumber);
										mobile.addProperty("error", true);
										mobile.addProperty("message",
												"Phone is mandatory and should be valid with country code");
										isError = true;
									}
									jsonObject.add("mobile", mobile);

								}

							}
						}

						if (isError) {
							resError.add(jsonObject);
						} else {
							res.add(jsonObject);
						}
					}
				}

				++i;
			}
			for (int k = 0; k < res.size(); k++) {
				resError.add(res.get(k));
			}
			// Closing the workbook
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<BulkUser> result = new ArrayList<BulkUser>();
		result = new Gson().fromJson(new Gson().toJson(resError), new TypeToken<ArrayList<BulkUser>>() {
		}.getType());
		return result;
	}

	@Override
	public Boolean bulkUserCreation(ArrayList<BulkUser> users, Integer userID) throws SQLException {

		for (BulkUser bulkUser : users) {
			String email = bulkUser.getEmail().getValue();
			String mobile = bulkUser.getMobile().getValue();
			String name = bulkUser.getName().getValue();
			String sqlEmailMobileCheck = "SELECT *  from istar_user WHERE email='" + email + "' or mobile='" + mobile
					+ "'";
			ArrayList<HashMap<String, String>> resultCheck = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sqlEmailMobileCheck);
			if (resultCheck.size() == 0) {
				String encryptedPassword = StringUtils.getMd5("test123");
				//Here we are setting default designation as 1 which is defined as 'Unallocated' in our database
				String userSql = "INSERT INTO istar_user (email, password, created_at, mobile,auth_token,login_type, is_verified, is_supend, is_supended, is_deleted, show_real_time_notification,designation_id) VALUES"
						+ " (?, ?, now(), ?, NULL, NULL, 't', NULL, 'f', 'f', 'f',1);";

				HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
				userData.put(1, email);
				userData.put(2, encryptedPassword);
				userData.put(3, mobile);
				Integer userId = DBUtils.getInstance().updateObject(userSql, userData);

				if (userId != 0) {
					String addressSql = "INSERT INTO address(addressline1,addressline2,pincode_id,address_geo_longitude,address_geo_latitude, pincode) "
							+ "VALUES (NULL,NULL, NULL, NULL, NULL,NULL);";

					HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();

					Integer addressId = DBUtils.getInstance().updateObject(addressSql, addressData);

					String profileImage = "https://storage.googleapis.com/istar-user-images/files/"
							+ name.trim().substring(0, 1).toUpperCase() + ".png";
					String userProfileSql = "INSERT INTO user_profile ( address_id, name, dob, gender, profile_image,user_id, aadhar_no,father_name, mother_name, user_category, religion, caste_category, place_of_birth ) "
							+ "VALUES ( ?, ?, NULL, NULL, ?, ?, NULL, NULL, NULL, NULL, NULL, NULL, NULL ); ";

					HashMap<Integer, Object> userProfileData = new HashMap<Integer, Object>();
					userProfileData.put(1, addressId);
					userProfileData.put(2, StringUtils.wordsCapitalize(StringUtils.cleanHTML(name)));
					userProfileData.put(3, profileImage);
					userProfileData.put(4, userId);
					DBUtils.getInstance().updateObject(userProfileSql, userProfileData);

					String organizationID = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
							"SELECT * from org_user WHERE userid=" + userID).get(0).get("organizationid");

					String orgUserSql = "INSERT INTO org_user (organizationid, userid) VALUES (?,?);";
					HashMap<Integer, Object> orgUserData = new HashMap<Integer, Object>();
					orgUserData.put(1, Integer.parseInt(organizationID));
					orgUserData.put(2, userId);
					DBUtils.getInstance().updateObject(orgUserSql, orgUserData);

					try {

						String roleUserSql = "INSERT INTO user_role (userid, roleid) VALUES (?,?);";
						HashMap<Integer, Object> roleUserData = new HashMap<Integer, Object>();
						roleUserData.put(1, userId);
						roleUserData.put(2, 14);
						DBUtils.getInstance().updateObject(roleUserSql, roleUserData);

						String sqlSalesProfile = "INSERT INTO sales_manager_profile (timezone, location, language,currency,user_id, language_pref) VALUES ( NULL, NULL, NULL, NULL, ?, ? );  ";
						HashMap<Integer, Object> salesMangerProfileData = new HashMap<Integer, Object>();
						salesMangerProfileData.put(1, userId);
						salesMangerProfileData.put(2, "en-IN");
						DBUtils.getInstance().updateObject(sqlSalesProfile, salesMangerProfileData);

						/*
						 * (new Thread(new Runnable() { public void run() { try { String jws = null;
						 * UserDAO dao = new UserDAOPG(); User u = dao.findbyID(userId);
						 * u.setPassword(null); Calendar c = Calendar.getInstance(); c.setTime(new
						 * Date()); c.add(Calendar.DAY_OF_MONTH, 30); Date expiryDate = c.getTime(); //
						 * This is for 30 days jws = Jwts.builder().setSubject(u.getId() +
						 * "").setExpiration(expiryDate) .claim("roles",
						 * u.getRoles()).signWith(KeySingleton.getInstance().getKey()) .compact();
						 * dao.SendVerificationMail(u, jws, "activate"); } catch (Exception e) {
						 * e.printStackTrace(); } } })).start();
						 */
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}

		return true;
	}

	@Override
	public User createUser(User user, Integer uId) throws SQLException {
		try {
			String encryptedPassword = StringUtils.getMd5("test123");
			String userSql = "INSERT INTO istar_user (email, password, created_at, mobile,auth_token,login_type, is_verified, is_supend, is_supended, is_deleted, show_real_time_notification) VALUES"
					+ " (?, ?, now(), ?, NULL, NULL, 't', NULL, 'f', 'f', 'f');";

			HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
			userData.put(1, user.getEmail().trim().toLowerCase());
			userData.put(2, encryptedPassword);
			userData.put(3, user.getMobile());
			Integer userId = DBUtils.getInstance().updateObject(userSql, userData);

			if (userId != 0) {
				Integer pincodeId = null;
				if (user.getPincode() != null) {
					try {
						Integer pin = Integer.parseInt(user.getPincode().trim());
						String sqlPincode = "SELECT * from pincode WHERE pin=" + pin;
						ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
								.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
						if (resultPincode.size() > 0) {
							pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
						}
					} catch (Exception e) {

					}
				}

				String addressSql = "INSERT INTO address(addressline1,addressline2,pincode_id,address_geo_longitude,address_geo_latitude, pincode) "
						+ "VALUES (?,?, ?, NULL, NULL,?);";

				HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
				addressData.put(1, user.getAddressLine1());
				addressData.put(2, user.getAddressLine2());
				addressData.put(3, pincodeId);
				addressData.put(4, user.getPincode());
				Integer addressId = DBUtils.getInstance().updateObject(addressSql, addressData);

				String profileImage = "https://storage.googleapis.com/istar-user-images/files/"
						+ StringUtils.cleanHTML(user.getName()).trim().substring(0, 1).toUpperCase() + ".png";
				String userProfileSql = "INSERT INTO user_profile ( address_id, name, dob, gender, profile_image,user_id, aadhar_no,father_name, mother_name, user_category, religion, caste_category, place_of_birth ) "
						+ "VALUES ( ?, ?, NULL, NULL, ?, ?, NULL, NULL, NULL, NULL, NULL, NULL, NULL ); ";

				HashMap<Integer, Object> userProfileData = new HashMap<Integer, Object>();
				userProfileData.put(1, addressId);
				new StringUtils();
				userProfileData.put(2, StringUtils.wordsCapitalize(StringUtils.cleanHTML(user.getName())));
				userProfileData.put(3, profileImage);
				userProfileData.put(4, userId);
				DBUtils.getInstance().updateObject(userProfileSql, userProfileData);

				String organizationID = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
						"SELECT * from org_user WHERE userid=" + uId).get(0).get("organizationid");

				String orgUserSql = "INSERT INTO org_user (organizationid, userid) VALUES (?,?);";
				HashMap<Integer, Object> orgUserData = new HashMap<Integer, Object>();
				orgUserData.put(1, Integer.parseInt(organizationID));
				orgUserData.put(2, userId);
				DBUtils.getInstance().updateObject(orgUserSql, orgUserData);

				try {
					if (user.getTeamIds() != null) {
						for (Integer teamID : user.getTeamIds()) {
							if (teamID != null) {
								String groupUserSql = "INSERT INTO group_user (qroupid, userid) VALUES (?,?);";
								HashMap<Integer, Object> groupUserData = new HashMap<Integer, Object>();
								groupUserData.put(1, teamID);
								groupUserData.put(2, userId);
								DBUtils.getInstance().updateObject(groupUserSql, groupUserData);
							}
						}
					}
					if (user.getRoleIds() != null) {
						for (Integer roleID : user.getRoleIds()) {
							String roleUserSql = "INSERT INTO user_role (userid, roleid) VALUES (?,?);";
							HashMap<Integer, Object> roleUserData = new HashMap<Integer, Object>();
							roleUserData.put(1, userId);
							roleUserData.put(2, roleID);
							DBUtils.getInstance().updateObject(roleUserSql, roleUserData);
							if (roleID == 13 || roleID == 14 || roleID == 15) {
								String salesManagerProfileSql = "SELECT * from sales_manager_profile WHERE user_id="
										+ userId;
								ArrayList<HashMap<String, String>> resultManagerProfile = DBUtils.getInstance()
										.executeQuery(Thread.currentThread().getStackTrace(), salesManagerProfileSql);
								if (resultManagerProfile.size() == 0) {
									String sqlSalesProfile = "INSERT INTO sales_manager_profile (timezone, location, language,currency,user_id, language_pref) VALUES ( NULL, NULL, NULL, NULL, ?, ? );  ";
									HashMap<Integer, Object> salesMangerProfileData = new HashMap<Integer, Object>();
									salesMangerProfileData.put(1, userId);
									salesMangerProfileData.put(2, "en-IN");
									DBUtils.getInstance().updateObject(sqlSalesProfile, salesMangerProfileData);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			user = new UserDAOPG().findbyID(userId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User updateUser(User user) throws SQLException {
		String sql = "update user_profile set name = ? where user_id = ?";
		HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
		userData.put(1, StringUtils.wordsCapitalize(StringUtils.cleanHTML(user.getName())));
		userData.put(2, user.getId());
		DBUtils.getInstance().updateObject(sql, userData);

		if (user.getDepartment() != null) {
			sql = "update istar_user set department = ? where id = ?";
			userData = new HashMap<Integer, Object>();
			userData.put(1, user.getDepartment());
			userData.put(2, user.getId());
			DBUtils.getInstance().updateObject(sql, userData);
		}
		if (user.getDesignationId() != null) {
			sql = "update istar_user set designation_id = ? where id = ?";
			userData = new HashMap<Integer, Object>();
			userData.put(1, user.getDesignationId());
			userData.put(2, user.getId());
			DBUtils.getInstance().updateObject(sql, userData);
		}
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance().executeQuery(
				Thread.currentThread().getStackTrace(), "SELECT * from user_profile WHERE user_id=" + user.getId());
		if (result.size() > 0) {
			String addressId = result.get(0).get("address_id");
			if (addressId != null) {

				if (!user.getPincode().trim().isEmpty()) {
					String sqlPincode = "SELECT * from pincode WHERE pin= " + user.getPincode();
					ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
					if (resultPincode.size() > 0) {
						Integer pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
						sql = "update address set pincode_id=? where id = ?";
						HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
						addressData.put(1, pincodeId);
						addressData.put(2, Integer.parseInt(addressId));
						DBUtils.getInstance().updateObject(sql, addressData);
					}
				}
				sql = "update address set addressline1=?, addressline2=?, pincode=? where id = ?";
				HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
				addressData.put(1, user.getAddressLine1());
				addressData.put(2, user.getAddressLine2());
				addressData.put(3, user.getPincode());
				addressData.put(4, Integer.parseInt(addressId));
				DBUtils.getInstance().updateObject(sql, addressData);
			} else {
				Integer pincodeId = null;
				if (!user.getPincode().trim().isEmpty()) {
					String sqlPincode = "SELECT * from pincode WHERE pin=" + user.getPincode();
					ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
					if (resultPincode.size() > 0) {
						pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
					}
				}
				String addressSql = "INSERT INTO address(addressline1,addressline2,pincode_id,address_geo_longitude,address_geo_latitude, pincode) "
						+ "VALUES (?,?, ?, NULL, NULL,?);";

				HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
				addressData.put(1, user.getAddressLine1());
				addressData.put(2, user.getAddressLine2());
				addressData.put(3, pincodeId);
				addressData.put(4, user.getPincode());

				Integer addrID = DBUtils.getInstance().updateObject(addressSql, addressData);

				sql = "update user_profile set address_id = ? where user_id = ?";
				HashMap<Integer, Object> addresIdData = new HashMap<Integer, Object>();
				addresIdData.put(1, addrID);
				addresIdData.put(2, user.getId());
				DBUtils.getInstance().updateObject(sql, addresIdData);
			}
		}

		if (user.getRoleIds() != null) {
			String rolesql = "delete from user_role where userid = ?";
			HashMap<Integer, Object> roleData = new HashMap<Integer, Object>();
			roleData.put(1, user.getId());
			DBUtils.getInstance().updateObject(rolesql, roleData);
			for (Integer roleID : user.getRoleIds()) {
				if (roleID != null) {
					String userRoleSql = "insert into user_role (userid, roleid) values(?,?)";
					HashMap<Integer, Object> userRoleData = new HashMap<Integer, Object>();
					userRoleData.put(1, user.getId());
					userRoleData.put(2, roleID);
					DBUtils.getInstance().updateObject(userRoleSql, userRoleData);
					if (roleID == 13 || roleID == 14 || roleID == 15) {
						String salesManagerProfileSql = "SELECT * from sales_manager_profile WHERE user_id="
								+ user.getId();
						ArrayList<HashMap<String, String>> resultManagerProfile = DBUtils.getInstance()
								.executeQuery(Thread.currentThread().getStackTrace(), salesManagerProfileSql);
						if (resultManagerProfile.size() == 0) {
							String sqlSalesProfile = "INSERT INTO sales_manager_profile (timezone, location, language,currency,user_id, language_pref) VALUES ( NULL, NULL, NULL, NULL, ?, ? );  ";
							HashMap<Integer, Object> salesMangerProfileData = new HashMap<Integer, Object>();
							salesMangerProfileData.put(1, user.getId());
							salesMangerProfileData.put(2, "en-IN");
							DBUtils.getInstance().updateObject(sqlSalesProfile, salesMangerProfileData);
						}
					}
				}
			}
		}

		if (user.getTeamIds() != null) {
			String teamsql = "delete from group_user where userid = ?";
			HashMap<Integer, Object> teamData = new HashMap<Integer, Object>();
			teamData.put(1, user.getId());
			DBUtils.getInstance().updateObject(teamsql, teamData);
			for (Integer teamID : user.getTeamIds()) {

				if (teamID != null) {
					String userteamSql = "insert into group_user (qroupid , userid) values(?,?) ";
					HashMap<Integer, Object> userteamData = new HashMap<Integer, Object>();
					userteamData.put(1, teamID);
					userteamData.put(2, user.getId());
					DBUtils.getInstance().updateObject(userteamSql, userteamData);
				}
			}
		}
		user = new UserDAOPG().findbyID(user.getId());
		return user;
	}

	@Override
	public User updateUserProfile(User user) throws SQLException {
		String sql = "update user_profile set name = ? where user_id = ?";
		HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
		userData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(user.getName())));
		userData.put(2, user.getId());
		DBUtils.getInstance().updateObject(sql, userData);

		if (user.getDepartment() != null && user.getDepartment().trim().length() > 0) {
			sql = "update istar_user set department = ? where id = ?";
			userData = new HashMap<Integer, Object>();
			userData.put(1, user.getDepartment());
			userData.put(2, user.getId());
			DBUtils.getInstance().updateObject(sql, userData);
		}
		if (user.getDesignationId() != null) {
			sql = "update istar_user set designation_id = ? where id = ?";
			userData = new HashMap<Integer, Object>();
			userData.put(1, user.getDesignationId());
			userData.put(2, user.getId());
			DBUtils.getInstance().updateObject(sql, userData);
		}

		ArrayList<HashMap<String, String>> result = DBUtils.getInstance().executeQuery(
				Thread.currentThread().getStackTrace(), "SELECT * from user_profile WHERE user_id=" + user.getId());
		if (result.size() > 0) {
			String addressId = result.get(0).get("address_id");
			if (addressId != null) {
				if (user.getCity() != null) {
					String sqlPincode = "SELECT * from pincode WHERE lower(city)=lower('" + user.getCity().trim()
							+ "') ORDER BY pin";
					ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
					if (resultPincode.size() > 0) {
						Integer pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
						sql = "update address set pincode_id=? where id = ?";
						HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
						addressData.put(1, pincodeId);
						addressData.put(2, Integer.parseInt(addressId));
						DBUtils.getInstance().updateObject(sql, addressData);
					}
				}

				if (!user.getPincode().trim().isEmpty()) {
					String sqlPincode = "SELECT * from pincode WHERE pin=" + user.getPincode();
					ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
					if (resultPincode.size() > 0) {
						Integer pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
						sql = "update address set pincode_id=? where id = ?";
						HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
						addressData.put(1, pincodeId);
						addressData.put(2, Integer.parseInt(addressId));
						DBUtils.getInstance().updateObject(sql, addressData);
					}
				}

				if (!user.getAddressLine1().trim().isEmpty() || !user.getAddressLine2().trim().isEmpty()
						|| !user.getPincode().trim().isEmpty()) {
					sql = "update address set addressline1=?, addressline2=?, pincode=? where id = ?";
					HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
					addressData.put(1, user.getAddressLine1());
					addressData.put(2, user.getAddressLine2());
					addressData.put(3, user.getPincode());
					addressData.put(4, Integer.parseInt(addressId));
					DBUtils.getInstance().updateObject(sql, addressData);
				}
			} else {
				Integer pincodeId = null;
				if (!user.getPincode().trim().isEmpty()) {
					String sqlPincode = "SELECT * from pincode WHERE pin=" + user.getPincode();
					ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
					if (resultPincode.size() > 0) {
						pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
					}
				}
				String addressSql = "INSERT INTO address(addressline1,addressline2,pincode_id,address_geo_longitude,address_geo_latitude, pincode) "
						+ "VALUES (?,?, ?, NULL, NULL,?);";

				HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
				addressData.put(1, user.getAddressLine1());
				addressData.put(2, user.getAddressLine2());
				addressData.put(3, pincodeId);
				addressData.put(4, user.getPincode());
				Integer addrID = DBUtils.getInstance().updateObject(addressSql, addressData);

				sql = "update user_profile set address_id = ? where user_id = ?";
				HashMap<Integer, Object> addresIdData = new HashMap<Integer, Object>();
				addresIdData.put(1, addrID);
				addresIdData.put(2, user.getId());
				DBUtils.getInstance().updateObject(sql, addresIdData);
			}
		}
		user = new UserDAOPG().findbyID(user.getId());
		return user;
	}

	@Override
	public Boolean isUserAlreadyExistWithEmail(String email) throws SQLException {
		if (DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				"select * from istar_user where email='" + email.trim() + "'").size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean isUserAlreadyExistWithMobile(String mobile) throws SQLException {
		if (DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				"select * from istar_user where mobile='" + mobile.trim() + "'").size() == 0) {
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<Role> getAllRoles() throws SQLException {
		String sql = "SELECT * from role ORDER BY id";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		ArrayList<Role> roles = new ArrayList<Role>();
		for (HashMap<String, String> row : result) {
			if (row.get("role_name").equalsIgnoreCase("SALES_ASSOCIATE")
					|| row.get("role_name").equalsIgnoreCase("SALES_MANAGER")
					|| row.get("role_name").equalsIgnoreCase("IT_ADMIN")) {
				Role role = new Role();
				role.setId(Integer.parseInt(row.get("id")));
				role.setName(row.get("role_name"));
				roles.add(role);
			}
		}
		return roles;
	}

	@Override
	public Boolean deleteUser(Integer userId) throws SQLException {
		String sqlUser = "update istar_user set is_deleted=? where id = ?";
		HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
		userData.put(1, true);
		userData.put(2, userId);
		DBUtils.getInstance().updateObject(sqlUser, userData);

		sqlUser = "delete from group_user where userid = ?";
		userData = new HashMap<Integer, Object>();
		userData.put(1, userId);
		DBUtils.getInstance().updateObject(sqlUser, userData);

		sqlUser = "DELETE FROM user_manager WHERE manager_id = ?";
		userData = new HashMap<Integer, Object>();
		userData.put(1, userId);
		DBUtils.getInstance().updateObject(sqlUser, userData);

		sqlUser = "DELETE FROM user_manager WHERE user_id = ?";
		userData = new HashMap<Integer, Object>();
		userData.put(1, userId);
		DBUtils.getInstance().updateObject(sqlUser, userData);

		return true;

	}

	@Override
	public User findUserProfile(Integer userId) throws SQLException {
		String sql = "SELECT istar_user. ID, email, mobile, istar_user.designation_id, organization_designation.designation,"
				+ " istar_user.department, user_profile. NAME, user_profile.profile_image, organization. NAME AS company_name, organization. ID AS organization_id,"
				+ " pincode.city, pincode.pin, address.addressline1, address.addressline2, string_agg (DISTINCT ROLE .role_name, ',') AS role_names,"
				+ " string_agg ( DISTINCT istar_group. ID :: TEXT, ',' ) AS team_ids FROM istar_user LEFT JOIN user_profile ON istar_user. ID = user_profile.user_id"
				+ " LEFT JOIN organization_designation ON organization_designation. ID = istar_user.designation_id LEFT JOIN address ON user_profile.address_id ="
				+ " address. ID LEFT JOIN pincode ON pincode. ID = address.pincode_id LEFT JOIN org_user ON org_user.userid = istar_user. ID LEFT JOIN organization"
				+ " ON org_user.organizationid = organization. ID LEFT JOIN user_role ON user_role.userid = user_profile.user_id LEFT JOIN ROLE ON ROLE . ID ="
				+ " user_role.roleid LEFT JOIN group_user ON group_user.userid = istar_user. ID LEFT JOIN istar_group ON istar_group. ID = group_user.qroupid AND"
				+ " istar_group.is_deleted = FALSE WHERE istar_user. ID = " + userId
				+ " GROUP BY istar_user. ID, email, mobile, istar_user.designation_id, user_profile. NAME,"
				+ " user_profile.profile_image, organization. NAME, organization. ID, pincode.pin, address.addressline1, address.addressline2, pincode.city,"
				+ " organization_designation.designation";
		User user = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			user = new User();
			user.setId(Integer.parseInt(row.get("id")));
			user.setEmail(row.get("email"));
			user.setMobile(row.get("mobile"));
			user.setName(row.get("name"));

			if (row.get("profile_image") != null) {
				user.setProfileImage(row.get("profile_image"));
			} else {
				String profileImage = "https://storage.googleapis.com/istar-user-images/files/"
						+ user.getName().trim().substring(0, 1).toUpperCase() + ".png";
				user.setProfileImage(profileImage);
			}
			if (row.get("designation_id") != null) {
				user.setDesignationId(Integer.parseInt(row.get("designation_id")));
				user.setDesignation(row.get("designation"));
			}
			user.setCompany(row.get("company_name"));
			if (row.get("city") != null) {
				user.setCity(row.get("city"));
			} else {
				user.setCity("No location");
			}
			if (row.get("pin") != null) {
				user.setPincode(row.get("pin"));
			}
			if (row.get("addressline1") != null) {
				user.setAddressLine1(row.get("addressline1"));
			}
			if (row.get("addressline2") != null) {
				user.setAddressLine2(row.get("addressline2"));
			}
			if (row.get("role_names") != null) {
				ArrayList<String> roles = new ArrayList<String>(Arrays.asList(row.get("role_names").split(",")));
				user.setRoles(roles);
			}
			if (row.get("team_ids") != null) {
				ArrayList<String> teamIdsstr = new ArrayList<String>(Arrays.asList(row.get("team_ids").split(",")));
				ArrayList<Integer> teamIds = new ArrayList<Integer>();
				for (String team : teamIdsstr) {
					teamIds.add(Integer.parseInt(team));
				}
				user.setTeamIds(teamIds);
			}
			user.setDepartment(row.get("department"));
			user.setSipCredentials(getSipCredential(user));
		}
		return user;
	}

	private ArrayList<SIPCredential> getSipCredential(User user) throws SQLException {
		String sql = "SELECT * from user_sip WHERE user_id=" + user.getId();
		ArrayList<SIPCredential> sipCredentials = new ArrayList<SIPCredential>();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			SIPCredential sipCredential = new SIPCredential();
			sipCredential.setId(Integer.parseInt(row.get("id")));
			sipCredential.setUsername(row.get("sip_username"));
			sipCredential.setPassword(row.get("sip_password"));
			sipCredential.setUrl(row.get("sip_url"));
			sipCredential.setProvider(row.get("provider"));

			if (row.get("is_active").equalsIgnoreCase("t")) {
				sipCredential.setIsActive(true);
			} else {
				sipCredential.setIsActive(false);
			}
			sipCredentials.add(sipCredential);
		}
		return sipCredentials;
	}

	@Override
	public ArrayList<User> getManagers(Integer userId) throws SQLException {
		String managerSql = "SELECT user_id, name from user_profile where user_profile.user_id in ( select id from istar_user where istar_user.id in "
				+ "(SELECT org_user.userid  from org_user LEFT JOIN user_role on org_user.userid = user_role.userid where organizationid in "
				+ "(SELECT org_user.organizationid from org_user where org_user.userid = " + userId
				+ ") and user_role.roleid=13) AND istar_user.is_deleted = FALSE )";
		ArrayList<HashMap<String, String>> managerList = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), managerSql);
		ArrayList<User> users = new ArrayList<User>();
		for (HashMap<String, String> row : managerList) {
			User user = new User();
			user.setId(Integer.parseInt(row.get("user_id")));
			user.setName(row.get("name"));
			users.add(user);
		}
		return users;

	}

	@Override
	public ArrayList<Designation> getDesignations(Integer userId) throws SQLException {
		String sql = "SELECT * from organization_designation WHERE organization_id in ((SELECT organizationid from org_user WHERE userid="
				+ userId + "),0) order by id ";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		ArrayList<Designation> designations = new ArrayList<Designation>();
		for (HashMap<String, String> row : result) {
			Designation designation = new Designation();
			designation.setId(Integer.parseInt(row.get("id")));
			designation.setDesignation(row.get("designation"));
			designation.setOrganizationId(Integer.parseInt(row.get("organization_id")));
			designations.add(designation);
		}
		return designations;
	}

	@Override
	public Designation addDesignation(Designation designation, Integer userId) throws SQLException {
		String orgSql = "SELECT organizationid from org_user WHERE userid=" + userId;
		String orgId = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), orgSql).get(0)
				.get("organizationid");

		String sql = "SELECT * from organization_designation WHERE organization_id in (" + orgId
				+ ",0) and lower(designation)=lower('" + designation.getDesignation().replaceAll("'", "''") + "')";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (result.size() > 0) {
			for (HashMap<String, String> row : result) {
				designation = new Designation();
				designation.setId(Integer.parseInt(row.get("id")));
				designation.setDesignation(row.get("designation"));
				designation.setOrganizationId(Integer.parseInt(row.get("organization_id")));
			}
		} else {
			String insertDesignationSql = "INSERT INTO organization_designation (designation, organization_id) VALUES (?, ?);";

			HashMap<Integer, Object> designationData = new HashMap<Integer, Object>();
			designationData.put(1, designation.getDesignation());
			designationData.put(2, Integer.parseInt(orgId));
			Integer designationid = DBUtils.getInstance().updateObject(insertDesignationSql, designationData);

			String designationSql = "select * from organization_designation where id in (" + designationid + ",0)";
			ArrayList<HashMap<String, String>> designationResult = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), designationSql);
			for (HashMap<String, String> row : designationResult) {
				designation = new Designation();
				designation.setId(Integer.parseInt(row.get("id")));
				designation.setDesignation(row.get("designation"));
				designation.setOrganizationId(Integer.parseInt(row.get("organization_id")));
			}

		}
		return designation;
	}

	@Override
	public ArrayList<User> getDesignationsWiseUsers(Integer userId, Integer designationId) throws SQLException {

		HashMap<Integer, Designation> designationMap = new HashMap<Integer, Designation>();
		String desginationSql = "SELECT * from organization_designation WHERE organization_id in ((SELECT organizationid from org_user WHERE userid="
				+ userId + "),0)";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				desginationSql)) {
			Designation designation = new Designation();
			designation.setId(Integer.parseInt(row.get("id")));
			designation.setDesignation(row.get("designation"));
			designation.setOrganizationId(Integer.parseInt(row.get("organization_id")));
			designationMap.put(Integer.parseInt(row.get("id")), designation);
		}

		String sql = "SELECT istar_user.*, user_profile. NAME, user_profile.profile_image, reportee.user_id as reportee_id, reportee_user.designation_id"
				+ " AS reportee_designation_id, reportee_profile. NAME AS reportee_name, reportee_profile.profile_image AS reportee_image,"
				+ " owner_user. ID AS owner_id, owner_user.designation_id AS owner_designation_id, owner_profile. NAME AS owner_name,"
				+ " owner_profile.profile_image AS owner_profile_image FROM istar_user LEFT JOIN user_profile "
				+ " ON istar_user. ID = user_profile.user_id LEFT JOIN user_manager AS reportee ON istar_user. ID = reportee.manager_id"
				+ " LEFT JOIN istar_user AS reportee_user ON reportee.user_id = reportee_user. ID LEFT JOIN user_profile AS"
				+ " reportee_profile ON reportee_user. ID = reportee_profile.user_id LEFT JOIN user_manager AS OWNER ON istar_user. ID"
				+ " = OWNER .user_id LEFT JOIN istar_user AS owner_user ON OWNER .manager_id = owner_user. ID LEFT JOIN user_profile"
				+ " AS owner_profile ON owner_profile.user_id = OWNER .manager_id WHERE istar_user. ID IN ( SELECT userid FROM org_user"
				+ " WHERE organizationid IN ( SELECT organizationid FROM org_user WHERE userid = " + userId
				+ " ) ) AND istar_user.is_deleted"
				+ " = FALSE AND istar_user.is_supended = FALSE AND istar_user.designation_id = " + designationId;

		ArrayList<User> userList = new ArrayList<User>();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (row.get("designation_id") != null) {

				boolean isUserAlready = false;
				for (User user : userList) {
					if (Integer.parseInt(row.get("id")) == user.getId().intValue()) {
						if (row.get("reportee_id") != null) {
							User reporteeUser = new User();
							reporteeUser.setId(Integer.parseInt(row.get("reportee_id")));
							if (row.get("reportee_designation_id") != null && designationMap
									.get(Integer.parseInt(row.get("reportee_designation_id"))) != null) {
								reporteeUser.setDesignationId(Integer.parseInt(row.get("reportee_designation_id")));
								reporteeUser.setDesignation(designationMap
										.get(Integer.parseInt(row.get("reportee_designation_id"))).getDesignation());
							}
							reporteeUser.setName(row.get("reportee_name"));
							reporteeUser.setProfileImage(row.get("reportee_image"));

							user.getReporteeUser().add(reporteeUser);
						}
						isUserAlready = true;
					}
				}

				if (!isUserAlready) {
					User user = new User();
					user.setId(Integer.parseInt(row.get("id")));
					user.setEmail(row.get("email"));
					user.setMobile(row.get("mobile"));
					user.setName(row.get("name"));
					user.setProfileImage(row.get("profile_image"));
					if (row.get("designation_id") != null
							&& designationMap.get(Integer.parseInt(row.get("designation_id"))) != null) {
						user.setDesignationId(Integer.parseInt(row.get("designation_id")));
						user.setDesignation(
								designationMap.get(Integer.parseInt(row.get("designation_id"))).getDesignation());
					}
					user.setReporteeUser(new ArrayList<User>());

					if (row.get("owner_id") != null) {
						User owner = new User();

						owner.setId(Integer.parseInt(row.get("owner_id")));
						if (row.get("owner_designation_id") != null
								&& designationMap.get(Integer.parseInt(row.get("owner_designation_id"))) != null) {
							owner.setDesignationId(Integer.parseInt(row.get("owner_designation_id")));
							owner.setDesignation(designationMap.get(Integer.parseInt(row.get("owner_designation_id")))
									.getDesignation());
						}
						owner.setName(row.get("owner_name"));
						owner.setProfileImage(row.get("owner_profile_image"));
						user.setOwner(owner);
					}
					if (row.get("reportee_id") != null) {
						User reporteeUser = new User();
						reporteeUser.setId(Integer.parseInt(row.get("reportee_id")));
						if (row.get("reportee_designation_id") != null
								&& designationMap.get(Integer.parseInt(row.get("reportee_designation_id"))) != null) {

							reporteeUser.setDesignationId(Integer.parseInt(row.get("reportee_designation_id")));

							reporteeUser.setDesignation(designationMap
									.get(Integer.parseInt(row.get("reportee_designation_id"))).getDesignation());
						}
						reporteeUser.setName(row.get("reportee_name"));
						reporteeUser.setProfileImage(row.get("reportee_image"));

						// for (User u : userList) {
						// if (Integer.parseInt(row.get("id")) == u.getId().intValue()) {
						user.getReporteeUser().add(reporteeUser);
						// }
						// }
					}
					userList.add(user);
				}

			}

		}

		return userList;
	}

	@Override
	public User getDesignationsWiseSingleUser(Integer userId, Integer designationId) throws SQLException {

		HashMap<Integer, Designation> designationMap = new HashMap<Integer, Designation>();
		String desginationSql = "SELECT * from organization_designation WHERE organization_id in ((SELECT organizationid from org_user WHERE userid="
				+ userId + "),0)";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				desginationSql)) {
			Designation designation = new Designation();
			designation.setId(Integer.parseInt(row.get("id")));
			designation.setDesignation(row.get("designation"));
			designation.setOrganizationId(Integer.parseInt(row.get("organization_id")));
			designationMap.put(Integer.parseInt(row.get("id")), designation);
		}

		String sql = "SELECT istar_user.*, user_profile. NAME, user_profile.profile_image, reportee.user_id as reportee_id, reportee_user.designation_id"
				+ " AS reportee_designation_id, reportee_profile. NAME AS reportee_name, reportee_profile.profile_image AS reportee_image,"
				+ " owner_user. ID AS owner_id, owner_user.designation_id AS owner_designation_id, owner_profile. NAME AS owner_name,"
				+ " owner_profile.profile_image AS owner_profile_image FROM istar_user LEFT JOIN user_profile "
				+ " ON istar_user. ID = user_profile.user_id LEFT JOIN user_manager AS reportee ON istar_user. ID = reportee.manager_id"
				+ " LEFT JOIN istar_user AS reportee_user ON reportee.user_id = reportee_user. ID LEFT JOIN user_profile AS"
				+ " reportee_profile ON reportee_user. ID = reportee_profile.user_id LEFT JOIN user_manager AS OWNER ON istar_user. ID"
				+ " = OWNER .user_id LEFT JOIN istar_user AS owner_user ON OWNER .manager_id = owner_user. ID LEFT JOIN user_profile"
				+ " AS owner_profile ON owner_profile.user_id = OWNER .manager_id WHERE istar_user. ID IN (" + userId
				+ " ) AND istar_user.is_deleted"
				+ " = FALSE AND istar_user.is_supended = FALSE AND istar_user.designation_id = " + designationId;

		ArrayList<User> userList = new ArrayList<User>();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (row.get("designation_id") != null) {

				boolean isUserAlready = false;
				for (User user : userList) {
					if (Integer.parseInt(row.get("id")) == user.getId().intValue()) {
						if (row.get("reportee_id") != null) {
							User reporteeUser = new User();
							reporteeUser.setId(Integer.parseInt(row.get("reportee_id")));
							if (row.get("reportee_designation_id") != null) {
								reporteeUser.setDesignationId(Integer.parseInt(row.get("reportee_designation_id")));
								reporteeUser.setDesignation(designationMap
										.get(Integer.parseInt(row.get("reportee_designation_id"))).getDesignation());
							}
							reporteeUser.setName(row.get("reportee_name"));
							reporteeUser.setProfileImage(row.get("reportee_image"));

							user.getReporteeUser().add(reporteeUser);
						}
						isUserAlready = true;
					}
				}

				if (!isUserAlready) {
					User user = new User();
					user.setId(Integer.parseInt(row.get("id")));
					user.setEmail(row.get("email"));
					user.setMobile(row.get("mobile"));
					user.setName(row.get("name"));
					user.setProfileImage(row.get("profile_image"));
					if (row.get("designation_id") != null) {
						user.setDesignationId(Integer.parseInt(row.get("designation_id")));
						user.setDesignation(
								designationMap.get(Integer.parseInt(row.get("designation_id"))).getDesignation());
					}
					user.setReporteeUser(new ArrayList<User>());

					if (row.get("owner_id") != null) {
						User owner = new User();

						owner.setId(Integer.parseInt(row.get("owner_id")));
						if (row.get("owner_designation_id") != null
								&& designationMap.get(Integer.parseInt(row.get("owner_designation_id"))) != null) {
							owner.setDesignationId(Integer.parseInt(row.get("owner_designation_id")));
							owner.setDesignation(designationMap.get(Integer.parseInt(row.get("owner_designation_id")))
									.getDesignation());
						}
						owner.setName(row.get("owner_name"));
						owner.setProfileImage(row.get("owner_profile_image"));
						user.setOwner(owner);
					}
					if (row.get("reportee_id") != null) {
						User reporteeUser = new User();
						reporteeUser.setId(Integer.parseInt(row.get("reportee_id")));
						if (row.get("reportee_designation_id") != null
								&& designationMap.get(Integer.parseInt(row.get("reportee_designation_id"))) != null) {
							reporteeUser.setDesignationId(Integer.parseInt(row.get("reportee_designation_id")));
							reporteeUser.setDesignation(designationMap
									.get(Integer.parseInt(row.get("reportee_designation_id"))).getDesignation());
						}
						reporteeUser.setName(row.get("reportee_name"));
						reporteeUser.setProfileImage(row.get("reportee_image"));
						for (User u : userList) {
							if (Integer.parseInt(row.get("id")) == u.getId().intValue()) {
								user.getReporteeUser().add(reporteeUser);
							}
						}
					}
					userList.add(user);
				}

			}

		}
		if (userList.size() > 0) {
			return userList.get(0);
		} else {
			return null;
		}

	}

	@Override
	public boolean deleteOwner(Integer ownerId) throws SQLException {
		HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
		String deleteUserManagerSql = "delete from user_manager where manager_id = ?";
		userData.put(1, ownerId);
		DBUtils.getInstance().updateObject(deleteUserManagerSql, userData);

		String deleteGroupUserSql = "delete from group_user where userid = ?";
		userData = new HashMap<Integer, Object>();
		userData.put(1, ownerId);
		DBUtils.getInstance().updateObject(deleteGroupUserSql, userData);

		return true;
	}

	@Override
	public boolean deactivateUser(Integer userId) throws SQLException {
		String sql = "UPDATE license_issued set expiryon=now() where user_id = ? and expiryon > now()";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, userId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

	@Override
	public ArrayList<DesignationUser> getAllAssociateWithManagerByuserId(Integer userId) throws SQLException {

		String managersql = "select istar_user.* ,user_profile.user_id,user_profile.name,user_profile.profile_image as image from user_profile "
				+ "left join user_manager on user_manager.user_id = user_profile.user_id "
				+ "left join istar_user on istar_user.id = user_profile.user_id " + "where user_manager.manager_id = "
				+ userId + " and istar_user.is_deleted = false";

		ArrayList<DesignationUser> managerAssociateList = new ArrayList<DesignationUser>();
		for (HashMap<String, String> managerow : DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), managersql)) {
			DesignationUser designationUser = new DesignationUser();
			designationUser.setId(Integer.parseInt(managerow.get("user_id")));
			designationUser.setName(managerow.get("name"));
			designationUser.setImage(managerow.get("image"));
			designationUser.setDesignation(managerow.get("designation"));
			Integer managerId = Integer.parseInt(managerow.get("user_id"));
			String sql = "SELECT istar_user. ID, user_profile. NAME, user_profile.profile_image, reportee.user_id AS reportee_id, reportee_user.designation_id AS reportee_designation_id,"
					+ " reportee_designation.designation AS reportee_designation, reportee_profile. NAME AS reportee_name, reportee_profile.profile_image AS reportee_image FROM istar_user"
					+ " LEFT JOIN user_profile ON istar_user. ID = user_profile.user_id LEFT JOIN user_manager AS reportee ON istar_user. ID = reportee.manager_id LEFT JOIN istar_user"
					+ " AS reportee_user ON reportee.user_id = reportee_user. ID LEFT JOIN organization_designation AS reportee_designation ON reportee_designation. ID ="
					+ " reportee_user.designation_id LEFT JOIN user_profile AS reportee_profile ON reportee_user. ID = reportee_profile.user_id LEFT JOIN user_manager AS OWNER ON istar_user. ID"
					+ " = OWNER .user_id WHERE istar_user. ID = " + managerId
					+ " AND istar_user.is_deleted = FALSE AND istar_user.is_supended = FALSE ";

			ArrayList<User> users = new ArrayList<User>();

			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql)) {
				User user = new User();
				if (row.get("reportee_id") != null) {
					user.setId(Integer.parseInt(row.get("reportee_id")));
					user.setName(row.get("reportee_name"));
					if (row.get("reportee_designation_id") != null) {
						user.setDesignationId(Integer.parseInt(row.get("reportee_designation_id")));
						user.setDesignation(row.get("reportee_designation"));
					}

					user.setProfileImage(row.get("reportee_image"));
					users.add(user);
				}
			}
			designationUser.setUser(users);
			managerAssociateList.add(designationUser);
		}

		return managerAssociateList;
	}

	 
	@Override
	public Designation updateDesignationOfUser(User user, Integer authUserId) throws SQLException {
		Designation designation = null;
		ArrayList<User> userList = new ArrayList<User>();
		if (user.getDesignationId() != null) {
			for (Integer userId : user.getUserIds()) {
				String userSql = "update istar_user set designation_id = ? where id = ?";
				HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
				userData.put(1, user.getDesignationId());
				userData.put(2, userId);
				DBUtils.getInstance().updateObject(userSql, userData);
			}
			String sql = "SELECT * from organization_designation WHERE id=" + user.getDesignationId();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql)) {
				designation = new Designation();
				designation.setId(Integer.parseInt(row.get("id")));
				designation.setDesignation(row.get("designation"));
			}
		}

		return designation;
	} 

	@Override
	public BulkUser userVerification(BulkUser user) throws SQLException {

		String email = user.getEmail().getValue();
		String mobile = user.getMobile().getValue();
		String name = user.getName().getValue();
		BulkUserField userName = new BulkUserField();
		BulkUserField userEmail = new BulkUserField();
		BulkUserField userMobile = new BulkUserField();

		if (name.trim().length() > 2) {

			userName.setValue(name);
			userName.setError("false");
			userName.setMessage("Looks Good");
		} else {
			userName.setValue(name.trim());
			userName.setError("true");
			userName.setMessage("Error in name!");
		}

		// boolean isValid = new PhoneNumberValidator().validate(mobile);

		ArrayList<HashMap<String, String>> userDataList = new ArrayList<HashMap<String, String>>();
		String sql = "";
		ValidateResponse validate = new PhoneNumberValidator().validate(mobile.trim());
		if (validate.getIsSuccess()) {
			sql = "SELECT * FROM istar_user WHERE mobile like '%" + mobile + "%' and is_deleted = FALSE";
			userDataList = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
			if (userDataList.size() == 0) {
				userMobile.setValue(mobile);
				userMobile.setError("false");
				userMobile.setMessage("Looks Good");
			} else {
				userMobile.setValue(mobile);
				userMobile.setError("true");
				userMobile.setMessage("phone number is already exist !");
			}
		} else {
			userMobile.setValue(mobile);
			userMobile.setError("true");
			userMobile.setMessage("Invalid phone number!");
		}

		if (Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z0-9]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email)
				.find()) {
			sql = "SELECT * FROM istar_user WHERE email like '%" + email + "%' and is_deleted = FALSE";
			userDataList = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(), sql);
			if (userDataList.size() == 0) {
				userEmail.setValue(email);
				userEmail.setError("false");
				userEmail.setMessage("Look Good");
			} else {
				userEmail.setValue(email);
				userEmail.setError("true");
				userEmail.setMessage("Email address is already exist");
			}
		} else {
			userEmail.setValue(email);
			userEmail.setError("true");
			userEmail.setMessage("Invalid Email address");
		}

		user = new BulkUser();
		user.setName(userName);
		user.setEmail(userEmail);
		user.setMobile(userMobile);
		return user;
	}

	@Override
	public Boolean deletBulkUsers(ArrayList<Integer> userIds) throws SQLException {
		for (Integer id : userIds) {
			String sqlUser = "update istar_user set is_deleted=? where id = ?";
			HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
			userData.put(1, true);
			userData.put(2, id);
			DBUtils.getInstance().updateObject(sqlUser, userData);

			sqlUser = "delete from group_user where userid = ?";
			userData = new HashMap<Integer, Object>();
			userData.put(1, id);
			DBUtils.getInstance().updateObject(sqlUser, userData);

			sqlUser = "DELETE FROM user_manager WHERE manager_id = ?";
			userData = new HashMap<Integer, Object>();
			userData.put(1, id);
			DBUtils.getInstance().updateObject(sqlUser, userData);

			sqlUser = "DELETE FROM user_manager WHERE user_id = ?";
			userData = new HashMap<Integer, Object>();
			userData.put(1, id);
			DBUtils.getInstance().updateObject(sqlUser, userData);

		}
		return true;
	}

	@Override
	public User createV1User(User user, Integer uId) throws SQLException {
		try {
			String encryptedPassword = StringUtils.getMd5("test123");

			String userSql = "INSERT INTO istar_user (email, password, created_at, mobile,auth_token,login_type, is_verified, is_supend, is_supended, is_deleted, show_real_time_notification, designation_id) VALUES"
					+ " (?, ?, now(), ?, NULL, NULL, 't', NULL, 'f', 'f', 'f', ?)";

			HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
			userData.put(1, user.getEmail().trim().toLowerCase());
			userData.put(2, encryptedPassword);
			userData.put(3, user.getMobile());
			userData.put(4, user.getDesignationId());
			Integer userId = DBUtils.getInstance().updateObject(userSql, userData);

			if (userId != 0) {

				User userAddress = getCurrentLocation(user);
				Integer pincodeId = null;
				if (userAddress.getPincode() != null) {
					try {
						Integer pin = Integer.parseInt(userAddress.getPincode().trim());
						String sqlPincode = "SELECT * from pincode WHERE pin=" + pin;
						ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
								.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
						if (resultPincode.size() > 0) {
							pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
						}
					} catch (Exception e) {

					}
				}

				String addressSql = "INSERT INTO address(addressline1,addressline2,pincode_id,address_geo_longitude,address_geo_latitude, pincode) "
						+ "VALUES (?, ?, ?, ?, ?,?);";

				HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
				addressData.put(1, userAddress.getAddressLine1());
				addressData.put(2, userAddress.getAddressLine2());
				addressData.put(3, pincodeId);
				addressData.put(4, Float.valueOf(userAddress.getLongitude()));
				addressData.put(5, Float.valueOf(userAddress.getLatitude()));
				addressData.put(6, userAddress.getPincode());
				Integer addressId = DBUtils.getInstance().updateObject(addressSql, addressData);

				String profileImage = "https://storage.googleapis.com/istar-user-images/files/"
						+ StringUtils.cleanHTML(user.getName()).trim().substring(0, 1).toUpperCase() + ".png";
				String userProfileSql = "INSERT INTO user_profile ( address_id, name, dob, gender, profile_image,user_id, aadhar_no,father_name, mother_name, user_category, religion, caste_category, place_of_birth ) "
						+ "VALUES ( ?, ?, NULL, NULL, ?, ?, NULL, NULL, NULL, NULL, NULL, NULL, NULL ); ";

				HashMap<Integer, Object> userProfileData = new HashMap<Integer, Object>();
				userProfileData.put(1, addressId);
				new StringUtils();
				userProfileData.put(2, StringUtils.wordsCapitalize(StringUtils.cleanHTML(user.getName())));
				userProfileData.put(3, profileImage);
				userProfileData.put(4, userId);
				DBUtils.getInstance().updateObject(userProfileSql, userProfileData);

				String organizationID = DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
						"SELECT * from org_user WHERE userid=" + uId).get(0).get("organizationid");

				String orgUserSql = "INSERT INTO org_user (organizationid, userid) VALUES (?,?);";
				HashMap<Integer, Object> orgUserData = new HashMap<Integer, Object>();
				orgUserData.put(1, Integer.parseInt(organizationID));
				orgUserData.put(2, userId);
				DBUtils.getInstance().updateObject(orgUserSql, orgUserData);

				try {
					if (user.getTeamIds() != null && user.getTeamIds().size() > 0) {
						for (Integer teamID : user.getTeamIds()) {
							if (teamID != null) {
								if (user.getDesignation().equalsIgnoreCase("Sales Associate")) {
									String groupUserSql = "INSERT INTO group_user (qroupid, userid) VALUES (?,?);";
									HashMap<Integer, Object> groupUserData = new HashMap<Integer, Object>();
									groupUserData.put(1, teamID);
									groupUserData.put(2, userId);
									DBUtils.getInstance().updateObject(groupUserSql, groupUserData);
									groupUserSql = "SELECT * FROM istar_qroup WHERE id = " + teamID;
									ArrayList<HashMap<String, String>> groupData = DBUtils.getInstance()
											.executeQuery(Thread.currentThread().getStackTrace(), groupUserSql);
									if (groupData.size() > 0) {
										Integer owner = Integer.parseInt(groupData.get(0).get("owner"));
										groupUserSql = "INSERT INTO user_manager (user_id, manager_id) VALUES (?,?);";
										HashMap<Integer, Object> data = new HashMap<Integer, Object>();
										data.put(1, userId);
										data.put(2, owner);
										DBUtils.getInstance().updateObject(groupUserSql, data);
									}
								} else {
									String groupSql = "SELECT * FROM group_user WHERE qroupid = " + teamID;
									for (HashMap<String, String> row : DBUtils.getInstance()
											.executeQuery(Thread.currentThread().getStackTrace(), groupSql)) {
										String sql = "UPDATE user_manager SET manager_id = ? WHERE user_id = ?";
										HashMap<Integer, Object> data = new HashMap<Integer, Object>();
										data.put(1, userId);
										data.put(2, Integer.parseInt(row.get("userid")));
										DBUtils.getInstance().updateObject(sql, data);
									}
									String groupUserSql = "UPDATE istar_group set OWNER = ? WHERE id = ?";
									HashMap<Integer, Object> groupUserData = new HashMap<Integer, Object>();
									groupUserData.put(1, userId);
									groupUserData.put(2, teamID);
									DBUtils.getInstance().updateObject(groupUserSql, groupUserData);
								}
							}
						}
					}
					if (user.getOwnerId() != null) {

						String userManagerSql = "INSERT INTO user_manager (user_id, manager_id) VALUES (?, ?)";
						HashMap<Integer, Object> userManagerData = new HashMap<Integer, Object>();
						userManagerData.put(1, userId);
						userManagerData.put(2, user.getOwnerId());
						DBUtils.getInstance().updateObject(userManagerSql, userManagerData);
					}
					if (user.getReporteeUsersIds() != null && user.getReporteeUsersIds().size() > 0) {
						for (Integer id : user.getReporteeUsersIds()) {
							String userManagerSql = "INSERT INTO user_manager (user_id, manager_id) VALUES (?, ?)";
							HashMap<Integer, Object> userManagerData = new HashMap<Integer, Object>();
							userManagerData.put(1, id);
							userManagerData.put(2, userId);
							DBUtils.getInstance().updateObject(userManagerSql, userManagerData);
						}
					}
					if (user.getDesignation() != null) {
						Integer roleID;
						if (user.getDesignation().equalsIgnoreCase("Sales Associate")) {
							roleID = 14;
						} else if (user.getDesignation().equalsIgnoreCase("IT_ADMIN")) {
							roleID = 15;
						} else {
							roleID = 13;
						}
						String roleUserSql = "INSERT INTO user_role (userid, roleid) VALUES (?,?);";
						HashMap<Integer, Object> roleUserData = new HashMap<Integer, Object>();
						roleUserData.put(1, userId);
						roleUserData.put(2, roleID);
						DBUtils.getInstance().updateObject(roleUserSql, roleUserData);
						if (roleID == 13 || roleID == 14 || roleID == 15) {
							String salesManagerProfileSql = "SELECT * from sales_manager_profile WHERE user_id="
									+ userId;
							ArrayList<HashMap<String, String>> resultManagerProfile = DBUtils.getInstance()
									.executeQuery(Thread.currentThread().getStackTrace(), salesManagerProfileSql);
							if (resultManagerProfile.size() == 0) {
								String sqlSalesProfile = "INSERT INTO sales_manager_profile (timezone, location, language,currency,user_id, language_pref) VALUES ( NULL, NULL, NULL, NULL, ?, ? );  ";
								HashMap<Integer, Object> salesMangerProfileData = new HashMap<Integer, Object>();
								salesMangerProfileData.put(1, userId);
								salesMangerProfileData.put(2, "en-IN");
								DBUtils.getInstance().updateObject(sqlSalesProfile, salesMangerProfileData);
							}
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			user = new UserDAOPG().findbyID(userId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User updateV1User(User user, Integer userId) throws SQLException, IOException, InterruptedException {
		String sql = "update user_profile set name = ? where user_id = ?";
		HashMap<Integer, Object> userData = new HashMap<Integer, Object>();
		userData.put(1, StringUtils.wordsCapitalize(StringUtils.cleanHTML(user.getName())));
		userData.put(2, user.getId());
		DBUtils.getInstance().updateObject(sql, userData);

		if (user.getDesignationId() != null) {
			sql = "update istar_user set designation_id = ? where id = ?";
			userData = new HashMap<Integer, Object>();
			userData.put(1, user.getDesignationId());
			userData.put(2, user.getId());
			DBUtils.getInstance().updateObject(sql, userData);
		}
		User userAddress = getCurrentLocation(user);
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance().executeQuery(
				Thread.currentThread().getStackTrace(), "SELECT * from user_profile WHERE user_id=" + user.getId());
		if (result.size() > 0) {
			String addressId = result.get(0).get("address_id");
			if (addressId != null) {

				if (!userAddress.getPincode().trim().isEmpty()) {
					String sqlPincode = "SELECT * from pincode WHERE pin= " + userAddress.getPincode();
					ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
					if (resultPincode.size() > 0) {
						Integer pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
						sql = "update address set pincode_id=? where id = ?";
						HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
						addressData.put(1, pincodeId);
						addressData.put(2, Integer.parseInt(addressId));
						DBUtils.getInstance().updateObject(sql, addressData);
					}
				}
				sql = "update address set addressline1=?, addressline2=?, pincode=? where id = ?";
				HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
				addressData.put(1, userAddress.getAddressLine1());
				addressData.put(2, userAddress.getAddressLine2());
				addressData.put(3, userAddress.getPincode());
				addressData.put(4, Integer.parseInt(addressId));
				DBUtils.getInstance().updateObject(sql, addressData);
			} else {
				Integer pincodeId = null;
				if (!userAddress.getPincode().trim().isEmpty()) {
					String sqlPincode = "SELECT * from pincode WHERE pin=" + userAddress.getPincode();
					ArrayList<HashMap<String, String>> resultPincode = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), sqlPincode);
					if (resultPincode.size() > 0) {
						pincodeId = Integer.parseInt(resultPincode.get(0).get("id"));
					}
				}
				String addressSql = "INSERT INTO address(addressline1,addressline2,pincode_id,address_geo_longitude,address_geo_latitude, pincode) "
						+ "VALUES (?,?, ?, ?, ?,?);";

				HashMap<Integer, Object> addressData = new HashMap<Integer, Object>();
				addressData.put(1, userAddress.getAddressLine1());
				addressData.put(2, userAddress.getAddressLine2());
				addressData.put(3, pincodeId);
				addressData.put(4, userAddress.getLongitude());
				addressData.put(5, userAddress.getLatitude());
				addressData.put(6, userAddress.getPincode());

				Integer addrID = DBUtils.getInstance().updateObject(addressSql, addressData);

				sql = "update user_profile set address_id = ? where user_id = ?";
				HashMap<Integer, Object> addresIdData = new HashMap<Integer, Object>();
				addresIdData.put(1, addrID);
				addresIdData.put(2, user.getId());
				DBUtils.getInstance().updateObject(sql, addresIdData);
			}
		}

		if (user.getOwnerId() != null) {
			String userManagerSql = "DELETE FROM user_manager where user_id = ?";
			HashMap<Integer, Object> userManagerData = new HashMap<Integer, Object>();
			userManagerData.put(1, user.getId());
			DBUtils.getInstance().updateObject(userManagerSql, userManagerData);
			userManagerSql = "INSERT INTO user_manager (user_id, manager_id) VALUES (?, ?)";
			userManagerData = new HashMap<Integer, Object>();
			userManagerData.put(1, user.getId());
			userManagerData.put(2, user.getOwnerId());
			DBUtils.getInstance().updateObject(userManagerSql, userManagerData);
		}

		if (user.getReporteeUsersIds() != null && user.getReporteeUsersIds().size() > 0) {
			String userManagerSql = "DELETE FROM user_manager where manager_id = ?";
			HashMap<Integer, Object> userManagerData = new HashMap<Integer, Object>();
			userManagerData.put(1, user.getId());
			DBUtils.getInstance().updateObject(userManagerSql, userManagerData);
			for (Integer id : user.getReporteeUsersIds()) {
				userManagerSql = "INSERT INTO user_manager (user_id, manager_id) VALUES (?, ?)";
				userManagerData = new HashMap<Integer, Object>();
				userManagerData.put(1, id);
				userManagerData.put(2, user.getId());
				DBUtils.getInstance().updateObject(userManagerSql, userManagerData);
			}
		}
		if (user.getDesignation() != null) {
			Integer roleID;
			if (user.getDesignation().equalsIgnoreCase("Sales Associate")) {
				roleID = 14;
			} else if (user.getDesignation().equalsIgnoreCase("IT_ADMIN")) {
				roleID = 15;
			} else {
				roleID = 13;
			}
			String rolesql = "delete from user_role where userid = ?";
			HashMap<Integer, Object> roleData = new HashMap<Integer, Object>();
			roleData.put(1, user.getId());
			DBUtils.getInstance().updateObject(rolesql, roleData);

			String userRoleSql = "insert into user_role (userid, roleid) values(?,?)";
			HashMap<Integer, Object> userRoleData = new HashMap<Integer, Object>();
			userRoleData.put(1, user.getId());
			userRoleData.put(2, roleID);
			DBUtils.getInstance().updateObject(userRoleSql, userRoleData);
			if (roleID == 13 || roleID == 14 || roleID == 15) {
				String salesManagerProfileSql = "SELECT * from sales_manager_profile WHERE user_id=" + user.getId();
				ArrayList<HashMap<String, String>> resultManagerProfile = DBUtils.getInstance()
						.executeQuery(Thread.currentThread().getStackTrace(), salesManagerProfileSql);
				if (resultManagerProfile.size() == 0) {
					String sqlSalesProfile = "INSERT INTO sales_manager_profile (timezone, location, language,currency,user_id, language_pref) VALUES ( NULL, NULL, NULL, NULL, ?, ? );  ";
					HashMap<Integer, Object> salesMangerProfileData = new HashMap<Integer, Object>();
					salesMangerProfileData.put(1, user.getId());
					salesMangerProfileData.put(2, "en-IN");
					DBUtils.getInstance().updateObject(sqlSalesProfile, salesMangerProfileData);
				}

			}
		}

		if (user.getTeamIds() != null && user.getTeamIds().size() > 0) {
			if (user.getDesignation().equalsIgnoreCase("Sales Associate")) {
				String teamsql = "delete from group_user where userid = ?";
				HashMap<Integer, Object> teamData = new HashMap<Integer, Object>();
				teamData.put(1, user.getId());
				DBUtils.getInstance().updateObject(teamsql, teamData);

				for (Integer teamID : user.getTeamIds()) {
					if (teamID != null) {
						String userteamSql = "insert into group_user (qroupid , userid) values(?,?) ";
						HashMap<Integer, Object> userteamData = new HashMap<Integer, Object>();
						userteamData.put(1, teamID);
						userteamData.put(2, user.getId());
						DBUtils.getInstance().updateObject(userteamSql, userteamData);
					}
					String groupUserSql = "SELECT * FROM istar_group WHERE id = " + teamID;
					ArrayList<HashMap<String, String>> groupData = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), groupUserSql);
					if (groupData.size() > 0) {
						Integer owner = Integer.parseInt(groupData.get(0).get("owner"));
						groupUserSql = "INSERT INTO user_manager (user_id, manager_id) VALUES (?,?);";
						HashMap<Integer, Object> data = new HashMap<Integer, Object>();
						data.put(1, userId);
						data.put(2, owner);
						DBUtils.getInstance().updateObject(groupUserSql, data);
					}
				}
			} else {
				String teamSql = "SELECT istar_user.id FROM istar_group LEFT JOIN group_user on group_user.qroupid = istar_group.id"
						+ " LEFT JOIN istar_user on istar_user.id = group_user.userid and istar_user.is_deleted = FALSE WHERE"
						+ " organization_id in (SELECT organizationid FROM org_user WHERE userid = " + userId
						+ ") AND istar_group.is_deleted = FALSE GROUP BY istar_group.id, " + "istar_user.id";
				HashMap<Integer, Integer> data = new HashMap<Integer, Integer>();
				Integer i = 0;
				for (HashMap<String, String> row : DBUtils.getInstance()
						.executeQuery(Thread.currentThread().getStackTrace(), teamSql)) {
					if (row.get("id") != null) {
						data.put(i++, Integer.parseInt(row.get("id")));
					}
				}
				if (data.containsKey(user.getId())) {
					teamSql = "DELETE FROM group_user WHERE userid = ?";
					HashMap<Integer, Object> userteamData = new HashMap<Integer, Object>();
					userteamData.put(1, user.getId());
					DBUtils.getInstance().updateObject(teamSql, userteamData);
					teamSql = "DELETE FROM user_manager WHERE userid = ?";
					userteamData = new HashMap<Integer, Object>();
					userteamData.put(1, user.getId());
					DBUtils.getInstance().updateObject(teamSql, userteamData);
				}
				teamSql = "UPDATE istar_group set owner = ? where owner  = ?";
				HashMap<Integer, Object> teamData = new HashMap<Integer, Object>();
				teamData.put(1, null);
				teamData.put(2, user.getId());
				DBUtils.getInstance().updateObject(teamSql, teamData);

				for (Integer teamID : user.getTeamIds()) {
					String groupUserSql = "SELECT * FROM group_user WHERE qroupid = " + teamID;
					for (HashMap<String, String> row : DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), groupUserSql)) {
						teamSql = "UPDATE user_manager SET manager_id = ? WHERE user_id = ?";
						teamData = new HashMap<Integer, Object>();
						teamData.put(1, user.getId());
						teamData.put(2, Integer.parseInt(row.get("userid")));
						DBUtils.getInstance().updateObject(teamSql, teamData);
					}
					teamSql = "UPDATE istar_group set OWNER = ? WHERE id = ?";
					teamData = new HashMap<Integer, Object>();
					teamData.put(1, userId);
					teamData.put(2, teamID);
					DBUtils.getInstance().updateObject(teamSql, teamData);
				}
			}
		}
		user = new UserDAOPG().findbyID(user.getId());
		return user;
	}

	@Override
	public User getUserCreationFieldsData(String designation, Integer userId) throws SQLException {

		User users = new User();
		String sql = "";
		switch (designation) {

		case "Sales Associate":
			ArrayList<Team> teamList = new ArrayList<Team>();
			sql = "SELECT * FROM istar_group WHERE organization_id in (SELECT organizationid FROM org_user WHERE userid = "
					+ userId + ") and is_deleted = false";
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql)) {
				Team team = new Team();
				team.setId(Integer.parseInt(row.get("id")));
				team.setName(row.get("name"));
				teamList.add(team);
			}
			users.setTeams(teamList);
			break;

		case "Sales Manager":
			ArrayList<Team> teamList1 = new ArrayList<Team>();
			sql = "SELECT * FROM istar_group WHERE organization_id in (SELECT organizationid FROM org_user WHERE userid = "
					+ userId + ") and is_deleted = false";
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql)) {
				Team team = new Team();
				team.setId(Integer.parseInt(row.get("id")));
				team.setName(row.get("name"));
				teamList1.add(team);
			}
			users.setTeams(teamList1);
			ArrayList<User> ownerList = new ArrayList<User>();
			sql = "select istar_user.id, user_profile.name from user_role LEFT JOIN istar_user on istar_user.id = user_role.userid and istar_user.is_deleted = FALSE "
					+ "LEFT JOIN user_profile on user_profile.user_id = istar_user.id WHERE user_role.userid in (SELECT org_user.userid FROM org_user WHERE org_user.organizationid"
					+ " in (SELECT org_user.organizationid FROM org_user WHERE org_user.userid = " + userId
					+ ")) and user_role.roleid = 13;";
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql)) {
				User user = new User();
				user.setId(Integer.parseInt(row.get("id")));
				user.setName(row.get("name"));
				ownerList.add(user);
			}
			users.setReporteeUser(ownerList);
			break;

		default:
			ArrayList<User> reporteeUserList = new ArrayList<User>();
			ArrayList<User> ownerList1 = new ArrayList<User>();
			sql = "select istar_user.id, user_profile.name from user_role LEFT JOIN istar_user on istar_user.id = user_role.userid and istar_user.is_deleted = FALSE "
					+ "LEFT JOIN user_profile on user_profile.user_id = istar_user.id WHERE user_role.userid in (SELECT org_user.userid FROM org_user WHERE org_user.organizationid"
					+ " in (SELECT org_user.organizationid FROM org_user WHERE org_user.userid = " + userId
					+ ")) and user_role.roleid = 13;";
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql)) {
				User user = new User();
				user.setId(Integer.parseInt(row.get("id")));
				user.setName(row.get("name"));
				ownerList1.add(user);
				reporteeUserList.add(user);
			}
			users.setReporteeUser(reporteeUserList);
			users.setReporteeUser(ownerList1);
			break;
		}
		return users;
	}

	@Override
	public User getUserUpdationFieldsData(Integer user_id) throws SQLException {
		User user = new User();

		String sql = "SELECT istar_user.email, istar_user.mobile, user_profile.name, user_profile.profile_image, organization_designation.designation "
				+ "FROM istar_user LEFT JOIN user_profile on user_profile.user_id = istar_user.id LEFT JOIN organization_designation on "
				+ "organization_designation.id = istar_user.designation_id WHERE istar_user.id = " + user_id;

		ArrayList<HashMap<String, String>> designationData = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);

		user.setId(user_id);
		user.setEmail(designationData.get(0).get("email"));
		user.setMobile(designationData.get(0).get("mobile"));
		user.setName(designationData.get(0).get("name"));
		user.setProfileImage(designationData.get(0).get("profile_image"));
		String designation = designationData.get(0).get("designation");

		switch (designation) {

		case "Sales Associate":
			ArrayList<Integer> teamIds = new ArrayList<Integer>();
			ArrayList<HashMap<String, String>> teamData = DBUtils.getInstance().executeQuery(
					Thread.currentThread().getStackTrace(), "SELECT * from group_user WHERE userid = " + user_id);
			if (teamData.size() > 0) {
				for (HashMap<String, String> row : teamData) {
					teamIds.add(Integer.parseInt(row.get("qroupid")));
				}
				user.setTeamIds(teamIds);
			}
			break;

		case "Sales Manager":
			ArrayList<Integer> teamIds1 = new ArrayList<Integer>();
			ArrayList<HashMap<String, String>> teamData1 = DBUtils.getInstance().executeQuery(
					Thread.currentThread().getStackTrace(),
					"SELECT * from istar_group WHERE owner = " + user_id + " AND is_deleted = FALSE");
			if (teamData1.size() > 0) {
				for (HashMap<String, String> row : teamData1) {

					teamIds1.add(Integer.parseInt(row.get("id")));
				}
				user.setTeamIds(teamIds1);
			}

			sql = "SELECT * FROM user_manager WHERE user_id = " + user_id;
			ArrayList<HashMap<String, String>> ownerData = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql);
			if (ownerData.size() > 0) {
				User owner = new User();
				owner.setId(Integer.parseInt(ownerData.get(0).get("manager_id")));
				user.setOwner(owner);
			}
			break;

		default:
			sql = "SELECT * FROM user_manager WHERE user_id = " + user_id;
			ArrayList<HashMap<String, String>> ownerData1 = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql);
			if (ownerData1.size() > 0) {
				User owner = new User();
				owner.setId(Integer.parseInt(ownerData1.get(0).get("manager_id")));
				user.setOwner(owner);
			}
			ArrayList<Integer> reporteeUsersIds = new ArrayList<Integer>();
			sql = "SELECT * FROM user_manager WHERE manager_id = " + user_id;
			ArrayList<HashMap<String, String>> reporteeData = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql);
			if (reporteeData.size() > 0) {
				reporteeUsersIds.add(Integer.parseInt(reporteeData.get(0).get("user_id")));

				user.setReporteeUsersIds(reporteeUsersIds);
			}
			break;
		}
		return user;
	}

	@Override
	public User getCurrentLocation(User user) throws IOException, InterruptedException {
		User userAddress = new User();
		if (user.getLatitude() != null && user.getLongitude() != null) {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + user.getLatitude()
							+ "," + user.getLongitude() + "&key=AIzaSyCCOVWoXx7cBAf3jBKpGcVMOaZzDxhq0V8"))
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			String responseBody = response.body();
			JSONObject myObject = new JSONObject(responseBody);
			JSONArray jsonArray = myObject.getJSONArray("results");
			if (jsonArray.length() > 0) {
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String address = jsonObject.getString("formatted_address").toString();
				String[] addressArray = address.split(",", 2);
				userAddress.setAddressLine1(addressArray[0]);
				userAddress.setAddressLine2(addressArray[1]);
				JSONArray jsaddress_components = jsonObject.getJSONArray("address_components");
				Integer arraySize = jsaddress_components.length() - 1;
				JSONObject jsObj = jsaddress_components.getJSONObject(arraySize);
				String pincode = jsObj.getString("long_name");
				userAddress.setPincode(pincode);
				userAddress.setLatitude(user.getLatitude());
				userAddress.setLongitude(user.getLongitude());
				// System.out.println("Address : " + address + "\nPincode : " + pincode +
				// "\nArray Size : " + arraySize);
			} else {
				userAddress.setAddressLine1("3632/1");
				userAddress.setAddressLine2(
						"HAL 2nd Stage, Doopanahalli, Indiranagar, Bengaluru, Karnataka 560008, India");
				userAddress.setPincode("560008");
				userAddress.setLatitude("12.9659472");
				userAddress.setLongitude("77.6380792");
			}
		}
		return userAddress;
	}
}
