package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.booking.output.BookingFullResponse;
import hotel.items.HotelItem;

import java.util.List;

public class ShowBookingsItem extends HotelItem {
    public ShowBookingsItem(HotelApplContext context) {
        super(context);
    }

    @Override
    public String displayName() {
        return "Show bookings";
    }

    @Override
    public void perform() {
    	inOut.outputlLine("Bookings list:");
		List<BookingFullResponse> bookings = bookingService.getBookingsFullResponse();
		showFullBookings(bookings, "no rooms are found");
    }
}
