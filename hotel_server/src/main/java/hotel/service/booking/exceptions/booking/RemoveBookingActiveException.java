package hotel.service.booking.exceptions.booking;

import hotel.service.booking.exceptions.BookingException;

public class RemoveBookingActiveException extends BookingException {
	private static final long serialVersionUID = 1L;
	
	public RemoveBookingActiveException(int bookingId) {
		super("booking with id %d is active and can't be canceled".formatted(bookingId));
	}

	
	

}
