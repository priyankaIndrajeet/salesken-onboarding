package pojos;

public class Dashboard {
	Boolean organization;
	Boolean user;
	Boolean team;
	Boolean product;
	Boolean pipeline;
	Boolean persona;
	Boolean simplePlaybook;
	Boolean advancedPlaybook;
	Boolean isWizardDone;
	float overAllpercentage;

	public Dashboard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Boolean getOrganization() {
		return organization;
	}

	public void setOrganization(Boolean organization) {
		this.organization = organization;
	}

	public Boolean getUser() {
		return user;
	}

	public void setUser(Boolean user) {
		this.user = user;
	}

	public Boolean getTeam() {
		return team;
	}

	public void setTeam(Boolean team) {
		this.team = team;
	}

	public Boolean getProduct() {
		return product;
	}

	public void setProduct(Boolean product) {
		this.product = product;
	}

	public Boolean getPipeline() {
		return pipeline;
	}

	public void setPipeline(Boolean pipeline) {
		this.pipeline = pipeline;
	}

	public Boolean getPersona() {
		return persona;
	}

	public void setPersona(Boolean persona) {
		this.persona = persona;
	}

	public Boolean getSimplePlaybook() {
		return simplePlaybook;
	}

	public void setSimplePlaybook(Boolean simplePlaybook) {
		this.simplePlaybook = simplePlaybook;
	}

	public Boolean getAdvancedPlaybook() {
		return advancedPlaybook;
	}

	public void setAdvancedPlaybook(Boolean advancedPlaybook) {
		this.advancedPlaybook = advancedPlaybook;
	}

	public Boolean getIsWizardDone() {
		return isWizardDone;
	}

	public void setIsWizardDone(Boolean isWizardDone) {
		this.isWizardDone = isWizardDone;
	}

	public float getOverAllpercentage() {
		return overAllpercentage;
	}

	public void setOverAllpercentage(float overAllpercentage) {
		this.overAllpercentage = overAllpercentage;
	}

}
