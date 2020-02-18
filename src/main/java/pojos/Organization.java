package pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Organization {
	private Integer id;
	private String name;
	private String organizationType;
	private String industry;
	private String website;
	private String addressLine1;
	private String addressLine2;
	private String socialSite;
	private String landmark;
	private Pincode pincode;
	private Integer pin;
	private String boardlineNumber;
	private String profile;

	public Organization() {
		super();
	}

	public Organization(Integer id, String name, String organizationType, String industry, String website,
			String addressLine1, String addressLine2, String socialSite, String landmark, Integer pin,String profile) {
		super();
		this.id = id;
		this.name = name;
		this.organizationType = organizationType;
		this.industry = industry;
		this.website = website;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.socialSite = socialSite;
		this.landmark = landmark;
		this.pin = pin;
		this.profile=profile;
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

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
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

	public String getSocialSite() {
		return socialSite;
	}

	public void setSocialSite(String socialSite) {
		this.socialSite = socialSite;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public Pincode getPincode() {
		return pincode;
	}

	public void setPincode(Pincode pincode) {
		this.pincode = pincode;
	}

	public Integer getPin() {
		return pin;
	}

	public void setPin(Integer pin) {
		this.pin = pin;
	}

	public String getBoardlineNumber() {
		return boardlineNumber;
	}

	public void setBoardlineNumber(String boardlineNumber) {
		this.boardlineNumber = boardlineNumber;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

}
