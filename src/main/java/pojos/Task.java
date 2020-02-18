package pojos;

public class Task {
	private Integer id;
	private String name;
	private String description;
	private Integer owner;
	private String actorName;
	private Integer actor;
	private String profileImage;
	private String agentNumber;
	private String status;
	private String startDate;
	private String endDate;
	private Boolean isActive;
	private String createdAt;
	private String updatedAt;
	private String taskType;
	private Integer leadId;
	private String companyName;
	private Integer callDuration;
	private Float score;
	private String analytics;
	private Float callRating;
	private Integer salesContactId;
	private String contactPersonName;
	private String contactPersonNumber;
	private String duration;
	private Integer pipelineId;
	private Integer stageId;
	private String stageName;
	private Float voiceQuality;
	private Float talkRatio;
	private Float sentiment;
	private Float specialScore;
	private String disposition;
	private String callsid;
	private String direction;
	private Float cost;
	private String pipelineName;

	public Task() {
		super();
	}

	public Task(Integer id, String name, String description, Integer owner, Integer actor, String status,
			String startDate, String endDate, Boolean isActive, String createdAt, String updatedAt, String taskType,
			Integer leadId, Integer callDuration, Float score, String analytics, Float callRating,
			Integer salesContactId, Integer pipelineId, Integer stageId, Float voiceQuality, Float talkRatio,
			Float sentiment, Float specialScore, String disposition, String callsid, String direction, Float cost) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.actor = actor;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.taskType = taskType;
		this.leadId = leadId;
		this.callDuration = callDuration;
		this.score = score;
		this.analytics = analytics;
		this.callRating = callRating;
		this.salesContactId = salesContactId;
		this.pipelineId = pipelineId;
		this.stageId = stageId;
		this.voiceQuality = voiceQuality;
		this.talkRatio = talkRatio;
		this.sentiment = sentiment;
		this.specialScore = specialScore;
		this.disposition = disposition;
		this.callsid = callsid;
		this.direction = direction;
		this.cost = cost;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public Integer getActor() {
		return actor;
	}

	public void setActor(Integer actor) {
		this.actor = actor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Integer getLeadId() {
		return leadId;
	}

	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}

	public Integer getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(Integer callDuration) {
		this.callDuration = callDuration;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getAnalytics() {
		return analytics;
	}

	public void setAnalytics(String analytics) {
		this.analytics = analytics;
	}

	public Float getCallRating() {
		return callRating;
	}

	public void setCallRating(Float callRating) {
		this.callRating = callRating;
	}

	public Integer getSalesContactId() {
		return salesContactId;
	}

	public void setSalesContactId(Integer salesContactId) {
		this.salesContactId = salesContactId;
	}

	public Integer getPipelineId() {
		return pipelineId;
	}

	public void setPipelineId(Integer pipelineId) {
		this.pipelineId = pipelineId;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public Float getVoiceQuality() {
		return voiceQuality;
	}

	public void setVoiceQuality(Float voiceQuality) {
		this.voiceQuality = voiceQuality;
	}

	public Float getTalkRatio() {
		return talkRatio;
	}

	public void setTalkRatio(Float talkRatio) {
		this.talkRatio = talkRatio;
	}

	public Float getSentiment() {
		return sentiment;
	}

	public void setSentiment(Float sentiment) {
		this.sentiment = sentiment;
	}

	public Float getSpecialScore() {
		return specialScore;
	}

	public void setSpecialScore(Float specialScore) {
		this.specialScore = specialScore;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getCallsid() {
		return callsid;
	}

	public void setCallsid(String callsid) {
		this.callsid = callsid;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public String getAgentNumber() {
		return agentNumber;
	}

	public void setAgentNumber(String agentNumber) {
		this.agentNumber = agentNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonNumber() {
		return contactPersonNumber;
	}

	public void setContactPersonNumber(String contactPersonNumber) {
		this.contactPersonNumber = contactPersonNumber;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getPipelineName() {
		return pipelineName;
	}

	public void setPipelineName(String pipelineName) {
		this.pipelineName = pipelineName;
	}

}
