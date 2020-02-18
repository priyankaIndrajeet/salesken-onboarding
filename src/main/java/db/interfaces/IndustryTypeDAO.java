package db.interfaces;

import java.sql.SQLException;
import java.util.List;

import pojos.IndustryType;

public interface IndustryTypeDAO {
	public List<IndustryType> getIndustryTypes() throws  SQLException ;
}
