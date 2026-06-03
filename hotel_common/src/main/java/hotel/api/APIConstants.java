package hotel.api;

import java.util.List;
import java.util.Map;

public interface APIConstants {
	// === IHotelAccountService ===
	final String ACCOUNT_CREATE = "/account/create";
	final String ACCOUNT_REMOVE = "/account/remove";
	final String ACCOUNTS_GET = "/account/get";
	final String ACCOUNT_LOGIN = "/account/login";
	final String ACCOUNT_LOGOUT = "/account/logout";
	
	// === IHotelBookingService ===
	final String ROOM_TYPE_CREATE = "/room-type/create";
	final String ROOM_TYPES_GET = "/room-type/get";
	final String ROOM_TYPE_REMOVE = "/room-type/remove";

	final String ROOM_CREATE = "/room/create";
	final String ROOM_REMOVE = "/room/remove";
	final String ROOMS_GET = "/room/get";

	final String BOOKING_CREATE = "/booking/create";
	final String BOOKING_CANCEL = "/booking/cancel";
	final String BOOKINGS_GET = "/booking/get";
	final String BOOKING_GET_FOR_GUEST = "/booking/getforguest/";
	final String BOOKING_REMOVE = "/booking/remove";
 
	final String AVAILABLE_ROOMS = "/booking/available";
    // === IHotelAnalyticsService ===
	final String GET_TOTAL_BOOKINGS = "/analytics/bookings/total";
	final String GET_AVERAGE_BOOKING_PRICE = "/analytics/bookings/average-price";
	final String GET_MOST_POPULAR_ROOM_TYPES = "/analytics/room-types/popular";
	final String GET_AVAILABLE_ROOMS_COUNT = "/analytics/rooms/available-count";
	final String GET_OCCUPIED_ROOMS_COUNT = "/analytics/rooms/occupied-count";
	final String GET_MOST_POPULAR_ROOM_TYPES_FOR_AGE_RANGE = "/analytics/room-types/popular-by-age";
	final String GET_BOOKINGS_BY_CHECKIN = "/analytics/bookings/by-checkin";
    
    List<String> requestsWithOutAuthorization = List.of("/account/login", ACCOUNT_CREATE);
    Map<Integer, List<String>> excessLevels = Map.of(2, List.of(GET_TOTAL_BOOKINGS, 
    															GET_AVERAGE_BOOKING_PRICE,
    															GET_MOST_POPULAR_ROOM_TYPES,
    															GET_AVAILABLE_ROOMS_COUNT,
    															GET_OCCUPIED_ROOMS_COUNT,
    															GET_MOST_POPULAR_ROOM_TYPES_FOR_AGE_RANGE,
    															ACCOUNT_LOGOUT,
    															GET_BOOKINGS_BY_CHECKIN), 
    												1, List.of(ACCOUNT_REMOVE,
    														   ACCOUNTS_GET,
    														   ROOM_TYPE_CREATE,
    														   ROOM_TYPES_GET,
    														   ROOM_TYPE_REMOVE,
    														   ROOM_CREATE,
    														   ROOM_REMOVE,
    														   ROOMS_GET,
    														   BOOKING_REMOVE,
    														   BOOKINGS_GET,
    														   ACCOUNT_LOGOUT),
    												0, List.of(AVAILABLE_ROOMS, 
    														   BOOKING_CREATE, 
    														   BOOKING_CANCEL,
    														   BOOKING_GET_FOR_GUEST,
    														   ACCOUNT_LOGOUT));
}
