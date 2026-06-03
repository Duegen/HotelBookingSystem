package hotel.validation.exceptions.roomtype;

import hotel.exceptions.ValidationException;

public class DTORoomTypeCapacityException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTORoomTypeCapacityException(int capacity, int roomMinCap, int roomMaxCap) {
		super("room type validation error: invalid capacity '%d', capacity should be %d-%d".formatted(capacity, roomMinCap, roomMaxCap));
	}
}
