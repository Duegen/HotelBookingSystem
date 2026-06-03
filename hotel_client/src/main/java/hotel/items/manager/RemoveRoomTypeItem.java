package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.booking.input.RoomTypeIdRequest;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.items.HotelItem;

import java.util.List;
import java.util.Objects;

public class RemoveRoomTypeItem extends HotelItem {

	public RemoveRoomTypeItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Remove room type";
	}

	@Override
	public void perform() {
		try {
			List<RoomTypeResponse> roomTypes = bookingService.getRoomTypesDTO();
			inOut.outputlLine("Room types list:");
			showRoomTypes(roomTypes, "no room types are found");
			RoomTypeIdRequest roomTypeId = getExistingRoomType();
			if(Objects.isNull(roomTypeId))
				return;
			RoomTypeResponse removed = bookingService.removeRoomType(roomTypeId);
			inOut.output("Room type removed: room type id - %d, category - %s, price per night - %.2f, capacity - %d"
					.formatted(removed.roomTypeId(), removed.category(), removed.pricePerNight(), removed.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
	}

}
