package db.interfaces;

import java.sql.SQLException;
import pojos.Dashboard;
import pojos.Organization;
import pojos.User;
import pojos.Users;

public interface OrganizationDAO {
	public Organization createOrganization(Organization organization) throws SQLException;

	public Organization findOrganizationById(Integer id) throws SQLException;

	public Organization findOrganizationByUserId(Integer userId) throws SQLException;

	public Organization update(Organization org) throws SQLException;

	public Users getMembers(User u, String status, String role, String pattern, String limit, String offset)
			throws SQLException;

	public Dashboard getDashboardCompletionInfo(Integer user_Id) throws SQLException;

	public boolean isValidOrgWebsite(String website, Integer org_id) throws SQLException;

}
