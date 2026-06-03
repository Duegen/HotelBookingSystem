package hotel.persistence;

import hotel.interfaces.IRoomTypesPersistence;
import hotel.model.RoomType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class RoomTypesPersistenceEmbeddedImp implements IRoomTypesPersistence {
	private static RoomTypesPersistenceEmbeddedImp instance;

	private RoomTypesPersistenceEmbeddedImp() {

	}

	public static RoomTypesPersistenceEmbeddedImp getInstance() {
		if (instance == null)
			instance = new RoomTypesPersistenceEmbeddedImp();
		return instance;
	}

	@Override
	public void saveRoomTypes(List<RoomType> roomTypes, Path dataFile) throws IOException {
		if (Objects.isNull(roomTypes))
			throw new IllegalArgumentException("room types list can not be null");
		if(Objects.isNull(dataFile))
			throw new IllegalArgumentException("file path can not be null");
		Files.createDirectories(dataFile.getParent());
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile.toString()))) {
				out.writeObject(roomTypes);
		} catch (IOException e) {
			throw new IOException("saving room types list to file is failed");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoomType> loadRoomTypes(Path dataFile) throws IOException {
		if (!Files.exists(dataFile))
			throw new FileNotFoundException("room types data file not found");
		if (Files.size(dataFile) == 0)
			throw new IOException("room types data file is empty");
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
			return (List<RoomType>) in.readObject();
		} catch(IOException | ClassNotFoundException e) {
			throw new IOException("loading room types list from file is failed");
		}
	}
}
