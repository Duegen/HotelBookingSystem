package hotel.items.accountant;

import hotel.HotelApplContext;
import hotel.dto.booking.input.DateRequest;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.items.HotelItem;

import java.time.LocalDate;
import java.util.List;

public class ShowAllStatisticsItem extends HotelItem {

	public ShowAllStatisticsItem(HotelApplContext context) {
		super(context);
		
	}

	@Override
	public String displayName() {
		return "Show all statistics";
	}

	@Override
	public void perform() {
		LocalDate today = LocalDate.now();
		inOut.outputlLine("Bookings count: "+ analyticsService.bookingsNumber());
		inOut.outputlLine("Average booking price: "+ analyticsService.averageBookingPrice());
		List<RoomTypeResponse> popularRT = analyticsService.mostPopularRoomTypes();
		inOut.outputlLine("Most popular room types:");
		popularRT.stream().forEach(rt -> inOut.outputlLine("room type id - %d, category - %s, price per night - %.2f, capacity - %d"
				.formatted(rt.roomTypeId(), rt.category(), rt.pricePerNight(), rt.capacity())));
		inOut.outputlLine("Available rooms today: "+ 
		analyticsService.getAvailableRoomsCount(new DateRequest(today)));
		inOut.outputlLine("Occupied rooms today: "+ 
				analyticsService.getOccupiedRoomsCount(new DateRequest(today)));
	}


}
