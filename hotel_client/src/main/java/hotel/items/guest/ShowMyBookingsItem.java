package hotel.items.guest;

import hotel.HotelApplContext;
import hotel.dto.booking.output.BookingResponse;
import hotel.items.HotelItem;

import java.util.List;

public class ShowMyBookingsItem extends HotelItem {

	public ShowMyBookingsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "My bookings list";
	}

	@Override
	public void perform() {
		try {
			List<BookingResponse> bookings = bookingService.getBookingsForGuest();
			if (bookings.isEmpty()) {
			    inOut.outputlLine("No bookings found ");
			    return;
			}
			inOut.outputlLine("Your bookings:");
			showBookings(bookings, "No bookings are found");
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		} 
	}

}
