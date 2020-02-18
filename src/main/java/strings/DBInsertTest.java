package strings;

public class DBInsertTest {
	public static void main(String[] args) {
		String address = "INSERT INTO address (  addressline1 ,  addressline2 ,  pincode_id ,  address_geo_longitude ,  address_geo_latitude ,  pincode ) VALUES ( ?, ?, ?, ?, ?, ?);";
		String billing_temp = "INSERT INTO billing_temp  (CustomerName ,  Nooflicensespurchased ,  ContractType ,  BillingCurrency ,  Price_per_license ,  VOIP_Billing ,  Implementation_Services ,  Implementation_Fee ,  Total_Value_(INR) ,  Billing_Frequency ,  Payment_Terms ,  Contract_start_date ,  Payment_Method ) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String bulk_license = "INSERT INTO bulk_license  (  createdat ,  description ,  imageurl ,  isdeleted ,  name_ ,  updatedat ,  expiryon ,  licensecount ,  stripesubscriptionid ,  licensetype_id ,  organization_id ) VALUES ( ?, ?, ?, ?, ?,?,?,?,?,?,?)";
		String calling_issues = "INSERT INTO calling_issues  (  sid ,  reason_ ,  actor ,  cx_actor ,  status ,  created_at ,  updated_at ) VALUES ( ?,?,?,?,?,?,?);";
		String cluster_job = "INSERT INTO  public . cluster_job  (   name_ ,  is_active ,  organization_id ,  created_at ,  updated_at ,  type_id ,  speaker_choice ) VALUES (  ?,?,?,?,?,?,?);";
		String cluster_job_task = "INSERT INTO cluster_job_task ( job_id ,  task_id ) VALUES (?,?);";
		String cluster_keywords = "INSERT INTO public.cluster_keywords (  keyword, node_id) VALUES ( ?, ?)";
		String cluster_node = "INSERT INTO public.cluster_node (  job_id, parent_node_id, name_, created_at, image_url) VALUES (?,?,?,?,? );";
		String cluster_node_block = "INSERT INTO cluster_node_block (node_id, block_id) VALUES (?,?);";
		String cluster_node_snippets = "INSERT INTO cluster_node_snippets(node_id, snippet_id) VALUES (?,?);";
		String conversation_block = "INSERT INTO public.conversation_block (  text_, task_id, from_time, to_time, speaker, confidence, created_at) VALUES (?,?,?,?,?,?,?);";
		String country_entry = "INSERT INTO public.country_entry ( city, ccflips_code, country_name, country_code, timezone, utc) VALUES ( ?,?,?,?,?,?);";
		String crm_question = "INSERT INTO public.crm_question ( product_id, question) VALUES (?,?)";
		String csharp_version = "INSERT INTO public.csharp_version ( version, download_url, name, created_at, updated_at) VALUES (?,?,?,?,?)";
		String faq = "INSERT INTO public.faq ( question, answer, keywords, product_id, type, type_of_relationship) VALUES (?,?,?,?,?,? );";
		String faq_keyword = "INSERT INTO public.faq_keyword ( keyword, faq_id) VALUES (?,?);";
		String faq_shown = "INSERT INTO public.faq_shown (  temp_snippet_id, task_id, score, match_method, shown_at, snippet_text, faq_keyword_id, created_at, snippet_id) VALUES (?,?,?,?,?,?,?,?,?);";
		String group_user = "INSERT INTO public.group_user (qroupid, userid) VALUES (?,?)";
		String industry_type = "INSERT INTO public.industry_type (  name) VALUES ( ?)";
		String integration_metadata = "INSERT INTO public.integration_metadata ( salesken_property_name, salesken_property_value, integration_provider, integration_property_name, integration_property_value) VALUES (?,?,?,?,?)";
		String invite = "INSERT INTO public.invite (  email, token, status) VALUES ( ?,?,?)";
		String invoice = "INSERT INTO public.invoice ( createdat, description, imageurl, isdeleted, name_, updatedat, amount, duedate, status, stripeinvoiceid, bulklicense_id) VALUES ( ?,?,?,?,?,?,?,?,?,?,?)";
		String istar_group = "INSERT INTO public.istar_group (  created_at, name, updated_at, organization_id, description, group_type, group_mode_type, is_deleted, owner) VALUES (?,?,?,?,?,?,?,?,? )";
		String istar_user = "INSERT INTO public.istar_user (id, email, password, created_at, mobile, auth_token, login_type, is_verified, is_supend, is_supended, is_deleted, show_real_time_notification, fcm_token, department, designation_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String job = "INSERT INTO public.job (  name, created_on) VALUES (?,?  )";
		String job_tasks = "INSERT INTO public.job_tasks (  name, url, created_on, job_id, status, completed_on, retry_count, task_id) VALUES (?,?,?,?,?,?,?,?)";
		String lead = "INSERT INTO public.lead ( owner, actor, stage, product_id, lead_source, company_name, address, created_at, updated_at, status, country, state, city, pin_code, reason, value, company_details, company_website, timezone) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String license_issued = "INSERT INTO public.license_issued ( createdat, description, imageurl, isdeleted, name_, updatedat, expiryon, licensekey, bulklicense_id, user_id) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String license_type = "INSERT INTO public.license_type (  createdat, description, imageurl, isdeleted, name_, updatedat, currency, price, stripeproductid) VALUES ( )";
		String notification = "INSERT INTO public.notification ( type, to_user, from_user, is_read, body, created_at, click_action) VALUES ( ?, ?, ?, ?, ?, ?, ?);";
		String number_change_request = "INSERT INTO public.number_change_request ( raised_by, call_sid, validation_code, new_number, is_active, is_successful, created_at, updated_at) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);";
		String org_browser = "INSERT INTO public.org_browser (org_id, javascript_snippet, crm_name) VALUES ( ?, ?, ?);";
		String org_user = "INSERT INTO public.org_user (organizationid, userid) VALUES (?, ?);";
		String organization = "INSERT INTO public.organization ( name, organization_type, industry, profile, image, created_at, updated_at, website, founded, contact_name, contact_email, contact_phone, employee_count, address_line_1, address_line_2, socialsite, landmark, pincode_id, address_id) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String pincode = "INSERT INTO public.pincode (city, country, pin, state, lattiude, longitude, state_code) VALUES ( ?, ?, ?, ?, ?, ?, ?);";
		String pipeline = "INSERT INTO public.pipeline ( xml, created_at, updated_at, name, organization_id, is_active) VALUES ( ?, ?, ?, ?, ?, ?);";
		String pipeline_product = "INSERT INTO pipeline_product(pipeline_id, product_id) VALUES (?,?)";
		String pipeline_stage = "INSERT INTO public.pipeline_stage (stage_name, created_at, updated_at, pipeline_id, deleted, order_id) VALUES ( ?, ?, ?, ?, ?, ?);";
		String pipeline_team = "INSERT INTO pipeline_team (pipeline_id, team_id) VALUES (?,?)";

		String product = "INSERT INTO public.product ( name, organization_id, created_at, updated_at, signal_color, description, image, deleted, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String product_asset = "INSERT INTO public.product_asset ( product_id, asset_type, asset_url, asset_value, asset_name, created_at, updated_at, is_active) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);";
		String product_signal = "INSERT INTO public.product_signal ( name, color, value, product_id, created_at, updated_at, is_active, type, engine, match_type, do_generate) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String role = "INSERT INTO role ( role_name, created_at) VALUES ( ?, ?);";
		String sales_contact_person = "INSERT INTO public.sales_contact_person (name, email, phone_number, lead_id, office_phone_number, company_name, city, state, created_at, updated_at, language_pref, country, job_title) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String sales_favorite = "INSERT INTO public.sales_favorite ( task_id, is_favorite, created_at, updated_at, user_id) VALUES ( ?, ?, ?, ?, ?);";
		String sales_ideal = "INSERT INTO public.sales_ideal (task_id, is_ideal, created_at, updated_at, user_id) VALUES ( ?, ?, ?, ?, ?);";
		String sales_manager_profile = "INSERT INTO sales_manager_profile  ( timezone, location, language, currency, user_id, language_pref, check_point_questions) VALUES ( ?, ?, ?, ?, ?, ?, ?);";
		String sales_user_task_signal = "INSERT INTO public.sales_user_task_signal ( name, value, user_id, task_id, signal_id, created_at, actor) VALUES ( ?, ?, ?, ?, ?, ?, ?);";
		String signal_caught = "INSERT INTO signal_caught (snippet_id, signal_generated_id, score, match_method) VALUES (?, ?, ?, ?);";
		String signal_generated = "INSERT INTO public.signal_generated ( signal_id, text_, created_at, snippets_id, is_active) VALUES (?, ?, ?, ?, ?);";

		String snippet = "INSERT INTO public.snippet ( from_time, to_time, confidence, text_, speaker, task_id) VALUES ( ?, ?, ?, ?, ?, ?);";
		String snippet_asr = "INSERT INTO public.snippet_asr ( snippet_id, from_time, to_time, confidence, text_, words, speaker, task_id, created_at, created_by) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String stage_task = "INSERT INTO public.stage_task ( task_name, task_type, description, created_at, updated_at, stage_id, deleted) VALUES (?, ?, ?, ?, ?, ?, ?);";
		String task = "INSERT INTO public.task ( name, description, priority, owner, actor, status, start_date, end_date, duration_in_hours, assignee_group, assignee_member, is_repeatative, followup_date, is_active, tags, created_at, updated_at, is_timed_task, follow_up_duration_in_days, task_type, lead_id, call_duration, score, latitude, longitude, analytics, call_rating, sales_contact_id, pipeline_id, stage_id, voice_quality, talk_ratio, sentiment, special_score, disposition, callsid, direction, cost, agent_amplitude, customer_amplitude, audio_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String task_comment = "INSERT INTO public.task_comment ( task_id, user_id, comment, created_at, updated_at, comment_type, snippet_id, snippet_time) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);";
		String temp_snippet = "INSERT INTO public.temp_snippet ( text, task_id, speaker, from_time, is_final, to_time) VALUES ( ?, ?, ?, ?, ?, ?);";
		String textual_data = "INSERT INTO textual_data ( original_text, changed_text, score, use_score, roberta_score, roberta_trained) VALUES ( ?, ?, ?, ?, ?, ?);";
		// Need to delete
		String textual_data1 = "INSERT INTO public.textual_data1 ( original_text, changed_text, use_score) VALUES ( ?, ?, ?);";
		String tomcat_sessions = "INSERT INTO public.tomcat_sessions (session_id, valid_session, max_inactive, last_access, app_name, session_data) VALUES (?, ?, ?, ?, ?, ?);";
		String twilio_call_log = "INSERT INTO public.twilio_call_log ( sid, duration, country, task_id, price, price_unit, call_from, call_to, parent_call_sid, direction, start_time, status, asr, nlu) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String twilio_js_log = "INSERT INTO public.twilio_js_log (sid, user_id, message, created_at) VALUES ( ?, ?, ?, ?);";
		String twilio_must_allow_numbers = "INSERT INTO public.twilio_must_allow_numbers ( phone_number, is_active, created_at) VALUES ( ?, ?, ?);";
		String user_login_log = "INSERT INTO public.user_login_log (user_id, created_at, action,  city) VALUES ( ?, ?, ?, ?);";
		String user_manager = "INSERT INTO user_manager (user_id, manager_id) VALUES (?, ?);";
		String user_profile = "INSERT INTO user_profile (address_id, name, dob, gender, profile_image, user_id, aadhar_no, father_name, mother_name, user_category, religion, caste_category, place_of_birth, twilio_number, sip_username, sip_password, sip_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String user_role = "INSERT INTO user_role(userid, roleid) VALUES ('198101', '13');";
		String user_sip = "INSERT INTO public.user_sip ( user_id, sip_username, sip_password, sip_url, provider, is_active) VALUES ( ?, ?, ?, ?, ?, ?);";
		String vocab_test = "INSERT INTO vocab_test ( test_text, reference_text, user_agreement, status, score, created_at, type_of_match, http_session) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);";
		String welcome_message = "INSERT INTO public.welcome_message ( message, organization_id, title, type) VALUES ( ?, ?, ?, ?);";

		String advanced_playbook_dimension = "INSERT INTO public.advanced_playbook_dimension ( name_, created_at) VALUES ( ?, ?);";
		String advanced_playbook_level = "INSERT INTO advanced_playbook_level (  level, advanced_playbook_id, dimension_id, speaker) VALUES (  ?, ?, ?, ?);";
		String advanced_playbook_node = "INSERT INTO public.advanced_playbook_node ( advanced_playbook_id, snippet_text, created_at, updated_at, level_id) VALUES (  ?, ?, ?, ?, ?);";
		String advanced_playbook_node_mapping = "INSERT INTO advanced_playbook_node_mapping  (child_node_id, parent_node_id) VALUES (?, ?);";
		String advanced_playbook_node_traversal = "INSERT INTO public.advanced_playbook_node_traversal ( snippet_id, task_id, callsid, score, match_method, shown_at, snippet_text, snippet_speaker, advanced_playbook_node_id, created_at) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String lead_source = "INSERT INTO public.lead_source (name, created_at, updated_at, image_url) VALUES ( ?, ?, ?, ?);";
		String organization_designation = "INSERT INTO public.organization_designation (  designation, organization_id) VALUES (  ?, ?);";
		String persona = "INSERT INTO public.persona (  name, image, created_at, updated_at, is_deleted, organization_id) VALUES (  ?, ?, ?, ?, ?, ?);";
		String persona_data = "INSERT INTO public.persona_data (  persona_id, value, created_at, updated_at, metadata_id) VALUES ( ?, ?, ?, ?, ?);";
		String persona_metadata = "INSERT INTO public.persona_metadata (id, key, persona_id, created_at, updated_at, relation_type_id) VALUES (?, ?, ?, ?, ?, ?);";
		String persona_relation_data = "INSERT INTO persona_relation_data (type_id, persona_data_id, corresponding_id) VALUES (?, ?, ?);";
		String pipeline_lead_source = "INSERT INTO pipeline_lead_source (pipeline_id, lead_source_id) VALUES (?, ?);";
		String pipeline_persona = "INSERT INTO pipeline_persona (pipeline_id, persona_id) VALUES (?, ?);";
		String product_data = "INSERT INTO public.product_data ( product_id, value, created_at, updated_at, metadata_id) VALUES (?, ?, ?, ?, ?);";
		String product_metadata = "INSERT INTO public.product_metadata ( key, product_id, created_at, updated_at) VALUES ( ?, ?, ?, ?);";
		String simple_playbook = "INSERT INTO public.simple_playbook ( organization_id, created_by, created_at, updated_at) VALUES ( ?, ?, ?, ?);";
		String simple_playbook_activation_keyword = "INSERT INTO public.simple_playbook_activation_keyword ( simple_playbook_id, keyword_text, created_at, updated_at) VALUES ( ?, ?, ?, ?);";
		String simple_playbook_caught = "INSERT INTO public.simple_playbook_caught ( temp_snippet_id, simple_playbook_activation_keyword_id, task_id, snippet_id, score, match_method, shown_at, snippet_text, created_at) VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		String simple_playbook_node = "INSERT INTO public.simple_playbook_node ( playbook_id, created_by, speaker, question, answer, created_at, updated_at) VALUES ( ?, ?, ?, ?, ?, ?, ?);";

	}
}
