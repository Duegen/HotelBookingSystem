package hotel.service.booking.exceptions;

import hotel.exceptions.ConflictException;

public class RoomTypeException extends ConflictException{
	private static final long serialVersionUID = 1L;

	public RoomTypeException(String message) {
		super(message);
	}
	
}
