package hotel.items.accountant;

import hotel.HotelApplContext;
import hotel.dto.booking.input.AgeRangeRequest;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.items.HotelItem;

import java.util.List;

public class ShowMostPopularRoomForRangeAgeItem extends HotelItem {

	public ShowMostPopularRoomForRangeAgeItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Show most popular room for age range";
	}

	@Override
	public void perform() {
		
		try {
			Integer minAge = inOut.inputInteger("Enter minimum age");
		if(minAge==null)
			return;
		Integer maxAge = inOut.inputInteger("Enter maximum age");
		if(maxAge==null)
			return;
			List<RoomTypeResponse> roomTypes;
			roomTypes = analyticsService.getMostPopularRoomTypesForAgeRange(new AgeRangeRequest(minAge, maxAge));
			if (roomTypes == null || roomTypes.size() == 0) {
				inOut.outputlLine("No rooms found for selected age range");
				return;
			}
			inOut.outputlLine("Most popular room types for age range " + minAge + " - " + maxAge);
			for (RoomTypeResponse roomType : roomTypes)
				inOut.outputlLine("room type id - %d, category - %s, price per night - %.2f, capacity - %d".formatted(
						roomType.roomTypeId(), roomType.category(), roomType.pricePerNight(), roomType.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
		
	}


}
