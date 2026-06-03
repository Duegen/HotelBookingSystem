package hotel.interfaces;

import hotel.dto.booking.input.*;
import hotel.dto.booking.output.BookingFullResponse;
import hotel.dto.booking.output.BookingResponse;
import hotel.dto.booking.output.RoomResponse;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.exceptions.ConflictException;
import hotel.exceptions.ValidationException;

import java.util.List;

public interface IHotelBookingServiceCommon {
	RoomTypeResponse createRoomType(RoomTypeCreateRequest dto) throws ConflictException, ValidationException;
	RoomResponse createRoom(RoomCreateRequest dto) throws ConflictException, ValidationException;
	BookingResponse createBooking(BookingCreateRequest dto) throws ConflictException, ValidationException;
	
	RoomTypeResponse removeRoomType(RoomTypeIdRequest dto) throws ConflictException, ValidationException;
	RoomResponse removeRoom(RoomNumberRequest dto) throws ConflictException, ValidationException;
	BookingResponse cancelBooking(BookingIdRequest dto) throws ConflictException, ValidationException;
	BookingFullResponse removeBooking(BookingIdRequest dto) throws ConflictException, ValidationException;
	
	List<BookingResponse> getBookingsForGuest();
	List<RoomResponse> getAvailableRooms(BookingDatesRequest dto) throws ValidationException;
	List<RoomTypeResponse> getRoomTypesDTO();
	List<RoomResponse> getRoomsDTO();
	List<BookingFullResponse> getBookingsFullResponse();
}
