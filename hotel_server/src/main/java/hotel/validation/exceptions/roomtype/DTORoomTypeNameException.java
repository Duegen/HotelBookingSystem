package hotel.validation.exceptions.roomtype;

import hotel.exceptions.ValidationException;

import java.util.Objects;

public class DTORoomTypeNameException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTORoomTypeNameException(String name) {
		String message;
		if(Objects.isNull(name))
			message = "room type validation error: name can't be null";
		else
			message = "room type validation error: name can't be blanked";
		super(message);
	}

}
