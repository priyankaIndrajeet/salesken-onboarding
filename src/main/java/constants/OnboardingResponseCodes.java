package constants;

public interface OnboardingResponseCodes {
	/**
	 * 350001
	 */
	public static final int SUCCESS = 200;
	public static final int PROBLEM_WITH_DB = 350000;

	public static final int NULL_ORG_ID_PASSED = 350001;
	public static final int INVALID_ORG_ID_PASSED = 350002;
	public static final int NULL_ORG_NAME_PASSED = 350003;
	public static final int INVALID_ORG_NAME_PASSED = 350004;

	public static final int NULL_USERID_IN_ONBOARDING = 350005;
	public static final int INVALID_USERID_IN_ONBOARDING = 350006;

	public static final int NULL_ORG_FOR_REQ_USERID = 350007;

	public static final int TEAM_OBJ_NULL_IN_CREATE_TEAM = 350008;
	public static final int INVALID_NAME_IN_CREATE_TEAM = 350009;
	public static final int NULL_TEAM_ID_PASSED = 3500010;
	public static final int INVALID_TEAM_ID_PASSED = 3500011;

	public static final int TEAM_IS_NOT_DELETED = 3500012;
	public static final int UNAUTHORIZED_USER = 3500013;

	public static final int USER_ALREADY_EXIST_REQ_EMAIL = 3500014;
	public static final int NULL_EMAIL_IN_USERCREATION = 3500015;
	public static final int USER_ALREADY_EXIST_REQ_MOBILE = 3500016;
	public static final int NULL_MOBILE_IN_USERCREATION = 3500017;
	public static final int NULL_USER_IN_USERCREATION = 3500018;

	public static final int PRODUCT_OBJ_NULL_IN_CREATE_PRODUCT = 3500019;
	public static final int PRODUCT_NOT_CREATED_WITH_REQ_OBJ = 3500020;
	public static final int PRODUCTSPECS_NOT_CREATED_WITH_REQ_OBJ = 3500021;
	public static final int PRODUCTSPECS_KEY_NOT_PASSED = 3500022;

	public static final int NULL_PRODUCT_NAME_PASSED = 3500023;
	public static final int NULL_PRODUCT_ID_PASSED = 3500024;
	public static final int NULL_PRODUCT_SPECIFICATION_ID_PASSED = 3500025;
	public static final int NULL_PRODUCT_SPECIFICATION_KEY_PASSED = 3500026;
	public static final int NULL_USER_IN_USERUPDATION = 3500027;
	public static final int NULL_PIPELINE_ID_PASSED = 3500028;
	public static final int NULL_USER_NAME = 3500029;
	public static final int INVALID_USER_NAME = 3500030;
	public static final int NULL_USER_ID = 3500031;

	public static final int PIPELINE_IS_NULL = 3500032;
	public static final int PIPELINE_NAME_IS_NULL = 3500033;

	public static final int PIPELINE_ID_IS_NULL = 3500034;

	public static final int PIPELINE_STAGE_ID_IS_NULL = 3500035;
	public static final int PIPELINE_STAGE_NAME_IS_NULL = 3500036;
	public static final int NULL_FILE_IN_BULK_UPLOAD = 3500037;
	public static final int PIPELINE_STAGE_TASK_TYPE_IS_NULL = 3500038;
	public static final int PIPELINE_STAGE_TASK_ID_IS_NULL = 3500039;
	public static final int PIPELINE_STAGE_TASK_IS_NULL = 3500040;
	public static final int PIPELINE_STAGE_IS_NULL = 3500041;
	public static final int TASK_ID_IS_NULL = 3500042;
	public static final int STAGE_ID_IS_NULL = 3500043;
	public static final int PERSONA_IS_NULL = 3500044;
	public static final int PERSONA_NAME_IS_NULL = 3500045;
	public static final int PERSONA_ID_IS_NULL = 3500046;
	public static final int PERSONA_DATA_ID_IS_NULL = 3500047;
	public static final int NULL_TEAM_OBJ_PASSED = 3500048;
	public static final int ORG_OBJ_NULL_IN_CREATE_ORG = 3500049;
	public static final int PERSONA_KEY_IS_NULL = 3500050;

	public static final int NULL_TEAM_NAME_PASSED = 3500051;
	public static final int TEAM_OBJ_NULL_IN_UPDATE_TEAM = 3500052;
	public static final int NULL_ORG_OBJ_PASSED = 3500053;
	public static final int PRODUCT_ID_NULL_IN_PRODUCTSPECS = 3500054;
	public static final int PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS = 3500055;
	public static final int NULL_USER_IN_DEACTIVATE = 3500056;
	public static final int INVALID_PIPELINE_ID_PASSED = 3500057;
	public static final int NULL_USERIDS = 3500058;
	public static final int NULL_MANAGER_ID = 3500059;
	public static final int NULL_MANAGER_DESIGNATION = 3500060;
	public static final int NULL_USER_IN_ASSIGN_OWNER = 3500061;
	public static final int PRODUCT_ASSET_NOT_CREATED = 3500062;
	public static final int PRODUCT_ID_NULL_IN_PRODUCT_ASSET = 3500063;
	public static final int PRODUCT_ASSET_OBJ_NULL_IN_CREATE_PRODUCT_ASSET = 3500064;
	public static final int PRODUCTSPECS_ID_NULL_IN_PRODUCTSPECS = 3500065;
	public static final int PIPELINE_IS_NOT_CREATED = 3500066;
	public static final int INVALID_PIPELINE_NAME = 3500067;
	public static final int PIPELINE_STAGE_NAME_INVALID = 3500068;
	public static final int PIPELINE_STAGE_NOT_CREATED = 3500069;
	public static final int STAGEID_NULL_IN_STAGE_TASK_CREATION = 3500070;
	public static final int STAGETASK_NOT_CREATED = 3500071;
	public static final int PIPELINE_IS_NOT_UPDATED = 3500072;
	public static final int PIPELINE_STAGE_TASK_NAME_IS_INVALID = 3500073;
	public static final int PERSONA_NAME_IS_INVALID = 3500074;
	public static final int ORG_CONFIG_NOT_CREATED = 3500075;
	public static final int ORG_CONFIG_OBJ_NULL = 3500076;
	public static final int INVALID_PROPERTY_VALUE_IN_ORG_CONFIG = 3500077;
	public static final int NULL_PROPERTY_VALUE_IN_ORG_CONFIG = 3500078;
	public static final int INVALID_PROPERTY_NAME_IN_ORG_CONFIG = 3500079;
	public static final int NULL_PROPERTY_NAME_IN_ORG_CONFIG = 3500080;
	public static final int TEAM_IS_NOT_BELONG_TO_WITH_REQUESTED_ID = 3500081;
	public static final int NULL_PLAYBOOK_NAME_PASSED = 3500082;
	public static final int NULL_PLAYBOOK_ID_PASSED = 3500083;
	public static final int NULL_PLAYBOOK_KEY_ID_PASSED = 3500084;
	public static final int NULL_STAGE_TASK_ID_PASSED = 3500085;
	public static final int STAGE_TASK_LEVEL_IS_NULL = 3500086;
	public static final int DIMENSION_ID_IS_NULL = 3500087;
	public static final int PLAYBOOK_SNIPPET_ID_IS_NULL = 3500088;
	public static final int PLAYBOOK_SNIPPET_TEXT_IS_NULL = 3500089;
	public static final int PLAYBOOK_SNIPPET_TEXT_IS_INVALID = 3500090;
	public static final int PRODUCTSPECS_VALUE_INVALID_IN_PRODUCTSPECS = 3500091;
	public static final int PRODUCTSPECS_KEY_INVALID_IN_PRODUCTSPECS = 3500092;
	public static final int PERSONA_VALUE_IS_NULL = 3500093;
	public static final int PERSONA_META_DATA_ID_IS_NULL = 3500094;
	public static final int PERSONA_KEY_IS_INVALID = 3500095;
	public static final int PERSONA_VALUE_IS_INVALID = 3500096;
	public static final int PERSONA_META_DATA_IS_NULL = 3500097;
	public static final int PERSONA_DATA_IS_NULL = 3500098;
	public static final int PIPELINE_ORDER_ID_IS_NULL = 3500099;
	public static final int PRODUCT_FEATURE_IS_NULL = 35000100;
	public static final int PRODUCT_FEATURE_IDS_IS_NULL = 35000101;
	public static final int PERSONA_DATA_ID_IS_NULL_IN_PRODUCT_FEATURE = 35000102;
	public static final int TYPE_ID_IS_NULL_IN_PRODUCT_FEATURE = 35000103;
	public static final int NULL_PLAYBOOK_NODE_ID_PASSED = 35000104;
	public static final int NODE_VALUE_NULL_PASSED = 35000105;
	public static final int PERSONA_OBJ_IS_NULL = 35000106;
	public static final int NO_PIPELINES = 35000107;
	public static final int NULL_LEAD_SOURCE_ID_PASSED = 35000108;
	public static final int PRODUCT_ALREADY_EXIST_WITH_SAME_NAME = 35000109;
	public static final int SELECTED_OWNER_IS_ALREADY_ASSOCIATE_OF_USER = 35000110;
	
	public static final int PHONE_NUMBER_VALIDATION_FAILED = 35000111;
	
	public static final int NULL_PINCODE_IN_ONBOARDING = 35000112;
	public static final int INVALID_PINCODE_IN_ONBOARDING = 35000113;
	public static final int ZOHO_TOKEN_INVALID = 35000114;
	public static final int INCORRECT_URL_FOR_ORG_WEBSITE = 35000115;
	public static final int ORG_WEBSITE_IS_NOT_PASSED = 35000116;
	public static final int ORG_WEBSITE_IS_ALREADY_EXIST = 35000117;
	public static final int NULL_USERIDS_PASSED = 35000118;


}
