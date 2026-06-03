package hotel.service.booking.exceptions.booking;

import hotel.service.booking.exceptions.BookingException;

public class BookingNotFoundException extends BookingException {
	private static final long serialVersionUID = 1L;
	
	public BookingNotFoundException(int bookingId) {
		super("booking with id %d not found".formatted(bookingId));
	}
	

}
