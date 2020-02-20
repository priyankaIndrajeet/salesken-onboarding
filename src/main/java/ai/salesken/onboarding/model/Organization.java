package ai.salesken.onboarding.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Organization {
	private Integer id;
	private String name;
	private String website;
	private String industry_type;
	private String board_number;
	private String address_line1;
	private String address_line2;
	private String landmark;
	private String city;
	private String state;
	private String country;
	private Integer zipcode;
	private String description;

	public Organization() {
		super();
	}

	public Organization(Integer id, String name, String website, String industry_type, String board_number,
			String address_line1, String address_line2, String landmark, String city, String state, String country,
			Integer zipcode, String description) {
		super();
		this.id = id;
		this.name = name;
		this.website = website;
		this.industry_type = industry_type;
		this.board_number = board_number;
		this.address_line1 = address_line1;
		this.address_line2 = address_line2;
		this.landmark = landmark;
		this.city = city;
		this.state = state;
		this.country = country;
		this.zipcode = zipcode;
		this.description = description;
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

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIndustry_type() {
		return industry_type;
	}

	public void setIndustry_type(String industry_type) {
		this.industry_type = industry_type;
	}

	public String getBoard_number() {
		return board_number;
	}

	public void setBoard_number(String board_number) {
		this.board_number = board_number;
	}

	public String getAddress_line1() {
		return address_line1;
	}

	public void setAddress_line1(String address_line1) {
		this.address_line1 = address_line1;
	}

	public String getAddress_line2() {
		return address_line2;
	}

	public void setAddress_line2(String address_line2) {
		this.address_line2 = address_line2;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getZipcode() {
		return zipcode;
	}

	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
