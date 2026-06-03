package hotel.service.account.exceptions;

public class NoFreeGuestIdException extends GuestException {
	private static final long serialVersionUID = 1L;

	public NoFreeGuestIdException() {
		super("no free guest ids are found");
	}
}
