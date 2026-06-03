package hotel.service.account.exceptions;

public class GuestNotFoundException extends GuestException {
	private static final long serialVersionUID = 1L;
	
	public GuestNotFoundException(int guestId) {
		super("guest with id %d not found".formatted(guestId));
	}
	

}
