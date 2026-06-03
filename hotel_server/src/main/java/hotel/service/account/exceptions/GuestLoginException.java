package hotel.service.account.exceptions;

public class GuestLoginException extends GuestException {
	private static final long serialVersionUID = 1L;

	public GuestLoginException() {
		super("invalid credentials");
	}

}
