package services.utils;

import java.sql.SQLException;

public interface ZohoUtils {

	Boolean syncData(Integer uID,Integer pipelineId) throws SQLException;

}
