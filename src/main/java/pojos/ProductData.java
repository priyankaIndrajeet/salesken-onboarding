package pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class ProductData {
	private Integer id;
	private String value;
	private Integer productId;
	private Integer metadataId;

	public ProductData() {
		super();
		// TODO Auto-generated constructor stub
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(Integer metadataId) {
		this.metadataId = metadataId;
	}

}
