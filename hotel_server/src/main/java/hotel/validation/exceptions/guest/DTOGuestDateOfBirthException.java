package hotel.validation.exceptions.guest;

import hotel.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Objects;

public class DTOGuestDateOfBirthException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTOGuestDateOfBirthException(LocalDate dateOfBirth) {
		String message;
		if(Objects.isNull(dateOfBirth))
			message = "date of birth validation error: date of birth can't be null";
		else
			message = "date of birth validation error: age of guest can't be less then 18 years";
		super(message);
	}
}
