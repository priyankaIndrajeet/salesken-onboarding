/**
 * 
 */
package pojos;

import java.util.ArrayList;

/**
 * @author Vaibhav Verma
 *
 */
public class Lead {
	private Integer id;
	private Integer owner;
	private Integer actor;
	private Integer productId;
	private String stage;
	private String leadSource;
	private String companyName;
	private String address;
	private String createdAt;
	private String updatedAt;
	private String status;
	private String country;
	private String state;
	private String city;
	private String pinCode;
	private String reason;
	private String value;
	private String companyDetails;
	private String companyWebsite;
	private String timezone;
	private ArrayList<SalesContactPerson> salesContactPersons = new ArrayList<SalesContactPerson>();
	private Task lastTask;

	public Lead() {
		super();
	}

	public Lead(Integer id, Integer owner, Integer actor, Integer productId, String stage, String leadSource,
			String companyName, String address, String createdAt, String updatedAt, String status, String country,
			String state, String city, String pinCode, String reason, String value, String companyDetails,
			String companyWebsite, String timezone, ArrayList<SalesContactPerson> salesContactPersons) {
		super();
		this.id = id;
		this.owner = owner;
		this.actor = actor;
		this.productId = productId;
		this.stage = stage;
		this.leadSource = leadSource;
		this.companyName = companyName;
		this.address = address;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.status = status;
		this.country = country;
		this.state = state;
		this.city = city;
		this.pinCode = pinCode;
		this.reason = reason;
		this.value = value;
		this.companyDetails = companyDetails;
		this.companyWebsite = companyWebsite;
		this.timezone = timezone;
		this.salesContactPersons = salesContactPersons;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public Integer getActor() {
		return actor;
	}

	public void setActor(Integer actor) {
		this.actor = actor;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCompanyDetails() {
		return companyDetails;
	}

	public void setCompanyDetails(String companyDetails) {
		this.companyDetails = companyDetails;
	}

	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public ArrayList<SalesContactPerson> getSalesContactPersons() {
		return salesContactPersons;
	}

	public void setSalesContactPersons(ArrayList<SalesContactPerson> salesContactPersons) {
		this.salesContactPersons = salesContactPersons;
	}

	public Task getLastTask() {
		return lastTask;
	}

	public void setLastTask(Task lastTask) {
		this.lastTask = lastTask;
	}

}
