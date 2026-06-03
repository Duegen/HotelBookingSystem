package hotel.dto.booking.output;

import java.io.Serializable;

public record RoomResponse(
		int RoomNumber,
		String category,
		double pricePerNight,
		int capacity) implements Serializable {}
