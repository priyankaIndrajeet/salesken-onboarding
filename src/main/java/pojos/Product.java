package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {
	private Integer id;
	private String name;
	private Integer organizationId;
	private String createdAt;
	private String updatedAt;
	private String description;
	private String image;
	private Boolean deleted;
	private Float price;
	private Integer currencyId;
	private String currencyName;
	private ArrayList<ProductAsset> assets = new ArrayList<ProductAsset>();
	private ArrayList<ProductFeature> productFeatures;
	private ArrayList<Team> teams ;
	private ArrayList<Persona> personas;
	private ArrayList<Integer> processIds;
	
	public Product() {
		super();
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String discription) {
		this.description = discription;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public ArrayList<ProductAsset> getAssets() {
		return assets;
	}

	public void setAssets(ArrayList<ProductAsset> assets) {
		this.assets = assets;
	}

	public Product(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;

	}

	public ArrayList<ProductFeature> getProductFeatures() {
		return productFeatures;
	}

	public void setProductFeatures(ArrayList<ProductFeature> productFeatures) {
		this.productFeatures = productFeatures;
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}

	public ArrayList<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(ArrayList<Persona> personas) {
		this.personas = personas;
	}

	public ArrayList<Integer> getProcessIds() {
		return processIds;
	}

	public void setProcessIds(ArrayList<Integer> processIds) {
		this.processIds = processIds;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	

	
}
