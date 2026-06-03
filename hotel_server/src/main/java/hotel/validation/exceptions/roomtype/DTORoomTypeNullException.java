package hotel.validation.exceptions.roomtype;

import hotel.exceptions.ValidationException;

public class DTORoomTypeNullException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTORoomTypeNullException() {
		super("room type validation error: DTO can't be null");
	}
	
	
}
