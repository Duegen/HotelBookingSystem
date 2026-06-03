package hotel.service.account.exceptions;

import hotel.exceptions.ConflictException;

public class GuestException extends ConflictException {
	private static final long serialVersionUID = 1L;

	public GuestException(String message) {
		super(message);
	}
}
