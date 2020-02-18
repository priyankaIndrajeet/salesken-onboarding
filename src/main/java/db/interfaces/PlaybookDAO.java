package db.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import pojos.AdvancedPlaybook;
import pojos.AdvancedPlaybookNode;
import pojos.Dimension;

public interface PlaybookDAO {
	public AdvancedPlaybook getAllActivityofStageTask(Integer stageTaskId) throws SQLException;

	public ArrayList<Dimension> getAllDimension() throws SQLException;

	public AdvancedPlaybookNode createAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybook) throws SQLException;

	public AdvancedPlaybookNode updateAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybook) throws SQLException;

	public Boolean deleteAdvancedPlaybookNode(Integer playbookSnippetId) throws SQLException;

	public AdvancedPlaybookNode addMappingInAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybook)
			throws SQLException;

	public Boolean updateDimension(AdvancedPlaybookNode advancedPlaybook) throws SQLException;

	public Boolean deleteMappingOfAdvancedPlaybookNode(Integer nodeId) throws SQLException;

	public Boolean deletePlaybookStageTask(Integer stageTaskId) throws SQLException;

	public AdvancedPlaybookNode getAllChildByNodeId(Integer nodeId) throws SQLException;

	public Integer addLevel(AdvancedPlaybookNode advancedPlaybookNode) throws SQLException;

}
