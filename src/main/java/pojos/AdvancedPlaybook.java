package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class AdvancedPlaybook {
	private Integer id;
	private Integer stageTaskId;
	private ArrayList<AdvancedPlaybookLevel> levels=new ArrayList<AdvancedPlaybookLevel>();
	public AdvancedPlaybook() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStageTaskId() {
		return stageTaskId;
	}
	public void setStageTaskId(Integer stageTaskId) {
		this.stageTaskId = stageTaskId;
	}
	public ArrayList<AdvancedPlaybookLevel> getLevels() {
		return levels;
	}
	public void setLevels(ArrayList<AdvancedPlaybookLevel> levels) {
		this.levels = levels;
	}
	 


}
