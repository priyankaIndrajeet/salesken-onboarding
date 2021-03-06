package ai.salesken.onboarding.utils.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ai.salesken.onboarding.model.ValidateResponse;
import ai.salesken.onboarding.utils.Validator;
import ai.salesken.onboarding.utils.DButils.DBProperties;

public class PhoneNumberValidator implements Validator {
	private static final Logger logger = LogManager.getLogger(PhoneNumberValidator.class);

	@Override
	public ValidateResponse validate(String rawPhoneNumber) {
		// TODO Facing maven issues adding twilio pom in this project so using appie as
		// microservice, also the user may not have an org configuration so sticking to
		// a user who already does
		ValidateResponse response = new ValidateResponse();
		try {
			String sendGet = new URLUtilImpl().sendGet(DBProperties.getProperty("BASE_URL")
					+ "utils?method=PhoneNumberValidator&user=218782&phoneNumber=" + rawPhoneNumber);
			JsonObject asJsonObject = new JsonParser().parse(sendGet).getAsJsonObject();
			response.setIsSuccess(asJsonObject.get("success").getAsBoolean());
			if (asJsonObject.get("success").getAsBoolean())
				response.setSuccessMessage(asJsonObject.get("number").getAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
