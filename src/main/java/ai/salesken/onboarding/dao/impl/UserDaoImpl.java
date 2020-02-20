package ai.salesken.onboarding.dao.impl;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import ai.salesken.onboarding.dao.UserDao;
import ai.salesken.onboarding.enums.UserStatus;
import ai.salesken.onboarding.model.User;
import ai.salesken.onboarding.model.ValidateResponse;
import ai.salesken.onboarding.utils.DButils.DBUtils;
import ai.salesken.onboarding.utils.impl.PhoneNumberValidator;
import ai.salesken.onboarding.utils.impl.StringUtilImpl;

public class UserDaoImpl implements UserDao {
	@Override
	public User findbyEmail(String email) throws SQLException {
		User user = null;
		String sql = "SELECT istar_user. ID,  istar_user.email, istar_user.mobile,  user_profile.name, sales_manager_profile.language_pref AS langg, user_profile.profile_image AS profileImage, istar_user.password, istar_user.is_verified AS isVerified, istar_user.is_supended AS isSuspended, istar_user.is_deleted isDeleted FROM istar_user, user_profile, sales_manager_profile WHERE istar_user.email='"
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

			user.setMobile(row.get("mobile"));
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
			user.setMobile(row.get("mobile"));
			user.setFirst_name(row.get("name"));
			user.setPassword(row.get("password"));
			user.setProfile_image(row.get("profileImage"));
			user.setRoles(getRoles(id));
			user.setStatus(UserStatus.Inactive.name());
			getLicenseKeys(user);
			// user.setLicenseKeys(getLicenseKeys(id));
		}
		return user;
	}

	@Override
	public ArrayList<User> getPreviewFromFile(Integer id, String filepath) throws SQLException {
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
											name.addProperty("value", StringUtilImpl.stringCapitalize(cellValue));
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
		ArrayList<User> result = new ArrayList<User>();
		result = new Gson().fromJson(new Gson().toJson(resError), new TypeToken<ArrayList<User>>() {
		}.getType());
		return result;
	}
}
