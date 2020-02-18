package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class PersonaMetadata {
	private Integer id;
	private String key;
	private Integer personaId;
	private Integer relationTypeId;
	private ArrayList<PersonaData> personaDatas;
	private ArrayList<Product> personaProductFeatures; // This is for relation type of mapping
	public PersonaMetadata() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	 
	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	public ArrayList<PersonaData> getPersonaDatas() {
		return personaDatas;
	}

	public void setPersonaDatas(ArrayList<PersonaData> personaDatas) {
		this.personaDatas = personaDatas;
	}

	public Integer getRelationTypeId() {
		return relationTypeId;
	}

	public void setRelationTypeId(Integer relationTypeId) {
		this.relationTypeId = relationTypeId;
	}

	public ArrayList<Product> getPersonaProductFeatures() {
		return personaProductFeatures;
	}

	public void setPersonaProductFeatures(ArrayList<Product> personaProductFeatures) {
		this.personaProductFeatures = personaProductFeatures;
	}
 

 

	 
 

}
