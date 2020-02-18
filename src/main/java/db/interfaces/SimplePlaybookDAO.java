package db.interfaces;

import java.sql.SQLException;

import pojos.SimplePlaybook;
import pojos.SimplePlaybookNode;

public interface SimplePlaybookDAO {

	public SimplePlaybook viewSimplePlaybookByUserId(Integer userId) throws SQLException;
	
	public SimplePlaybookNode addNode(SimplePlaybookNode simplePlaybookNode, Integer userId) throws SQLException;

	public SimplePlaybookNode updateNode(SimplePlaybookNode simplePlaybookNode, Integer userId) throws SQLException;

	public Boolean deleteNode(Integer nodeId) throws SQLException;
}
