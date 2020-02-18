package pojos;

import java.util.ArrayList;

public class PipelineFields {
	ArrayList<Team> teams;
	ArrayList<Product> products;
	ArrayList<Persona> personas;
	ArrayList<PipelineStage> stageDropdown;
	ArrayList<LeadSource> leadSources;
	public ArrayList<Team> getTeams() {
		return teams;
	}

	public void setTeams(ArrayList<Team> teams) {
		this.teams = teams;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public ArrayList<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(ArrayList<Persona> personas) {
		this.personas = personas;
	}

	public ArrayList<PipelineStage> getStageDropdown() {
		return stageDropdown;
	}

	public void setStageDropdown(ArrayList<PipelineStage> stageDropdown) {
		this.stageDropdown = stageDropdown;
	}

	public ArrayList<LeadSource> getLeadSources() {
		return leadSources;
	}

	public void setLeadSources(ArrayList<LeadSource> leadSources) {
		this.leadSources = leadSources;
	}

	 
	

}
