package services.onboarding;

import javax.ws.rs.core.Response;
import pojos.AdvancedPlaybookNode;

public interface PlaybookService {

	public Response viewStageTaskActivity(Integer stageTaskId);

	public Response viewAllDimension();

	public Response createAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybook);

	public Response updateAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybook);

	public Response deleteAdvancedPlaybookNode(Integer playbookSnippetId);

	public Response addMappingInAdvancedPlaybookNode(AdvancedPlaybookNode advancedPlaybook);

	public Response updateDimension(AdvancedPlaybookNode advancedPlaybook);

	public Response deletePlaybookOfStage(Integer stageTaskId);

	public Response viewAllChildsByNodeId(Integer nodeId);

	public Response isSaleskenSuggest();

	public Response addLevel(AdvancedPlaybookNode advancedPlaybookNode);

}
