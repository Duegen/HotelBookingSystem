package hotel.validation;

import java.time.LocalDate;

public class Constants {
	public final static int TYPE_MIN_ID = 1;
	public final static int TYPE_MAX_ID = 100;
	public final static int GUEST_MIN_ID = 1;
	public final static int GUEST_MAX_ID = 1000;
	public final static int BOOKING_MIN_ID = 1;
	public final static int BOOKING_MAX_ID = 100_000;
	public static final int ROOM_MIN_NUMBER = 0;
	public static final int ROOM_MAX_NUMBER = 100;
	public static final int ROOM_MIN_CAP = 1;
	public static final int ROOM_MAX_CAP = 10;
	public static final double PRICE_PER_NIGHT_MIN = 100.0;
	public static final double PRICE_PER_NIGHT_MAX = 10000.0;
	public final static LocalDate MIN_BD = LocalDate.of(1950, 1, 1);
	public final static LocalDate MAX_BD = LocalDate.now().minusYears(18);
	public final static int MIN_AGE = 18;
	public final static int MAX_AGE = 100;
}
