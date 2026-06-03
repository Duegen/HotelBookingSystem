package hotel.dto.booking.input;

import java.io.Serializable;

public record BookingIdRequest(
		Integer bookingId) implements Serializable{}
