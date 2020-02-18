package services.onboarding;

import javax.ws.rs.core.Response;

import pojos.LeadSource;
import pojos.Pipeline;
import pojos.PipelineStage;
import pojos.PipelineStageTask;

public interface PipelineService {
	public Response view();

	public Response create(Pipeline pipeline);

	public Response update(Pipeline pipeline);

	public Response createStage(PipelineStage pipelineStage);

	public Response createStageTask(PipelineStageTask pipelineStageTask);

	public Response pipelinedeletion(Integer pipelineId);

	public Response stageDeletion(Integer stageId);

	public Response taskdeletion(Integer taskId);

	public Response viewPipeline(Integer pipelineId);

	public Response addTeamPipeline(Integer teamId, Integer pipelineId);

	public Response deleteTeamPipeline(Integer teamId, Integer pipelineId);

	public Response addProductPipeline(Integer productId, Integer pipelineId);

	public Response deleteProductPipeline(Integer productId, Integer pipelineId);

	public Response addPersonaPipeline(Integer personaId, Integer pipelineId);

	public Response deletePersonaPipeline(Integer personaId, Integer pipelineId);

	public Response updateStage(PipelineStage pipelineStage);

	public Response updateStageTask(PipelineStageTask pipelineStageTask);

	public Response creationFields();

	public Response updateOrderPipelineStages(Pipeline pipeline);

 
	public Response addLeadSourcePipeline(Integer leadSourceID, Integer pipelineId);

	public Response deleteLeadSourcePipeline(Integer leadSourceId, Integer pipelineId);

	public Response viewLeadSource();

	public Response viewPipelineStageByid(Integer stageId);
}
