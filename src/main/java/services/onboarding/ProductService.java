package services.onboarding;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import pojos.Product;
import pojos.ProductAsset;
import pojos.ProductData;
import pojos.ProductMetadata;

public interface ProductService {
	public Response view();

	public Response createProduct(Product product);

	public Response updateProduct(Product product);

	public Response productSpecification(Integer productID);

	public Response createProductMetadata(ProductMetadata productMetadata);

	public Response deleteProductData(Integer specificationId);

 
	public Response deleteProduct(Integer productID);

	public Response view(Integer productId);

	public Response deleteProductAsset(Integer productAssetId);

	public Response addProductAsset(ProductAsset productAsset);

	public Response uploadProductImage(InputStream uploadedInputStream, FormDataContentDisposition fileDetail);

	public Response createProductData(ProductData productData);

	public Response updateMetadata(ProductMetadata productMetadata);

	public Response updateProductData(ProductData productData);

	public Response deleteProductMetadata(Integer productMetaDataId);

	public Response createMapping(Product product);

	public Response currency();
 
}
