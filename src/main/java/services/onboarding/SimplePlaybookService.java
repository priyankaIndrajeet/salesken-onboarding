package services.onboarding;

import javax.ws.rs.core.Response;

import pojos.SimplePlaybookNode;

public interface SimplePlaybookService {
	public Response viewByUserId();

	public Response addNodeInSimpleplaybook(SimplePlaybookNode simplePlaybookNode);

	public Response updateNodeInSimpleplaybook(SimplePlaybookNode simplePlaybookNode);

	public Response deleteNodeInSimpleplaybook(Integer nodeId);
}
