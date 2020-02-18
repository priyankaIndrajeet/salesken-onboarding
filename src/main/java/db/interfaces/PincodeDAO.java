package db.interfaces;

import java.sql.SQLException;

import pojos.Pincode;

public interface PincodeDAO {
	/**
	 * This method will return the pincode object getting it from respective database.
	 * 
	 * @param id
	 * @return User Pojo
	 * @throws SQLException
	 */
	public Pincode findbyID(Integer id) throws SQLException;
	public Pincode findbyPin(Integer id) throws SQLException;
}
