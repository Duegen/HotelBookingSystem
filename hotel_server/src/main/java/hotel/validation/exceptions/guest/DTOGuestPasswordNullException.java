package hotel.validation.exceptions.guest;

import hotel.exceptions.ValidationException;

public class DTOGuestPasswordNullException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOGuestPasswordNullException() {
		super("password validation error: password dto can't be null");
	}

}
