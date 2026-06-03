package hotel.service.booking.exceptions.roomtype;

import hotel.service.booking.exceptions.RoomTypeException;

public class RoomTypeNotFoundException extends RoomTypeException {
	private static final long serialVersionUID = 1L;

	public RoomTypeNotFoundException(int typeId) {
		super("room type with id %d not found".formatted(typeId));
	}

}
