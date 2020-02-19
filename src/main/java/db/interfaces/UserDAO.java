package db.interfaces;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import pojos.BulkUser;
import pojos.Designation;
import pojos.DesignationUser;
import pojos.Role;
import pojos.User;

/**
 * This is the UserDAO an interface which will perform all DB related operation
 * on the user entity.
 * 
 * @author Vaibhav Verma
 *
 */
/**
 * @author User
 *
 */
public interface UserDAO {

	/**
	 * This method will return the user object getching it from respective database.
	 * 
	 * @param id
	 * @return User Pojo
	 * @throws SQLException
	 */
	public User findbyID(Integer id) throws SQLException;

	/**
	 * This method will return the user object getching it from respective database.
	 * 
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	/**
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public User findbyEmail(String email) throws SQLException;

	/**
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public User updateProfile(User user) throws SQLException;

	/**
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public User updatePassword(User user) throws SQLException;

	/**
	 * @return
	 * @throws SQLException
	 */
	public Boolean forgotPassword() throws SQLException;

	public Boolean isValidLicense(User user) throws SQLException;

	public ArrayList<User> findbyOrganizationId(Integer id) throws SQLException;

	public ArrayList<BulkUser> getPreviewFromFile(Integer id, String filePath) throws SQLException;

	public Boolean bulkUserCreation(ArrayList<BulkUser> users, Integer userID) throws SQLException;

	public User createUser(User user, Integer userID) throws SQLException;

	public User updateUser(User user) throws SQLException;

	public Boolean isUserAlreadyExistWithMobile(String mobile) throws SQLException;

	public Boolean isUserAlreadyExistWithEmail(String email) throws SQLException;

	public ArrayList<Role> getAllRoles() throws SQLException;

	public Boolean deleteUser(Integer userId) throws SQLException;

	public User findUserProfile(Integer userId) throws SQLException;

	public ArrayList<User> getManagers(Integer userId) throws SQLException;

	public ArrayList<Designation> getDesignations(Integer userId) throws SQLException;

	public ArrayList<User> getDesignationsWiseUsers(Integer userId, Integer designationId) throws SQLException;

	public boolean deleteOwner(Integer user_Id) throws SQLException;

	public User updateUserProfile(User user) throws SQLException;

	public boolean deactivateUser(Integer userId) throws SQLException;

	public ArrayList<DesignationUser> getAllAssociateWithManagerByuserId(Integer userId) throws SQLException;

 
 
	public BulkUser userVerification(BulkUser user) throws SQLException;

	public Designation addDesignation(Designation designation, Integer userId) throws SQLException;

	public User getDesignationsWiseSingleUser(Integer userId, Integer designationId) throws SQLException;

	public Designation updateDesignationOfUser(User user, Integer userId) throws SQLException;

	public Boolean deletBulkUsers(ArrayList<Integer> userIds) throws SQLException;

	public User createV1User(User user, Integer uId) throws SQLException;

	public User updateV1User(User user ,Integer userId) throws SQLException, IOException, InterruptedException;

	public User getUserCreationFieldsData(String designation, Integer userId) throws SQLException;

	public User getUserUpdationFieldsData(Integer user_id) throws SQLException;

	public User getCurrentLocation(User user) throws IOException, InterruptedException;

}
