

package hotel.service.account.exceptions;

public class DuplicateEmailException extends GuestException {
	private static final long serialVersionUID = 1L;

	public DuplicateEmailException(String email) {
		super("guest with email '%s' is already exists".formatted(email));
	}
	
	
}
