package hotel.validation.exceptions.booking;

import hotel.exceptions.ValidationException;

public class DTOBookingCheckOutException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOBookingCheckOutException() {
		super("check out date validation error: check out date can't be null");
	}
}
