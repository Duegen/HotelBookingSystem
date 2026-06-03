package hotel.items.guest;

import hotel.HotelApplContext;
import hotel.dto.booking.input.BookingIdRequest;
import hotel.dto.booking.output.BookingResponse;
import hotel.items.HotelItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CancelMyBookingItem extends HotelItem {
    public CancelMyBookingItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Cancel my booking";
    }

    @Override
    public void perform() {
    	try {
			inOut.outputlLine("Your bookings:");
			List<BookingResponse> activeBookings = bookingService.getBookingsForGuest()
					.stream().filter(bk -> LocalDate.now().isBefore(bk.checkIn())).toList();
			if (activeBookings.isEmpty()) {
			    inOut.outputlLine("No bookings available for canceling are found");
			    return;
			}
			showBookings(activeBookings, "No bookings available for canceling are found");

			Integer bookingId = inOut.inputInteger("Enter booking id");
			if(Objects.isNull(bookingId))
				return;
			BookingResponse removed = bookingService.cancelBooking(new BookingIdRequest(bookingId));
			inOut.outputlLine("Booking canceled: room number - %d, category - %s, price per night - %.2f, capacity - %d, check in - %s, check out - %s"
					.formatted(removed.roomNumber(), removed.caregory(), removed.pricePerNight(), removed.capacity(), removed.checkIn(), removed.checkOut()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
    }
}
