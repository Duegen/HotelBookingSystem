package hotel.service.booking.exceptions.room;

import hotel.service.booking.exceptions.RoomException;

public class RoomAlreadyExistsException extends RoomException{
	private static final long serialVersionUID = 1L;

	public RoomAlreadyExistsException(int roomNumber) {
		super("room with number %d is already existsts".formatted(roomNumber));
	}
}
