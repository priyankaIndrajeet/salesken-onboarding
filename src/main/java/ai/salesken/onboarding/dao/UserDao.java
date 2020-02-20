package ai.salesken.onboarding.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import ai.salesken.onboarding.model.User;

public interface UserDao {
	public User findbyEmail(String email) throws SQLException;

	public Boolean isValidLicense(User user) throws SQLException;

	public User findbyID(Integer id) throws SQLException;

	public ArrayList<User> getPreviewFromFile(Integer id, String filepath) throws SQLException;
}
