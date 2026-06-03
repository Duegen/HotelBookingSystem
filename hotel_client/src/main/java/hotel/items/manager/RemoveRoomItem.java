package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.booking.input.RoomNumberRequest;
import hotel.dto.booking.output.RoomResponse;
import hotel.items.HotelItem;

import java.util.List;
import java.util.Objects;

public class RemoveRoomItem extends HotelItem {

	public RemoveRoomItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Remove room";
	}

	@Override
	public void perform() {
		try {
			List<RoomResponse> roomTypes = bookingService.getRoomsDTO();
			inOut.outputlLine("Rooms list:");
			showRooms(roomTypes, "no room types are found");
			RoomNumberRequest roomId = getExistingRoom();
			if(Objects.isNull(roomId))
				return;
			RoomResponse removed = bookingService.removeRoom(roomId);
			inOut.output("Room removed: room number - %d, category - %s, price per night - %.2f, capacity - %d"
					.formatted(removed.RoomNumber(), removed.category(), removed.pricePerNight(), removed.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}

	}

}
