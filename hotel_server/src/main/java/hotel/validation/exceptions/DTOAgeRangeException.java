package hotel.validation.exceptions;

import hotel.exceptions.ValidationException;

public class DTOAgeRangeException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOAgeRangeException(int minAge, int maxAge) {
		super("age range '%d-%d' is invalid".formatted(minAge, maxAge));
	}

}
