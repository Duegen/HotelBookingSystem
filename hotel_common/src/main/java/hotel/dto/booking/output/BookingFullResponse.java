package hotel.dto.booking.output;

import hotel.dto.account.output.GuestResponse;

import java.io.Serializable;
import java.time.LocalDate;

public record BookingFullResponse(
	int BookingId,
	GuestResponse guest,
	int roomNumber,
	RoomTypeResponse roomType,
	LocalDate checkIn,
	LocalDate checkOut) implements Serializable{
}
