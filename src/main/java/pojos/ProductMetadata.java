package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductMetadata {
	private Integer id;
	private String key;
	private Integer productId;
	  private ArrayList<ProductData> productDatas;

	public ProductMetadata() {
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

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public ArrayList<ProductData> getProductDatas() {
		return productDatas;
	}

	public void setProductDatas(ArrayList<ProductData> productDatas) {
		this.productDatas = productDatas;
	}
	

}
