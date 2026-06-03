package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.booking.input.RoomTypeCreateRequest;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.items.HotelItem;

public class AddRoomTypeItem extends HotelItem {

	public AddRoomTypeItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Add new room type";
	}

	@Override
	public void perform() {
		try {
			RoomTypeCreateRequest newRoomType = addRoomType();
			RoomTypeResponse createdRoomType = bookingService.createRoomType(newRoomType);
			inOut.outputlLine("Room type created: category - %s, price per night - %.2f, capacity - %d"
					.formatted(createdRoomType.category(), createdRoomType.pricePerNight(), createdRoomType.capacity()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
		
	}

}
