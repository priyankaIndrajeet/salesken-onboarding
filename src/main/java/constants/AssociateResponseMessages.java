package constants;

public interface AssociateResponseMessages {

	/**
	 * If "things to consider" are all passed then the response code is 200 and
	 * response message is Success.
	 */
	public static final String SUCCESS = "SUCCESS";
	public static final String PROBLEM_WITH_DB = "The Database/Backend did not respond.";

	/**
	 * Authentication
	 */
	public static final String NULL_VALUES_PASSED_IN_AUTH = "Null Email/Password";
	public static final String LIC_INVALID_IN_AUTH = "Invalid License";
	public static final String USERNAME_INVALID_IN_AUTH = "Invalid Username";
	public static final String WRONG_PASSWORD_IN_AUTH = "Wrong Password";

	public static final String USER_DELETED_WITH_RQUESTED_ID = "User is deleted with requested id";
	public static final String USER_SUSPENDED_WITH_RQUESTED_ID = "User is suspended with requested id.";

	public static final String USER_ISNOT_VERIFIED_WITH_RQUESTED_ID = "User is not verified with requested id.";
	public static final String USER_ROLE_ISNOT_ASSOCIATE_WITH_REQUESTED_ID = "User is not belongs to associate role.";

	public static final String EMAIL_IS_NULL_IN_FORGOT_PASSWORD = "Email is null.";
	public static final String USER_NOT_FOUND_WITH_REQUESTED_EMAIL = "User not found with requested email.";
	
	public static final String EMAIL_COULD_NOT_BE_SENT = "Could not send the email";

	/**
	 * Incomplete Task
	 */
	public static final String NULL_USERID_INCOMPLTE_TASK = "Null userID";
	public static final String INVALID_USERID_INCOMPLTE_TASK = "Invalid User ID";

	/**
	 * Today's Completed Task
	 */
	public static final String NULL_USERID_IN_TODAYCOMPLETED_TASK = " Null user ID";
	public static final String INVALID_USERID_IN_TODAYCOMPLETED_TASK = "User not found with requested id";

	/**
	 * Reset Password
	 */
	public static final String NULL_USERID_IN_RESETPASSWORD = "Null user ID";
	public static final String INVALID_USERID_IN_RESETPASSWORD = "User not found with requested id";
	public static final String SAME_PASSWORD_AS_OLD_IN_RESETPASSWORD = " The new password can't be same as old password";
	public static final String WRONG_OLD_PASSWORD = "Please provide correct current password to change new password";
	public static final String NULL_OLD_PASSWORD = "Current password is null";
	public static final String NULL_NEW_PASSWORD = "New password is null";


	/**
	 * Add Lead
	 */
	public static final String NULL_INPUT_PARAM_IN_ADDLEAD = "Input parameters are null";
	public static final String INVALID_USERID_IN_ADDLEAD = "Invalid User ID";
	public static final String INVALID_INPUT_PARAMS_IN_ADDLEAD = " Input parameter are invalid.";
	public static final String NULL_USERID_IN_ADDLEAD = "Null user ID";

	/**
	 * View Leads
	 */
	public static final String NULL_USERID_IN_VIEWLEADS = "Null user ID";
	public static final String INVALID_USERID_IN_VIEWLEADS = "Invalid user id";

	/**
	 * Phone Number Validate
	 */
	public static final String NULL_PHONENUMBER_IN_PHONENUMBER_VALIDATE = "Phone number is null";
	public static final String INVALID_PHONENUMBER_IN_PHONENUMBER_VALIDATE = "Invalid phone number";

	/**
	 * Get Task Between Two Dates
	 */
	public static final String NULL_PARAMS_IN_GETTASKBETWEENTWODATES = "start date or end date is null.";
	public static final String INVALID_STARTDATE_IN_GETTASKBETWEENTWODATES = "Invalid start date";
	public static final String INVALID_ENDDATE_IN_GETTASKBETWEENTWODATES = "Invalid end date";
	public static final String INVALID_USERID_IN_GETTASKBETWEENTWODATES = "Invalid User ID";
	public static final String NULL_USERID_IN_GETTASKBETWEENTWODATES = "User id is null";

	/**
	 * View Product
	 */
	public static final String NULL_USERID_IN_VIEWPRODUCT = "User id is null";
	public static final String INVALID_USERID_IN_VIEWPRODUCT = "Invalid User ID";

	/**
	 * Raise Ticket
	 */
	public static final String NULL_USERID_IN_RAISETICKET = "User id is null";
	public static final String INVALID_USERID_IN_RAISETICKET = "Invalid User ID";
	public static final String NULL_PARAMS_IN_RAISETICKET = "Required fields are null in Ticket object.";

	public static final String NULL_USERID_IN_PROFILE_UPDATE = "Null user ID.";
	public static final String INVALID_USERID_IN_PROFILE_UPDATE = "User not found with requested id";

	/**
	 * Create Task
	 */
	public static final String NULL_USERID_IN_CREATETASK = "User id is null";
	public static final String NULL_PARAMS_IN_CREATETASK = "required fields are null in Task object.";
	public static final String INVALID_USERID_IN_CREATETASK = "Invalid User ID";
	public static final String INVALID_PARAMS_IN_CREATETASK = "Invalid required fields for task creation.";

	/**
	 * Disposition
	 */
	public static final String NULL_USERID_IN_DISPOSITION = "Null user ID";
	public static final String INVALID_USERID_IN_DISPOSITION = "Invalid User ID";
	public static final String NULL_DISP_OBJECT_IN_DISPOSITION = "Null Disposition Object";
	public static final String INVALID_DISP_TYPE_IN_DISPOSITION = "Invalid disposition type in Disposition Object";
	public static final String NULL_DISP_TYPE_IN_DISPOSITION = "Disposition type is null";

	/**
	 * Disposition - Call Answered
	 */
	public static final String NULL_PARAMS_IN_DISP_CALLANSWERED = "Input params are null in Disposition.";
	public static final String INVALID_PARAMS_DISP_CALLANSWERED = "Invalid Input params in Disposition.";

	/**
	 * Disposition - VoiceMail
	 */
	public static final String NULL_PARAMS_IN_DISP_VOICEMAIL = "Input params are null in Disposition.";
	public static final String INVALID_PARAMS_DISP_VOICEMAIL = "Invalid Input params in Disposition.";

	/**
	 * Disposition - Dropped
	 */
	public static final String NULL_PARAMS_IN_DISP_DROPPED = "Input params are null in Disposition.";
	public static final String INVALID_PARAMS_DISP_DROPPED = "Invalid Input params in Disposition.";

	/**
	 * Disposition - WrongNumber
	 */
	public static final String NULL_PARAMS_IN_DISP_WRONGNUMBER = "Input params are null in Disposition.";
	public static final String INVALID_PARAMS_DISP_WRONGNUMBER = "Invalid Input params in Task Submission.";

	/**
	 * Disposition - WrongPerson
	 */
	public static final String NULL_PARAMS_IN_DISP_WRONGPERSON = "Input params are null in Disposition.";
	public static final String INVALID_PARAMS_DISP_WRONGPERSON = "Invalid Input params in Disposition.";

	/**
	 * Disposition - NoResponse
	 */
	public static final String NULL_PARAMS_IN_DISP_NORESPONSE = "Input params are null in Disposition.";
	public static final String INVALID_PARAMS_DISP_NORESPONSE = "Invalid Input params in Disposition.";

	/**
	 * Disposition - NotDisposed
	 */
	public static final String NULL_PARAMS_IN_DISP_NOTDISPOSED = "Input params are null in Disposition.";
	public static final String INVALID_PARAMS_DISP_NOTDISPOSED = " Invalid Input params in Disposition.";

}
