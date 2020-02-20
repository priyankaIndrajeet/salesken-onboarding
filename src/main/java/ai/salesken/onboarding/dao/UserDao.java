package ai.salesken.onboarding.dao;

import java.sql.SQLException;

import ai.salesken.onboarding.model.User;

public interface UserDao {
	public User findbyEmail(String email) throws SQLException;

	public Boolean isValidLicense(User user) throws SQLException;
}
