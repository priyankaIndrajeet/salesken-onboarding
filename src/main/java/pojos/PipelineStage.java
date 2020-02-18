package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PipelineStage {
	private Integer id;
	private Integer pipelineId;
	private String name;
	private Integer orderId;
	private ArrayList<PipelineStageTask> tasks = new ArrayList<PipelineStageTask>();
	private Boolean isDeleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<PipelineStageTask> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<PipelineStageTask> tasks) {
		this.tasks = tasks;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getPipelineId() {
		return pipelineId;
	}

	public void setPipelineId(Integer pipelineId) {
		this.pipelineId = pipelineId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
