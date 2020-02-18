package db.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import pojos.OrganizationConfiguration;

public interface OrganizationConfigurationDAO {
	public OrganizationConfiguration createConfiguration(OrganizationConfiguration configuration, Integer userId) throws SQLException;

	public OrganizationConfiguration findById(Integer id) throws SQLException;

	public ArrayList<OrganizationConfiguration> viewConfiguration(Integer userId) throws SQLException;

}
