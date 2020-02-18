/**
 * 
 */
package pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Vaibhav Verma
 *
 */
@JsonInclude(Include.NON_NULL)

public class ProductAsset {
	private Integer id;
	private String asset_type;
	private String asset_url;
	private String asset_name;
	private String asset_value;
	private Integer productId;

	public ProductAsset() {
		super();
	}

	public ProductAsset(String asset_type, String asset_url, String asset_name, String asset_value,
		Integer id) {
	super();
	this.asset_type = asset_type;
	this.asset_url = asset_url;
	this.asset_name = asset_name;
	this.asset_value = asset_value;
	this.id = id;
}

	public String getAsset_type() {
		return asset_type;
	}

	public void setAsset_type(String asset_type) {
		this.asset_type = asset_type;
	}

	public String getAsset_url() {
		return asset_url;
	}

	public void setAsset_url(String asset_url) {
		this.asset_url = asset_url;
	}

	public String getAsset_name() {
		return asset_name;
	}

	public void setAsset_name(String asset_name) {
		this.asset_name = asset_name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}


	public String getAsset_value() {
		return asset_value;
	}


	public void setAsset_value(String asset_value) {
		this.asset_value = asset_value;
	}

}
