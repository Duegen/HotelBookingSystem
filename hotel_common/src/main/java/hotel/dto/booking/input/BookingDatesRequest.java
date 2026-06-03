package hotel.dto.booking.input;

import java.io.Serializable;
import java.time.LocalDate;

public record BookingDatesRequest(
		LocalDate checkIn,
		LocalDate checkOut) implements Serializable{}
