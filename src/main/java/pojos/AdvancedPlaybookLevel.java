package pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AdvancedPlaybookLevel {
	private Integer level;
	//private Integer dimensionId;
	private String dimensionName;
	private AdvancedPlaybookNodeHolder agentNodeHolder;
	private AdvancedPlaybookNodeHolder customerNodeHolder;

 	public AdvancedPlaybookLevel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	 
	 
	public String getDimensionName() {
		return dimensionName;
	}
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}
	public AdvancedPlaybookNodeHolder getAgentNodeHolder() {
		return agentNodeHolder;
	}
	public void setAgentNodeHolder(AdvancedPlaybookNodeHolder agentNodeHolder) {
		this.agentNodeHolder = agentNodeHolder;
	}
	public AdvancedPlaybookNodeHolder getCustomerNodeHolder() {
		return customerNodeHolder;
	}
	public void setCustomerNodeHolder(AdvancedPlaybookNodeHolder customerNodeHolder) {
		this.customerNodeHolder = customerNodeHolder;
	}
	 
	
}
