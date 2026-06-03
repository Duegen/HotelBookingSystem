package hotel.service.booking.exceptions.roomtype;

import hotel.service.booking.exceptions.RoomTypeException;

public class RoomTypeAlreadyExistsException extends RoomTypeException {
	private static final long serialVersionUID = 1L;

	public RoomTypeAlreadyExistsException(int roomTypeId) {
		super("room type with id %d is already existsts".formatted(roomTypeId));
	}

}
