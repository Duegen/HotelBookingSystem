package hotel.dto.booking.input;

import java.io.Serializable;

public record RoomTypeCreateRequest(
	String category,
	double pricePerNight,
	int capacity) implements Serializable{}
