package ai.salesken.onboarding.constants;

public class ResponseMessages {
	/**
	 * If "things to consider" are all passed then the response code is 200 and
	 * response message is Success.
	 */
	public static final String SUCCESS = "SUCCESS";
	public static final String DB_EXCEPTION = "The Database/Backend did not respond.";
	public static final String UNKNOWN_EXCEPTION = "Unknown exception occured";

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

	/**
	 * Organization
	 */
	public static final String INVALID_INPUT_ORG_NAME = "Invalid Org Name passed, don't enter white space.";
	public static final String NULL_ORG_NAME_PASSED = "Organization name is null, please enter organization name.";
	public static final String NULL_ORG_OBJ_PASSED = "Organization object is null passed";
	public static final String PHONE_NUMBER_VALIDATION_FAILED = "Phone number is not valid, please enter correct phone number";
	public static final String NAME_LENGTH_IS_GREATER_THAN_256 = "Input name length is greater than 256, please enter name length less than 256";
	public static final String WEBSITE_LENGTH_IS_GREATER_THAN_256 = "Input website length is greater than 256, please enter website length less than 256";
	public static final String ADDRESS_LINE_1_LENGTH_IS_GREATER_THAN_256 = "Input address line 1 length is greater than 256, please enter address line 1 length less than 256";
	public static final String ADDRESS_LINE_2_LENGTH_IS_GREATER_THAN_256 = "Input address line 2 length is greater than 256, please enter address line 2 length less than 256";
	public static final String CITY_LENGTH_IS_GREATER_THAN_256 = "Input city length is greater than 256, please enter city length less than 256";
	public static final String COUNTRY_LENGTH_IS_GREATER_THAN_256 = "Input country length is greater than 256, please enter country length less than 256";
	public static final String LANDMARK_LENGTH_IS_GREATER_THAN_256 = "Input landmark length is greater than 256, please enter landmark length less than 256";
	public static final String STATE_LENGTH_IS_GREATER_THAN_256 = "Input state length is greater than 256, please enter state length less than 256";
	public static final String DESCRIPTION_LENGTH_IS_GREATER_THAN_1024 = "Input organization profile description length is greater than 1024, please enter description length less than 1024";
	public static final String INVALID_INPUT_BOARD_LINE_NUMBER = "Input boardline number is not correct, please enter valid boardline number";

	/**
	 * User
	 **/
	public static final String INVALID_USERID_IN_ONBOARDING = "Invalid user id";
	public static final String NULL_USERID_IN_ONBOARDING = "Null user ID";
	public static final String NULL_FILE_IN_BULK_UPLOAD = "File is not passed in bulk upload";
}
