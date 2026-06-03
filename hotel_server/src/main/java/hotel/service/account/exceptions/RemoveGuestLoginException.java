package hotel.service.account.exceptions;

public class RemoveGuestLoginException extends GuestException {
	private static final long serialVersionUID = 1L;

	public RemoveGuestLoginException(Integer guestId) {
		super("guest with id '%d' is logged in and can't be removed".formatted(guestId));
	}

}
