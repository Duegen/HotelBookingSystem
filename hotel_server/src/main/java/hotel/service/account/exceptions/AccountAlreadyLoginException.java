package hotel.service.account.exceptions;

public class AccountAlreadyLoginException extends GuestException {
	private static final long serialVersionUID = 1L;

	public AccountAlreadyLoginException() {
		super("user is already logined");
	}

}
