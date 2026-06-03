package hotel.service.booking.exceptions.booking;

import hotel.service.booking.exceptions.BookingException;

public class RemoveExpireBookingException extends BookingException {
	private static final long serialVersionUID = 1L;

	public RemoveExpireBookingException(int bookingId) {
		super("booking with id %d is expired and can't be removed".formatted(bookingId));
	}
}
