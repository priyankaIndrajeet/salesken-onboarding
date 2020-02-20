package ai.salesken.onboarding.constants;

public class ResponseCodes {
	/**
	 * If "things to consider" are all passed then the response code is 200 and
	 * response message is Success.
	 */
	public static final int SUCCESS = 200;
	public static final int DB_ERROR = 350000;
	public static final Integer UNKNOWN_EXCEPTION = 350001;

	/**
	 * Authentication
	 */
	public static final int NULL_VALUES_PASSED_IN_AUTH = 350002;
	public static final int LIC_INVALID_IN_AUTH = 350003;
	public static final int USERNAME_INVALID_IN_AUTH = 350004;
	public static final int WRONG_PASSWORD_IN_AUTH = 350005;

	public static final int USER_DELETED_WITH_RQUESTED_ID = 350006;
	public static final int USER_SUSPENDED_WITH_RQUESTED_ID = 350007;

	public static final int USER_ISNOT_VERIFIED_WITH_RQUESTED_ID = 350008;

	/**
	 * Orgnization
	 */
	public static final Integer INVALID_PARAMETERS_PASSED = 350009;
	public static final Integer PHONE_NUMBER_VALIDATION_FAILED = 350010;
	public static final Integer NULL_PARAMETERS_PASSED = 350011;
	public static final Integer NULL_ORG_OBJ_PASSED = 350012;
	public static final Integer NAME_LENGTH_IS_GREATER_THAN_256 = 350013;
	public static final Integer WEBSITE_LENGTH_IS_GREATER_THAN_256 = 350014;
	public static final Integer ADDRESS_LINE_1_LENGTH_IS_GREATER_THAN_256 = 350015;
	public static final Integer ADDRESS_LINE_2_LENGTH_IS_GREATER_THAN_256 = 350016;
	public static final Integer CITY_LENGTH_IS_GREATER_THAN_256 = 350017;
	public static final Integer COUNTRY_LENGTH_IS_GREATER_THAN_256 = 350018;
	public static final Integer LANDMARK_LENGTH_IS_GREATER_THAN_256 = 350019;
	public static final Integer STATE_LENGTH_IS_GREATER_THAN_256 = 350020;
	public static final Integer DESCRIPTION_LENGTH_IS_GREATER_THAN_1024 = 350021;
	/**
	 * User
	 **/
	public static final Integer INVALID_USERID_IN_ONBOARDING = 350022;
	public static final Integer NULL_USERID_IN_ONBOARDING = 350023;
	public static final Integer NULL_FILE_IN_BULK_UPLOAD = 350024;
}
