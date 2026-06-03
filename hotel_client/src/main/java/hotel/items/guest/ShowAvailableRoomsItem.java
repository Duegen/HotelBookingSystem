package hotel.items.guest;

import hotel.HotelApplContext;
import hotel.dto.booking.input.BookingDatesRequest;
import hotel.dto.booking.output.RoomResponse;
import hotel.items.HotelItem;

import java.util.List;
import java.util.Objects;

public class ShowAvailableRoomsItem extends HotelItem{

	public ShowAvailableRoomsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Available rooms list";
	}

	@Override
	public void perform() {
		try {
			BookingDatesRequest dates = inputCheckInCheckOut();
			if(Objects.isNull(dates))
				return;
			List<RoomResponse> availableRooms = bookingService.getAvailableRooms(dates);
			
			if(availableRooms.isEmpty()) {
				inOut.outputlLine("No available rooms for selected dates");
				return;
			}
			inOut.outputlLine("Available rooms for dates %s -> %s: ".formatted(dates.checkIn().toString(), dates.checkOut().toString()));
			showRooms(availableRooms, "No available rooms for selected dates");
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
		
	}

}
