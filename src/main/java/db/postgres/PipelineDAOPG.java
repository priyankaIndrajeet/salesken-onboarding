package db.postgres;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import db.DBUtils;
import db.interfaces.OrganizationDAO;
import db.interfaces.PipelineDAO;
import db.interfaces.ProductDAO;
import db.interfaces.TeamDAO;
import pojos.LeadSource;
import pojos.Organization;
import pojos.Persona;
import pojos.Pipeline;
import pojos.PipelineFields;
import pojos.PipelineStage;
import pojos.PipelineStageTask;
import pojos.Product;
import pojos.Team;
import pojos.User;
import strings.StringUtils;

public class PipelineDAOPG implements PipelineDAO {

	@Override
	public Pipeline findbyId(Integer pipelineId) throws SQLException {
		String pipelineSql = "SELECT pipeline.name,pipeline.organization_id from pipeline where id = " + pipelineId
				+ " and is_active='t'";
		ArrayList<HashMap<String, String>> pipeline = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), pipelineSql);
		Integer org_id = Integer.parseInt(pipeline.get(0).get("organization_id"));
		Pipeline pipelines = new Pipeline();
		try {
			pipelines.setId(pipelineId);
			pipelines.setName(pipeline.get(0).get("name"));
			String productSql = "SELECT name as product_name, product_id  FROM pipeline_product LEFT JOIN product on pipeline_product.product_id = product.id where pipeline_id = "
					+ pipelineId + "  and product.deleted=FALSE ";
			ArrayList<Product> products = new ArrayList<Product>();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), productSql)) {
				Product product = new Product();
				product.setId(Integer.parseInt(row.get("product_id")));
				product.setName(row.get("product_name"));
				products.add(product);
			}
			String teamSql = "SELECT  string_agg(pipeline_team.team_id :: CHARACTER VARYING, ',') as team_id from pipeline_team where pipeline_team.pipeline_id = "
					+ pipelineId + " GROUP BY pipeline_team.pipeline_id";
			ArrayList<Team> teams = new ArrayList<Team>();
			pipelines.setProducts(products);
			ArrayList<HashMap<String, String>> teamDetails = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), teamSql);
			try {
				String teamID = teamDetails.get(0).get("team_id");
				String teamId[] = teamID.split(",");
				for (String id : teamId) {
					Team team = new Team();
					String teamNameSql = "SELECT istar_group.name from istar_group where id = " + id
							+ "  and is_deleted=false;";
					ArrayList<HashMap<String, String>> teamName = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), teamNameSql);
					team.setId(Integer.parseInt(id));
					team.setName(teamName.get(0).get("name"));
					teams.add(team);
				}
				pipelines.setTeams(teams);
			} catch (Exception e) {

			}
			String stageSql = "SELECT pipeline_stage.id as stage_id, pipeline_stage.stage_name,order_id from pipeline_stage where pipeline_stage.pipeline_id = "
					+ pipelineId + " and deleted=false ORDER BY order_id asc;";

			ArrayList<PipelineStage> stages = new ArrayList<PipelineStage>();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), stageSql)) {
				PipelineStage ps = new PipelineStage();
				ps.setId(Integer.parseInt(row.get("stage_id")));
				ps.setName(row.get("stage_name"));
				if (row.get("order_id") != null) {
					ps.setOrderId(Integer.parseInt(row.get("order_id")));
				}
				Integer stageId = Integer.parseInt(row.get("stage_id"));
				String taskSql = "SELECT stage_task.task_type, stage_task.id as task_id from stage_task where stage_id in ( SELECT pipeline_stage.id  from pipeline_stage where pipeline_stage.id = "
						+ stageId + ") and deleted=false;";
				ArrayList<HashMap<String, String>> taskType = DBUtils.getInstance()
						.executeQuery(Thread.currentThread().getStackTrace(), taskSql);
				ArrayList<PipelineStageTask> task = new ArrayList<PipelineStageTask>();
				for (HashMap<String, String> allTask : taskType) {
					PipelineStageTask stageTask = new PipelineStageTask();
					stageTask.setId(Integer.parseInt(allTask.get("task_id")));
					stageTask.setTaskType(allTask.get("task_type"));
					task.add(stageTask);
				}
				ps.setTasks(task);
				stages.add(ps);
			}
			pipelines.setStages(stages);
			String personaSql = "SELECT id, name FROM persona WHERE id in (select persona_id from pipeline_persona where pipeline_id = "
					+ pipelineId + ") and organization_id = " + org_id + " and is_deleted = false ";
			ArrayList<Persona> personas = new ArrayList<Persona>();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), personaSql)) {
				Persona persona = new Persona();
				persona.setId(Integer.parseInt(row.get("id")));
				persona.setName(row.get("name"));
				personas.add(persona);
			}
			pipelines.setPersonas(personas);
			String leadSourceSql = "SELECT lead_source.* FROM lead_source, pipeline_lead_source WHERE lead_source. ID"
					+ " = pipeline_lead_source.lead_source_id AND pipeline_lead_source.pipeline_id = " + pipelineId
					+ " GROUP BY lead_source.id";
			ArrayList<LeadSource> leadSources = new ArrayList<LeadSource>();
			for (HashMap<String, String> rowLeadSource : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), leadSourceSql)) {
				LeadSource leadSource = new LeadSource();
				leadSource.setId(Integer.parseInt(rowLeadSource.get("id")));
				leadSource.setName(rowLeadSource.get("name"));
				leadSource.setImageUrl(rowLeadSource.get("image_url"));
				leadSources.add(leadSource);
			}
			pipelines.setLeadSources(leadSources);

			return pipelines;

		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Pipeline pipelineCreation(Pipeline pipeline, Integer orgId) throws SQLException {

		String pipelineSql = "insert into pipeline (created_at,updated_at,name,organization_id,is_active) values(now(),now(),?,?,true)";
		HashMap<Integer, Object> pipelineData = new HashMap<Integer, Object>();
		String pipelineName = "Unknow Name";
		if (pipeline.getName() != null) {
			pipelineName = pipeline.getName().trim();
		}
		pipelineData.put(1, StringUtils.wordsCapitalize(StringUtils.cleanHTML(pipelineName)));
		pipelineData.put(2, orgId);
		Integer pipelineId = DBUtils.getInstance().updateObject(pipelineSql, pipelineData);
		Integer orderId = 1;
		if (pipeline.getStages() != null) {
			for (PipelineStage stage : pipeline.getStages()) {
				if (stage.getOrderId() != null) {
					String PipelineStageSql = "insert into pipeline_stage (stage_name,created_at,updated_at,pipeline_id,deleted,order_id) values(?,now(),now(),?,false,?)";
					HashMap<Integer, Object> pipelineStageData = new HashMap<Integer, Object>();
					String stageName = null;
					if (stage.getName() != null) {
						stageName = stage.getName().trim();
					}
					pipelineStageData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(stageName)));
					pipelineStageData.put(2, pipelineId);
					pipelineStageData.put(3, orderId);
					Integer stageId = DBUtils.getInstance().updateObject(PipelineStageSql, pipelineStageData);

					for (PipelineStageTask stagetask : stage.getTasks()) {
						String description = stagetask.getDescription();
						if (stagetask.getDescription() == null) {
							description = "No Description for this task";
						}
						String stageTaskSql = "insert into stage_task (task_name,task_type,description,created_at,updated_at,stage_id,deleted) values(?,?,?,now(),now(),?,false)";
						HashMap<Integer, Object> stageTaskData = new HashMap<Integer, Object>();
						String stageTask = null;
						if (stagetask.getName() != null) {
							stageTask = stagetask.getName().trim();
						}
						stageTaskData.put(1, StringUtils.cleanHTML(stageTask));
						stageTaskData.put(2, stagetask.getTaskType());
						stageTaskData.put(3, description);
						stageTaskData.put(4, stageId);
						DBUtils.getInstance().updateObject(stageTaskSql, stageTaskData);
					}
				}
				orderId++;
			}
		}

		if (pipeline.getLeadSourceIds() != null) {
			for (Integer leadSourceId : pipeline.getLeadSourceIds()) {
				String leadSourceSql = "INSERT INTO pipeline_lead_source (pipeline_id, lead_source_id) VALUES (?,?);;";
				HashMap<Integer, Object> leadSourceData = new HashMap<Integer, Object>();
				leadSourceData.put(1, pipelineId);
				leadSourceData.put(2, leadSourceId);
				DBUtils.getInstance().updateObject(leadSourceSql, leadSourceData);
			}
		}
		if (pipeline.getProductIds() != null) {
			for (Integer productId : pipeline.getProductIds()) {
				String productSql = "insert into pipeline_product (pipeline_id,product_id) values(?,?)";
				HashMap<Integer, Object> productData = new HashMap<Integer, Object>();
				productData.put(1, pipelineId);
				productData.put(2, productId);
				DBUtils.getInstance().updateObject(productSql, productData);
			}
		}

		if (pipeline.getTeamIds() != null) {
			for (Integer teamId : pipeline.getTeamIds()) {
				String teamSql = "insert into pipeline_team (pipeline_id,team_id) values(?,?)";
				HashMap<Integer, Object> teamData = new HashMap<Integer, Object>();
				teamData.put(1, pipelineId);
				teamData.put(2, teamId);
				DBUtils.getInstance().updateObject(teamSql, teamData);
			}
		}
		if (pipeline.getPersonaIds() != null) {
			for (Integer personaId : pipeline.getPersonaIds()) {
				String personaSql = "insert into pipeline_persona (pipeline_id,persona_id) values(?,?)";
				HashMap<Integer, Object> personaData = new HashMap<Integer, Object>();
				personaData.put(1, pipelineId);
				personaData.put(2, personaId);
				DBUtils.getInstance().updateObject(personaSql, personaData);
			}
		}
		pipeline = new PipelineDAOPG().findbyId(pipelineId);

		return pipeline;
	}

	@Override
	public ArrayList<Pipeline> getPipelineFromUser(Integer userID) throws SQLException {
		String pipelineSql = "SELECT id, name FROM pipeline where organization_id in (SELECT org_user.organizationid from org_user where org_user.userid = "
				+ userID + ") and is_active = 't' order by id desc";
		ArrayList<HashMap<String, String>> pipelineDetails = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), pipelineSql);
		ArrayList<Pipeline> pipelineView = new ArrayList<Pipeline>();

		System.out.println(pipelineDetails);
		for (HashMap<String, String> pipeline : pipelineDetails) {
			Integer pipelineId = Integer.parseInt(pipeline.get("id"));
			pipelineView.add(findbyId(pipelineId));
		}
		return pipelineView;
	}

	@Override
	public Pipeline pipelineUpdation(Pipeline pipeline) throws SQLException {

		if (pipeline.getName() != null && pipeline.getName().trim().length() > 0) {
			String PipelineSql = "update pipeline set updated_at=now(), name=? where id = ? ";
			HashMap<Integer, Object> pipelineData = new HashMap<Integer, Object>();
			pipelineData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(pipeline.getName()).trim()));
			pipelineData.put(2, pipeline.getId());
			DBUtils.getInstance().updateObject(PipelineSql, pipelineData);
		}
		Integer pipelineId = pipeline.getId();
		Integer orderId = 1;
		if (pipeline.getStages() != null) {
			for (PipelineStage stages : pipeline.getStages()) {
				if (stages.getId() != null) {
					String PipelineStageSql = "update pipeline_stage set stage_name=?,updated_at=now(),order_id=? where id = ?";
					HashMap<Integer, Object> PipelineStageData = new HashMap<Integer, Object>();
					PipelineStageData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(stages.getName()).trim()));
					PipelineStageData.put(2, stages.getId());
					PipelineStageData.put(3, orderId);
					DBUtils.getInstance().updateObject(PipelineStageSql, PipelineStageData);

					for (PipelineStageTask stagetask : stages.getTasks()) {
						String StageTaskSql = "update stage_task set task_name=?,task_type=?,description=?,updated_at=now(),stage_id=? where id = ?";
						HashMap<Integer, Object> StageTaskData = new HashMap<Integer, Object>();
						String stageTask = null;
						if (stagetask.getName() != null) {
							stageTask = stagetask.getName().trim();
						}
						StageTaskData.put(1, StringUtils.cleanHTML(stageTask));
						StageTaskData.put(2, stagetask.getTaskType());
						StageTaskData.put(3, stagetask.getDescription());
						StageTaskData.put(4, stages.getId());
						StageTaskData.put(5, stagetask.getId());
						DBUtils.getInstance().updateObject(StageTaskSql, StageTaskData);

					}
				} else {
					String PipelineStageSql = "insert into pipeline_stage (stage_name,created_at,updated_at,pipeline_id,deleted,order_id) values(?,now(),now(),?,false,?)";
					HashMap<Integer, Object> pipelineStageData = new HashMap<Integer, Object>();
					String stageName = null;
					if (stages.getName() != null) {
						stageName = stages.getName().trim();
					}
					pipelineStageData.put(1, StringUtils.stringCapitalize((StringUtils.cleanHTML(stageName))));
					pipelineStageData.put(2, pipelineId);
					pipelineStageData.put(3, orderId);
					Integer stageId = DBUtils.getInstance().updateObject(PipelineStageSql, pipelineStageData);

					for (PipelineStageTask stagetask : stages.getTasks()) {
						String description = stagetask.getDescription();
						if (stagetask.getDescription() == null) {
							description = "No Description for this task";
						}
						String stageTaskSql = "insert into stage_task (task_name,task_type,description,created_at,updated_at,stage_id,deleted) values(?,?,?,now(),now(),?,false)";
						HashMap<Integer, Object> stageTaskData = new HashMap<Integer, Object>();
						String stageTask = null;
						if (stagetask.getName() != null) {
							stageTask = stagetask.getName().trim();
						}
						stageTaskData.put(1, StringUtils.cleanHTML(stageTask));
						stageTaskData.put(2, stagetask.getTaskType());
						stageTaskData.put(3, description);
						stageTaskData.put(4, stageId);
						DBUtils.getInstance().updateObject(stageTaskSql, stageTaskData);
					}
				}
				orderId++;
			}
		}

		if (pipeline.getProductIds() != null) {
			String productSql = "delete from pipeline_product where pipeline_id = ?";
			HashMap<Integer, Object> productData = new HashMap<Integer, Object>();
			productData.put(1, pipelineId);
			DBUtils.getInstance().updateObject(productSql, productData);
			for (Integer productId : pipeline.getProductIds()) {
				String ProductSql = "insert into pipeline_product (pipeline_id,product_id) values(?,?)";
				HashMap<Integer, Object> ProductData = new HashMap<Integer, Object>();
				ProductData.put(1, pipelineId);
				ProductData.put(2, productId);
				DBUtils.getInstance().updateObject(ProductSql, ProductData);
			}
		}
		if (pipeline.getLeadSourceIds() != null) {
			String leadSourceDel = "delete from pipeline_lead_source where pipeline_id = ?";
			HashMap<Integer, Object> leaddelData = new HashMap<Integer, Object>();
			leaddelData.put(1, pipelineId);
			DBUtils.getInstance().updateObject(leadSourceDel, leaddelData);
			for (Integer leadSourceId : pipeline.getLeadSourceIds()) {
				String leadSourceSql = "INSERT INTO pipeline_lead_source (pipeline_id, lead_source_id) VALUES (?,?);;";
				HashMap<Integer, Object> leadSourceData = new HashMap<Integer, Object>();
				leadSourceData.put(1, pipelineId);
				leadSourceData.put(2, leadSourceId);
				DBUtils.getInstance().updateObject(leadSourceSql, leadSourceData);
			}
		}

		if (pipeline.getTeamIds() != null) {
			String teamSql = "delete from pipeline_team where pipeline_id = ?";
			HashMap<Integer, Object> teamData = new HashMap<Integer, Object>();
			teamData.put(1, pipelineId);
			DBUtils.getInstance().updateObject(teamSql, teamData);

			for (Integer teamId : pipeline.getTeamIds()) {
				String TeamSql = "insert into pipeline_team (pipeline_id,team_id) values(?,?)";
				HashMap<Integer, Object> TeamData = new HashMap<Integer, Object>();
				TeamData.put(1, pipelineId);
				TeamData.put(2, teamId);

				DBUtils.getInstance().updateObject(TeamSql, TeamData);
			}
		}
		if (pipeline.getPersonaIds() != null) {
			String personaSql = "delete from pipeline_persona where pipeline_id = ?";
			HashMap<Integer, Object> personaData = new HashMap<Integer, Object>();
			personaData.put(1, pipelineId);
			DBUtils.getInstance().updateObject(personaSql, personaData);
			for (Integer personaId : pipeline.getPersonaIds()) {
				personaSql = "insert into pipeline_persona (pipeline_id,persona_id) values(?,?)";
				personaData = new HashMap<Integer, Object>();
				personaData.put(1, pipelineId);
				personaData.put(2, personaId);
				DBUtils.getInstance().updateObject(personaSql, personaData);
			}
		}
		return pipeline;
	}

	@Override
	public PipelineStage createpipelinestage(PipelineStage pipelineStage) throws SQLException {
		int orderId = 0;
		String pipelinsStage = "SELECT * from pipeline_stage WHERE pipeline_id=" + pipelineStage.getPipelineId()
				+ " order by order_id desc";
		for (HashMap<String, String> stageRow : DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), pipelinsStage)) {
			if (stageRow.get("order_id") != null) {
				if (Integer.parseInt(stageRow.get("order_id")) > orderId) {
					orderId = Integer.parseInt(stageRow.get("order_id"));
				}
			}
		}
		orderId = orderId + 1;

		String Sql = "INSERT into pipeline_stage (stage_name, created_at , updated_at, pipeline_id, deleted,order_id) VALUES (? , now(), now(), ?,'f',?) ";
		HashMap<Integer, Object> StageData = new HashMap<Integer, Object>();
		StageData.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(pipelineStage.getName())));
		StageData.put(2, pipelineStage.getPipelineId());
		StageData.put(3, orderId);
		Integer stageId = DBUtils.getInstance().updateObject(Sql, StageData);

		if (pipelineStage.getTasks() != null) {
			for (PipelineStageTask stagetask : pipelineStage.getTasks()) {
				String StageTaskSql = "insert into stage_task (task_name,task_type,description,created_at,updated_at,stage_id,deleted) values(?,?,?,now(),now(),?,false)";
				HashMap<Integer, Object> StageTaskData = new HashMap<Integer, Object>();
				StageTaskData.put(1, StringUtils.cleanHTML(stagetask.getName()));
				StageTaskData.put(2, stagetask.getTaskType());
				StageTaskData.put(3, stagetask.getDescription());
				StageTaskData.put(4, stageId);
				DBUtils.getInstance().updateObject(StageTaskSql, StageTaskData);
			}
		}
		pipelineStage = null;
		String sqlStage = "select * from pipeline_stage where id=" + stageId;
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				sqlStage)) {
			pipelineStage = new PipelineStage();
			pipelineStage.setId(Integer.parseInt(row.get("id")));
			pipelineStage.setName(row.get("stage_name"));
			pipelineStage.setPipelineId(Integer.parseInt(row.get("pipeline_id")));
			if (row.get("order_id") != null) {
				pipelineStage.setOrderId(Integer.parseInt(row.get("order_id")));
			}

			String sqlStageTasks = "SELECT * from stage_task WHERE stage_id=" + stageId;
			for (HashMap<String, String> stageTaskRow : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), sqlStageTasks)) {
				PipelineStageTask stageTask = new PipelineStageTask();
				stageTask.setId(Integer.parseInt(stageTaskRow.get("id")));
				stageTask.setName(stageTaskRow.get("task_name"));
				stageTask.setTaskType(stageTaskRow.get("task_type"));
				stageTask.setDescription(stageTaskRow.get("description"));
				pipelineStage.getTasks().add(stageTask);
			}
		}
		return pipelineStage;
	}

	@Override
	public PipelineStageTask createpipelinestagetask(PipelineStageTask pipelineStageTask) throws SQLException {

		String StageTaskSql = "insert into stage_task (task_name,task_type,description,created_at,updated_at,stage_id,deleted) values(?,?,?,now(),now(),?,false)";
		HashMap<Integer, Object> StageTaskData = new HashMap<Integer, Object>();
		StageTaskData.put(1, StringUtils.cleanHTML(pipelineStageTask.getName()));
		StageTaskData.put(2, pipelineStageTask.getTaskType());
		StageTaskData.put(3, pipelineStageTask.getDescription());
		StageTaskData.put(4, pipelineStageTask.getStageId());

		Integer taskID = DBUtils.getInstance().updateObject(StageTaskSql, StageTaskData);
		pipelineStageTask = null;
		String sqlStage = "select * from stage_task where id=" + taskID;
		for (HashMap<String, String> stageTaskRow : DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sqlStage)) {
			pipelineStageTask = new PipelineStageTask();
			pipelineStageTask.setId(Integer.parseInt(stageTaskRow.get("id")));
			pipelineStageTask.setName(stageTaskRow.get("task_name"));
			pipelineStageTask.setTaskType(stageTaskRow.get("task_type"));
			pipelineStageTask.setDescription(stageTaskRow.get("description"));
			pipelineStageTask.setStageId(Integer.parseInt(stageTaskRow.get("stage_id")));
		}

		return pipelineStageTask;
	}

	@Override
	public boolean pipelinedeletion(Integer pipelineId) throws SQLException {

		String Sql = "update pipeline set is_active = false where id = ?";
		HashMap<Integer, Object> pipelinedata = new HashMap<Integer, Object>();
		pipelinedata.put(1, pipelineId);
		DBUtils.getInstance().updateObject(Sql, pipelinedata);

		Sql = "update pipeline_stage set deleted = true where pipeline_id = ?";
		HashMap<Integer, Object> stageData = new HashMap<Integer, Object>();
		stageData.put(1, pipelineId);
		DBUtils.getInstance().updateObject(Sql, stageData);

		Sql = "update stage_task set deleted = true where stage_id in (select id from pipeline_stage where pipeline_id = ?)";
		HashMap<Integer, Object> taskData = new HashMap<Integer, Object>();
		taskData.put(1, pipelineId);
		DBUtils.getInstance().updateObject(Sql, taskData);

		String productSql = "delete from pipeline_product where pipeline_id = ?";
		HashMap<Integer, Object> productData = new HashMap<Integer, Object>();
		productData.put(1, pipelineId);
		DBUtils.getInstance().updateObject(productSql, productData);

		String teamSql = "delete from pipeline_team where pipeline_id = ?";
		HashMap<Integer, Object> teamData = new HashMap<Integer, Object>();
		teamData.put(1, pipelineId);
		DBUtils.getInstance().updateObject(teamSql, teamData);

		String personaSql = "delete from pipeline_persona where pipeline_id = ?";
		HashMap<Integer, Object> personaData = new HashMap<Integer, Object>();
		personaData.put(1, pipelineId);
		DBUtils.getInstance().updateObject(personaSql, personaData);

		String leadSourceDel = "delete from pipeline_lead_source where pipeline_id = ?";
		HashMap<Integer, Object> leaddelData = new HashMap<Integer, Object>();
		leaddelData.put(1, pipelineId);
		DBUtils.getInstance().updateObject(leadSourceDel, leaddelData);

		return true;
	}

	@Override
	public boolean stagedeletion(Integer stageId) throws SQLException {

		String Sql = "update pipeline_stage set deleted = true where id = ?";

		HashMap<Integer, Object> stageData = new HashMap<Integer, Object>();
		stageData.put(1, stageId);
		DBUtils.getInstance().updateObject(Sql, stageData);

		Sql = "update stage_task set deleted = true where stage_id = ?";
		HashMap<Integer, Object> taskData = new HashMap<Integer, Object>();
		taskData.put(1, stageId);
		DBUtils.getInstance().updateObject(Sql, taskData);
		return true;

	}

	@Override
	public boolean taskdeletion(Integer taskId) throws SQLException {

		String Sql = "update stage_task set deleted = true where id = ?";
		HashMap<Integer, Object> taskData = new HashMap<Integer, Object>();
		taskData.put(1, taskId);
		DBUtils.getInstance().updateObject(Sql, taskData);
		return true;
	}

	@Override
	public boolean addTeamPipeline(Integer teamId, Integer pipelineId) throws SQLException {
		String sql = "INSERT into pipeline_team (pipeline_id, team_id) VALUES (?,?)";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, pipelineId);
		data.put(2, teamId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

	@Override
	public boolean deleteTeamPipeline(Integer teamId, Integer pipelineId) throws SQLException {
		String sql = "delete from pipeline_team where pipeline_id = ? and team_id = ?";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, pipelineId);
		data.put(2, teamId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

	@Override
	public boolean addProductPipeline(Integer productId, Integer pipelineId) throws SQLException {
		String sql = "INSERT into pipeline_product (pipeline_id, product_id) VALUES (?, ?)";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, pipelineId);
		data.put(2, productId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

	@Override
	public boolean deleteProductPipeline(Integer productId, Integer pipelineId) throws SQLException {
		String sql = "delete from pipeline_product where pipeline_id = ? and product_id = ?";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, pipelineId);
		data.put(2, productId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

	@Override
	public boolean addPersonaPipeline(Integer personaId, Integer pipelineId) throws SQLException {
		String sql = "INSERT into pipeline_persona (pipeline_id, persona_id) VALUES (?, ?)";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, pipelineId);
		data.put(2, personaId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

	@Override
	public boolean deletePersonaPipeline(Integer personaId, Integer pipelineId) throws SQLException {
		String sql = "delete from pipeline_persona where pipeline_id = ? and persona_id = ?";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, pipelineId);
		data.put(2, personaId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

	@Override
	public PipelineStage updatePipelineStage(PipelineStage pipelineStage) throws SQLException {
		String sql = "UPDATE pipeline_stage set stage_name = ? where id = ? and pipeline_id = ?";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, StringUtils.stringCapitalize(StringUtils.cleanHTML(pipelineStage.getName())));

		data.put(2, pipelineStage.getId());
		data.put(3, pipelineStage.getPipelineId());
		DBUtils.getInstance().updateObject(sql, data);
		if (pipelineStage.getTasks() != null) {
			for (PipelineStageTask stageTask : pipelineStage.getTasks()) {

				String taskSql = "UPDATE stage_task set  task_name = ?, task_type = ?,description = ? WHERE stage_id = ?  and id = ?";
				HashMap<Integer, Object> taskData = new HashMap<Integer, Object>();
				taskData.put(1, StringUtils.cleanHTML(stageTask.getName()));
				taskData.put(2, stageTask.getTaskType());
				taskData.put(3, stageTask.getDescription());
				taskData.put(4, pipelineStage.getId());
				taskData.put(5, stageTask.getId());
				DBUtils.getInstance().updateObject(taskSql, taskData);
			}
		}
		return pipelineStage;
	}

	@Override
	public PipelineStageTask updatePipelineStageTask(PipelineStageTask pipelineStageTask) throws SQLException {
		String taskSql = "UPDATE stage_task set  task_name = ?, task_type = ?,description = ? WHERE id = ?";
		HashMap<Integer, Object> taskData = new HashMap<Integer, Object>();
		taskData.put(1, StringUtils.cleanHTML(pipelineStageTask.getName()));
		taskData.put(2, pipelineStageTask.getTaskType());
		taskData.put(3, pipelineStageTask.getDescription());
		taskData.put(4, pipelineStageTask.getId());
		DBUtils.getInstance().updateObject(taskSql, taskData);
		return pipelineStageTask;
	}

	@Override
	public PipelineFields getPipelineCreationFields(User user) throws SQLException {

		PipelineFields pipelineFields = new PipelineFields();
		OrganizationDAO orgDAO = new OrganizationDAOPG();
		Organization organization = orgDAO.findOrganizationByUserId(user.getId());

		TeamDAO dao = new TeamDAOPG();
		ArrayList<Team> teams = new ArrayList<Team>();
		for (Team team : dao.findbyOrganizationId(organization.getId())) {
			team.setUserIds(null);
			team.setProducts(null);
			team.setOwner(null);
			team.setPersonas(null);
			teams.add(team);
		}
		ArrayList<PipelineStage> pipelineStages = new ArrayList<PipelineStage>();

		String defaultStageSql = "SELECT * from pipeline_stage WHERE pipeline_id is null and DELETEd=false;";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				defaultStageSql)) {
			PipelineStage pipelineStage = new PipelineStage();
			pipelineStage.setId(Integer.parseInt(row.get("id")));
			pipelineStage.setName(row.get("stage_name"));
			if (row.get("order_id") != null) {
				pipelineStage.setOrderId(Integer.parseInt(row.get("order_id")));
			}
			pipelineStages.add(pipelineStage);
		}

		ArrayList<LeadSource> leadSources = new ArrayList<LeadSource>();

		String defaultLeadSourceSql = "SELECT * from lead_source;";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				defaultLeadSourceSql)) {
			LeadSource leadSource = new LeadSource();
			leadSource.setId(Integer.parseInt(row.get("id")));
			leadSource.setName(row.get("name"));
			leadSource.setImageUrl(row.get("image_url"));
			leadSources.add(leadSource);
		}
		pipelineFields.setLeadSources(leadSources);
		ProductDAO productDAO = new ProductDAOPG();
		ArrayList<Product> products = productDAO.viewProducts(user);
		ArrayList<Persona> personas = new PersonaDAOPG().viewPersonaByUserId(user.getId());
		pipelineFields.setStageDropdown(pipelineStages);
		pipelineFields.setPersonas(personas);
		pipelineFields.setTeams(teams);
		pipelineFields.setProducts(products);
		return pipelineFields;
	}

	@Override
	public ArrayList<LeadSource> viewAllLeadSource(User user) throws SQLException {
		ArrayList<LeadSource> leadSources = new ArrayList<LeadSource>();
		String defaultLeadSourceSql = "SELECT * from lead_source order by id;";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				defaultLeadSourceSql)) {
			LeadSource leadSource = new LeadSource();
			leadSource.setId(Integer.parseInt(row.get("id")));
			leadSource.setName(row.get("name"));
			leadSource.setImageUrl(row.get("image_url"));
			leadSources.add(leadSource);
		}

		return leadSources;
	}

	@Override
	public PipelineStage viewPipelineStage(Integer stageId) throws SQLException {
		PipelineStage pipelineStage = null;
		String defaultLeadSourceSql = "SELECT pipeline_stage. ID AS stage_id, pipeline_stage.stage_name,pipeline_stage.order_id, stage_task. ID stage_task_id, stage_task.task_type, stage_task.description FROM pipeline_stage LEFT JOIN stage_task ON pipeline_stage. ID = stage_task.stage_id AND stage_task.deleted = FALSE WHERE "
				+ "pipeline_stage. ID = " + stageId + "  ORDER BY stage_task_id;";

		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				defaultLeadSourceSql)) {
			if (pipelineStage == null) {
				pipelineStage = new PipelineStage();
				pipelineStage.setId(Integer.parseInt(row.get("stage_id")));
				pipelineStage.setName(row.get("stage_name"));
				if (row.get("order_id") != null) {
					pipelineStage.setOrderId(Integer.parseInt(row.get("order_id")));
				}
			}
			if (row.get("stage_task_id") != null) {
				PipelineStageTask stageTask = new PipelineStageTask();
				stageTask.setId(Integer.parseInt(row.get("stage_task_id")));
				stageTask.setTaskType(row.get("task_type"));
				stageTask.setDescription(row.get("description"));
				pipelineStage.getTasks().add(stageTask);
			}
		}
		return pipelineStage;
	}

	@Override
	public Pipeline pipelineDetails(Integer pipelineId) throws SQLException {
		String pipelineSql = "SELECT pipeline.name from pipeline where id = " + pipelineId + " and is_active='t'";
		ArrayList<HashMap<String, String>> pipeline = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), pipelineSql);
		Pipeline pipelines = new Pipeline();
		try {
			pipelines.setId(pipelineId);
			pipelines.setName(pipeline.get(0).get("name"));
			String productSql = "SELECT name as product_name, product_id  FROM pipeline_product LEFT JOIN product on pipeline_product.product_id = product.id where pipeline_id = "
					+ pipelineId + "  and product.deleted=FALSE ";
			ArrayList<Product> products = new ArrayList<Product>();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), productSql)) {
				Product product = new Product();
				product.setId(Integer.parseInt(row.get("product_id")));
				product.setName(row.get("product_name"));
				products.add(product);
			}
			String teamSql = "SELECT  string_agg(pipeline_team.team_id :: CHARACTER VARYING, ',') as team_id from pipeline_team where pipeline_team.pipeline_id = "
					+ pipelineId + " GROUP BY pipeline_team.pipeline_id";
			ArrayList<Team> teams = new ArrayList<Team>();
			pipelines.setProducts(products);
			ArrayList<HashMap<String, String>> teamDetails = DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), teamSql);
			try {
				String teamID = teamDetails.get(0).get("team_id");
				String teamId[] = teamID.split(",");
				for (String id : teamId) {
					Team team = new Team();
					String teamNameSql = "SELECT istar_group.name from istar_group where id = " + id
							+ "  and is_deleted=false;";
					ArrayList<HashMap<String, String>> teamName = DBUtils.getInstance()
							.executeQuery(Thread.currentThread().getStackTrace(), teamNameSql);
					team.setId(Integer.parseInt(id));
					team.setName(teamName.get(0).get("name"));
					teams.add(team);
				}
				pipelines.setTeams(teams);
			} catch (Exception e) {

			}
			String stageSql = "SELECT pipeline_stage.id as stage_id, pipeline_stage.stage_name,pipeline_stage.order_id from pipeline_stage "
					+ "where pipeline_stage.pipeline_id = " + pipelineId + " and deleted=false ORDER BY order_id asc;";
			ArrayList<PipelineStage> stageDropDown = new ArrayList<PipelineStage>();

			ArrayList<PipelineStage> stages = new ArrayList<PipelineStage>();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), stageSql)) {
				PipelineStage psForDropDown = new PipelineStage();
				psForDropDown.setId(Integer.parseInt(row.get("stage_id")));
				psForDropDown.setName(row.get("stage_name"));
				stageDropDown.add(psForDropDown);
				PipelineStage ps = new PipelineStage();
				ps.setId(Integer.parseInt(row.get("stage_id")));
				ps.setName(row.get("stage_name"));
				if (row.get("order_id") != null)
					ps.setOrderId(Integer.parseInt(row.get("order_id")));
				Integer stageId = Integer.parseInt(row.get("stage_id"));
				String taskSql = "SELECT stage_task.task_type, stage_task.id as task_id from stage_task where stage_id in ( SELECT pipeline_stage.id  from pipeline_stage where pipeline_stage.id = "
						+ stageId + ") and deleted=false;";
				ArrayList<HashMap<String, String>> taskType = DBUtils.getInstance()
						.executeQuery(Thread.currentThread().getStackTrace(), taskSql);
				ArrayList<PipelineStageTask> task = new ArrayList<PipelineStageTask>();
				for (HashMap<String, String> allTask : taskType) {
					PipelineStageTask stageTask = new PipelineStageTask();
					stageTask.setId(Integer.parseInt(allTask.get("task_id")));
					stageTask.setTaskType(allTask.get("task_type"));
					task.add(stageTask);
				}
				ps.setTasks(task);
				stages.add(ps);
			}
			pipelines.setStages(stages);

			String defaultStageSql = "SELECT * from pipeline_stage WHERE pipeline_id is null and DELETEd=false;";
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), defaultStageSql)) {
				boolean isAlreadyAdded = false;
				for (PipelineStage ps : stageDropDown) {
					if (ps.getName().equalsIgnoreCase(row.get("stage_name"))) {
						System.out.println("match");
						isAlreadyAdded = true;
					}
				}
				if (!isAlreadyAdded) {
					PipelineStage pipelineStage = new PipelineStage();
					pipelineStage.setId(Integer.parseInt(row.get("id")));
					pipelineStage.setName(row.get("stage_name"));
					stageDropDown.add(pipelineStage);
				}

			}

			pipelines.setStageDropdown(stageDropDown);

			String personaSql = "SELECT persona_id, name  from pipeline_persona LEFT JOIN persona ON persona.id = pipeline_persona.persona_id WHERE pipeline_id = "
					+ pipelineId + " and persona.is_deleted = 'f'";
			ArrayList<Persona> personas = new ArrayList<Persona>();
			for (HashMap<String, String> row : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), personaSql)) {
				Persona persona = new Persona();
				persona.setId(Integer.parseInt(row.get("persona_id")));
				persona.setName(row.get("name"));
				personas.add(persona);
			}
			pipelines.setPersonas(personas);
			String leadSourceSql = "SELECT lead_source.* FROM lead_source, pipeline_lead_source WHERE lead_source. ID"
					+ " = pipeline_lead_source.lead_source_id AND pipeline_lead_source.pipeline_id = " + pipelineId
					+ " GROUP BY lead_source.id";
			ArrayList<LeadSource> leadSources = new ArrayList<LeadSource>();
			for (HashMap<String, String> rowLeadSource : DBUtils.getInstance()
					.executeQuery(Thread.currentThread().getStackTrace(), leadSourceSql)) {
				LeadSource leadSource = new LeadSource();
				leadSource.setId(Integer.parseInt(rowLeadSource.get("id")));
				leadSource.setName(rowLeadSource.get("name"));
				leadSource.setImageUrl(rowLeadSource.get("image_url"));
				leadSources.add(leadSource);
			}
			pipelines.setLeadSources(leadSources);

			return pipelines;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Pipeline pipelineStageOrderUpdation(Pipeline pipeline) throws SQLException {
		String updateSql = "UPDATE pipeline_stage SET order_id=null WHERE pipeline_id=?";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, pipeline.getId());
		DBUtils.getInstance().updateObject(updateSql, data);
		Integer orderId = 1;
		for (PipelineStage pipelineStage : pipeline.getStages()) {
			if (pipelineStage.getId() != null && pipelineStage.getOrderId() != null) {
				String sql = "UPDATE pipeline_stage SET order_id=? WHERE id=?";
				HashMap<Integer, Object> orderData = new HashMap<Integer, Object>();
				orderData.put(1, orderId);
				orderData.put(2, pipelineStage.getId());
				DBUtils.getInstance().updateObject(sql, orderData);
				++orderId;
			}

		}
		String pipelineStage = "SELECT * from pipeline_stage WHERE pipeline_id=" + pipeline.getId()
				+ " and order_id is null ";
		for (HashMap<String, String> row : DBUtils.getInstance().executeQuery(Thread.currentThread().getStackTrace(),
				pipelineStage)) {
			String sql = "UPDATE pipeline_stage SET order_id=? WHERE id=?";
			HashMap<Integer, Object> orderData = new HashMap<Integer, Object>();
			orderData.put(1, orderId);
			orderData.put(2, Integer.parseInt(row.get("id")));
			DBUtils.getInstance().updateObject(sql, orderData);
			++orderId;
		}
		pipeline = findbyId(pipeline.getId());
		return pipeline;
	}

	@Override
	public Boolean addLeadSourcePipeline(Integer leadSourceId, Integer pipelineId) throws SQLException {
		String sql = "SELECT * from pipeline_lead_source WHERE pipeline_lead_source.pipeline_id=" + pipelineId
				+ " and pipeline_lead_source.lead_source_id=" + leadSourceId;
		ArrayList<HashMap<String, String>> result = DBUtils.getInstance()
				.executeQuery(Thread.currentThread().getStackTrace(), sql);
		if (result.size() == 0) {
			String leadSourceSql = "INSERT INTO pipeline_lead_source (pipeline_id, lead_source_id) VALUES (?,?);;";
			HashMap<Integer, Object> leadSourceData = new HashMap<Integer, Object>();
			leadSourceData.put(1, pipelineId);
			leadSourceData.put(2, leadSourceId);
			DBUtils.getInstance().updateObject(leadSourceSql, leadSourceData);
		}
		return true;
	}

	@Override
	public Boolean deleteLeadSourcePipeline(Integer leadSourceId, Integer pipelineId) throws SQLException {
		String sql = "DELETE from pipeline_lead_source WHERE pipeline_id=? and lead_source_id=?";
		HashMap<Integer, Object> data = new HashMap<Integer, Object>();
		data.put(1, pipelineId);
		data.put(2, leadSourceId);
		DBUtils.getInstance().updateObject(sql, data);
		return true;
	}

}
