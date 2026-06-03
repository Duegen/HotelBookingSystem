package hotel.dto.booking.output;

import java.io.Serializable;
import java.time.LocalDate;

public record BookingResponse(
		int BookingId,
		int roomNumber,
		String caregory,
		double pricePerNight,
		int capacity,
		LocalDate checkIn,
		LocalDate checkOut) implements Serializable{

}
