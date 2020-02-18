package constants;

public interface AssociateResponseCodes {
	/**
	 * If "things to consider" are all passed then the response code is 200 and
	 * response message is Success.
	 */
	public static final int SUCCESS = 200;

	public static final int PROBLEM_WITH_DB = 310000;
	/**
	 * Authentication
	 */
	public static final int NULL_VALUES_PASSED_IN_AUTH = 310001;
	public static final int LIC_INVALID_IN_AUTH = 310002;
	public static final int USERNAME_INVALID_IN_AUTH = 310003;
	public static final int WRONG_PASSWORD_IN_AUTH = 310004;

	public static final int USER_DELETED_WITH_RQUESTED_ID = 310054;
	public static final int USER_SUSPENDED_WITH_RQUESTED_ID = 310055;

	public static final int USER_ISNOT_VERIFIED_WITH_RQUESTED_ID = 310056;
	public static final int USER_ROLE_ISNOT_ASSOCIATE_WITH_REQUESTED_ID = 310057;

	public static final int EMAIL_IS_NULL_IN_FORGOT_PASSWORD = 310058;
	public static final int USER_NOT_FOUND_WITH_REQUESTED_EMAIL = 310059;
	
	public static final int EMAIL_COULD_NOT_BE_SENT = 310060;

	/**
	 * Incomplete Task
	 */
	public static final int NULL_USERID_IN_INCOMPLETE_TASK = 310005;
	public static final int INVALID_USERID_IN_INCOMPLETE_TASK = 310006;

	/**
	 * Today's Completed Task
	 */
	public static final int NULL_USERID_IN_TODAYCOMPLETED_TASK = 310007;
	public static final int INVALID_USERID_IN_TODAYCOMPLETED_TASK = 310008;

	/**
	 * Profile Update
	 */
	public static final int NULL_USERID_IN_PROFILE_UPDATE = 310009;
	public static final int INVALID_USERID_IN_PROFILE_UPDATE = 310011;

	/**
	 * Reset Password
	 */
	public static final int NULL_USERID_IN_RESETPASSWORD = 310012;
	public static final int INVALID_USERID_IN_RESETPASSWORD = 310014;
	public static final int SAME_PASSWORD_AS_OLD_IN_RESETPASSWORD = 310015;

	/**
	 * Add Lead
	 */
	public static final int NULL_INPUT_PARAM_IN_ADDLEAD = 310016;
	public static final int NULL_USERID_IN_ADDLEAD = 310010;
	public static final int INVALID_USERID_IN_ADDLEAD = 310017;
	public static final int INVALID_INPUT_PARAMS_IN_ADDLEAD = 310018;

	/**
	 * View Leads
	 */
	public static final int NULL_USERID_IN_VIEWLEADS = 310019;
	public static final int INVALID_USERID_IN_VIEWLEADS = 310020;

	/**
	 * Phone Number Validate
	 */
	public static final int NULL_PHONENUMBER_IN_PHONENUMBER_VALIDATE = 310021;
	public static final int INVALID_PHONENUMBER_IN_PHONENUMBER_VALIDATE = 310022;

	/**
	 * Get Task Between Two Dates
	 */
	public static final int NULL_PARAMS_IN_GETTASKBETWEENTWODATES = 310023;
	public static final int INVALID_STARTDATE_IN_GETTASKBETWEENTWODATES = 310024;
	public static final int INVALID_ENDDATE_IN_GETTASKBETWEENTWODATES = 310025;
	public static final int INVALID_USERID_IN_GETTASKBETWEENTWODATES = 310026;
	public static final int NULL_USERID_IN_GETTASKBETWEENTWODATES = 310013;

	/**
	 * View Product
	 */
	public static final int NULL_USERID_IN_VIEWPRODUCT = 310027;
	public static final int INVALID_USERID_IN_VIEWPRODUCT = 310028;

	/**
	 * Raise Ticket
	 */
	public static final int NULL_USERID_IN_RAISETICKET = 310029;
	public static final int INVALID_USERID_IN_RAISETICKET = 310030;
	public static final int NULL_PARAMS_IN_RAISETICKET = 310031;

	/**
	 * Create Task
	 */
	public static final int NULL_USERID_IN_CREATETASK = 310032;
	public static final int NULL_PARAMS_IN_CREATETASK = 310033;
	public static final int INVALID_USERID_IN_CREATETASK = 310034;
	public static final int INVALID_PARAMS_IN_CREATETASK = 310035;

	/**
	 * Disposition
	 */
	public static final int NULL_USERID_IN_DISPOSITION = 310036;
	public static final int INVALID_USERID_IN_DISPOSITION = 310038;
	public static final int NULL_DISP_OBJECT_IN_DISPOSITION = 310037;
	public static final int INVALID_DISP_TYPE_IN_DISPOSITION = 310039;
	public static final int NULL_DISP_TYPE_IN_DISPOSITION = 310052;

	/**
	 * Disposition - Call Answered
	 */
	public static final int NULL_PARAMS_IN_DISP_CALLANSWERED = 310040;
	public static final int INVALID_PARAMS_DISP_CALLANSWERED = 310041;

	/**
	 * Disposition - VoiceMail
	 */
	public static final int NULL_PARAMS_IN_DISP_VOICEMAIL = 310042;
	public static final int INVALID_PARAMS_DISP_VOICEMAIL = 310043;

	/**
	 * Disposition - Dropped
	 */
	public static final int NULL_PARAMS_IN_DISP_DROPPED = 310044;
	public static final int INVALID_PARAMS_DISP_DROPPED = 310045;

	/**
	 * Disposition - WrongNumber
	 */
	public static final int NULL_PARAMS_IN_DISP_WRONGNUMBER = 310046;
	public static final int INVALID_PARAMS_DISP_WRONGNUMBER = 310047;

	/**
	 * Disposition - WrongPerson
	 */
	public static final int NULL_PARAMS_IN_DISP_WRONGPERSON = 310048;
	public static final int INVALID_PARAMS_DISP_WRONGPERSON = 310049;

	/**
	 * Disposition - NoResponse
	 */
	public static final int NULL_PARAMS_IN_DISP_NORESPONSE = 310050;
	public static final int INVALID_PARAMS_DISP_NORESPONSE = 310051;

	/**
	 * Disposition - NotDisposed
	 */
	public static final int NULL_PARAMS_IN_DISP_NOTDISPOSED = 310052;
	public static final int INVALID_PARAMS_DISP_NOTDISPOSED = 310053;
	


	public static final int WRONG_OLD_PASSWORD = 310054;
	public static final int NULL_OLD_PASSWORD = 310055;
	public static final int NULL_NEW_PASSWORD = 310055;
}
