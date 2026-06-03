package hotel.interfaces;

import hotel.model.Room;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IRoomsPersistence {
	void saveRooms(List<Room> rooms, Path dataFile) throws IOException;
	List<Room> loadRooms(Path dataFile) throws IOException;
}
