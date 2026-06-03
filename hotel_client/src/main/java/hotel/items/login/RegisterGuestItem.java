package hotel.items.login;

import hotel.HotelApplContext;
import hotel.dto.account.input.GuestCreateRequest;
import hotel.items.HotelItem;

import java.util.Objects;

public class RegisterGuestItem extends HotelItem {

	public RegisterGuestItem(HotelApplContext context) {
		super(context);

	}

	@Override
	public String displayName() {
		return "Register new guest";
	}

	@Override
	public void perform() {
		try {
			GuestCreateRequest guestDTO = inputNewGuest();
			if(Objects.isNull(guestDTO))
				return;
			accountService.createGuest(guestDTO);
			inOut.outputlLine("Guest registered");
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}

	}

}
