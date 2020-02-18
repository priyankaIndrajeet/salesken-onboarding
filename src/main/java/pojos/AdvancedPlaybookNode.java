package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AdvancedPlaybookNode {
	private Integer id;
	private Integer playbookId;
	private String snippetText;
	private String speaker;
	private Integer dimensionId;// This is for creation
	private Integer level;
	private Integer stageTaskId;
	private ArrayList<Integer> childIds = new ArrayList<Integer>();

	public AdvancedPlaybookNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSnippetText() {
		return snippetText;
	}

	public void setSnippetText(String snippetText) {
		this.snippetText = snippetText;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public Integer getDimensionId() {
		return dimensionId;
	}

	public void setDimensionId(Integer dimensionId) {
		this.dimensionId = dimensionId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public ArrayList<Integer> getChildIds() {
		return childIds;
	}

	public void setChildIds(ArrayList<Integer> childIds) {
		this.childIds = childIds;
	}

	public Integer getStageTaskId() {
		return stageTaskId;
	}

	public void setStageTaskId(Integer stageTaskId) {
		this.stageTaskId = stageTaskId;
	}

	public Integer getPlaybookId() {
		return playbookId;
	}

	public void setPlaybookId(Integer playbookId) {
		this.playbookId = playbookId;
	}
}
