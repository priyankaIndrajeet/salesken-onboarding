/**
 * 
 */
package pojos;

/**
 * @author Vaibhav Verma
 *
 */
public class Disposition {
	private String dispositionType;
	private Integer taskId;
	private Float callRating;
	private String notes;
	private Integer productId;
	private Boolean isFollowUp;
	private String followupTaskType;
	private String followupDate;
	private String followupTime;
	private Integer followpActor;
	private String leadStatus;

	public Disposition() {
		super();
	}

	public String getDispositionType() {
		return dispositionType;
	}

	public void setDispositionType(String dispositionType) {
		this.dispositionType = dispositionType;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Float getCallRating() {
		return callRating;
	}

	public void setCallRating(Float callRating) {
		this.callRating = callRating;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getFollowupTaskType() {
		return followupTaskType;
	}

	public void setFollowupTaskType(String followupTaskType) {
		this.followupTaskType = followupTaskType;
	}

	public String getFollowupDate() {
		return followupDate;
	}

	public void setFollowupDate(String followupDate) {
		this.followupDate = followupDate;
	}

	public String getFollowupTime() {
		return followupTime;
	}

	public void setFollowupTime(String followupTime) {
		this.followupTime = followupTime;
	}

	public Integer getFollowpActor() {
		return followpActor;
	}

	public void setFollowpActor(Integer followpActor) {
		this.followpActor = followpActor;
	}

	public String getLeadStatus() {
		return leadStatus;
	}

	public void setLeadStatus(String leadStatus) {
		this.leadStatus = leadStatus;
	}

	public Boolean getIsFollowUp() {
		return isFollowUp;
	}

	public void setIsFollowUp(Boolean isFollowUp) {
		this.isFollowUp = isFollowUp;
	}

	public enum DispositionType {
		NoResponse, VoiceMail, Dropped, WrongNumber, NotDisposed, WrongPerson, CallAnswered
	}
}
