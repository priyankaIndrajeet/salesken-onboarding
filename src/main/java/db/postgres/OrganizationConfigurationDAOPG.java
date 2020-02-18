package db.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import db.DBUtils;
import db.interfaces.OrganizationConfigurationDAO;
import pojos.OrganizationConfiguration;

public class OrganizationConfigurationDAOPG implements OrganizationConfigurationDAO {

	@Override
	public OrganizationConfiguration findById(Integer id) throws SQLException {
		OrganizationConfiguration configuration = null;
		String sql = "SELECT * from organization_configuration WHERE id=" + id;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			configuration = new OrganizationConfiguration();
			configuration.setId(Integer.parseInt(row.get("id")));
			configuration.setOrganizationId(Integer.parseInt(row.get("organization_id")));
			configuration.setPropertyName(row.get("property_name"));
			configuration.setPropertyValue(row.get("property_value"));
		}
		return configuration;
	}

	@Override
	public OrganizationConfiguration createConfiguration(OrganizationConfiguration configuration, Integer userId)
			throws SQLException {
		String organizationId = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), "SELECT * from org_user WHERE userid=" + userId)
				.get(0).get("organizationid");

		String orgConfigSql = "SELECT * from organization_configuration WHERE property_name='"
				+ configuration.getPropertyName() + "' and organization_id=" + organizationId;
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), orgConfigSql);
		if (result.size() == 0) {
			String sqlConfig = "INSERT INTO  organization_configuration (organization_id, property_name, property_value, created_at, updated_at)"
					+ " VALUES (?, ?, ?, now(), now());";
			HashMap<Integer, Object> configData = new HashMap<Integer, Object>();
			configData.put(1, Integer.parseInt(organizationId));
			configData.put(2, configuration.getPropertyName());
			configData.put(3, configuration.getPropertyValue());
			Integer configurationId = DBUtils.getInstance().updateObject(sqlConfig, configData);
			configuration = new OrganizationConfigurationDAOPG().findById(configurationId);
		} else {
			String sqlConfig = "UPDATE organization_configuration SET property_value=?,updated_at=now() WHERE id=?;";
			HashMap<Integer, Object> configData = new HashMap<Integer, Object>();
			configData.put(1, configuration.getPropertyValue());
			configData.put(2, Integer.parseInt(result.get(0).get("id")));
			DBUtils.getInstance().updateObject(sqlConfig, configData);
			Integer configurationId = Integer.parseInt(result.get(0).get("id"));
			configuration = new OrganizationConfigurationDAOPG().findById(configurationId);
		}

		return configuration;
	}

	@Override
	public ArrayList<OrganizationConfiguration> viewConfiguration(Integer userId) throws SQLException {
		String sql = "SELECT * from organization_configuration WHERE organization_id in (SELECT organizationid from org_user WHERE userid="
				+ userId + ") ";
		ArrayList<OrganizationConfiguration> organizationConfigurations = new ArrayList<OrganizationConfiguration>();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			OrganizationConfiguration configuration = new OrganizationConfiguration();
			configuration.setId(Integer.parseInt(row.get("id")));
			configuration.setOrganizationId(Integer.parseInt(row.get("organization_id")));
			configuration.setPropertyName(row.get("property_name"));
			configuration.setPropertyValue(row.get("property_value"));
			organizationConfigurations.add(configuration);
		}
		return organizationConfigurations;
	}
}
