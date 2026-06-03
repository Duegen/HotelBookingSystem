package hotel.interfaces;

import java.io.IOException;

public interface IHotelPersistenceService {
	void saveHotelData() throws IOException;
	boolean loadHotelData() throws IOException;
	public void saveUserData() throws IOException;
}
