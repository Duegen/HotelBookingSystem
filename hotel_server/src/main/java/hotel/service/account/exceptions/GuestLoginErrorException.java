package hotel.service.account.exceptions;


public class GuestLoginErrorException extends GuestException {
	private static final long serialVersionUID = 1L;

	public GuestLoginErrorException() {
		super("unknown login error");
	}
}
