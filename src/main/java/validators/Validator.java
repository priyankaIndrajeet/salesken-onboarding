package validators;

import pojos.ValidateResponse;

public interface Validator {
	public ValidateResponse validate(String value);
}
