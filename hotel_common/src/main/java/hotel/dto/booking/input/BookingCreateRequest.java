package hotel.dto.booking.input;

import java.io.Serializable;
import java.time.LocalDate;

public record BookingCreateRequest(
	int guestId,
	int roomNumber,
	LocalDate checkIn,
	LocalDate checkOut) implements Serializable{}
