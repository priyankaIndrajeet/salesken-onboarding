package ai.salesken.onboarding.services.utils;

import ai.salesken.onboarding.model.SaleskenResponse;

public interface PhoneNumberValidateServices {
	/**
	 * See <a href=
	 * "https://github.com/ISTARSkills/javacore/wiki/PhoneNumberValidate">Phone
	 * number validate </a>
	 * 
	 * @return
	 */
	public SaleskenResponse phoneNumberValidate(String phoneNumber);
}
