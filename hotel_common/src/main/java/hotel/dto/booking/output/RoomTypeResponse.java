package hotel.dto.booking.output;

import java.io.Serializable;

public record RoomTypeResponse(
		int roomTypeId,
		String category,
		double pricePerNight,
		int capacity) implements Serializable{}
