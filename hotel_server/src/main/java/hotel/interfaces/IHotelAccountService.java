package hotel.interfaces;

import hotel.exceptions.ConflictException;
import hotel.exceptions.ValidationException;
import hotel.model.Guest;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IHotelAccountService extends IHotelAccountServiceCommon{
	void addGuest(Guest dto) throws ConflictException, ValidationException;
	Guest findGuestById(int guestId);
	Guest findGuestByEmail(String email);
	Map<Integer, Guest> getGuests();
	void setBookingService(IHotelBookingService bookingService);
	AtomicBoolean getDatabaseIsModified();
}
