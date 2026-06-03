package hotel.items.accountant;

import hotel.HotelApplContext;
import hotel.dto.booking.input.DateRequest;
import hotel.items.HotelItem;

import java.time.LocalDate;

public class ShowBookingsByCheckInItem extends HotelItem {

	public ShowBookingsByCheckInItem(HotelApplContext context) {
		super(context);
	}


	@Override
	public String displayName() {
		return "Show bookings for selected day";
	}


	@Override
	public void perform() {
		LocalDate selected = inOut.inputDate("Enter date in format", dateFormat);
		if(selected==null)
			return;
		showBookings(analyticsService.showBookingsByCheckIn(new DateRequest(selected)), "No bookings found for "+ selected);
		
	}

}
