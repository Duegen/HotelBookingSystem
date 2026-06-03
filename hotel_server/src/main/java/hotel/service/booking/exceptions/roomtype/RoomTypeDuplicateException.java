package hotel.service.booking.exceptions.roomtype;

import hotel.model.RoomType;
import hotel.service.booking.exceptions.RoomTypeException;

public class RoomTypeDuplicateException extends RoomTypeException {
	private static final long serialVersionUID = 1L;

	public RoomTypeDuplicateException(RoomType roomType) {
		super("duplicated room type [name=%s, price per night=%.2f, capacity=%d]".formatted(roomType.getCategory(), roomType.getPricePerNight(), roomType.getCapacity()));
	}
}
