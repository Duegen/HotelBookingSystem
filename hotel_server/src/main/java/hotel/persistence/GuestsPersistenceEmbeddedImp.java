package hotel.persistence;

import hotel.interfaces.IGuestsPersistence;
import hotel.model.Guest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class GuestsPersistenceEmbeddedImp implements IGuestsPersistence{
	private static GuestsPersistenceEmbeddedImp instance;
	
	private GuestsPersistenceEmbeddedImp() {

	}
	
	public static GuestsPersistenceEmbeddedImp getInstance() {
		if (instance == null) 
			instance = new GuestsPersistenceEmbeddedImp();
		return instance;
	}
	
	@Override
	public void saveGuests(List<Guest> guests, Path dataFile) throws IOException {
		if (Objects.isNull(guests))
			throw new IllegalArgumentException("guests list can not be null");
		Files.createDirectories(dataFile.getParent());
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile.toString()))){
				out.writeObject(guests);
		}catch(IOException e) {
			throw new IOException("saving guests list to file is failed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Guest> loadGuests(Path dataFile) throws IOException {
		if(!Files.exists(dataFile))
			throw new FileNotFoundException("guests data file not found");
		if(Files.size(dataFile) == 0)
			throw new IOException("guests data file is empty");
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
			return (List<Guest>) in.readObject();
		}catch(IOException | ClassNotFoundException e) {
			throw new IOException("loading guests list from file is failed");
		}
	}
}
