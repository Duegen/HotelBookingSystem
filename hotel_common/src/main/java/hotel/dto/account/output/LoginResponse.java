package hotel.dto.account.output;

import java.io.Serializable;

public record LoginResponse(
		int excessLevel,
		int userId,
		String userName) implements Serializable{}
