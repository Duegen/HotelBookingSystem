package hotel.service.booking.exceptions.room;

import hotel.service.booking.exceptions.RoomException;

public class RoomNotFoundException extends RoomException {
	private static final long serialVersionUID = 1L;
	
	public RoomNotFoundException(int roomNumber) {
		super("Room with id %d not found".formatted(roomNumber));
	}
}
