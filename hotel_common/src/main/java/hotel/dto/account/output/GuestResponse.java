package hotel.dto.account.output;

import java.io.Serializable;
import java.time.LocalDate;

public record GuestResponse(
		int guestId,
		String name,
		String email,
		LocalDate dateOfBirth) implements Serializable{}
