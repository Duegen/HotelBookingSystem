package hotel.interfaces;

import hotel.dto.booking.input.AgeRangeRequest;
import hotel.dto.booking.input.DateRequest;
import hotel.dto.booking.output.BookingResponse;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.exceptions.ValidationException;

import java.util.List;

public interface IAnalyticsCommon {
	double averageBookingPrice();
	List<RoomTypeResponse> mostPopularRoomTypes();
	int getAvailableRoomsCount(DateRequest date);
	int getOccupiedRoomsCount(DateRequest date);
	List<RoomTypeResponse> getMostPopularRoomTypesForAgeRange(AgeRangeRequest ageRange) throws ValidationException;
	int bookingsNumber();
	List<BookingResponse> showBookingsByCheckIn(DateRequest date);
}
