package hotel.validation.exceptions.booking;

import hotel.exceptions.ValidationException;

import java.time.LocalDate;

public class DTOBookingDateCheckInException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOBookingDateCheckInException() {
		super("Checkin date can't be earlier then %s".formatted(LocalDate.now()));
	}

}
