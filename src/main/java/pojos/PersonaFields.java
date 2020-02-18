package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class PersonaFields {
	private ArrayList<PersonaMetadata> personaMetadatas = new ArrayList<PersonaMetadata>();

	public ArrayList<PersonaMetadata> getPersonaMetadatas() {
		return personaMetadatas;
	}

	public void setPersonaMetadatas(ArrayList<PersonaMetadata> personaMetadatas) {
		this.personaMetadatas = personaMetadatas;
	}

	 

}
