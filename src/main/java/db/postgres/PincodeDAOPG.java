package db.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import db.DBUtils;
import db.interfaces.PincodeDAO;
import pojos.Pincode;

public class PincodeDAOPG implements PincodeDAO {

	@Override
	public Pincode findbyID(Integer id) throws SQLException {
		Pincode pincode = null;
		String sql = "SELECT * from pincode where id="+id;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			pincode = new Pincode();
			pincode.setId(Integer.parseInt(row.get("id")));
			pincode.setCity(row.get("city"));
			pincode.setCountry(row.get("country"));
			pincode.setState(row.get("state"));
			pincode.setPin(row.get("pin"));
		}
		return pincode;
	}
	
	@Override
	public Pincode findbyPin(Integer id) throws SQLException {
		Pincode pincode = null;
		String pincodeSql = "select * from pincode where pin = " + id;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				pincodeSql)) {
			pincode = new Pincode();
			pincode.setId(Integer.parseInt(row.get("id")));
			pincode.setCity(row.get("city"));
			pincode.setCountry(row.get("country"));
			pincode.setState(row.get("state"));
			pincode.setPin(row.get("pin"));
		}
		return pincode;
	}

}
