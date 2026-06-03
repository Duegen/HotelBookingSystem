package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.account.output.GuestResponse;
import hotel.items.HotelItem;

import java.util.List;

public class ShowGuestsItem extends HotelItem {

	public ShowGuestsItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Show guests";
	}

	@Override
	public void perform() {
		inOut.outputlLine("Guests list:");
		List<GuestResponse> guests = accountService.getGuestsResponse();
		showGuests(guests, "no guests are found");
	}

}
