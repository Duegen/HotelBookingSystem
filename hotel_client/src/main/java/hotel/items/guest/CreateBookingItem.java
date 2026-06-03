package hotel.items.guest;

import hotel.HotelApplContext;
import hotel.dto.booking.input.BookingCreateRequest;
import hotel.dto.booking.input.BookingDatesRequest;
import hotel.dto.booking.input.RoomNumberRequest;
import hotel.dto.booking.output.BookingResponse;
import hotel.dto.booking.output.RoomResponse;
import hotel.items.HotelItem;

import java.util.List;
import java.util.Objects;

public class CreateBookingItem extends HotelItem{

	public CreateBookingItem(HotelApplContext context) {
		super(context);		
	}

	@Override
	public String displayName() {
		return "Create booking";
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
			inOut.outputlLine("Available rooms: ");
			showRooms(availableRooms, "No available rooms for selected dates");
			RoomNumberRequest roomNumber = getExistingRoom();
			if(Objects.isNull(roomNumber))
				return;
			if(Objects.isNull(roomNumber)) {
				inOut.outputlLine("Room number can't be null");
				return;
			}
				
			BookingCreateRequest bookingDTO = new BookingCreateRequest(context.getUserId(), roomNumber.roomNumber(), dates.checkIn(), dates.checkOut());	
			BookingResponse booking = bookingService.createBooking(bookingDTO);
			inOut.outputlLine("Booking created: room number - %d, category - %s, price per night - %.2f, capacity - %d, check in - %s, check out - %s"
					.formatted(booking.roomNumber(), booking.caregory(), booking.pricePerNight(), booking.capacity(), booking.checkIn(), booking.checkOut()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
	}

}
