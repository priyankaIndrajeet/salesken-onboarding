package pojos;

/**
 * @author salesken
 *
 */
public class SalesContactPerson {
	private Integer id;
	private String name;
	private String email;
	private String phoneNumber;
	private Integer leadId;
	private String officePhoneNumber;
	private String companyName;
	private String city;
	private String state;
	private String createdAt;
	private String updatedAt;
	private String languagePref;
	private String country;
	private String jobTitle;

	public SalesContactPerson() {
		super();
	}

	public SalesContactPerson(Integer id, String name, String email, String phoneNumber, Integer leadId, String officePhoneNumber, String companyName, String city, String state, String createdAt,
			String updatedAt, String languagePref, String country, String jobTitle) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.leadId = leadId;
		this.officePhoneNumber = officePhoneNumber;
		this.companyName = companyName;
		this.city = city;
		this.state = state;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.languagePref = languagePref;
		this.country = country;
		this.jobTitle = jobTitle;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getLeadId() {
		return leadId;
	}

	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}

	public String getOfficePhoneNumber() {
		return officePhoneNumber;
	}

	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getLanguagePref() {
		return languagePref;
	}

	public void setLanguagePref(String languagePref) {
		this.languagePref = languagePref;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

}
