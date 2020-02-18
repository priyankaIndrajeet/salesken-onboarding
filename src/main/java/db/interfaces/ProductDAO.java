package db.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import pojos.Currency;
import pojos.Persona;
import pojos.Product;
import pojos.ProductAsset;
import pojos.ProductData;
import pojos.ProductMetadata;
import pojos.User;

public interface ProductDAO {
	public ArrayList<Product> viewProducts(User user) throws SQLException;

 
	public Product findById(Integer id) throws SQLException;

	public Product updateProduct(Product product) throws SQLException;

	public ArrayList<ProductMetadata> productMetadata(Integer productID) throws SQLException;

	public ProductMetadata createProductMetadata(ProductMetadata productMetadata) throws SQLException;

	public Boolean productDeletion(Integer productID) throws SQLException;

	public Boolean deleteProductAsset(Integer assetId) throws SQLException;

	public ProductAsset createProductAsset(ProductAsset productAsset) throws SQLException;

	public ProductData createProductData(ProductData productData) throws SQLException;

	public ProductMetadata updateProductMetadata(ProductMetadata productMetadata) throws SQLException;

	public ProductData updateProductData(ProductData productData) throws SQLException;

	public Boolean deleteProductData(Integer specificationId) throws SQLException;

	public Boolean deleteProductMetaData(Integer productMetaDataId) throws SQLException;

	public String getProductName(int productId) throws SQLException;

	public void createProductMapping(Product product) throws SQLException;

	public Product createProduct(Product product, Integer managerID) throws SQLException;

	public boolean isValidProductName(String name, Integer Org_id, Integer productId) throws SQLException;


	public ArrayList<Currency> viewCurrency() throws SQLException;

}
