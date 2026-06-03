package hotel.service.booking.exceptions.booking;

import hotel.service.booking.exceptions.BookingException;

public class BookingAlreadyExistsException extends BookingException {
	private static final long serialVersionUID = 1L;

	public BookingAlreadyExistsException(int bookingId) {
		super("booking with id %d is already exists".formatted(bookingId));
	}

	
}
