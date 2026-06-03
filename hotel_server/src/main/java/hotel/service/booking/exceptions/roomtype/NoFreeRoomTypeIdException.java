package hotel.service.booking.exceptions.roomtype;

import hotel.service.booking.exceptions.RoomTypeException;

public class NoFreeRoomTypeIdException extends RoomTypeException {
	private static final long serialVersionUID = 1L;

	public NoFreeRoomTypeIdException() {
		super("no free room type ids are found");
	}
}
