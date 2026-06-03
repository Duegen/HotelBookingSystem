package hotel;

import java.util.Objects;

public class HotelApplConstants {
	public static final String HOST = init("HOST", "localhost");
	public final static String DATE_FORMAT = init("DATE_FORMAT", "dd.MM.yyyy");
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
