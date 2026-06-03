package hotel.interfaces;

import hotel.model.Booking;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IBookingsPersistence {
	void saveBookings(List<Booking> bookings, Path dataFile) throws IOException;
	List<Booking> loadBookings(Path dataFile) throws IOException;
}
