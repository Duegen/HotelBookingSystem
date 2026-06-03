package hotel.dto.account.input;

import java.io.Serializable;

public record GuestIdRequest(
		Integer guestId) implements Serializable{}
