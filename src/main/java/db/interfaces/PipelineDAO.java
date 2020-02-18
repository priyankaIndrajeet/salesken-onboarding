package db.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import pojos.LeadSource;
import pojos.Pipeline;
import pojos.PipelineFields;
import pojos.PipelineStage;
import pojos.PipelineStageTask;
import pojos.User;

public interface PipelineDAO {
	public ArrayList<Pipeline> getPipelineFromUser(Integer userID) throws SQLException;

	public Pipeline pipelineCreation(Pipeline pipeline, Integer id) throws SQLException;

	public Pipeline pipelineUpdation(Pipeline pipeline) throws SQLException;

	public PipelineStage createpipelinestage(PipelineStage pipelineStage) throws SQLException;

	public PipelineStageTask createpipelinestagetask(PipelineStageTask pipelineStageTask) throws SQLException;

	public boolean pipelinedeletion(Integer pipelineId) throws SQLException;

	public boolean stagedeletion(Integer stageId) throws SQLException;

	public boolean taskdeletion(Integer taskId) throws SQLException;

	public Pipeline findbyId(Integer pipelineId) throws SQLException;

	public boolean addTeamPipeline(Integer teamId, Integer pipelineId) throws SQLException;

	public boolean deleteTeamPipeline(Integer teamId, Integer pipelineId) throws SQLException;

	public boolean addProductPipeline(Integer productId, Integer pipelineId) throws SQLException;

	public boolean deleteProductPipeline(Integer productId, Integer pipelineId) throws SQLException;

	public boolean addPersonaPipeline(Integer personaId, Integer pipelineId) throws SQLException;

	public boolean deletePersonaPipeline(Integer personaId, Integer pipelineId) throws SQLException;

	public PipelineStage updatePipelineStage(PipelineStage pipelineStage) throws SQLException;

	public PipelineStageTask updatePipelineStageTask(PipelineStageTask pipelineStageTask) throws SQLException;

	public PipelineFields getPipelineCreationFields(User user) throws SQLException;

	public Pipeline pipelineDetails(Integer pipelineId) throws SQLException;

	public Pipeline pipelineStageOrderUpdation(Pipeline pipeline) throws SQLException;

	public Boolean addLeadSourcePipeline(Integer teamId, Integer pipelineId) throws SQLException;

	public Boolean deleteLeadSourcePipeline(Integer leadSourceId, Integer pipelineId) throws SQLException;

	public ArrayList<LeadSource> viewAllLeadSource(User user) throws SQLException;

	public PipelineStage viewPipelineStage(Integer stageId) throws SQLException;

 	
}
