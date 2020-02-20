package ai.salesken.onboarding.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;

import ai.salesken.onboarding.constants.ResponseCodes;
import ai.salesken.onboarding.constants.ResponseMessages;
import ai.salesken.onboarding.dao.OrganizationDao;
import ai.salesken.onboarding.model.Organization;
import ai.salesken.onboarding.model.SaleskenResponse;
import ai.salesken.onboarding.utils.DButils.DBUtils;

public class OrganizationDaoImpl implements OrganizationDao {

	@Override
	public Organization createOrganization(Organization organization) throws SQLException {
		String orgSql = "INSERT INTO  organization ( name, industry , website, contact_phone, address_line_1, address_line_2, landmark, city, state, country, zipcode, profile,created_at, updated_at )"
				+ " VALUES" + "(?,?,?,?,?,?,?,?,?,?,?,?,now(),now())";

		HashMap<Integer, Object> orgData = new HashMap<Integer, Object>();
		orgData.put(1, organization.getName());
		orgData.put(2, organization.getIndustry_type());
		orgData.put(3, organization.getWebsite());
		orgData.put(4, organization.getBoard_number());
		orgData.put(5, organization.getAddress_line1());
		orgData.put(6, organization.getAddress_line2());
		orgData.put(7, organization.getLandmark());
		orgData.put(8, organization.getCity());
		orgData.put(9, organization.getState());
		orgData.put(10, organization.getCountry());
		orgData.put(11, organization.getZipcode());
		orgData.put(12, organization.getDescription());

		Integer orgId = DBUtils.getInstance().updateObject(orgSql, orgData);
		organization.setId(orgId);

		return organization;

	}

	@Override
	public SaleskenResponse isValidOrgFields(Organization organization) {
		SaleskenResponse response = new SaleskenResponse();

		if (organization.getName() != null && organization.getName().trim().length() > 256) {
			response = new SaleskenResponse(ResponseCodes.NAME_LENGTH_IS_GREATER_THAN_256,
					ResponseMessages.NAME_LENGTH_IS_GREATER_THAN_256);
			return response;
		}
		if (organization.getWebsite() != null && organization.getWebsite().trim().length() > 256) {
			response = new SaleskenResponse(ResponseCodes.WEBSITE_LENGTH_IS_GREATER_THAN_256,
					ResponseMessages.WEBSITE_LENGTH_IS_GREATER_THAN_256);
			return response;
		}
		if (organization.getAddress_line1() != null && organization.getAddress_line1().trim().length() > 256) {
			response = new SaleskenResponse(ResponseCodes.ADDRESS_LINE_1_LENGTH_IS_GREATER_THAN_256,
					ResponseMessages.ADDRESS_LINE_1_LENGTH_IS_GREATER_THAN_256);
			return response;
		}
		if (organization.getAddress_line2() != null && organization.getAddress_line2().trim().length() > 256) {
			response = new SaleskenResponse(ResponseCodes.ADDRESS_LINE_2_LENGTH_IS_GREATER_THAN_256,
					ResponseMessages.ADDRESS_LINE_2_LENGTH_IS_GREATER_THAN_256);
			return response;
		}
		if (organization.getCity() != null && organization.getCity().trim().length() > 256) {
			response = new SaleskenResponse(ResponseCodes.CITY_LENGTH_IS_GREATER_THAN_256,
					ResponseMessages.CITY_LENGTH_IS_GREATER_THAN_256);
			return response;
		}

		if (organization.getCountry() != null && organization.getCountry().trim().length() > 256) {
			response = new SaleskenResponse(ResponseCodes.COUNTRY_LENGTH_IS_GREATER_THAN_256,
					ResponseMessages.COUNTRY_LENGTH_IS_GREATER_THAN_256);
			return response;
		}
		if (organization.getLandmark() != null && organization.getLandmark().trim().length() > 256) {
			response = new SaleskenResponse(ResponseCodes.LANDMARK_LENGTH_IS_GREATER_THAN_256,
					ResponseMessages.LANDMARK_LENGTH_IS_GREATER_THAN_256);
			return response;
		}
		if (organization.getState() != null && organization.getState().trim().length() > 256) {
			response = new SaleskenResponse(ResponseCodes.STATE_LENGTH_IS_GREATER_THAN_256,
					ResponseMessages.STATE_LENGTH_IS_GREATER_THAN_256);
			return response;
		}
		if (organization.getDescription() != null && organization.getDescription().trim().length() > 1024) {
			response = new SaleskenResponse(ResponseCodes.DESCRIPTION_LENGTH_IS_GREATER_THAN_1024,
					ResponseMessages.DESCRIPTION_LENGTH_IS_GREATER_THAN_1024);
			return response;
		}

		return response;
	}

}
