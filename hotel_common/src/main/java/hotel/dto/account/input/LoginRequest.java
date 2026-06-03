package hotel.dto.account.input;

import java.io.Serializable;

public record LoginRequest(
		String email,
		String password) implements Serializable{}
