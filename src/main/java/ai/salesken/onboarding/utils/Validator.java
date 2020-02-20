package ai.salesken.onboarding.utils;

import ai.salesken.onboarding.model.ValidateResponse;

public interface Validator {
	public ValidateResponse validate(String value);
}
