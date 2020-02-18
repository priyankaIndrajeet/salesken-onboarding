package services.utils.impl;

import pojos.SaleskenResponse;
import services.utils.PhoneNumberValidateServices;

public class PhoneNumberValidateServicesImpl implements PhoneNumberValidateServices {

	@Override
	public SaleskenResponse phoneNumberValidate(String phoneNumber) {
		SaleskenResponse response = null;

		/*-if (phoneNumber != null) {
			PhoneNumberValidateDAO pnvdao = new PhoneNumberValidateDAOPG();
			if (pnvdao.phoneNumberValidate(phoneNumber)) {
				response = new SaleskenResponse(AssociateResponseCodes.SUCCESS, AssociateResponseMessages.SUCCESS);
			} else {
				response = new SaleskenResponse(AssociateResponseCodes.INVALID_PHONENUMBER_IN_PHONENUMBER_VALIDATE,
						AssociateResponseMessages.INVALID_PHONENUMBER_IN_PHONENUMBER_VALIDATE);
		
			}
		} else {
			response = new SaleskenResponse(AssociateResponseCodes.NULL_PHONENUMBER_IN_PHONENUMBER_VALIDATE,
					AssociateResponseMessages.NULL_PHONENUMBER_IN_PHONENUMBER_VALIDATE);
		
		}*/

		return response;
	}
}
