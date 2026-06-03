package hotel.validation.exceptions.guest;

import hotel.exceptions.ValidationException;

public class DTOGuestIdException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOGuestIdException(int id, int guestMinId, int guestMaxId) {
		super("guest id validation error: invalid guest id value '%d', id must be %d-%d".formatted(id, guestMinId, guestMaxId));
	}
}
