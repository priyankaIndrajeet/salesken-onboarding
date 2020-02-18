package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)

public class AdvancedPlaybookNodeHolder {
	private Integer dimentionsId;
	private String dimentionName;
	private ArrayList<AdvancedPlaybookNode> advancedPlaybookNodes = new ArrayList<AdvancedPlaybookNode>();

	public AdvancedPlaybookNodeHolder() {
		super();
	}

	public Integer getDimentionsId() {
		return dimentionsId;
	}

	public void setDimentionsId(Integer dimentionsId) {
		this.dimentionsId = dimentionsId;
	}

	public String getDimentionName() {
		return dimentionName;
	}

	public void setDimentionName(String dimentionName) {
		this.dimentionName = dimentionName;
	}

	public ArrayList<AdvancedPlaybookNode> getAdvancedPlaybookNodes() {
		return advancedPlaybookNodes;
	}

	public void setAdvancedPlaybookNodes(ArrayList<AdvancedPlaybookNode> advancedPlaybookNodes) {
		this.advancedPlaybookNodes = advancedPlaybookNodes;
	}

}
