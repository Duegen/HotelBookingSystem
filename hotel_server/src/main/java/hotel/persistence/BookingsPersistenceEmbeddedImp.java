package hotel.persistence;

import hotel.interfaces.IBookingsPersistence;
import hotel.model.Booking;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class BookingsPersistenceEmbeddedImp implements IBookingsPersistence{
	private static BookingsPersistenceEmbeddedImp instance;
	
	private BookingsPersistenceEmbeddedImp() {

	}
	
	public static BookingsPersistenceEmbeddedImp getInstance() {
		if (instance == null) 
			instance = new BookingsPersistenceEmbeddedImp();
		return instance;
	}

	@Override
	public void saveBookings(List<Booking> bookings, Path dataFile) throws IOException {
		if (Objects.isNull(bookings))
			throw new IllegalArgumentException("bookings list can not be null");
		Files.createDirectories(dataFile.getParent());
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dataFile.toString()))){
				out.writeObject(bookings);
		}catch(IOException e) {
			throw new IOException("saving bookings list to file is failed");
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Booking> loadBookings(Path dataFile) throws IOException {
		if(!Files.exists(dataFile))
			throw new FileNotFoundException("booking data file not found");
		if(Files.size(dataFile) == 0)
			throw new IOException("bookings data file is empty");
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(dataFile.toFile()))) {
			return (List<Booking>) in.readObject();		
		}catch(IOException | ClassNotFoundException e) {
			throw new IOException("loading booking list from file is failed");
		}
	}
}
