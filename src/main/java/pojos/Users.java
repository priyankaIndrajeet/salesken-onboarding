package pojos;

import java.util.ArrayList;

public class Users {

	ArrayList<User> UserList;
	private int Total;
	
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Users(ArrayList<User> userList, int total) {
		super();
		UserList = userList;
		Total = total;
	}

	public ArrayList<User> getUserList() {
		return UserList;
	}

	public void setUserList(ArrayList<User> userList) {
		UserList = userList;
	}

	public int getTotal() {
		return Total;
	}

	public void setTotal(int total) {
		Total = total;
	}
}
