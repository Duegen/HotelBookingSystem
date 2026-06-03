package hotel.validation.exceptions.booking;

import hotel.exceptions.ValidationException;

public class DTOBookingNullException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOBookingNullException() {
		super("booking dto validation error: dto can't be null");
	}
}
