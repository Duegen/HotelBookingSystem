package hotel.protocol;

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
import hotel.interfaces.IHotelAccountService;
import hotel.interfaces.IHotelBookingService;
import telran.net.protocol.ProtocolJava;
import telran.net.protocol.RequestJava;
import telran.net.protocol.ResponseJava;
import telran.net.protocol.TCPResponseCode;
import telran.net.server.ServerClientJava;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HotelTcpProtocol implements ProtocolJava, APIConstants {
	private final IHotelBookingService bookingService;
	private final IHotelAccountService accountService;
	private final IAnalyticsCommon analyticsService;
	
	public HotelTcpProtocol(IHotelBookingService bookingService, IHotelAccountService accountService,
			IAnalyticsCommon analyticsService) {
		this.bookingService = bookingService;
		this.accountService = accountService;
		this.analyticsService = analyticsService;
	}

	@Override
	public ResponseJava getResponse(RequestJava request) {
		try {
			if(ServerClientJava.getLocal().excessLevel() == -1 && !requestsWithOutAuthorization.contains(request.requestType))
					return unauthorized();
			else if(ServerClientJava.getLocal().excessLevel() != -1 
					&& !excessLevels.get(ServerClientJava.getLocal().excessLevel()).contains(request.requestType))
				return forbidden();
			Serializable data = request.requestData;
			return switch (request.requestType) {
				case ACCOUNT_CREATE -> createAccount(data);
				case ACCOUNT_REMOVE -> removeAccount(data);
				case ACCOUNTS_GET -> getAccounts();
				case ACCOUNT_LOGIN -> login(data);
				case ACCOUNT_LOGOUT -> logout();
				
				case ROOM_TYPE_CREATE -> roomTypeCreate(data);
				case ROOM_TYPES_GET -> getRoomTypes();
				case ROOM_TYPE_REMOVE -> roomTypeRemove(data);
				
				case ROOM_CREATE -> roomCreate(data);
				case ROOM_REMOVE -> roomRemove(data);
				case ROOMS_GET -> getRooms();
				
				case BOOKING_CREATE -> bookingCreate(data);
				case BOOKING_CANCEL -> bookingCancel(data);
				case BOOKINGS_GET -> getBookings();
				case BOOKING_GET_FOR_GUEST -> getBookingForGuest();
				case BOOKING_REMOVE -> bookingRemove(data);
				case AVAILABLE_ROOMS -> availableRooms(data);
				
				case GET_TOTAL_BOOKINGS -> getTotalBookings();
				case GET_AVERAGE_BOOKING_PRICE -> getAverageBookingPrice();
				case GET_MOST_POPULAR_ROOM_TYPES -> getMostPopularRoomTypes();
				case GET_AVAILABLE_ROOMS_COUNT -> getAvailableRoomsCount(data);
				case GET_OCCUPIED_ROOMS_COUNT -> getOccupiedRoomsCount(data);
				case GET_MOST_POPULAR_ROOM_TYPES_FOR_AGE_RANGE -> getMostPopularRoomTypesForAgeRange(data);
				case GET_BOOKINGS_BY_CHECKIN -> getBookingsByCheckin(data);
				
				default -> badRequest("Unknown request type: " + request.requestType);
			};
		} catch (Exception e) {
			return badRequest(e.getMessage());
		}
	}

	private ResponseJava getBookingsByCheckin(Serializable data) {
		if (data.getClass().equals(DateRequest.class)) {
			try {
				List<BookingResponse> result = analyticsService.showBookingsByCheckIn((DateRequest) data);
				return ok((Serializable)result);
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid date format");
	}

	private ResponseJava getMostPopularRoomTypesForAgeRange(Serializable data) {
		if (data.getClass().equals(AgeRangeRequest.class)) {
			try {
				List<RoomTypeResponse> result = analyticsService.getMostPopularRoomTypesForAgeRange((AgeRangeRequest) data);
				return ok((Serializable)result);
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid age range format");
		
	}

	private ResponseJava getOccupiedRoomsCount(Serializable data) {
		if (data.getClass().equals(DateRequest.class)) {
			try {
				int result = analyticsService.getOccupiedRoomsCount((DateRequest) data);
				return ok((Serializable)result);
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid date format");
	}

	private ResponseJava getAvailableRoomsCount(Serializable data) {
		if (data.getClass().equals(DateRequest.class)) {
			try {
				int result = analyticsService.getAvailableRoomsCount((DateRequest) data);
				return ok((Serializable)result);
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid date format");
	}

	private ResponseJava getMostPopularRoomTypes() {
		try {
			List<RoomTypeResponse> result = analyticsService.mostPopularRoomTypes();
			return ok((Serializable) result);
		} catch (Exception e) {
			return unknown("unknown server error");
		} 
	}

	private ResponseJava getAverageBookingPrice() {
		try {
			double result = analyticsService.averageBookingPrice();
			return ok((Serializable) result);
		} catch (Exception e) {
			return unknown("unknown server error");
		} 
	}

	private ResponseJava getTotalBookings() {
		try {
			int result = analyticsService.bookingsNumber();
			return ok((Serializable) result);
		} catch (Exception e) {
			return unknown("unknown server error");
		} 
	}

	private ResponseJava availableRooms(Serializable data) {
		if (data.getClass().equals(BookingDatesRequest.class)) {
			try {
				List<RoomResponse> result = bookingService.getAvailableRooms((BookingDatesRequest) data);
				return ok((Serializable)result);
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid dates format");
	}

	private ResponseJava bookingRemove(Serializable data) {
		if (data.getClass().equals(BookingIdRequest.class)) {
			try {
				BookingFullResponse result = bookingService.removeBooking((BookingIdRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid booking id format");
	}

	private ResponseJava getBookingForGuest() {
		try {
			List<BookingResponse> result = bookingService.getBookingsForGuest();
			return ok((Serializable) result);
		} catch (Exception e) {
			return unknown("unknown server error");
		} 
	}

	private ResponseJava getBookings() {
		try {
			List<BookingFullResponse> result = bookingService.getBookingsFullResponse().stream().sorted(Comparator
			        .comparingLong((BookingFullResponse bk) -> bk.guest().guestId())
			        .thenComparing(Comparator.comparing(BookingFullResponse::checkIn).reversed())
			        .thenComparingLong(BookingFullResponse::BookingId)).toList();
			return ok((Serializable) result);
		} catch (Exception e) {
			return unknown("unknown server error");
		} 
	}

	private ResponseJava bookingCancel(Serializable data) {
		if (data.getClass().equals(BookingIdRequest.class)) {
			int bookingId = ((BookingIdRequest) data).bookingId();
			if(Objects.isNull(bookingService.getBookings().get(bookingId)) 
					|| ServerClientJava.getLocal().guestId() != bookingService.getBookings().get(bookingId).getGuestId())
				return forbidden();
			try {
				BookingResponse result = bookingService.cancelBooking((BookingIdRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid booking id format");
	}

	private ResponseJava bookingCreate(Serializable data) {
		if (data.getClass().equals(BookingCreateRequest.class)) {
			if(ServerClientJava.getLocal().guestId() != ((BookingCreateRequest) data).guestId())
				return forbidden();
			try {
				BookingResponse result = bookingService.createBooking((BookingCreateRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid booking data format");
	}

	private ResponseJava getRooms() {
		try {
			List<RoomResponse> result = bookingService.getRoomsDTO();
			return ok((Serializable) result);
		} catch (Exception e) {
			return unknown("unknown server error");
		} 
	}

	private ResponseJava roomRemove(Serializable data) {
		if (data.getClass().equals(RoomNumberRequest.class)) {
			try {
				RoomResponse result = bookingService.removeRoom((RoomNumberRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid room number format");
	}

	private ResponseJava roomCreate(Serializable data) {
		if (data.getClass().equals(RoomCreateRequest.class)) {
			try {
				RoomResponse result = bookingService.createRoom((RoomCreateRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid room data format");
	}

	private ResponseJava roomTypeRemove(Serializable data) {
		if (data.getClass().equals(RoomTypeIdRequest.class)) {
			try {
				RoomTypeResponse result = bookingService.removeRoomType((RoomTypeIdRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid room type id format");
	}

	private ResponseJava getRoomTypes() {
		try {
			List<RoomTypeResponse> result = bookingService.getRoomTypesDTO();
			return ok((Serializable) result);
		} catch (Exception e) {
			return unknown("unknown server error");
		} 
	}

	private ResponseJava roomTypeCreate(Serializable data) {
		if (data.getClass().equals(RoomTypeCreateRequest.class)) {
			try {
				RoomTypeResponse result = bookingService.createRoomType((RoomTypeCreateRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid room type data format");
	}

	private ResponseJava logout() {
		accountService.logout();
		return ok(true);
	}

	private ResponseJava login(Serializable data) {
		if (data.getClass().equals(LoginRequest.class)) {
			try {
				LoginResponse result = accountService.login((LoginRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid login data format");
	}

	private ResponseJava getAccounts() {
		try {
			List<GuestResponse> result = accountService.getGuestsResponse();
			return ok((Serializable) result);
		} catch (Exception e) {
			return unknown("unknown server error");
		} 
	}

	private ResponseJava removeAccount(Serializable data) {
		if (data.getClass().equals(GuestIdRequest.class)) {
			try {
				GuestResponse result = accountService.removeGuest((GuestIdRequest) data);
				return ok(result);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid account remove data format");
	}

	private ResponseJava createAccount(Serializable data) {
		if (data.getClass().equals(GuestCreateRequest.class)) {
			try {		
				accountService.createGuest((GuestCreateRequest) data);
				return ok(true);
			} catch (ConflictException e) {
				return conflict(e.getMessage());
			} catch (ValidationException e) {
				return badRequest(e.getMessage());
			} catch (Exception e) {
				return unknown("unknown server error");
			} 
		} else
			return badRequest("invalid account create data format");
	}

	private ResponseJava ok(Serializable data) {
		return new ResponseJava(TCPResponseCode.OK, data);
	}

	private ResponseJava conflict(String message) {
		return new ResponseJava(TCPResponseCode.CONFLICT, message);
	}

	private ResponseJava unauthorized() {
		return new ResponseJava(TCPResponseCode.UNAUTHORIZED, "unauthorized");
	}

	private ResponseJava badRequest(String message) {
		return new ResponseJava(TCPResponseCode.WRONG_REQUEST, message);
	}
	
	private ResponseJava unknown(String message) {
		return new ResponseJava(TCPResponseCode.UNKNOWN, message);
	}
	
	private ResponseJava forbidden() {
		return new ResponseJava(TCPResponseCode.FORBIDDEN, "forbidden");
	}
}
