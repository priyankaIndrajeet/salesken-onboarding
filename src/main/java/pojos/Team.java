package pojos;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

  public class Team {
	private Integer id;
	private String name; 
	private Integer organizationId;
	private String description;
	private Integer ownerId;
	private User owner;
	private ArrayList<User> users=new ArrayList<User>();
	private Boolean isDeleted;
	private ArrayList<Integer> userIds = new ArrayList<Integer>();
	private ArrayList<Integer> processIds = new ArrayList<Integer>();
	 
  
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

 

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	

	public ArrayList<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(ArrayList<Integer> userIds) {
		this.userIds = userIds;
	}

	public Team() {
		super();
	}

	
	 
	public ArrayList<Integer> getProcessIds() {
		return processIds;
	}

	public void setProcessIds(ArrayList<Integer> processIds) {
		this.processIds = processIds;
	}
	
	
	
}
