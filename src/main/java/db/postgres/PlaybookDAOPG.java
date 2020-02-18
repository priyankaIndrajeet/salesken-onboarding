package db.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import db.DBUtils;
import db.interfaces.PlaybookDAO;
import pojos.AdvancedPlaybook;
import pojos.AdvancedPlaybookLevel;
import pojos.AdvancedPlaybookNode;
import pojos.AdvancedPlaybookNodeHolder;
import pojos.Dimension;
import strings.StringUtils;

public class PlaybookDAOPG implements PlaybookDAO {
	@Override
	public AdvancedPlaybook getAllActivityofStageTask(Integer stageTaskId) throws SQLException {

		AdvancedPlaybook advancedPlaybook = null;
		String sql = "SELECT advanced_playbook. ID, advanced_playbook.stage_task_id, advanced_playbook_node. ID AS node_id, advanced_playbook_node.snippet_text, advanced_playbook_level.speaker, "
				+ "advanced_playbook_level.dimension_id, advanced_playbook_dimension. NAME_ AS dimension_name, advanced_playbook_level. LEVEL FROM advanced_playbook "
				+ "LEFT JOIN advanced_playbook_level ON advanced_playbook_level.advanced_playbook_id = advanced_playbook.id LEFT JOIN advanced_playbook_node ON advanced_playbook_node.level_id = advanced_playbook_level.id "
				+ "LEFT JOIN advanced_playbook_dimension ON advanced_playbook_dimension. ID = advanced_playbook_level.dimension_id WHERE stage_task_id = "
				+ stageTaskId + " ORDER BY LEVEL;";

		HashMap<Integer, AdvancedPlaybookLevel> levelData = new HashMap<Integer, AdvancedPlaybookLevel>();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			if (advancedPlaybook == null) {
				advancedPlaybook = new AdvancedPlaybook();
				advancedPlaybook.setId(Integer.parseInt(row.get("id")));
				advancedPlaybook.setStageTaskId(Integer.parseInt(row.get("stage_task_id")));
			}

			String childSql = "( WITH RECURSIVE snipptree AS ( SELECT child_node_id, parent_node_id FROM advanced_playbook_node_mapping WHERE"
					+ " parent_node_id = " + row.get("node_id")
					+ " UNION ALL SELECT e.child_node_id, e.parent_node_id FROM advanced_playbook_node_mapping e INNER JOIN snipptree stree ON stree.child_node_id = e.parent_node_id ) SELECT DISTINCT child_node_id AS children FROM snipptree ORDER by children ) ";
			List<Integer> childList = new ArrayList<Integer>();
			for (HashMap<String, String> childRow : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), childSql)) {
				if (childRow.get("children") != null) {
					childList.add(Integer.parseInt(childRow.get("children")));
				}
			}

			if (row.get("level") != null) {
				if (levelData.containsKey(Integer.parseInt(row.get("level")))) {
					AdvancedPlaybookLevel advancedPlaybookLevel = levelData.get(Integer.parseInt(row.get("level")));
					if (row.get("speaker").equalsIgnoreCase("customer")) {
						AdvancedPlaybookNode node = new AdvancedPlaybookNode();
						node.setLevel(Integer.parseInt(row.get("level")));
						node.getChildIds().addAll(childList);
						if (row.get("node_id") != null) {
							node.setId(Integer.parseInt(row.get("node_id")));
						}
						node.setSnippetText(row.get("snippet_text"));
						node.setSpeaker(row.get("speaker"));

						if (row.get("dimension_id") != null) {
							advancedPlaybookLevel.getCustomerNodeHolder()
									.setDimentionsId(Integer.parseInt(row.get("dimension_id")));
							advancedPlaybookLevel.getCustomerNodeHolder().setDimentionName(row.get("dimension_name"));

							advancedPlaybookLevel.getCustomerNodeHolder().setDimentionName(row.get("dimension_name"));
						}
						if (node.getSnippetText() != null) {
							advancedPlaybookLevel.getCustomerNodeHolder().getAdvancedPlaybookNodes().add(node);
						}

					} else {
						AdvancedPlaybookNode node = new AdvancedPlaybookNode();
						node.setLevel(Integer.parseInt(row.get("level")));
						node.getChildIds().addAll(childList);
						if (row.get("node_id") != null) {
							node.setId(Integer.parseInt(row.get("node_id")));
						}
						node.setSnippetText(row.get("snippet_text"));
						node.setSpeaker(row.get("speaker"));

						if (row.get("dimension_id") != null) {
							advancedPlaybookLevel.getAgentNodeHolder()
									.setDimentionsId(Integer.parseInt(row.get("dimension_id")));
							advancedPlaybookLevel.getAgentNodeHolder().setDimentionName(row.get("dimension_name"));
							advancedPlaybookLevel.getAgentNodeHolder().setDimentionName(row.get("dimension_name"));
						}

						if (node.getSnippetText() != null) {
							advancedPlaybookLevel.getAgentNodeHolder().getAdvancedPlaybookNodes().add(node);
						}
					}

					levelData.put(Integer.parseInt(row.get("level")), advancedPlaybookLevel);
				} else {

					AdvancedPlaybookLevel advancedPlaybookLevel = new AdvancedPlaybookLevel();
					advancedPlaybookLevel.setLevel(Integer.parseInt(row.get("level")));

					advancedPlaybookLevel.setAgentNodeHolder(new AdvancedPlaybookNodeHolder());
					advancedPlaybookLevel.setCustomerNodeHolder(new AdvancedPlaybookNodeHolder());

					if (row.get("speaker").equalsIgnoreCase("customer")) {
						AdvancedPlaybookNode node = new AdvancedPlaybookNode();
						node.setLevel(Integer.parseInt(row.get("level")));
						node.getChildIds().addAll(childList);
						if (row.get("node_id") != null) {
							node.setId(Integer.parseInt(row.get("node_id")));
						}
						node.setSnippetText(row.get("snippet_text"));
						node.setSpeaker(row.get("speaker"));

						if (row.get("dimension_id") != null) {
							advancedPlaybookLevel.getCustomerNodeHolder()
									.setDimentionsId(Integer.parseInt(row.get("dimension_id")));
							advancedPlaybookLevel.getCustomerNodeHolder().setDimentionName(row.get("dimension_name"));
							advancedPlaybookLevel.getCustomerNodeHolder().setDimentionName(row.get("dimension_name"));
						}

						if (node.getSnippetText() != null) {
							advancedPlaybookLevel.getCustomerNodeHolder().getAdvancedPlaybookNodes().add(node);
						}

					} else {
						AdvancedPlaybookNode node = new AdvancedPlaybookNode();
						node.setLevel(Integer.parseInt(row.get("level")));
						node.getChildIds().addAll(childList);
						if (row.get("node_id") != null) {
							node.setId(Integer.parseInt(row.get("node_id")));
						}
						node.setSnippetText(row.get("snippet_text"));
						node.setSpeaker(row.get("speaker"));

						if (row.get("dimension_id") != null) {
							advancedPlaybookLevel.getAgentNodeHolder()
									.setDimentionsId(Integer.parseInt(row.get("dimension_id")));
							advancedPlaybookLevel.getAgentNodeHolder().setDimentionName(row.get("dimension_name"));
							advancedPlaybookLevel.getAgentNodeHolder().setDimentionName(row.get("dimension_name"));
						}

						if (node.getSnippetText() != null) {
							advancedPlaybookLevel.getAgentNodeHolder().getAdvancedPlaybookNodes().add(node);
						}

					}

					levelData.put(Integer.parseInt(row.get("level")), advancedPlaybookLevel);
				}
			}
		}
		if (advancedPlaybook != null) {
			ArrayList<AdvancedPlaybookLevel> levels = new ArrayList<AdvancedPlaybookLevel>(levelData.values());
			advancedPlaybook.setLevels(levels);
		}

		return advancedPlaybook;
	}

	@Override
	public ArrayList<Dimension> getAllDimension() throws SQLException {
		String sql = "SELECT * from advanced_playbook_dimension";
		ArrayList<Dimension> dimensionData = new ArrayList<Dimension>();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			Dimension dimension = new Dimension();
			dimension.setId(Integer.parseInt(row.get("id")));
			dimension.setName(row.get("name_"));
			dimensionData.add(dimension);
		}
		return dimensionData;
	}

	@Override
	public AdvancedPlaybookNode getAllChildByNodeId(Integer nodeId) throws SQLException {
		String sqlNode = "SELECT * from advanced_playbook_node WHERE id=" + nodeId;
		AdvancedPlaybookNode advancedPlaybookNode = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sqlNode)) {
			advancedPlaybookNode = new AdvancedPlaybookNode();
			advancedPlaybookNode.setId(Integer.parseInt(row.get("id")));

			String childSql = "( WITH RECURSIVE snipptree AS ( SELECT child_node_id, parent_node_id FROM advanced_playbook_node_mapping WHERE"
					+ " parent_node_id = " + row.get("id")
					+ " UNION ALL SELECT e.child_node_id, e.parent_node_id FROM advanced_playbook_node_mapping e INNER JOIN snipptree stree ON stree.child_node_id = e.parent_node_id ) SELECT DISTINCT child_node_id AS children FROM snipptree ORDER by children ) ";
			List<Integer> childList = new ArrayList<Integer>();
			for (HashMap<String, String> childRow : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), childSql)) {
				if (childRow.get("children") != null) {
					childList.add(Integer.parseInt(childRow.get("children")));
				}

			}

			if (row.get("id") != null) {
				ArrayList<HashMap<String, String>> levelData = DBUtils.getInstance().executeQuery(
						Thread.currentThread().getStackTrace(),
						"SELECT * FROM advanced_playbook_level WHERE id = " + row.get("level_id")
								+ " and advanced_playbook_id =" + row.get("advanced_playbook_id"));
				advancedPlaybookNode.setLevel(Integer.parseInt(levelData.get(0).get("level")));
				advancedPlaybookNode.getChildIds().addAll(childList);
				advancedPlaybookNode.setId(Integer.parseInt(row.get("id")));
				advancedPlaybookNode.setSnippetText(row.get("snippet_text"));
				advancedPlaybookNode.setSpeaker(levelData.get(0).get("speaker"));
			}

		}

		return advancedPlaybookNode;
	}

	@Override
	public AdvancedPlaybookNode createAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybookNode)
			throws SQLException {

		Integer advancedPlaybookId;
		String sql = "select * from advanced_playbook where stage_task_id = " + advancedPlaybookNode.getStageTaskId()
				+ "";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (result.size() == 0) {
			String advancedPlaybookSql = "INSERT INTO  advanced_playbook (created_by, created_at, updated_at, stage_task_id) VALUES ( NULL, now(), now(), ?)";
			HashMap<Integer, Object> advancedPlaybookData = new HashMap<Integer, Object>();
			advancedPlaybookData.put(1, advancedPlaybookNode.getStageTaskId());

			advancedPlaybookId = DBUtils.getInstance().updateObject(advancedPlaybookSql, advancedPlaybookData);
			advancedPlaybookNode.setPlaybookId(advancedPlaybookId);
		} else {
			advancedPlaybookId = Integer.parseInt(result.get(0).get("id"));
		}

		ArrayList<HashMap<String, String>> levelDataList = DBUtils.getInstance().executeQuery(
				Thread.currentThread().getStackTrace(),
				"SELECT * FROM advanced_playbook_level WHERE level = " + advancedPlaybookNode.getLevel()
						+ " and advanced_playbook_id = " + advancedPlaybookId + " and speaker = '"
						+ advancedPlaybookNode.getSpeaker() + "'");

		if (levelDataList.size() > 0) {
			if (levelDataList.get(0).get("dimension_id") == null) {
				sql = "update advanced_playbook_level set dimension_id = ? where level = ? and advanced_playbook_id = ? and speaker = ? "
						+ "";
				HashMap<Integer, Object> levelData = new HashMap<Integer, Object>();
				if (advancedPlaybookNode.getDimensionId() != null) {
					levelData.put(1, advancedPlaybookNode.getDimensionId());
				} else {
					levelData.put(1, null);
				}
				levelData.put(2, advancedPlaybookNode.getLevel());
				levelData.put(3, advancedPlaybookId);
				levelData.put(4, advancedPlaybookNode.getSpeaker());
			}
			if (advancedPlaybookNode.getSnippetText() != null) {
				sql = "INSERT INTO advanced_playbook_node ( advanced_playbook_id, snippet_text, created_at, updated_at, level_id) "
						+ "VALUES (?, ?, now(), now(), ?)";
				HashMap<Integer, Object> advancedPlaybookData = new HashMap<Integer, Object>();
				advancedPlaybookData.put(1, advancedPlaybookId);
				advancedPlaybookData.put(2, StringUtils.stringCapitalize(StringUtils.cleanHTML(advancedPlaybookNode.getSnippetText()).trim()));
				advancedPlaybookData.put(3, Integer.parseInt(levelDataList.get(0).get("id")));
				Integer nodeId = DBUtils.getInstance().updateObject(sql, advancedPlaybookData);
				advancedPlaybookNode.setId(nodeId);
			}

		} else {
			sql = "INSERT INTO advanced_playbook_level (level, advanced_playbook_id, dimension_id, speaker) VALUES (?, ?, ?, ?)";
			HashMap<Integer, Object> levelData = new HashMap<Integer, Object>();
			levelData.put(1, advancedPlaybookNode.getLevel());
			levelData.put(2, advancedPlaybookId);
			if (advancedPlaybookNode.getDimensionId() != null) {
				levelData.put(3, advancedPlaybookNode.getDimensionId());
			} else {
				levelData.put(3, null);
			}
			levelData.put(4, advancedPlaybookNode.getSpeaker());
			Integer levelId = DBUtils.getInstance().updateObject(sql, levelData);
			if (advancedPlaybookNode.getSnippetText() != null) {
				sql = "INSERT INTO advanced_playbook_node ( advanced_playbook_id, snippet_text, created_at, updated_at, level_id) "
						+ "VALUES (?, ?, now(), now(), ?)";
				HashMap<Integer, Object> advancedPlaybookData = new HashMap<Integer, Object>();
				advancedPlaybookData.put(1, advancedPlaybookId);
				advancedPlaybookData.put(2, advancedPlaybookNode.getSnippetText());
				advancedPlaybookData.put(3, levelId);
				Integer nodeId = DBUtils.getInstance().updateObject(sql, advancedPlaybookData);
				advancedPlaybookNode.setId(nodeId);
			}

		}

		return advancedPlaybookNode;
	}

	@Override
	public AdvancedPlaybookNode updateAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybook) throws SQLException {
		String sql = "UPDATE advanced_playbook_node SET snippet_text = ?, updated_at = now() WHERE id = ?";
		HashMap<Integer, Object> advancedPlaybookData = new HashMap<Integer, Object>();
		advancedPlaybookData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(advancedPlaybook.getSnippetText())));
		advancedPlaybookData.put(2, advancedPlaybook.getId());
		DBUtils.getInstance().updateObject(sql, advancedPlaybookData);
		return advancedPlaybook;
	}

	@Override
	public Boolean deleteAdvancedPlaybookNode(Integer nodeId) throws SQLException {
		deleteMappingOfAdvancedPlaybookNode(nodeId);
		String deleteNodeSql = "delete from advanced_playbook_node where id = ?";
		HashMap<Integer, Object> nodeData = new HashMap<Integer, Object>();
		nodeData.put(1, nodeId);
		DBUtils.getInstance().updateObject(deleteNodeSql, nodeData);
		return true;
	}

	@Override
	public AdvancedPlaybookNode addMappingInAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybookNode)
			throws SQLException {

		String deleteSql = "DELETE from advanced_playbook_node_mapping WHERE parent_node_id=?";
		HashMap<Integer, Object> deleteData = new HashMap<Integer, Object>();
		deleteData.put(1, advancedPlaybookNode.getId());
		DBUtils.getInstance().updateObject(deleteSql, deleteData);

		if (advancedPlaybookNode.getChildIds() != null) {
			for (Integer id : advancedPlaybookNode.getChildIds()) {

				String insertSnippetChildSql = "INSERT INTO advanced_playbook_node_mapping (child_node_id , parent_node_id) VALUES (? , ?)";
				HashMap<Integer, Object> insertData = new HashMap<Integer, Object>();
				insertData.put(1, id);
				insertData.put(2, advancedPlaybookNode.getId());
				DBUtils.getInstance().updateObject(insertSnippetChildSql, insertData);
			}
		}

		String sqlNode = "SELECT * from advanced_playbook_node WHERE id=" + advancedPlaybookNode.getId();
		advancedPlaybookNode = null;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sqlNode)) {
			advancedPlaybookNode = new AdvancedPlaybookNode();
			advancedPlaybookNode.setId(Integer.parseInt(row.get("id")));

			String childSql = "( WITH RECURSIVE snipptree AS ( SELECT child_node_id, parent_node_id FROM advanced_playbook_node_mapping WHERE"
					+ " parent_node_id = " + row.get("id")
					+ " UNION ALL SELECT e.child_node_id, e.parent_node_id FROM advanced_playbook_node_mapping e INNER JOIN snipptree stree ON stree.child_node_id = e.parent_node_id ) SELECT DISTINCT child_node_id AS children FROM snipptree ORDER by children ) ";
			List<Integer> childList = new ArrayList<Integer>();
			for (HashMap<String, String> childRow : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), childSql)) {
				if (childRow.get("children") != null) {
					childList.add(Integer.parseInt(childRow.get("children")));
				}

			}

			if (row.get("id") != null) {
				ArrayList<HashMap<String, String>> levelData = DBUtils.getInstance().executeQuery(
						Thread.currentThread().getStackTrace(),
						"SELECT * FROM advanced_playbook_level WHERE id = " + row.get("level_id")
								+ " and advanced_playbook_id =" + row.get("advanced_playbook_id"));
				advancedPlaybookNode.setLevel(Integer.parseInt(levelData.get(0).get("level")));
				advancedPlaybookNode.getChildIds().addAll(childList);
				advancedPlaybookNode.setId(Integer.parseInt(row.get("id")));
				advancedPlaybookNode.setSnippetText(row.get("snippet_text"));
				advancedPlaybookNode.setSpeaker(levelData.get(0).get("speaker"));
			}

		}

		return advancedPlaybookNode;
	}

	@Override
	public Boolean deleteMappingOfAdvancedPlaybookNode(Integer nodeId) throws SQLException {
		String sql = "delete from advanced_playbook_node_mapping where parent_node_id = ?";
		HashMap<Integer, Object> deleteData = new HashMap<Integer, Object>();
		deleteData.put(1, nodeId);
		DBUtils.getInstance().updateObject(sql, deleteData);
		sql = "delete from advanced_playbook_node_mapping where child_node_id = ?";
		deleteData = new HashMap<Integer, Object>();
		deleteData.put(1, nodeId);
		DBUtils.getInstance().updateObject(sql, deleteData);
		return true;
	}

	@Override
	public Boolean updateDimension(AdvancedPlaybookNode advancedPlaybookNode) throws SQLException {

		String sql = "select * from advanced_playbook where stage_task_id = " + advancedPlaybookNode.getStageTaskId();
		ArrayList<HashMap<String, String>> playbookData = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		Integer advancedPlaybookId = Integer.parseInt(playbookData.get(0).get("id"));
		sql = "SELECT * FROM advanced_playbook_level WHERE level = " + advancedPlaybookNode.getLevel()
				+ " and advanced_playbook_id = " + advancedPlaybookId + " and speaker = '"
				+ advancedPlaybookNode.getSpeaker() + "'";

		ArrayList<HashMap<String, String>> levelDataList = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);

		if (levelDataList.size() > 0) {
			sql = "update advanced_playbook_level set dimension_id = ? where level = ? and advanced_playbook_id = ? and speaker = ?";
			HashMap<Integer, Object> levelData = new HashMap<Integer, Object>();
			levelData.put(1, advancedPlaybookNode.getDimensionId());
			levelData.put(2, advancedPlaybookNode.getLevel());
			levelData.put(3, advancedPlaybookId);
			levelData.put(4, advancedPlaybookNode.getSpeaker());
			DBUtils.getInstance().updateObject(sql, levelData);
		}
		return true;
	}

	@Override
	public Boolean deletePlaybookStageTask(Integer stageTaskId) throws SQLException {
		String sql = "select * from advanced_playbook where stage_task_id =" + stageTaskId;
		ArrayList<HashMap<String, String>> playbookData = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		Integer advanced_playbook_id = Integer.parseInt(playbookData.get(0).get("id"));
		sql = "select * from advanced_playbook_node where advanced_playbook_id = " + advanced_playbook_id;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sql)) {
			deleteMappingOfAdvancedPlaybookNode(Integer.parseInt(row.get("id")));
		}
		sql = "DELETE FROM advanced_playbook_node WHERE advanced_playbook_id = ?";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, advanced_playbook_id);
		DBUtils.getInstance().updateObject(sql, data);
		sql = "DELETE FROM advanced_playbook WHERE stage_task_id = ?";
		data = new HashMap<Integer, Object>();
		data.put(1, stageTaskId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

	@Override
	public Integer addLevel(AdvancedPlaybookNode advancedPlaybookNode) throws SQLException {
		Integer advancedPlaybookId;
		String sql = "select * from advanced_playbook where stage_task_id = " + advancedPlaybookNode.getStageTaskId()
				+ "";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (result.size() == 0) {
			String advancedPlaybookSql = "INSERT INTO  advanced_playbook (created_by, created_at, updated_at, stage_task_id) VALUES ( NULL, now(), now(), ?)";
			HashMap<Integer, Object> advancedPlaybookData = new HashMap<Integer, Object>();
			advancedPlaybookData.put(1, advancedPlaybookNode.getStageTaskId());
			advancedPlaybookId = DBUtils.getInstance().updateObject(advancedPlaybookSql, advancedPlaybookData);
			advancedPlaybookNode.setPlaybookId(advancedPlaybookId);

			sql = "INSERT INTO advanced_playbook_level (level, advanced_playbook_id, dimension_id, speaker) VALUES (?, ?, ?, ?)";
			HashMap<Integer, Object> levelData = new HashMap<Integer, Object>();
			levelData.put(1, 1);
			levelData.put(2, advancedPlaybookId);
			if (advancedPlaybookNode.getDimensionId() != null) {
				levelData.put(3, advancedPlaybookNode.getDimensionId());
			} else {
				levelData.put(3, null);
			}
			levelData.put(4, "Agent");
			DBUtils.getInstance().updateObject(sql, levelData);
			levelData = new HashMap<Integer, Object>();
			levelData.put(1, 1);
			levelData.put(2, advancedPlaybookId);
			if (advancedPlaybookNode.getDimensionId() != null) {
				levelData.put(3, advancedPlaybookNode.getDimensionId());
			} else {
				levelData.put(3, null);
			}
			levelData.put(4, "Customer");
			DBUtils.getInstance().updateObject(sql, levelData);
			advancedPlaybookNode.setLevel(1);
			advancedPlaybookNode.setSpeaker("agent");
		} else {
			advancedPlaybookId = Integer.parseInt(result.get(0).get("id"));

			sql = "select max(level) as level from advanced_playbook_level where advanced_playbook_id = "
					+ advancedPlaybookId;
			ArrayList<HashMap<String, String>> maxLevel = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sql);

			sql = "INSERT INTO advanced_playbook_level (level, advanced_playbook_id, dimension_id, speaker) VALUES (?, ?, ?, ?)";
			HashMap<Integer, Object> levelData = new HashMap<Integer, Object>();
			if (maxLevel.get(0).get("level") != null) {
				levelData.put(1, Integer.parseInt(maxLevel.get(0).get("level")) + 1);
			} else {
				levelData.put(1, 1);
			}
			levelData.put(2, advancedPlaybookId);
			if (advancedPlaybookNode.getDimensionId() != null) {
				levelData.put(3, advancedPlaybookNode.getDimensionId());
			} else {
				levelData.put(3, null);
			}
			levelData.put(4, "Agent");
			DBUtils.getInstance().updateObject(sql, levelData);
			levelData = new HashMap<Integer, Object>();
			if (maxLevel.get(0).get("level") != null) {
				levelData.put(1, Integer.parseInt(maxLevel.get(0).get("level")) + 1);
			} else {
				levelData.put(1, 1);
			}
			levelData.put(2, advancedPlaybookId);
			if (advancedPlaybookNode.getDimensionId() != null) {
				levelData.put(3, advancedPlaybookNode.getDimensionId());
			} else {
				levelData.put(3, null);
			}
			levelData.put(4, "Customer");
			DBUtils.getInstance().updateObject(sql, levelData);
			if (maxLevel.get(0).get("level") != null) {
				advancedPlaybookNode.setLevel(Integer.parseInt(maxLevel.get(0).get("level")) + 1);
			} else {
				advancedPlaybookNode.setLevel(1);
			}

			advancedPlaybookNode.setSpeaker("agent");
		}

		return advancedPlaybookNode.getLevel();
	}
}