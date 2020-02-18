/**
 * 
 */
package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Vaibhav Verma
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	private Integer id;
	private String email;
	private String mobile;
	private String token;
	private String name;
	private String language;
	private String profileImage;
	private ArrayList<String> roles = new ArrayList<String>();
	private ArrayList<String> licenseKeys = new ArrayList<String>();
	private String password;
	private String newPassword;
	private Boolean isVerified;
	private Boolean isSuspended;
	private Boolean isDeleted;
	private String addressLine1;
	private String addressLine2;
	private String pincode;
	private ArrayList<Integer> teamIds;
	private ArrayList<Integer> roleIds;
	private String status;
	private String company;
	private String city;
	private String department;
	private Integer designationId;
	private String designation;
	private ArrayList<SIPCredential> sipCredentials = new ArrayList<SIPCredential>();
	private ArrayList<User> reporteeUser;
	private ArrayList<User> owners;
	private ArrayList<Team> teams;
	private User owner;
	private ArrayList<Integer> userIds;
	private ArrayList<Integer> reporteeUsersIds;
	private Integer ownerId;
	private Integer managerId;
	private String longitude;
	private String latitude;

	public User() {
		super();
	}

	public User(UserBuilder builder) {
		super();
		this.id = builder.id;
		this.email = builder.email;
		this.mobile = builder.mobile;
		this.token = builder.token;
		this.name = builder.name;
		this.language = builder.language;
		this.profileImage = builder.profileImage;
		this.roles = builder.roles;
		this.licenseKeys = builder.licenseKeys;
		this.password = builder.password;
		this.status = builder.status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Boolean getIsSuspended() {
		return isSuspended;
	}

	public void setIsSuspended(Boolean isSuspended) {
		this.isSuspended = isSuspended;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public ArrayList<String> getLicenseKeys() {
		return licenseKeys;
	}

	public void setLicenseKeys(ArrayList<String> licenseKeys) {
		this.licenseKeys = licenseKeys;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public ArrayList<Integer> getTeamIds() {
		return teamIds;
	}

	public void setTeamIds(ArrayList<Integer> teamIds) {
		this.teamIds = teamIds;
	}

	public ArrayList<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(ArrayList<Integer> roleIds) {
		this.roleIds = roleIds;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public ArrayList<SIPCredential> getSipCredentials() {
		return sipCredentials;
	}

	public void setSipCredentials(ArrayList<SIPCredential> sipCredentials) {
		this.sipCredentials = sipCredentials;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public ArrayList<User> getReporteeUser() {
		return reporteeUser;
	}

	public void setReporteeUser(ArrayList<User> reporteeUser) {
		this.reporteeUser = reporteeUser;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public ArrayList<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(ArrayList<Integer> userIds) {
		this.userIds = userIds;
	}

	public Integer getManagerId() {
		return managerId;
	}

	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	public Integer getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Integer designationId) {
		this.designationId = designationId;
	}

	public ArrayList<Integer> getReporteeUsersIds() {
		return reporteeUsersIds;
	}

	public void setReporteeUsersIds(ArrayList<Integer> reporteeUsersIds) {
		this.reporteeUsersIds = reporteeUsersIds;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}

	public ArrayList<User> getOwners() {
		return owners;
	}

	public void setOwners(ArrayList<User> owners) {
		this.owners = owners;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public static class UserBuilder {
		private Integer id;
		private String email;
		private String mobile;
		private String token;
		private String name;
		private String language;
		private String profileImage;
		private ArrayList<String> roles = new ArrayList<String>();
		private ArrayList<String> licenseKeys = new ArrayList<String>();
		private String password;
		private String status;

		public UserBuilder(Integer id) {
			this.id = id;
		}

		public UserBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserBuilder mobile(String mobile) {
			this.mobile = mobile;
			return this;
		}

		public UserBuilder token(String token) {
			this.token = token;
			return this;
		}

		public UserBuilder name(String name) {
			this.name = name;
			return this;
		}

		public UserBuilder language(String language) {
			this.language = language;
			return this;
		}

		public UserBuilder profileImage(String profileImage) {
			this.profileImage = profileImage;
			return this;
		}

		public UserBuilder roles(ArrayList<String> roles) {
			this.roles = roles;
			return this;
		}

		public UserBuilder licenseKeys(ArrayList<String> licenseKeys) {
			this.licenseKeys = roles;
			return this;
		}

		public UserBuilder password(String password) {
			this.password = password;
			return this;
		}

		public UserBuilder status(String status) {
			this.status = status;
			return this;
		}

		public User build() {
			User user = new User(this);
			return user;
		}

	}

	public enum UserRoleTypes {
		SUPER_ADMIN, OWNER, SALES_MANAGER, SALES_ASSOCIATE, IT_ADMIN
	}

}
