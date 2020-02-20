package ai.salesken.onboarding.dao;

import java.sql.SQLException;

import ai.salesken.onboarding.model.Organization;
import ai.salesken.onboarding.model.SaleskenResponse;

public interface OrganizationDao {
	public SaleskenResponse isValidOrgFields(Organization organization);

	public Organization createOrganization(Organization organization) throws SQLException;
}
