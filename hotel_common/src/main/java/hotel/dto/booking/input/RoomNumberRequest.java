package hotel.dto.booking.input;

import java.io.Serializable;

public record RoomNumberRequest(
		Integer roomNumber) implements Serializable{}
