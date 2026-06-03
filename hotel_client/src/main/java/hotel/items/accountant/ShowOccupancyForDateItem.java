package hotel.items.accountant;

import hotel.HotelApplContext;
import hotel.dto.booking.input.DateRequest;
import hotel.items.HotelItem;

import java.time.LocalDate;

public class ShowOccupancyForDateItem extends HotelItem {

	public ShowOccupancyForDateItem(HotelApplContext context) {
		super(context);
		
	}

	@Override
	public String displayName() {
		
		return "Show occupancy for date";
	}

	@Override
	public void perform() {
		LocalDate selected = inOut.inputDate("Enter date in format", dateFormat);
		if(selected == null)
			return;
		inOut.outputlLine("Occupied rooms for : "+ selected + ": "+
				analyticsService
				.getOccupiedRoomsCount(new DateRequest(selected)));
	}

}
