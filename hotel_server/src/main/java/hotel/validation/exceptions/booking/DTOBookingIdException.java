package hotel.validation.exceptions.booking;

import hotel.exceptions.ValidationException;

public class DTOBookingIdException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOBookingIdException(int bookingId, int bookingMinId, int bookingMaxId) {
		super("booking id validation error: invalid id value '%d', id must be %d-%d".formatted(bookingId, bookingMinId, bookingMaxId));
	}

}
