package services.onboarding;

import javax.ws.rs.core.Response;

import pojos.Designation;
import pojos.User;

public interface HierarchyService {
	public Response getManagers(Integer userId);

	public Response getAllDesignations();

	public Response assignOwner(User user);

	public Response getUserByDesignationWise(Integer designationId);

	public Response deleteOwner(Integer user_Id);

	public Response getManagerAssociate(Integer userId);

	public Response addDesignation(Designation designation);

	public Response getUserDetailByDesignationWise(Integer designationId, Integer userID);

	public Response updateDesignationOfUser(User user);
}
