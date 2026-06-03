package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.booking.input.RoomCreateRequest;
import hotel.dto.booking.output.RoomResponse;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.items.HotelItem;

import java.util.List;

public class AddRoomItem extends HotelItem {

	public AddRoomItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Add new room";
	}

	@Override
	public void perform() {
		try {
			inOut.outputlLine("List of room types:");
			List<RoomTypeResponse> roomTypes = bookingService.getRoomTypesDTO();
			showRoomTypes(roomTypes, "No available room types are found");
			RoomCreateRequest newRoom = addRoom();
			RoomResponse createdRoom = bookingService.createRoom(newRoom);
			inOut.outputlLine("Room created: room number - %d, category - %s, price per night - %.2f, capacity - %d"
					.formatted(createdRoom.RoomNumber(), createdRoom.category(), createdRoom.pricePerNight(), createdRoom.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
		
	}

}
