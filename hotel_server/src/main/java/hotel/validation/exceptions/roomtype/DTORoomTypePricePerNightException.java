package hotel.validation.exceptions.roomtype;

import hotel.exceptions.ValidationException;

public class DTORoomTypePricePerNightException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public DTORoomTypePricePerNightException(double pricePerNight, double pricePerNightMin, double pricePerNightMax) {
		super("room type validation error: invalid price per night '%.2f', price per night should be %.2f-%.2f".formatted(pricePerNight, pricePerNightMin, pricePerNightMax));
	}

}
