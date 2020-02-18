package db.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import db.DBUtils;
import db.interfaces.SimplePlaybookDAO;
import pojos.Organization;
import pojos.SimplePlaybook;
import pojos.SimplePlaybookNode;
import strings.StringUtils;

public class SimplePlaybookDAOPG implements SimplePlaybookDAO {

	@Override
	public SimplePlaybook viewSimplePlaybookByUserId(Integer userId) throws SQLException {
		String sqlPlaybook = "SELECT * from simple_playbook WHERE organization_id in (SELECT organizationid from org_user WHERE userid="
				+ userId + ")";
		ArrayList<HashMap<String, String>> resultSimplePlaybook = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlPlaybook);
		if (resultSimplePlaybook.size() == 0) {
			Organization organization = new OrganizationDAOPG().findOrganizationByUserId(userId);
			String simplaybookInsert = "INSERT INTO simple_playbook  ( organization_id ,  created_by ,  created_at ,  updated_at ) VALUES ( ?, ?, now(), now());";
			HashMap<Integer, Object> data = new HashMap<Integer, Object>();
			data.put(1, organization.getId());
			data.put(2, userId);
			DBUtils.getInstance().updateObject(simplaybookInsert, data);
		}

		String sql = "SELECT simple_playbook.*, simple_playbook_node. ID AS node_id, simple_playbook_node.created_by AS node_created_by, simple_playbook_node.speaker,"
				+ " simple_playbook_node.question, simple_playbook_node.answer FROM simple_playbook LEFT JOIN simple_playbook_node"
				+ " ON simple_playbook_node.playbook_id = simple_playbook. ID WHERE simple_playbook.organization_id IN"
				+ " ( SELECT organizationid FROM org_user WHERE userid = " + userId
				+ " )  ORDER BY simple_playbook_node. ID";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);

		SimplePlaybook simplePlaybook = null;
		ArrayList<SimplePlaybookNode> agentNodes = new ArrayList<SimplePlaybookNode>();
		ArrayList<SimplePlaybookNode> customerNodes = new ArrayList<SimplePlaybookNode>();
		for (HashMap<String, String> rowdata : result) {
			if (simplePlaybook == null) {
				simplePlaybook = new SimplePlaybook();
				simplePlaybook.setId(Integer.parseInt(rowdata.get("id")));
				simplePlaybook.setOrganizationId(Integer.parseInt(rowdata.get("organization_id")));
			}
			if (rowdata.get("node_id") != null) {
				if (rowdata.get("speaker") != null && rowdata.get("speaker").equalsIgnoreCase("CUSTOMER")) {
					SimplePlaybookNode simplePlaybookNode = new SimplePlaybookNode();
					simplePlaybookNode.setId(Integer.parseInt(rowdata.get("node_id")));
					simplePlaybookNode.setAnswer(rowdata.get("answer"));
					simplePlaybookNode.setPlaybookId(Integer.parseInt(rowdata.get("id")));
					simplePlaybookNode.setQuestion(rowdata.get("question"));
					simplePlaybookNode.setSpeaker(rowdata.get("speaker"));
					customerNodes.add(simplePlaybookNode);

				} else {

					SimplePlaybookNode simplePlaybookNode = new SimplePlaybookNode();
					simplePlaybookNode.setId(Integer.parseInt(rowdata.get("node_id")));
					simplePlaybookNode.setAnswer(rowdata.get("answer"));
					simplePlaybookNode.setPlaybookId(Integer.parseInt(rowdata.get("id")));
					simplePlaybookNode.setQuestion(rowdata.get("question"));
					simplePlaybookNode.setSpeaker(rowdata.get("speaker"));
					agentNodes.add(simplePlaybookNode);

				}
			}

		}
		if (simplePlaybook != null) {
			simplePlaybook.setAgentNodes(agentNodes);
			simplePlaybook.setCustomerNodes(customerNodes);
		}

		return simplePlaybook;
	}

	@Override
	public SimplePlaybookNode addNode(SimplePlaybookNode simplePlaybookNode, Integer userId) throws SQLException {
		String sqlPlaybook = "SELECT * from simple_playbook WHERE organization_id in (SELECT organizationid from org_user WHERE userid="
				+ userId + ")";
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlPlaybook);
		Integer simplePlaybookId = null;
		if (result.size() > 0) {
			simplePlaybookId = Integer.parseInt(result.get(0).get("id"));
		} else {
			Organization organization = new OrganizationDAOPG().findOrganizationByUserId(userId);
			String simplaybookInsert = "INSERT INTO simple_playbook  ( organization_id ,  created_by ,  created_at ,  updated_at ) VALUES ( ?, ?, now(), now());";
			HashMap<Integer, Object> data = new HashMap<Integer, Object>();
			data.put(1, organization.getId());
			data.put(2, userId);
			simplePlaybookId = DBUtils.getInstance().updateObject(simplaybookInsert, data);
		}
		if (simplePlaybookId != null) {
			String sql = "INSERT INTO simple_playbook_node (playbook_id, created_by, speaker, question, answer, created_at, updated_at) VALUES ( ?, ?, ?,?, ?, now(), now())";
			HashMap<Integer, Object> data = new HashMap<Integer, Object>();
			data.put(1, simplePlaybookId);
			data.put(2, userId);
			data.put(3, StringUtils.cleanHTML(simplePlaybookNode.getSpeaker()));
			data.put(4, StringUtils.cleanHTML(simplePlaybookNode.getQuestion()));
			data.put(5, StringUtils.cleanHTML(simplePlaybookNode.getAnswer()));
			Integer nodeID = DBUtils.getInstance().updateObject(sql, data);
			String viewNodeSql = "SELECT * from simple_playbook_node where id = " + nodeID;
			simplePlaybookNode = new SimplePlaybookNode();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), viewNodeSql)) {
				simplePlaybookNode.setId(Integer.parseInt(row.get("id")));
				simplePlaybookNode.setPlaybookId(Integer.parseInt(row.get("playbook_id")));
				simplePlaybookNode.setSpeaker(row.get("speaker"));
				simplePlaybookNode.setQuestion(row.get("question"));
				simplePlaybookNode.setAnswer(row.get("answer"));
				simplePlaybookNode.setCreatedBy(userId);
			}

		}
		return simplePlaybookNode;
	}

	@Override
	public SimplePlaybookNode updateNode(SimplePlaybookNode simplePlaybookNode, Integer userId) throws SQLException {

		String sql = "UPDATE simple_playbook_node SET  question  = ?, answer  = ?,updated_at=now() WHERE id  = ?;";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, StringUtils.cleanHTML(simplePlaybookNode.getQuestion()));
		data.put(2, StringUtils.cleanHTML(simplePlaybookNode.getAnswer()));
		data.put(3, simplePlaybookNode.getId());
		DBUtils.getInstance().updateObject(sql, data);
		String viewNodeSql = "SELECT * from simple_playbook_node where id = " + simplePlaybookNode.getId();
		simplePlaybookNode = new SimplePlaybookNode();
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				viewNodeSql)) {
			simplePlaybookNode.setId(Integer.parseInt(row.get("id")));
			simplePlaybookNode.setPlaybookId(Integer.parseInt(row.get("playbook_id")));
			simplePlaybookNode.setSpeaker(row.get("speaker"));
			simplePlaybookNode.setQuestion(row.get("question"));
			simplePlaybookNode.setAnswer(row.get("answer"));
			simplePlaybookNode.setCreatedBy(userId);
		}

		return simplePlaybookNode;
	}
	@Override
	public Boolean deleteNode(Integer nodeId) throws SQLException {
		String sql ="delete from simple_playbook_node where id = ?";
		 HashMap<Integer, Object> data = new HashMap<Integer, Object>();
			data.put(1, nodeId);
			DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

}
