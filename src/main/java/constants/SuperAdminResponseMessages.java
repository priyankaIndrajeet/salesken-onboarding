package constants;

public interface SuperAdminResponseMessages {
	/**
	 * If "things to consider" are all passed then the response code is 200 and
	 * response message is Success.
	 */
	public static final String SUCCESS = "SUCCESS";
	
	
	public static final String NULL_ORG_OBJECT_IN_CREATE_ORG = "Organization object is not passing for create organization";
	public static final String NULL_PARAMS_IN_CREATE_ORG = "required fields are null in Create Organization";
	public static final String INVALID_PARAMS_IN_CREATE_ORG = "Invalid required fields for task creation.";

}
