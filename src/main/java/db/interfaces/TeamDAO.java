package db.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;

import pojos.Team;
import pojos.User;
import pojos.Users;

public interface TeamDAO {

	public ArrayList<Team> findbyOrganizationId(Integer id) throws SQLException;

	public Team createTeam(Team team, User user) throws SQLException;

	public Team findbyId(Integer id) throws SQLException;

	public Team updateTeam(Team team) throws SQLException;

	public Boolean deleteTeam(Integer id) throws SQLException;

	public Team addMembersOwner(Team team) throws SQLException;

	public Team removeMembers(Team team) throws SQLException;

	public Users getUsersForTeamCreation(User user, String role, String search, String limit, String offset, Integer teamId) throws SQLException;

	public Users getUsersAndManagersForTeamCreation(Integer userId, String role, String search) throws SQLException;

	public Team dummyTeamCreation(Integer userId) throws SQLException;

	public void removeAllTeamMembers(Integer teamId) throws SQLException;

	public void createTeamMapping(Team team) throws SQLException;

	public String getTeamName(int teamId) throws SQLException;

}