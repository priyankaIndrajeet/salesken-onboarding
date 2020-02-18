package pojos;

public class Pincode {
	Integer id; 
	String city; 
	String state; 
	String country; 
	String pin;
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public Pincode(Integer id, String city, String state, String country, String pin) {
		super();
		this.id = id;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pin = pin;
	}
	public Pincode() {
		super();
	} 
	
	
}
