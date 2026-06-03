package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.account.output.GuestResponse;
import hotel.dto.booking.input.BookingIdRequest;
import hotel.dto.booking.output.BookingFullResponse;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.items.HotelItem;

import java.util.List;
import java.util.Objects;

public class RemoveBookingItem extends HotelItem {
    public RemoveBookingItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Remove booking";
    }

    @Override
    public void perform() {
        try {
			inOut.outputlLine("Bookings list:");
			List<BookingFullResponse> bookings = bookingService.getBookingsFullResponse();
			showFullBookings(bookings, "no bookings are found");
			BookingIdRequest bookingId = getExistingBooking();
			if(Objects.isNull(bookingId))
				return;
			BookingFullResponse removed = bookingService.removeBooking(bookingId);
			GuestResponse guest = removed.guest();
			RoomTypeResponse rt = removed.roomType();
			inOut.output("Booking removed: booking id - %d, guest id - %d, guest name - %s, guest email - %s, room number - %d, category - %s, price per night - %.2f, capacity - %d, check in - %s, check out - %s"
					.formatted(removed.BookingId(), guest.guestId(), guest.name(), guest.email(), removed.roomNumber(), 
							rt.category(), rt.pricePerNight(), rt.capacity(), removed.checkIn().toString(), removed.checkOut().toString()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
    }
}
