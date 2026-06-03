package hotel.validation.exceptions;

import hotel.exceptions.ValidationException;

public class DTOAgeRangeNullException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOAgeRangeNullException() {
		super("DTO can't be null");
	}

}
