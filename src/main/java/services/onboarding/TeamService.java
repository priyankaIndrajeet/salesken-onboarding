package services.onboarding;

import javax.ws.rs.core.Response;

import pojos.Team;

public interface TeamService {
	public Response createTeam(Team team);
	public Response updateTeam(Team team);
	public Response viewTeam(Integer id);
	public Response addMembersOwnerInTeam(Team team);
	public Response removeMembersInTeam(Team team);
	public Response deleteTeam(Integer teamID);
	public Response getUsersForTeamCreation(String role, String search, String limit, String offset,Integer teamId);
	public Response getUsersAndManagersForTeamCreation(String role, String search);
	public Response dummyTeamCreation();
	public Response createMapping(Team team);
 }
