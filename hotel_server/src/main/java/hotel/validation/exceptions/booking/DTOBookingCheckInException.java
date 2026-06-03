package hotel.validation.exceptions.booking;

import hotel.exceptions.ValidationException;

public class DTOBookingCheckInException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOBookingCheckInException() {
		super("check in date validation error: check in date can't be null");
	}

}
