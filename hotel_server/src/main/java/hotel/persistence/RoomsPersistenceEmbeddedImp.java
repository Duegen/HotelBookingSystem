package hotel.persistence;

import hotel.interfaces.IRoomsPersistence;
import hotel.model.Room;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class RoomsPersistenceEmbeddedImp implements IRoomsPersistence {
	private static RoomsPersistenceEmbeddedImp instance;

	private RoomsPersistenceEmbeddedImp() {

	}

	public static RoomsPersistenceEmbeddedImp getInstance() {
		if (instance == null)
			instance = new RoomsPersistenceEmbeddedImp();
		return instance;
	}

	@Override
	public void saveRooms(List<Room> rooms, Path dataFile) throws IOException {
		if (Objects.isNull(rooms))
			throw new IllegalArgumentException("rooms list can not be null");
		Files.createDirectories(dataFile.getParent());
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile.toString()))) {
			out.writeObject(rooms);
		} catch (IOException e) {
			throw new IOException("saving rooms list to file is failed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> loadRooms(Path dataFile) throws IOException {
		if (!Files.exists(dataFile))
			throw new FileNotFoundException("rooms data file not found");
		if (Files.size(dataFile) == 0)
			throw new IOException("rooms data file is empty");
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
			return (List<Room>) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new IOException("loading rooms list from file is failed");
		}
	}
}
