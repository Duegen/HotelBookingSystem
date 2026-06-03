package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.booking.output.RoomResponse;
import hotel.items.HotelItem;

import java.util.List;

public class ShowRoomsItem extends HotelItem {

	public ShowRoomsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Show rooms";
	}

	@Override
	public void perform() {
		inOut.outputlLine("Rooms list:");
		List<RoomResponse> rooms = bookingService.getRoomsDTO();
		showRooms(rooms, "no rooms are found");
	}

}
