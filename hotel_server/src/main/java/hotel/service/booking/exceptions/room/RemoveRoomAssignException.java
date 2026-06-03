package hotel.service.booking.exceptions.room;

import hotel.service.booking.exceptions.RoomException;

public class RemoveRoomAssignException extends RoomException {
	private static final long serialVersionUID = 1L;

	public RemoveRoomAssignException(int roomNumber) {
		super("room with number %d has related bookings and can't be removed".formatted(roomNumber));
	}

}
