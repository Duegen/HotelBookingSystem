package hotel.validation.exceptions.guest;

import hotel.exceptions.ValidationException;

public class DTOLoginNullException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOLoginNullException() {
		super("login validation error: login dto can't be null");
	}

}
