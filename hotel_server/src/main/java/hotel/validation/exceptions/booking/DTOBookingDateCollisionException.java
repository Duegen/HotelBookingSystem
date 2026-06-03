package hotel.validation.exceptions.booking;

import hotel.exceptions.ValidationException;

import java.time.LocalDate;

public class DTOBookingDateCollisionException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOBookingDateCollisionException(LocalDate checkIn, LocalDate checkOut) {
		super("dates validation error: check in date '%s' can't be after check out date '%s'".formatted(checkIn, checkOut));
	}
}
