package hotel.service.account.exceptions;

public class GuestAlreadyExistsException extends GuestException {
	private static final long serialVersionUID = 1L;

	public GuestAlreadyExistsException(int id) {
		super("guest with id %d is already exists".formatted(id));
	}

}
