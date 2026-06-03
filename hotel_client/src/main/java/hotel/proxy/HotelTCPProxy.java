package hotel.proxy;

import hotel.api.APIConstants;
import hotel.dto.account.input.GuestCreateRequest;
import hotel.dto.account.input.GuestIdRequest;
import hotel.dto.account.input.LoginRequest;
import hotel.dto.account.output.GuestResponse;
import hotel.dto.account.output.LoginResponse;
import hotel.dto.booking.input.*;
import hotel.dto.booking.output.BookingFullResponse;
import hotel.dto.booking.output.BookingResponse;
import hotel.dto.booking.output.RoomResponse;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.exceptions.ConflictException;
import hotel.exceptions.ValidationException;
import hotel.interfaces.IAnalyticsCommon;
import hotel.interfaces.IHotelAccountServiceCommon;
import hotel.interfaces.IHotelBookingServiceCommon;
import telran.net.client.TCPClientJava;

import java.util.List;

public class HotelTCPProxy  extends TCPClientJava implements IHotelBookingServiceCommon, 
		IHotelAccountServiceCommon, IAnalyticsCommon, APIConstants {

	public HotelTCPProxy(String hostName, int port) throws Exception {
		super(hostName, port);
		
	}

	@Override
	public double averageBookingPrice() {
		return sendRequest(GET_AVERAGE_BOOKING_PRICE, null);
	}

	@Override
	public List<RoomTypeResponse> mostPopularRoomTypes() {
		return sendRequest(GET_MOST_POPULAR_ROOM_TYPES, null);
	}

	@Override
	public int getAvailableRoomsCount(DateRequest date) {
		return sendRequest(GET_AVAILABLE_ROOMS_COUNT, date);	
	}

	@Override
	public int getOccupiedRoomsCount(DateRequest date) {
		return sendRequest(GET_OCCUPIED_ROOMS_COUNT, date);	
	}

	@Override
	public List<RoomTypeResponse> getMostPopularRoomTypesForAgeRange(AgeRangeRequest ageRange)
			throws ValidationException {
		return sendRequest(GET_MOST_POPULAR_ROOM_TYPES_FOR_AGE_RANGE, ageRange);	
	}

	@Override
	public int bookingsNumber() {
		return sendRequest(GET_TOTAL_BOOKINGS, null);
	}

	@Override
	public void createGuest(GuestCreateRequest dto) throws ConflictException, ValidationException {
		sendRequest(ACCOUNT_CREATE, dto);
	}

	@Override
	public GuestResponse removeGuest(GuestIdRequest dto) throws ConflictException, ValidationException {
		return sendRequest(ACCOUNT_REMOVE, dto);
	}

	@Override
	public List<GuestResponse> getGuestsResponse() {
		return sendRequest(ACCOUNTS_GET, null);
	}

	@Override
	public LoginResponse login(LoginRequest dto) throws ValidationException, ConflictException {
		return sendRequest(ACCOUNT_LOGIN, dto);
	}

	@Override
	public void logout() {
		sendRequest(ACCOUNT_LOGOUT, null);		
	}

	@Override
	public RoomTypeResponse createRoomType(RoomTypeCreateRequest dto) throws ConflictException, ValidationException {
		return sendRequest(ROOM_TYPE_CREATE, dto);
	}

	@Override
	public RoomResponse createRoom(RoomCreateRequest dto) throws ConflictException, ValidationException {
		return sendRequest(ROOM_CREATE, dto);
	}

	@Override
	public BookingResponse createBooking(BookingCreateRequest dto) throws ConflictException, ValidationException {
		return sendRequest(BOOKING_CREATE, dto);
	}

	@Override
	public RoomTypeResponse removeRoomType(RoomTypeIdRequest dto) throws ConflictException, ValidationException {
		return sendRequest(ROOM_TYPE_REMOVE, dto);
	}

	@Override
	public RoomResponse removeRoom(RoomNumberRequest dto) throws ConflictException, ValidationException {
		return sendRequest(ROOM_REMOVE, dto);
	}

	@Override
	public BookingResponse cancelBooking(BookingIdRequest dto) throws ConflictException, ValidationException {
		return sendRequest(BOOKING_CANCEL, dto);
	}

	@Override
	public BookingFullResponse removeBooking(BookingIdRequest dto) throws ConflictException, ValidationException {
		return sendRequest(BOOKING_REMOVE, dto);
	}

	@Override
	public List<BookingResponse> getBookingsForGuest() {
		return sendRequest(BOOKING_GET_FOR_GUEST, null);
	}

	@Override
	public List<RoomResponse> getAvailableRooms(BookingDatesRequest dto) throws ValidationException {
		return sendRequest(AVAILABLE_ROOMS, dto);
	}

	@Override
	public List<RoomTypeResponse> getRoomTypesDTO() {
		return sendRequest(ROOM_TYPES_GET, null);
	}

	@Override
	public List<RoomResponse> getRoomsDTO() {
		return sendRequest(ROOMS_GET, null);
	}

	@Override
	public List<BookingFullResponse> getBookingsFullResponse() {
		return sendRequest(BOOKINGS_GET, null);
	}

	@Override
	public List<BookingResponse> showBookingsByCheckIn(DateRequest date) {
		return sendRequest(GET_BOOKINGS_BY_CHECKIN, date);
	}

	

}
