package db.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db.DBUtils;
import db.interfaces.IndustryTypeDAO;
import pojos.IndustryType;

public class IndustryTypeDAOPG implements IndustryTypeDAO {

	@Override
	public List<IndustryType> getIndustryTypes() throws  SQLException {
		List<IndustryType> items = new ArrayList<IndustryType>();
		String sql = "SELECT * from industry_type";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			try {
				items.add(new IndustryType(row.get("name"), Integer.parseInt(row.get("id"))));
			} catch (NumberFormatException e) {
				 
			}
		}
		return items;
	}

}
