package hotel.validation.exceptions.roomtype;

import hotel.exceptions.ValidationException;

public class DTORoomTypeIdException extends ValidationException {
	private static final long serialVersionUID = 1L;
	
	public DTORoomTypeIdException(int typeId, int typeMinId, int typeMaxId) {
		super("room type validation error: invalid type id '%d', type id should be %d-%d".formatted(typeId, typeMinId, typeMaxId));
	}
}
