package hotel.service.booking.exceptions;

import hotel.exceptions.ConflictException;

public class RoomException extends ConflictException {
	private static final long serialVersionUID = 1L;

	public RoomException(String message) {
		super(message);
	}
}
