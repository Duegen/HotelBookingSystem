package hotel.service.booking.exceptions;

import hotel.exceptions.ConflictException;

public class BookingException extends ConflictException {
	private static final long serialVersionUID = 1L;

	public BookingException(String message) {
		super(message);
	}

	
}
