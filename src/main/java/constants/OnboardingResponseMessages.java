package constants;

public interface OnboardingResponseMessages {

	public static final String SUCCESS = "SUCCESS";
	public static final String PROBLEM_WITH_DB = "The Database/Backend did not respond.";

	public static final String NULL_ORG_ID_PASSED = "Null organization ID";
	public static final String INVALID_ORG_ID_PASSED = "Invalid organization ID";
	public static final String NULL_ORG_NAME_PASSED = "Null organization name passed";
	public static final String INVALID_ORG_NAME_PASSED = "Invalid organization name passed";

	public static final String NULL_USERID_IN_ONBOARDING = "Null user ID";
	public static final String INVALID_USERID_IN_ONBOARDING = "Invalid user id";

	public static final String NULL_ORG_FOR_REQ_USERID = "Organization is null for requested user id";

	public static final String TEAM_OBJ_NULL_IN_CREATE_TEAM = "Team object is not passed for team creation ";
	public static final String NULL_TEAM_OBJ_PASSED = "Team object is not passed for add members in team ";

	public static final String INVALID_NAME_IN_CREATE_TEAM = "Team name is invalid please provide valid team name";
	public static final String NULL_TEAM_ID_PASSED = "Null Team Id passed";
	public static final String INVALID_TEAM_ID_PASSED = "Team id is not found with requested id";
	public static final String TEAM_IS_NOT_DELETED = "Team is not deleted please try again";
	public static final String UNAUTHORIZED_USER = "Unauthorized user for requested organization";

	public static final String USER_ALREADY_EXIST_REQ_EMAIL = "User already exist with requested email";
	public static final String NULL_EMAIL_IN_USERCREATION = "Null email passed";
	public static final String NULL_MOBILE_IN_USERCREATION = "Null mobile passed";
	public static final String NULL_TEAM_NAME_PASSED = "Null Team name passed";
	public static final String TEAM_OBJ_NULL_IN_UPDATE_TEAM = "Team object is not passed for team updation ";

	public static final String USER_ALREADY_EXIST_REQ_MOBILE = "User already exist with requested mobile";

	public static final String NULL_USER_IN_USERCREATION = "Null user object passed in create user";

	public static final String PRODUCT_OBJ_NULL_IN_CREATE_PRODUCT = "Product object is not passed for product creation ";
	public static final String PRODUCTSPECS_OBJ_NULL_IN_CREATE_PRODUCTSPECS = "ProductSpecificationData object is not passed for ProductSpecificationData creation ";;

	public static final String PRODUCT_NOT_CREATED_WITH_REQ_OBJ = "Product is not created with requested object";
	public static final String PRODUCTSPECS_NOT_CREATED_WITH_REQ_OBJ = "Product specification is not created with requested object";
	public static final String PRODUCTSPECS_KEY_NOT_PASSED = "Product specification key is not passed";
	public static final String NULL_PRODUCT_SPECIFICATION_ID_PASSED = "Null specification id passed";
	public static final String NULL_PRODUCT_SPECIFICATION_KEY_PASSED = "Invalid product specification key passed";

	public static final String NULL_PRODUCT_NAME_PASSED = "Null product name passed";
	public static final String NULL_PRODUCT_ID_PASSED = "Null product id passed";

	public static final String NULL_PIPELINE_ID_PASSED = "Null pipeline id passed";
	public static final String NULL_USER_IN_USERUPDATION = "Null user object passed in update user";
	public static final String NULL_USER_NAME = "Null user name passed";
	public static final String NULL_USER_ID = "Null user id passed ";
	public static final String INVALID_USER_NAME = "Invalid user name passed ";

	public static final String PIPELINE_IS_NULL = "Pipeline object is not passed";
	public static final String PIPELINE_NAME_IS_NULL = "Pipeline name is null";

	public static final String PIPELINE_ID_IS_NULL = "Pipeline Id is null";

	public static final String PIPELINE_STAGE_ID_IS_NULL = "null pipeline stage id passed";
	public static final String PIPELINE_STAGE_NAME_IS_NULL = "Pipeline stage name is null";
	public static final String PIPELINE_STAGE_TASK_TYPE_IS_NULL = "Pipeline stage task type is null";
	public static final String PIPELINE_STAGE_TASK_ID_IS_NULL = "Pipeline stage task id is null";
	public static final String PIPELINE_STAGE_TASK_IS_NULL = "PipelineStageTask object is null in task creation";
	public static final String PIPELINE_STAGE_IS_NULL = "PipelineStage object is null in stage creation";
	public static final String NULL_FILE_IN_BULK_UPLOAD = "File is not passed in bulk upload";
	public static final String TASK_ID_IS_NULL = "task id is null in task deletion";
	public static final String STAGE_ID_IS_NULL = "stage id is null in task deletion";

	public static final String PERSONA_IS_NULL = "Persona is null in persona creation";
	public static final String PERSONA_NAME_IS_NULL = "Persona name is null ";
	public static final String PERSONA_ID_IS_NULL = "Persona id is null ";
	public static final String PERSONA_DATA_ID_IS_NULL = "Persona data id is null ";
	public static final String ORG_OBJ_NULL_IN_CREATE_ORG = "Organization object is not passed for organization creation ";
	public static final String PERSONA_KEY_IS_NULL = "Persona metadata key is null";
	public static final String NULL_ORG_OBJ_PASSED = "Organization object is not passed ";
	public static final String PRODUCT_ID_NULL_IN_PRODUCTSPECS = "Product id is null to create product specification";
	public static final String NULL_USER_IN_DEACTIVATE = "User object is null";
	public static final String INVALID_PIPELINE_ID_PASSED = "Pipeline id is invalid";
	public static final String NULL_USERIDS = "null user ids passed";
	public static final String NULL_MANAGER_ID = "null manager id passed";
	public static final String NULL_MANAGER_DESIGNATION = "null manager designation passed";
	public static final String NULL_USER_IN_ASSIGN_OWNER = "User object is not passed";
	public static final String PRODUCT_ASSET_NOT_CREATED = "Product Asset is not created";
	public static final String PRODUCT_ID_NULL_IN_PRODUCT_ASSET = "Product id is not passed to create Product Asset";
	public static final String PRODUCT_ASSET_OBJ_NULL_IN_CREATE_PRODUCT_ASSET = "ProductAsset Object is null";
	public static final String PRODUCTSPECS_ID_NULL_IN_PRODUCTSPECS = "Product specs id is null in product specification";
	public static final String PIPELINE_IS_NOT_CREATED = "Pipeline is not created with request object";
	public static final String INVALID_PIPELINE_NAME = "Pipeline name is invalid";
	public static final String PIPELINE_STAGE_NAME_INVALID = "Pipeline stage name is invalid";
	public static final String PIPELINE_STAGE_NOT_CREATED = "PipelineStage is not created with request object";
	public static final String STAGEID_NULL_IN_STAGE_TASK_CREATION = "Stage id is null in stage task creation";
	public static final String STAGETASK_NOT_CREATED = "Stage task is not created with requested object";
	public static final String PIPELINE_IS_NOT_UPDATED = "Pipeline is not update succesfully";
	public static final String PIPELINE_STAGE_TASK_NAME_IS_INVALID = "Stage task name is invalid";
	public static final String PERSONA_NAME_IS_INVALID = "Persona name is invalid";
	public static final String ORG_CONFIG_NOT_CREATED = "Organization configuration is not created with requested object";
	public static final String ORG_CONFIG_OBJ_NULL = "OrganiztionConfiguration object is null";
	public static final String INVALID_PROPERTY_VALUE_IN_ORG_CONFIG = "Invalid property value";
	public static final String NULL_PROPERTY_VALUE_IN_ORG_CONFIG = "Property value is null";
	public static final String INVALID_PROPERTY_NAME_IN_ORG_CONFIG = "Invalid property name";
	public static final String NULL_PROPERTY_NAME_IN_ORG_CONFIG = "Property name is null";
	public static final String TEAM_IS_NOT_BELONG_TO_WITH_REQUESTED_ID = "Team is not belongs to with requested id";
	public static final String NULL_PLAYBOOK_NAME_PASSED = "Null playbook name passed";
	public static final String NULL_PLAYBOOK_ID_PASSED = "Playbok id s null";
	public static final String NULL_PLAYBOOK_KEY_ID_PASSED = "Playbok key id s null";
	public static final String NULL_STAGE_TASK_ID_PASSED = "Null stage task id passed";
	public static final String STAGE_TASK_LEVEL_IS_NULL = "Null stage task level passed";
	public static final String DIMENSION_ID_IS_NULL = "Null stage task level passed";
	public static final String PLAYBOOK_SNIPPET_ID_IS_NULL = "Null playbook snippet id passed";
	public static final String PLAYBOOK_SNIPPET_TEXT_IS_NULL = "Null playbook snippet text passed";
	public static final String PLAYBOOK_SNIPPET_TEXT_IS_INVALID = "Invalid playbook snippet text passed";
	public static final String PRODUCTSPECS_VALUE_INVALID_IN_PRODUCTSPECS = "Product Data value is invalid in product specs";
	public static final String PRODUCTSPECS_KEY_INVALID_IN_PRODUCTSPECS = "Product MetaData key is invalid in product specs";
	public static final String PERSONA_VALUE_IS_NULL = "Persona value is null in persona data";
	public static final String PERSONA_META_DATA_ID_IS_NULL = "Persona metadata id is null ";
	public static final String PERSONA_KEY_IS_INVALID = "Persona metadata key is invalid";
	public static final String PERSONA_VALUE_IS_INVALID = "Persona value is invalid in persona data";
	public static final String PERSONA_META_DATA_IS_NULL = "Persona meta data is null";
	public static final String PERSONA_DATA_IS_NULL = "Persona data is null";
	public static final String PIPELINE_ORDER_ID_IS_NULL = "Order id is null for pipeline stage creation object";
	public static final String PRODUCT_FEATURE_IS_NULL = "Null object for product feature";
	public static final String PRODUCT_FEATURE_IDS_IS_NULL = "Null id passed for product feature";
	public static final String PERSONA_DATA_ID_IS_NULL_IN_PRODUCT_FEATURE = "Persona data id is null in product feature";
	public static final String TYPE_ID_IS_NULL_IN_PRODUCT_FEATURE = "Null type id passed in product feature";
	public static final String NULL_PLAYBOOK_NODE_ID_PASSED = "Null node id passed in playbook updation";
	public static final String NODE_VALUE_NULL_PASSED = "node value is null passed";
	public static final String PERSONA_OBJ_IS_NULL = "persona object is null passed";
	public static final String NO_PIPELINES = "pipelines are null";
	public static final String NULL_LEAD_SOURCE_ID_PASSED = "Lead source id is null";
	public static final String PRODUCT_ALREADY_EXIST_WITH_SAME_NAME = "Product already exist with given name";
	public static final String SELECTED_OWNER_IS_ALREADY_ASSOCIATE_OF_USER = "selected owner is a associate of user";
	public static final String PHONE_NUMBER_VALIDATION_FAILED = "The phone number seems invalid";

	public static final String NULL_PINCODE_IN_ONBOARDING = "Null pincode";
	public static final String INVALID_PINCODE_IN_ONBOARDING = "Invalid pincode provided";
	public static final String ZOHO_TOKEN_INVALID = "Zoho token is invalid please refresh it.";
	public static final String INCORRECT_URL_FOR_ORG_WEBSITE = "Please enter correct URL for organization website";
	public static final String ORG_WEBSITE_IS_NOT_PASSED = "Please enter website for Organozation";
	public static final String ORG_WEBSITE_IS_ALREADY_EXIST = "Please enter unique website for organozation";
	public static final String NULL_USERIDS_PASSED = "null user ids passed";

}
