package db.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import db.DBUtils;
import db.interfaces.PersonaDAO;
import pojos.Organization;
import pojos.Persona;
import pojos.PersonaData;
import pojos.PersonaFields;
import pojos.PersonaMetadata;
import pojos.Product;
import pojos.ProductFeature;
import pojos.Team;
import pojos.User;
import strings.StringUtils;

public class PersonaDAOPG implements PersonaDAO {

	@Override
	public Persona findById(Integer personaId) throws SQLException {
		Persona persona = null;
		String personasql = "select id,name,image,organization_id from persona where id =" + personaId
				+ " and is_deleted=FALSE";

		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				personasql)) {
			persona = new Persona();
			persona.setId(Integer.parseInt(row.get("id")));
			persona.setName(row.get("name"));
			persona.setImageUrl(row.get("image"));
			persona.setOrganizationId(Integer.parseInt(row.get("organization_id")));

			HashMap<Integer, PersonaMetadata> personaMetaDataMapping = new HashMap<Integer, PersonaMetadata>();
			String sql = "SELECT persona_metadata.id,persona_metadata.key,persona_metadata.relation_type_id,persona_data.id as persona_data_id,persona_data.value,"
					+ "persona_data.persona_id FROM persona_metadata LEFT JOIN persona_data ON persona_data.metadata_id = persona_metadata. ID AND "
					+ "persona_data.persona_id = " + personaId + " WHERE (  persona_metadata.persona_id = " + personaId
					+ " ) ORDER BY " + "persona_data. ID asc";
			for (HashMap<String, String> rowdata : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql)) {
				if (!personaMetaDataMapping.containsKey(Integer.parseInt(rowdata.get("id")))) {
					PersonaMetadata personaMetadata = new PersonaMetadata();
					personaMetadata.setPersonaDatas(new ArrayList<PersonaData>());
					personaMetadata.setId(Integer.parseInt(rowdata.get("id")));
					personaMetadata.setKey(rowdata.get("key"));
					if (rowdata.get("relation_type_id") != null) {
						personaMetadata.setRelationTypeId(Integer.parseInt(rowdata.get("relation_type_id")));
					}

					if (rowdata.get("persona_data_id") != null) {
						PersonaData persondata = new PersonaData();
						persondata.setId(Integer.parseInt(rowdata.get("persona_data_id")));
						persondata.setPersonaId(Integer.parseInt(rowdata.get("persona_id")));
						persondata.setValue(rowdata.get("value"));
						personaMetadata.getPersonaDatas().add(persondata);
					}
					personaMetaDataMapping.put(Integer.parseInt(rowdata.get("id")), personaMetadata);
				} else {

					PersonaMetadata personaMetadata = personaMetaDataMapping.get(Integer.parseInt(rowdata.get("id")));
					if (rowdata.get("persona_data_id") != null) {
						PersonaData persondata = new PersonaData();
						if (rowdata.get("relation_type_id") != null) {
							/*-persondata.setPersonaProductFeatures(
									getProductFeatureMapping(rowdata, Integer.parseInt(row.get("organization_id"))));*/
							personaMetadata.setRelationTypeId(Integer.parseInt(rowdata.get("relation_type_id")));
						}
						persondata.setId(Integer.parseInt(rowdata.get("persona_data_id")));
						persondata.setPersonaId(Integer.parseInt(rowdata.get("persona_id")));
						persondata.setValue(rowdata.get("value"));
						personaMetadata.getPersonaDatas().add(persondata);
					}
					personaMetaDataMapping.put(Integer.parseInt(rowdata.get("id")), personaMetadata);
				}
			}
			ArrayList<PersonaMetadata> results = new ArrayList<PersonaMetadata>();
			TreeMap<Integer, PersonaMetadata> sorted = new TreeMap<>(personaMetaDataMapping);
			for (Map.Entry<Integer, PersonaMetadata> entry : sorted.entrySet()) {
				results.add(entry.getValue());
			}

			Collections.sort(results, PersonaMetadataComparator);

			persona.setPersonaMetadatas(results);
		}

		return persona;
	}

	@Override
	public Persona createPersona(Persona persona, Integer userId) throws SQLException {
		String organizationID = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(),
						"SELECT org_user.organizationid FROM org_user WHERE org_user.userid =" + userId)
				.get(0).get("organizationid");

		String insertPersonaSql = "INSERT INTO  persona (name, image, created_at, updated_at, is_deleted, organization_id) VALUES (?,?,now(),now(),?,?);";
		HashMap<Integer, Object> personaData = new HashMap<Integer, Object>();
		personaData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(persona.getName())));
		personaData.put(2, persona.getImageUrl());
		personaData.put(3, false);
		personaData.put(4, Integer.parseInt(organizationID));
		int personaId = DBUtils.getInstance().updateObject(insertPersonaSql, personaData);

		String sqlDefaultCreate = "SELECT * from persona_metadata WHERE persona_id is null ORDER BY key";
		for (HashMap<String, String> rowdata : DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlDefaultCreate)) {
			PersonaMetadata personaMetadata = new PersonaMetadata();
			personaMetadata.setKey(StringUtils.wordsCapitalize(rowdata.get("key")));
			personaMetadata.setPersonaId(personaId);
			if (rowdata.get("relation_type_id") != null) {
				personaMetadata.setRelationTypeId(Integer.parseInt(rowdata.get("relation_type_id")));
			}
			createPersonaMetaData(personaMetadata);
		}

		persona = new PersonaDAOPG().findById(personaId);
		return persona;
	}

	@Override
	public Persona updatePersona(Persona persona, Integer userId) throws SQLException {
		String sql = "update persona set name=? , image=?, updated_at = now() where id =?";
		HashMap<Integer, Object> updatedata = new HashMap<Integer, Object>();
		updatedata.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(persona.getName())));
		updatedata.put(2, persona.getImageUrl());
		updatedata.put(3, persona.getId());
		DBUtils.getInstance().updateObject(sql, updatedata);

		return persona;
	}

	@Override
	public Boolean deletePersona(Integer personaId) throws SQLException {
		String deletePersonaSql = "UPDATE persona SET is_deleted = 't' WHERE persona.id = ?";
		HashMap<Integer, Object> persona = new HashMap<Integer, Object>();
		persona.put(1, personaId);
		DBUtils.getInstance().updateObject(deletePersonaSql, persona);
		return true;
	}

	@Override
	public PersonaData createPersonaData(PersonaData personaData) throws SQLException {
		Integer personaMetaDataId = personaData.getMetadataId();
		if (personaMetaDataId < 0) {
			personaMetaDataId = (-1) * personaMetaDataId;
		}
		String personaDataSql = "INSERT INTO persona_data  (persona_id ,  value ,  created_at ,  updated_at ,  metadata_id ) VALUES (?, ?, now(),now(), ?);";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, personaData.getPersonaId());
		data.put(2, StringUtils.cleanHTML(personaData.getValue()));
		data.put(3, personaMetaDataId);

		Integer personaDataId = DBUtils.getInstance().updateObject(personaDataSql, data);
		personaData.setId(personaDataId);
		return personaData;
	}

	@Override
	public PersonaMetadata createPersonaMetaData(PersonaMetadata personaMetadata) throws SQLException {
		String personaDataSql = "INSERT INTO persona_metadata  (key ,  persona_id ,  created_at ,  updated_at ,  relation_type_id ) VALUES (?, ?, now(), now(), ?);";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();

		data.put(1, StringUtils.wordsCapitalize(StringUtils.cleanHTML(personaMetadata.getKey())));
		data.put(2, personaMetadata.getPersonaId());
		data.put(3, personaMetadata.getRelationTypeId());

		Integer personaDataId = DBUtils.getInstance().updateObject(personaDataSql, data);
		personaMetadata.setId(personaDataId);
		return personaMetadata;
	}

	@Override
	public PersonaMetadata updatePersonaMetaData(PersonaMetadata personaMetadata) throws SQLException {
		String updatePersonaMetaDataSql = "UPDATE persona_metadata  SET  key =?, updated_at =now() WHERE id =?;";
		HashMap<Integer, Object> personaUpdate = new HashMap<Integer, Object>();
		personaUpdate.put(1, StringUtils.wordsCapitalize(StringUtils.cleanHTML(personaMetadata.getKey())));
		personaUpdate.put(2, personaMetadata.getId());
		DBUtils.getInstance().updateObject(updatePersonaMetaDataSql, personaUpdate);

		return personaMetadata;
	}

	@Override
	public PersonaData updatePersonaData(PersonaData personaData) throws SQLException {
		String updatePersonaDataSql = "UPDATE persona_data SET value = ? ,updated_at = now() WHERE id = ?";
		HashMap<Integer, Object> personaUpdate = new HashMap<Integer, Object>();
		personaUpdate.put(1, StringUtils.cleanHTML(personaData.getValue()));
		personaUpdate.put(2, personaData.getId());
		DBUtils.getInstance().updateObject(updatePersonaDataSql, personaUpdate);

		return personaData;
	}

	@Override
	public Boolean deletePersonaData(Integer personadataid) throws SQLException {

		String deletePersonaDataSql = "DELETE FROM persona_data WHERE id= ?";
		HashMap<Integer, Object> deletepersonaData = new HashMap<Integer, Object>();
		deletepersonaData.put(1, personadataid);
		DBUtils.getInstance().updateObject(deletePersonaDataSql, deletepersonaData);
		return true;
	}

	@Override
	public Boolean deletePersonaMetaData(Integer personaMeatadataid) throws SQLException {

		String sql = "SELECT * from persona_metadata WHERE id=" + personaMeatadataid + " and persona_id is not null";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (result.size() > 0) {
			String deletePersonaDataSql = "DELETE FROM persona_data WHERE metadata_id= ?";
			HashMap<Integer, Object> deletepersonaData = new HashMap<Integer, Object>();
			deletepersonaData.put(1, personaMeatadataid);
			DBUtils.getInstance().updateObject(deletePersonaDataSql, deletepersonaData);

			String deletePersonaMetaDataSql = "DELETE FROM persona_metadata WHERE id= ?";
			HashMap<Integer, Object> deletepersonaMetaData = new HashMap<Integer, Object>();
			deletepersonaMetaData.put(1, personaMeatadataid);
			DBUtils.getInstance().updateObject(deletePersonaMetaDataSql, deletepersonaMetaData);
		}

		return true;
	}

	@Override
	public ArrayList<Persona> viewPersonaByUserId(Integer userId) throws SQLException {
		Organization organization = new OrganizationDAOPG().findOrganizationByUserId(userId);
		Integer org_id = organization.getId();

		ArrayList<Persona> personaData = new ArrayList<Persona>();
		String sql = "select * from persona where organization_id =" + org_id + " and is_deleted=FALSE";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			Integer personaId = Integer.parseInt(row.get("id"));
			Persona persona = new Persona();
			persona.setId(Integer.parseInt(row.get("id")));
			persona.setName(row.get("name"));
			persona.setImageUrl(row.get("image"));
			persona.setOrganizationId(org_id);
			persona = getPersonaMappingData(persona);
			HashMap<Integer, PersonaMetadata> personaMetaDataMapping = new HashMap<Integer, PersonaMetadata>();
			String sqlPersonaMetaData = "SELECT persona_metadata.id,persona_metadata.key,persona_metadata.relation_type_id,persona_data.id as persona_data_id,persona_data.value,"
					+ "persona_data.persona_id FROM persona_metadata LEFT JOIN persona_data ON persona_data.metadata_id = persona_metadata. ID AND "
					+ "persona_data.persona_id = " + personaId + " WHERE (  persona_metadata.persona_id = " + personaId
					+ " ) ORDER BY " + "persona_data. ID ";
			for (HashMap<String, String> rowdata : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sqlPersonaMetaData)) {
				if (!personaMetaDataMapping.containsKey(Integer.parseInt(rowdata.get("id")))) {
					PersonaMetadata personaMetadata = new PersonaMetadata();
					personaMetadata.setPersonaDatas(new ArrayList<PersonaData>());
					personaMetadata.setId(Integer.parseInt(rowdata.get("id")));
					personaMetadata.setKey(rowdata.get("key"));

					if (rowdata.get("persona_data_id") != null) {

						PersonaData persondata = new PersonaData();
						if (rowdata.get("relation_type_id") != null) {
							persondata.setPersonaProductFeatures(null);
							personaMetadata.setRelationTypeId(Integer.parseInt(rowdata.get("relation_type_id")));
						}

						persondata.setId(Integer.parseInt(rowdata.get("persona_data_id")));
						persondata.setPersonaId(Integer.parseInt(rowdata.get("persona_id")));
						persondata.setValue(rowdata.get("value"));
						personaMetadata.getPersonaDatas().add(persondata);
					}
					personaMetaDataMapping.put(Integer.parseInt(rowdata.get("id")), personaMetadata);
				} else {
					PersonaMetadata personaMetadata = personaMetaDataMapping.get(Integer.parseInt(rowdata.get("id")));
					if (rowdata.get("persona_data_id") != null) {
						PersonaData persondata = new PersonaData();
						if (rowdata.get("relation_type_id") != null) {
							personaMetadata.setRelationTypeId(Integer.parseInt(rowdata.get("relation_type_id")));

							// persondata.setPersonaProductFeatures(getProductFeatureMapping(rowdata,
							// org_id));
						}
						persondata.setId(Integer.parseInt(rowdata.get("persona_data_id")));
						persondata.setPersonaId(Integer.parseInt(rowdata.get("persona_id")));
						persondata.setValue(rowdata.get("value"));
						personaMetadata.getPersonaDatas().add(persondata);
					}
					personaMetaDataMapping.put(Integer.parseInt(rowdata.get("id")), personaMetadata);
				}
			}

			ArrayList<PersonaMetadata> results = new ArrayList<PersonaMetadata>();
			TreeMap<Integer, PersonaMetadata> sorted = new TreeMap<>(personaMetaDataMapping);
			for (Map.Entry<Integer, PersonaMetadata> entry : sorted.entrySet()) {
				results.add(entry.getValue());
			}

			Collections.sort(results, PersonaMetadataComparator);

			persona.setPersonaMetadatas(results);
			personaData.add(persona);

		}

		return personaData;
	}

	public static Comparator<PersonaMetadata> PersonaMetadataComparator = new Comparator<PersonaMetadata>() {

		public int compare(PersonaMetadata p1, PersonaMetadata p2) {
			// ascending order
			return p1.getId().compareTo(p2.getId());

		}
	};

	@Override
	public ArrayList<Product> personaDataProductFeature(Integer personadataId, Integer userId) throws SQLException {
		String sqlPersonaData = "SELECT persona_metadata.*, persona_data. ID AS persona_data_id, persona_data. VALUE  FROM persona_metadata, persona_data WHERE persona_metadata. ID = persona_data.metadata_id AND persona_data. ID = "
				+ personadataId;

		for (HashMap<String, String> rowdata : DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlPersonaData)) {

			if (rowdata.get("relation_type_id") != null && Integer.parseInt(rowdata.get("relation_type_id")) == 1) {

				HashMap<Integer, Product> productMap = new HashMap<Integer, Product>();
				String sqlProductFeature = "SELECT product. ID AS product_id, product. NAME AS product_name, product_metadata.id as"
						+ " product_metadata_id, product_data. ID AS product_data_id, product_data. VALUE  FROM product LEFT "
						+ "JOIN product_metadata ON product_metadata. KEY = 'Product Features' and"
						+ " product_metadata.product_id=product.id LEFT JOIN product_data ON product_data.product_id = product. ID "
						+ "and product_metadata.id=product_data.metadata_id WHERE product.deleted = FALSE AND product.organization_id"
						+ "  IN (SELECT organizationid from org_user WHERE userid=" + userId
						+ ") ORDER BY product_id;;";

				for (HashMap<String, String> row1 : DBUtils.getInstance()
						.executeQuery(Thread.currentThread().getStackTrace(), sqlProductFeature)) {
					if (productMap.containsKey(Integer.parseInt(row1.get("product_id")))) {
						Product product = productMap.get(Integer.parseInt(row1.get("product_id")));
						ProductFeature productFeature = new ProductFeature();
						productFeature.setId(Integer.parseInt(row1.get("product_data_id")));
						productFeature.setTypeId(Integer.parseInt(rowdata.get("relation_type_id")));
						productFeature.setValue(row1.get("value"));
						product.getProductFeatures().add(productFeature);
						productMap.put(Integer.parseInt(row1.get("product_id")), product);
					} else {
						Product product = new Product();
						product.setAssets(null);
						product.setId(Integer.parseInt(row1.get("product_id")));
						product.setName(row1.get("product_name"));

						ArrayList<ProductFeature> productFeatures = new ArrayList<ProductFeature>();
						ProductFeature productFeature = new ProductFeature();
						if (row1.get("product_data_id") != null) {
							productFeature.setId(Integer.parseInt(row1.get("product_data_id")));
							productFeature.setTypeId(Integer.parseInt(rowdata.get("relation_type_id")));
							productFeature.setValue(row1.get("value"));
							productFeatures.add(productFeature);
						}
						product.setProductFeatures(productFeatures);
						productMap.put(Integer.parseInt(row1.get("product_id")), product);
					}
				}
				ArrayList<Product> products = new ArrayList<Product>(productMap.values());

				if (rowdata.get("persona_data_id") != null) {
					String sqlProdutDataFeatureMapping = "SELECT * from product_data WHERE id in (SELECT CORRESPONDING_id from persona_relation_data"
							+ " WHERE persona_data_id=" + rowdata.get("persona_data_id") + " and type_id="
							+ rowdata.get("relation_type_id") + ")";
					for (HashMap<String, String> productFeatureRow : DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), sqlProdutDataFeatureMapping)) {
						for (Product product : products) {
							if (product.getProductFeatures() != null) {
								for (ProductFeature productFeature : product.getProductFeatures()) {
									if (productFeature.getId() == Integer.parseInt(productFeatureRow.get("id"))) {
										productFeature.setIsSelected(true);
									}
								}
							}
						}

					}
				}
				return products;
			}
		}
		return null;

	}

	@Override
	public Persona getPersonaMappingData(Persona persona) throws NumberFormatException, SQLException {
		Integer personaId = persona.getId();
		String sql = "SELECT persona.*, String_agg(DISTINCT pipeline_product.product_id :: text, ',')as product, String_agg(DISTINCT pipeline_team.team_id :: text, ',') as team FROM persona "
				+ "LEFT JOIN pipeline_persona ON pipeline_persona.persona_id = persona.id LEFT JOIN pipeline_product ON pipeline_product.pipeline_id = pipeline_persona.pipeline_id "
				+ "LEFT JOIN pipeline_team ON pipeline_team.pipeline_id = pipeline_persona.pipeline_id WHERE persona. ID = "
				+ personaId + " GROUP BY persona.id";

		ArrayList<Product> productList = new ArrayList<Product>();
		ArrayList<Team> teamList = new ArrayList<Team>();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (row.get("product") != null) {

				for (String productid : row.get("product").split(",")) {

					String productName = new ProductDAOPG().getProductName(Integer.parseInt(productid));
					if (productName != null) {
						Product productData = new Product();
						productData.setId(Integer.parseInt(productid));
						productData.setName(productName);
						productData.setAssets(null);
						productList.add(productData);
					}
				}
			}
			if (row.get("team") != null) {

				for (String teamid : row.get("team").split(",")) {
					String teamName = new TeamDAOPG().getTeamName(Integer.parseInt(teamid));
					if (teamName != null) {
						Team teamData = new Team();
						teamData.setId(Integer.parseInt(teamid));
						teamData.setName(teamName);
						teamData.setUserIds(null);
						teamData.setUsers(null);
						teamData.setProducts(null);
						teamData.setProcessIds(null);
						teamData.setPersonas(null);
						teamList.add(teamData);
					}
				}
			}
		}
		persona.setProducts(productList);
		persona.setTeams(teamList);

		return persona;
	}

	@Override
	public PersonaFields getPersonaCreationFields(User user) throws SQLException {

		PersonaFields personaFields = new PersonaFields();
		String sqlPersonaMetadata = "SELECT * FROM persona_metadata WHERE persona_id IS NULL ORDER BY key";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sqlPersonaMetadata)) {
			PersonaMetadata personaMetadata = new PersonaMetadata();
			personaMetadata.setId(Integer.parseInt(row.get("id")));
			personaMetadata.setKey(row.get("key"));
			personaMetadata.setPersonaDatas(null);

			if (row.get("relation_type_id") != null) {

				personaMetadata.setRelationTypeId(Integer.parseInt(row.get("relation_type_id")));

			}

			personaFields.getPersonaMetadatas().add(personaMetadata);
		}
		return personaFields;
	}

	@Override
	public Boolean createPersonaMapping(Persona persona) throws SQLException {

		HashMap<Integer, Object> processMap = new HashMap<Integer, Object>();

		if (persona.getProcessIds() != null) {
			for (Integer id : persona.getProcessIds()) {
				String sql = "select * from pipeline_persona where pipeline_id = " + id + " and persona_id = "
						+ persona.getId() + "";
				ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
						.executeQuery(Thread.currentThread().getStackTrace(), sql);
				if (result.size() == 0) {
					String personaSql = "Insert into pipeline_persona (pipeline_id, persona_id) values(?,?)";
					processMap = new HashMap<Integer, Object>();
					processMap.put(1, id);
					processMap.put(2, persona.getId());
					DBUtils.getInstance().updateObject(personaSql, processMap);
				}
			}
		}
		return true;
	}

	@Override
	public String getPersonaName(int personaId) throws SQLException {
		String sql = "select name from persona where id = " + personaId + " and is_deleted = false";
		String name = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			name = row.get("name");
		}
		return name;
	}

	@Override
	public Boolean addProductFeatureInNeedMapping(ArrayList<ProductFeature> productFeatures) throws SQLException {
		for (ProductFeature productFeature : productFeatures) {
			String sql = "DELETE from persona_relation_data WHERE type_id=? and persona_data_id= ?";
			HashMap<Integer, Object> data = new HashMap<Integer, Object>();
			data.put(1, productFeature.getTypeId());
			data.put(2, productFeature.getPersonaDataId());
			DBUtils.getInstance().updateObject(sql, data);
		}
		for (ProductFeature productFeature : productFeatures) {

			String updatePersonaDataSql = "INSERT INTO  persona_relation_data  ( type_id ,  persona_data_id ,  corresponding_id ) VALUES (?, ?, ?);";
			HashMap<Integer, Object> personaUpdate = new HashMap<Integer, Object>();
			personaUpdate.put(1, productFeature.getTypeId());
			personaUpdate.put(2, productFeature.getPersonaDataId());
			personaUpdate.put(3, productFeature.getId());
			DBUtils.getInstance().updateObject(updatePersonaDataSql, personaUpdate);

		}

		return true;
	}

	@Override
	public PersonaMetadata getPersonaByMetadataId(Integer metadataId) throws SQLException {
		if (metadataId < 0) {
			metadataId = (-1) * metadataId;
		}
		String sql = "SELECT persona_metadata. ID, persona_metadata. KEY,persona_metadata.relation_type_id,persona_metadata.persona_id, persona_data. ID AS persona_data_id, persona_data. VALUE  FROM persona_metadata LEFT JOIN persona_data ON persona_metadata. ID "
				+ "= persona_data.metadata_id WHERE persona_metadata. ID = " + metadataId + " ORDER BY persona_data_id";
		PersonaMetadata personaMetadata = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (personaMetadata == null) {
				personaMetadata = new PersonaMetadata();
				personaMetadata.setPersonaDatas(new ArrayList<PersonaData>());
				personaMetadata.setId(Integer.parseInt(row.get("id")));
				personaMetadata.setKey(row.get("key"));

				if (row.get("persona_data_id") != null) {
					PersonaData persondata = new PersonaData();
					if (row.get("relation_type_id") != null) {
						persondata.setPersonaProductFeatures(null);
						personaMetadata.setRelationTypeId(Integer.parseInt(row.get("relation_type_id")));
					}

					persondata.setId(Integer.parseInt(row.get("persona_data_id")));
					persondata.setPersonaId(Integer.parseInt(row.get("persona_id")));
					persondata.setValue(row.get("value"));
					personaMetadata.getPersonaDatas().add(persondata);
				}
			}

			else {

				if (row.get("persona_data_id") != null) {
					PersonaData persondata = new PersonaData();
					if (row.get("relation_type_id") != null) {
						personaMetadata.setRelationTypeId(Integer.parseInt(row.get("relation_type_id")));
					}
					persondata.setId(Integer.parseInt(row.get("persona_data_id")));
					persondata.setPersonaId(Integer.parseInt(row.get("persona_id")));
					persondata.setValue(row.get("value"));
					personaMetadata.getPersonaDatas().add(persondata);
				}
			}
		}

		return personaMetadata;
	}
}
