package pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductFeature {
	private Integer id;
	private Integer typeId;
	private Integer personaDataId;// This is for only creation
	private String value;
	private Boolean isSelected=false;

	public ProductFeature() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getPersonaDataId() {
		return personaDataId;
	}

	public void setPersonaDataId(Integer personaDataId) {
		this.personaDataId = personaDataId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	

}
