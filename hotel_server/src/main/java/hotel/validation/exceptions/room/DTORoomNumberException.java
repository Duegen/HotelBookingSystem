package hotel.validation.exceptions.room;

import hotel.exceptions.ValidationException;

public class DTORoomNumberException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTORoomNumberException(int roomNumber, int roomMinNumber, int roomMaxNumber) {
		super("room validation error: invalid room number '%d': room number should be %d-%d".formatted(roomNumber, roomMinNumber, roomMaxNumber));
	}
}
