package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class SimplePlaybook {
	private Integer id;
	private Integer organizationId;
	private ArrayList<SimplePlaybookNode> agentNodes;
	private ArrayList<SimplePlaybookNode> customerNodes;

	public SimplePlaybook() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public ArrayList<SimplePlaybookNode> getAgentNodes() {
		return agentNodes;
	}

	public void setAgentNodes(ArrayList<SimplePlaybookNode> agentNodes) {
		this.agentNodes = agentNodes;
	}

	public ArrayList<SimplePlaybookNode> getCustomerNodes() {
		return customerNodes;
	}

	public void setCustomerNodes(ArrayList<SimplePlaybookNode> customerNodes) {
		this.customerNodes = customerNodes;
	}

}
