package hotel.dto.account.input;

import java.io.Serializable;
import java.time.LocalDate;

public record GuestCreateRequest(
	String name,
	String email,
	LocalDate dateOfBirth, 
	String password) implements Serializable{}
