package telran.net.server;

public record LoginData(
	String userId,
	int excessLevel,
	int guestId) {}
