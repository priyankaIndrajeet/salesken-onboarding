package pojos;

public class SIPCredential {
	private Integer id;
	private String username;
	private String password;
	private String url;
	private String provider;
	private Boolean isActive;

	public SIPCredential() {
		super();
	}

	public SIPCredential(Integer id, String username, String password, String url, String provider, Boolean isActive) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.url = url;
		this.provider = provider;
		this.isActive = isActive;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
