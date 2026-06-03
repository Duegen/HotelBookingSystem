package hotel.app;

import java.util.Objects;

public class HotelApplConstants {

	public final static String DIR = init("DIR", "Data");
	public final static String ROOM_FILE = init("ROOM_FILE", "rooms.dat");
	public final static String ROOMTYPE_FILE = init("ROOMTYPE_FILE", "roomtypes.dat");
	public final static String GUEST_FILE = init("GUEST_FILE", "guests.dat");
	public final static String BOOKING_FILE = init("BOOKING_FILE", "bookings.dat");
	public final static String DATE_FORMAT = init("DATE_FORMAT", "dd.MM.yyyy");
	public static final String ALGORITHM = init("ALGORITHM", "PBKDF2WithHmacSHA1");
    public static final int KEY_LENGTH = init("KEY_LENGTH", 256); 
    
    public static final String MANAGER_LOGIN = init("MANAGER_LOGIN", "manager@hotel.il");
    public static final String MANAGER_PASSWORD = init("MANAGER_PASSWORD", "Password1");
    public static final String ACCOUNTANT_LOGIN = init("ACCOUNTANT_LOGIN", "accountant@hotel.com");
    public static final String ACCOUNTANT_PASSWORD = init("ACCOUNTANT_PASSWORD", "Password1");
	
    public static final int PORT = init("PORT", 2050);
    
	private static String init(String propName, String defaultValue) {
		String prop = System.getenv(propName);
		if(Objects.isNull(prop))
			return defaultValue;
		else {
			return prop;
		}
	}

	private static int init(String propName, int defaultValue) {
		int propTmp;
		String prop = System.getenv(propName);
		if(Objects.isNull(prop))
			return defaultValue;
		else {
			try {
				propTmp = Integer.parseInt(prop);
			} catch (Exception e) {
				return defaultValue;
			}
			return propTmp;
		}
	}
}
