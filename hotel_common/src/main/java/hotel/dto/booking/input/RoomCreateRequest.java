package hotel.dto.booking.input;

import java.io.Serializable;

public record RoomCreateRequest(
		int roomNumber,
		int roomTypeId) implements Serializable{}
