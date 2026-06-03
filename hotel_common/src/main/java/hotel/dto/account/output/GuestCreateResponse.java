package hotel.dto.account.output;

import java.io.Serializable;
import java.time.LocalDate;

public record GuestCreateResponse(
		String name,
		String email,
		LocalDate dateOfBirth) implements Serializable{

}
