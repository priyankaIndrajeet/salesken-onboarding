package services.utils;

import pojos.SaleskenResponse;

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
