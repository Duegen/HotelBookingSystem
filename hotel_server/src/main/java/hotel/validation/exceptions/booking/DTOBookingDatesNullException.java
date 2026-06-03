package hotel.validation.exceptions.booking;

import hotel.exceptions.ValidationException;

public class DTOBookingDatesNullException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOBookingDatesNullException() {
		super("dates dto validation error: dates dto can't be null");
	}
}
