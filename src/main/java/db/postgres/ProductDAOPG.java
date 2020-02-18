package db.postgres;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import db.DBUtils;
import db.interfaces.ProductDAO;
import pojos.Currency;
import pojos.Persona;
import pojos.Product;
import pojos.ProductAsset;
import pojos.ProductData;
import pojos.ProductMetadata;
import pojos.Team;
import pojos.User;
import strings.StringUtils;

public class ProductDAOPG implements ProductDAO {
	@Override
	public Product findById(Integer id) throws SQLException {
		Product product = null;
		String sql = "SELECT product. ID AS pid, product. NAME AS pname, product.description ,product.currency_id,currency.name as currency_name, product.image , product.price , product_asset.asset_type,"
				+ " product_asset.asset_name, product_asset.asset_url, product_asset.asset_value,product_asset. ID AS asset_id FROM product "
				+ " LEFT JOIN currency on product.currency_id=currency.id LEFT JOIN product_asset ON product_asset.product_id = product. ID AND product_asset.is_active = TRUE "
				+ "WHERE product.id=" + id + " AND deleted = FALSE";

		ArrayList<HashMap<String, String>> table = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);

		for (HashMap<String, String> row : table) {
			if (product == null) {
				product = new Product(Integer.parseInt(row.get("pid")), row.get("pname"));
				if (row.get("description") != null) {
					product.setDescription(row.get("description"));
				}
				if (row.get("currency_id") != null) {
					product.setCurrencyId(Integer.parseInt(row.get("currency_id")));
					product.setCurrencyName(row.get("currency_name"));
				}
				if (row.get("image") != null) {
					product.setImage(row.get("image"));
				}
				if (row.get("price") != null) {
					product.setPrice(Float.parseFloat(row.get("price")));
				}
			}
			if (row.get("asset_id") != null) {
				product.getAssets().add(new ProductAsset(row.get("asset_type"), row.get("asset_url"),
						row.get("asset_name"), row.get("asset_value"), Integer.parseInt(row.get("asset_id"))));
			}
		}

		return product;

	}

	@Override
	public ArrayList<Currency> viewCurrency() throws SQLException {
		ArrayList<Currency> currencies = new ArrayList<Currency>();
		ArrayList<HashMap<String, String>> table = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), "SELECT * from currency");

		for (HashMap<String, String> row : table) {
			Currency currency = new Currency();
			currency.setId(Integer.parseInt(row.get("id")));
			currency.setName(row.get("name"));
			currencies.add(currency);
		}

		return currencies;

	}

	@Override
	public ArrayList<Product> viewProducts(User user) throws SQLException {

		HashMap<Integer, Product> initialProductMap = new HashMap<Integer, Product>();
		String sql = "SELECT product. ID AS pid, product. NAME AS pname, product.price,product.currency_id,currency.name as currency_name, product.description, product.image, product_asset.asset_type, product_asset.asset_url,product_asset.asset_name,product_asset.asset_value,"
				+ " "
				+ "String_agg ( DISTINCT pipeline_team.team_id :: TEXT, ',' ) AS team, product_asset.id as asset_id,String_agg ( DISTINCT pipeline_persona.persona_id:: TEXT, ',' ) AS persona "
				+ "FROM product LEFT JOIN currency on product.currency_id=currency.id LEFT JOIN product_asset ON product_asset.product_id = product. ID  AND product_asset.is_active = TRUE LEFT JOIN pipeline_product ON pipeline_product.product_id = product. ID"
				+ " LEFT JOIN pipeline_persona ON pipeline_persona.pipeline_id = pipeline_product.pipeline_id LEFT JOIN pipeline_team ON pipeline_team.pipeline_id = pipeline_product.pipeline_id  "
				+ " WHERE organization_id IN ( SELECT organizationid FROM org_user WHERE userid = " + user.getId()
				+ " ) AND deleted = FALSE"
				+ " GROUP BY product. ID, product_asset. ID, product_asset.asset_type, product_asset.asset_name, product_asset.asset_url, product_asset.asset_value,currency.name "
				+ "";
		ArrayList<HashMap<String, String>> table = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);

		for (HashMap<String, String> row : table) {
			Product product = new Product(Integer.parseInt(row.get("pid")), row.get("pname"));
			if (row.get("currency_id") != null) {
				product.setCurrencyId(Integer.parseInt(row.get("currency_id")));
				product.setCurrencyName(row.get("currency_name"));
			}
			if (row.get("description") != null) {
				product.setDescription(row.get("description"));
			}
			if (row.get("image") != null) {
				product.setImage(row.get("image"));
			}
			if (row.get("price") != null) {
				product.setPrice(Float.parseFloat(row.get("price")));
			}
			initialProductMap.put(Integer.parseInt(row.get("pid")), product);
		}

		for (Integer pid : initialProductMap.keySet()) {
			for (HashMap<String, String> row : table) {
				if (pid == Integer.parseInt(row.get("pid"))) {

					if (row.get("asset_id") != null) {
						initialProductMap.get(pid).getAssets()
								.add(new ProductAsset(row.get("asset_type"), row.get("asset_url"),
										row.get("asset_name"), row.get("asset_value"),
										Integer.parseInt(row.get("asset_id"))));
					}
					if (row.get("persona") != null) {
						ArrayList<Persona> personaList = new ArrayList<Persona>();
						for (String id : row.get("persona").split(",")) {
							String personaName = new PersonaDAOPG().getPersonaName(Integer.parseInt(id));
							if (personaName != null) {
								Persona personaData = new Persona();
								personaData.setId(Integer.parseInt(id));
								personaData.setName(personaName);
								personaList.add(personaData);
							}
						}
						initialProductMap.get(pid).setPersonas(personaList);
					}
					if (row.get("team") != null) {
						ArrayList<Team> teamList = new ArrayList<Team>();
						for (String id : row.get("team").split(",")) {
							String teamName = new TeamDAOPG().getTeamName(Integer.parseInt(id));
							if (teamName != null) {
								Team teamData = new Team();
								teamData.setId(Integer.parseInt(id));
								teamData.setName(teamName);
								teamList.add(teamData);
							}
						}
						initialProductMap.get(pid).setTeams(teamList);
					}
				}
			}
		}

		ArrayList<Product> results = new ArrayList<Product>();
		TreeMap<Integer, Product> sorted = new TreeMap<>(initialProductMap);
		for (Map.Entry<Integer, Product> entry : sorted.entrySet()) {
			results.add(entry.getValue());
		}
		Collections.reverse(results);
		return results;
	}

	@Override
	public Product createProduct(Product product, Integer managerID) throws SQLException {
		String organizationID = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(),
						"SELECT org_user.organizationid FROM org_user WHERE org_user.userid =" + managerID)
				.get(0).get("organizationid");

		String insertProductSql = "INSERT INTO product(name, organization_id,created_at,updated_at,description,image,deleted,price,currency_id) VALUES (?,?,now(),now(),?,?,?,?,?);";

		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(product.getName())));
		data.put(2, Integer.parseInt(organizationID));
		data.put(3, StringUtils.cleanHTML(product.getDescription()));
		data.put(4, product.getImage());
		data.put(5, false);

		if (product.getPrice() != null) {
			data.put(6, product.getPrice());
		} else {
			data.put(6, 0f);
		}
		data.put(7, product.getCurrencyId());
		int productId = DBUtils.getInstance().updateObject(insertProductSql, data);

		if (product.getAssets() != null) {
			for (ProductAsset productAsset : product.getAssets()) {
				productAsset.setProductId(productId);
				createProductAsset(productAsset);
			}
		}
		// create_metadata 5

		String[] keys = new String[] { "Product Features", "Key Value Proposition", "Brand Name", "Competitor",
				"Objection" };

		for (String key : keys) {
			ProductMetadata productMetadata = new ProductMetadata();
			productMetadata.setKey(key);
			productMetadata.setProductId(productId);
			createProductMetadata(productMetadata);
		}

		product = new ProductDAOPG().findById(productId);
		return product;
	}

	@Override
	public Product updateProduct(Product product) throws SQLException {
		String updateProductSql = "UPDATE product SET name=? , updated_at=now(), description=?, image=?, price=?,currency_id=? where id=?";

		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		ProductDAOPG dao = new ProductDAOPG();
		data.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(product.getName())));
		data.put(2, StringUtils.cleanHTML(product.getDescription()));
		data.put(3, product.getImage());
		if (product.getPrice() != null) {
			data.put(4, product.getPrice());
		} else {
			data.put(4, 0f);
		}
		data.put(5, product.getCurrencyId());
		data.put(6, product.getId());
		DBUtils.getInstance().updateObject(updateProductSql, data);
		Product productDel = dao.findById(product.getId());
		System.out.println();
		for (ProductAsset asset : productDel.getAssets()) {
			dao.deleteProductAsset(asset.getId());
		}
		for (ProductAsset asset : product.getAssets()) {
			asset.setProductId(product.getId());
			dao.createProductAsset(asset);
		}
		product = new ProductDAOPG().findById(product.getId());
		return product;
	}

	@Override
	public ArrayList<ProductMetadata> productMetadata(Integer productID) throws SQLException {
		String sql = "SELECT product_metadata. ID, product_metadata. KEY, product_data. ID AS product_data_id, product_data. VALUE , product_data.product_id FROM product_metadata LEFT JOIN product_data ON"
				+ " product_data.metadata_id = product_metadata. ID AND product_data.product_id = " + productID
				+ " WHERE (product_metadata.product_id = " + productID + " ) ORDER BY product_metadata.id";

		ArrayList<HashMap<String, String>> table = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);

		HashMap<Integer, ProductMetadata> dataMap = new HashMap<Integer, ProductMetadata>();

		for (HashMap<String, String> hashMap : table) {
			if (!dataMap.containsKey(Integer.parseInt(hashMap.get("id")))) {
				ProductMetadata productMetadata = new ProductMetadata();
				productMetadata.setProductDatas(new ArrayList<ProductData>());
				productMetadata.setId(Integer.parseInt(hashMap.get("id")));
				productMetadata.setKey(hashMap.get("key"));
				if (hashMap.get("value") != null) {
					ProductData productData = new ProductData();
					productData.setId(Integer.parseInt(hashMap.get("product_data_id")));
					productData.setProductId(Integer.parseInt(hashMap.get("product_id")));
					productData.setValue(hashMap.get("value"));
					productMetadata.getProductDatas().add(productData);
				}
				dataMap.put(Integer.parseInt(hashMap.get("id")), productMetadata);
			} else {
				ProductMetadata productMetadata = dataMap.get(Integer.parseInt(hashMap.get("id")));
				if (hashMap.get("value") != null) {
					ProductData productData = new ProductData();
					productData.setId(Integer.parseInt(hashMap.get("product_data_id")));
					productData.setProductId(Integer.parseInt(hashMap.get("product_id")));
					productData.setValue(hashMap.get("value"));
					productMetadata.getProductDatas().add(productData);
				}
				dataMap.put(Integer.parseInt(hashMap.get("id")), productMetadata);
			}

		}

		ArrayList<ProductMetadata> results = new ArrayList<ProductMetadata>();
		TreeMap<Integer, ProductMetadata> sorted = new TreeMap<>(dataMap);
		for (Map.Entry<Integer, ProductMetadata> entry : sorted.entrySet()) {
			results.add(entry.getValue());
		}

		Collections.sort(results, ProductMetadataComparator);

		return results;
	}

	public static Comparator<ProductMetadata> ProductMetadataComparator = new Comparator<ProductMetadata>() {

		public int compare(ProductMetadata p1, ProductMetadata p2) {

			// ascending order
			return p1.getId().compareTo(p2.getId());

		}
	};

	@Override
	public ProductMetadata createProductMetadata(ProductMetadata productMetadata) throws SQLException {
		String sqlProductMetaData = "INSERT INTO product_metadata( key ,  product_id ,  created_at ,  updated_at ) VALUES (?, ?,now(),now());";
		HashMap<Integer, Object> dataProductMetadata = new HashMap<Integer, Object>();
		dataProductMetadata.put(1, StringUtils.wordsCapitalize(StringUtils.cleanHTML(productMetadata.getKey().trim())));
		dataProductMetadata.put(2, productMetadata.getProductId());
		Integer productSpecificationID = DBUtils.getInstance().updateObject(sqlProductMetaData, dataProductMetadata);
		String sqlProductMetadata = "SELECT * from product_metadata WHERE id=" + productSpecificationID;
		productMetadata = null;
		for (HashMap<String, String> hashMap : DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlProductMetadata)) {
			productMetadata = new ProductMetadata();
			productMetadata.setId(Integer.parseInt(hashMap.get("id")));
			productMetadata.setKey(hashMap.get("key"));
			productMetadata.setProductId(Integer.parseInt(hashMap.get("product_id")));
		}
		return productMetadata;
	}

	@Override
	public ProductData createProductData(ProductData productData) throws SQLException {
		String sqlProductData = "INSERT INTO product_data  ( product_id ,  value ,  created_at ,  updated_at ,  metadata_id ) VALUES (?, ?, now(), now(), ?);";
		HashMap<Integer, Object> dataProductData = new HashMap<Integer, Object>();
		dataProductData.put(1, productData.getProductId());
		dataProductData.put(2, StringUtils.stringCapitalize(StringUtils.cleanHTML(productData.getValue()).trim()));
		dataProductData.put(3, productData.getMetadataId());
		Integer productSpecificationID = DBUtils.getInstance().updateObject(sqlProductData, dataProductData);
		String sqlProductMetadata = "SELECT * from product_data WHERE id=" + productSpecificationID;
		productData = null;
		for (HashMap<String, String> hashMap : DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlProductMetadata)) {
			productData = new ProductData();
			productData.setId(Integer.parseInt(hashMap.get("id")));
			productData.setValue(hashMap.get("value"));
			productData.setProductId(Integer.parseInt(hashMap.get("product_id")));
			productData.setMetadataId(Integer.parseInt(hashMap.get("metadata_id")));
		}
		return productData;
	}

	@Override
	public Boolean productDeletion(Integer productID) throws SQLException {
		String productSql = "update product set deleted = true where id = ?";
		HashMap<Integer, Object> ProducData = new HashMap<Integer, Object>();
		ProducData.put(1, productID);
		DBUtils.getInstance().updateObject(productSql, ProducData);

		return true;
	}

	@Override
	public Boolean deleteProductData(Integer productDataId) throws SQLException {
		String deleteSql = "DELETE from product_data WHERE id=? ;";
		HashMap<Integer, Object> productSpecsData = new HashMap<Integer, Object>();
		productSpecsData.put(1, productDataId);
		DBUtils.getInstance().updateObject(deleteSql, productSpecsData);
		return true;

	}

	@Override
	public Boolean deleteProductMetaData(Integer productMetaDataId) throws SQLException {
		String sqlProductMetadata = "SELECT * from product_metadata WHERE product_id is not null and id="
				+ productMetaDataId;
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlProductMetadata);

		if (result.size() > 0) {
			String deleteSql = "DELETE from product_data WHERE metadata_id=? ;";
			HashMap<Integer, Object> productSpecsData = new HashMap<Integer, Object>();
			productSpecsData.put(1, productMetaDataId);
			DBUtils.getInstance().updateObject(deleteSql, productSpecsData);

			String deleteMetaDataSql = "DELETE from product_metadata WHERE id=? ;";
			HashMap<Integer, Object> productData = new HashMap<Integer, Object>();
			productData.put(1, productMetaDataId);
			DBUtils.getInstance().updateObject(deleteMetaDataSql, productData);

		}

		return true;

	}

	@Override
	public ProductMetadata updateProductMetadata(ProductMetadata productMetadata) throws SQLException {
		String sql = "update product_metadata set key=?,updated_at = now() where id = ?";
		HashMap<Integer, Object> productSpecData = new HashMap<Integer, Object>();
		productSpecData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(productMetadata.getKey())));
		productSpecData.put(2, productMetadata.getId());
		DBUtils.getInstance().updateObject(sql, productSpecData);

		sql = "select * from product_metadata where id =" + productMetadata.getId();

		productMetadata = new ProductMetadata();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			productMetadata.setId(Integer.parseInt(row.get("id")));
			productMetadata.setKey(row.get("key"));
			productMetadata.setProductId(Integer.parseInt(row.get("product_id")));
		}
		return productMetadata;
	}

	@Override
	public ProductData updateProductData(ProductData productData) throws SQLException {
		String sql = "update product_data set value=?,updated_at = now() where id = ?";
		HashMap<Integer, Object> productSpecData = new HashMap<Integer, Object>();
		productSpecData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(productData.getValue())));
		productSpecData.put(2, productData.getId());
		DBUtils.getInstance().updateObject(sql, productSpecData);

		sql = "select * from product_data where id =" + productData.getId();

		productData = new ProductData();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			productData.setId(Integer.parseInt(row.get("id")));
			productData.setValue(row.get("value"));
			productData.setProductId(Integer.parseInt(row.get("product_id")));
		}
		return productData;
	}

	@Override
	public Boolean deleteProductAsset(Integer assetId) throws SQLException {
		String updateProductAsset = "UPDATE product_asset SET is_active = ? WHERE ID = ?;";
		HashMap<Integer, Object> productAssetData = new HashMap<Integer, Object>();
		productAssetData.put(1, false);
		productAssetData.put(2, assetId);
		DBUtils.getInstance().updateObject(updateProductAsset, productAssetData);
		return true;

	}

	@Override
	public ProductAsset createProductAsset(ProductAsset productAsset) throws SQLException {

		HashMap<Integer, Object> dataAsset = new HashMap<Integer, Object>();
		String insertProductAssetSql = "INSERT INTO product_asset(asset_type, asset_url,asset_name,created_at,updated_at,product_id,is_active) VALUES (?,?,?,now(),now(),?,?)";

		dataAsset.put(1, productAsset.getAsset_type());
		dataAsset.put(2, productAsset.getAsset_url());
		dataAsset.put(3, productAsset.getAsset_name());
		dataAsset.put(4, productAsset.getProductId());
		dataAsset.put(5, true);
		Integer productAssetID = DBUtils.getInstance().updateObject(insertProductAssetSql, dataAsset);
		getFileSize(productAssetID, productAsset.getAsset_url());
		productAsset = null;

		String sql = "SELECT * from product_asset WHERE id=" + productAssetID;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			productAsset = new ProductAsset();
			productAsset.setId(Integer.parseInt(row.get("id")));
			productAsset.setAsset_name(row.get("asset_name"));
			productAsset.setAsset_type(row.get("asset_type"));
			productAsset.setAsset_url(row.get("asset_url"));
			productAsset.setAsset_value(row.get("asset_value"));
			productAsset.setProductId(Integer.parseInt(row.get("product_id")));
		}

		return productAsset;

	}

	private static void getFileSize(Integer assetId, String asset_url) {

		Runnable runnable = new Runnable() {

			@Override

			public void run() {
				// TODO Auto-generated method stub
				URL url = null;
				try {
					url = new URL(asset_url);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				URLConnection conn = null;
				try {
					conn = url.openConnection();
					if (conn instanceof HttpURLConnection) {
						((HttpURLConnection) conn).setRequestMethod("HEAD");
					}
					conn.getInputStream();
					String sql = "update product_asset set asset_value = ? where id = ?";
					HashMap<Integer, Object> data = new HashMap<Integer, Object>();
					data.put(1, conn.getContentLength());
					data.put(2, assetId);
					// System.out.println("Testing---------------------");
					DBUtils.getInstance().updateObject(sql, data);

				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (conn instanceof HttpURLConnection) {
						((HttpURLConnection) conn).disconnect();
					}
				}
			}
		};

		Thread t = new Thread(runnable);
		t.start();
	}

	@Override
	public String getProductName(int productId) throws SQLException {
		String sql = "select name from product where id = " + productId + " and deleted = false";
		String name = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {

			name = row.get("name");

		}
		return name;
	}

	@Override
	public void createProductMapping(Product product) throws SQLException {
		HashMap<Integer, Object> processMap = new HashMap<Integer, Object>();
		if (product.getProcessIds() != null) {
			for (Integer id : product.getProcessIds()) {
				String sql = "select from pipeline_product where pipeline_id = " + id + " and product_id = "
						+ product.getId() + "";
				ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
						.executeQuery(Thread.currentThread().getStackTrace(), sql);
				if (result.size() == 0) {
					String productSql = "Insert into pipeline_product (pipeline_id, product_id) values(?,?)";
					processMap = new HashMap<Integer, Object>();
					processMap.put(1, id);
					processMap.put(2, product.getId());
					DBUtils.getInstance().updateObject(productSql, processMap);
				}
			}
		}
	}

	@Override
	public boolean isValidProductName(String name, Integer Org_id, Integer productId) throws SQLException {
		String sql = "SELECT product.name as name,id FROM product WHERE organization_id = " + Org_id
				+ " and deleted = FALSE";
		ArrayList<HashMap<String, String>> productList = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		for (HashMap<String, String> row : productList) {
			if (row.get("name").trim().equalsIgnoreCase(name.trim())
					&& (productId == null || Integer.parseInt(row.get("id")) != productId)) {
				return false;
			}
		}
		return true;
	}
}
