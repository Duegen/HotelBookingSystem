package hotel.items.manager;

import hotel.HotelApplContext;
import hotel.dto.account.input.GuestIdRequest;
import hotel.dto.account.output.GuestResponse;
import hotel.items.HotelItem;

import java.util.List;
import java.util.Objects;

public class RemoveGuestItem extends HotelItem {

	public RemoveGuestItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Remove guest";
	}

	@Override
	public void perform() {
		try {
			List<GuestResponse> guests = accountService.getGuestsResponse();
			inOut.outputlLine("Guests list:");
			showGuests(guests, "no guests are found");
			GuestIdRequest guestId = getExistingGuest();
			if(Objects.isNull(guestId))
				return;
			GuestResponse removed = accountService.removeGuest(guestId);
			inOut.output("Guest removed: guest id - %d, guest name - %s, guest email - %s, date of birth - %s"
					.formatted(removed.guestId(), removed.name(), removed.email(), removed.dateOfBirth()));
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
	}

}
