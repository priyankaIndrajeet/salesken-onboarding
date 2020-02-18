package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PersonaData {
	private Integer id;
	private String value;
	private Integer personaId;
	private Integer metadataId;
	private ArrayList<Product> personaProductFeatures;

	public PersonaData() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	public Integer getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(Integer metadataId) {
		this.metadataId = metadataId;
	}

	public ArrayList<Product> getPersonaProductFeatures() {
		return personaProductFeatures;
	}

	public void setPersonaProductFeatures(ArrayList<Product> personaProductFeatures) {
		this.personaProductFeatures = personaProductFeatures;
	}
}
