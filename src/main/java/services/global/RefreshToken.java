package services.global;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

public interface RefreshToken {
	public Response refreshJwtToken() throws SQLException;
}
