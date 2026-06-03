package hotel.validation.exceptions.guest;

import hotel.exceptions.ValidationException;

public class DTOGuestEmailFormatException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOGuestEmailFormatException(String email) {
		super("email validation error: invalid email format '%s'".formatted(email));
	}

}
