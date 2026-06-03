package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.items.HotelItem;

import java.util.List;

public class ShowRoomTypesItem extends HotelItem {

	public ShowRoomTypesItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Show room types";
	}

	@Override
	public void perform() {
		inOut.outputlLine("Room types list:");
		List<RoomTypeResponse> roomTypes = bookingService.getRoomTypesDTO();
		showRoomTypes(roomTypes, "no room types are found");
	}

}
