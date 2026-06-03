package hotel.service.booking.exceptions.roomtype;

import hotel.service.booking.exceptions.RoomTypeException;

public class RemoveRoomTypeAssignException extends RoomTypeException {
	private static final long serialVersionUID = 1L;

	public RemoveRoomTypeAssignException(int roomTypeId) {
		super("room type with id %d has related rooms and can't be removed".formatted(roomTypeId));
	}

}
