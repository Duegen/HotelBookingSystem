package hotel.interfaces;

import hotel.model.Guest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IGuestsPersistence {
	void saveGuests(List<Guest> guests, Path dataFile) throws IOException;
	List<Guest> loadGuests(Path dataFile) throws IOException;
}
