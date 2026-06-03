package hotel.validation.exceptions.guest;

import hotel.exceptions.ValidationException;

public class DTOGuestPasswordFormatException extends ValidationException {
	private static final long serialVersionUID = 1L;
	
	public DTOGuestPasswordFormatException() {
		super("password validation error: invalid password format");
	}
}
