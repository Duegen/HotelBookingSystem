package hotel.service.booking.exceptions.booking;

import hotel.service.booking.exceptions.BookingException;

public class NoFreeBookingIdException extends BookingException {
	private static final long serialVersionUID = 1L;

	public NoFreeBookingIdException() {
		super("no free booking ids are found");
	}
	
	
}
