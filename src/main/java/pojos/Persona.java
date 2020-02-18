package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class Persona {
	private Integer id;
	private String name;
	private String imageUrl;
	private ArrayList<PersonaMetadata> personaMetadatas = new ArrayList<PersonaMetadata>();
	private Integer organizationId;
	private ArrayList<Team> teams = new ArrayList<Team>();
	private ArrayList<Product> products = new ArrayList<Product>();
	private ArrayList<Integer> processIds = new ArrayList<Integer>();

	public Persona() {
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public ArrayList<PersonaMetadata> getPersonaMetadatas() {
		return personaMetadatas;
	}

	public void setPersonaMetadatas(ArrayList<PersonaMetadata> personaMetadatas) {
		this.personaMetadatas = personaMetadatas;
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public ArrayList<Integer> getProcessIds() {
		return processIds;
	}

	public void setProcessIds(ArrayList<Integer> processIds) {
		this.processIds = processIds;
	}

}
