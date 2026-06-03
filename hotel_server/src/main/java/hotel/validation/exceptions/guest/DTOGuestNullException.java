package hotel.validation.exceptions.guest;

import hotel.exceptions.ValidationException;

public class DTOGuestNullException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOGuestNullException() {
		super("guest dto validation error: dto can't be null");
	}
}
