package hotel.validation;

import hotel.dto.account.input.GuestCreateRequest;
import hotel.dto.account.input.GuestIdRequest;
import hotel.dto.account.input.LoginRequest;
import hotel.dto.booking.input.*;
import hotel.exceptions.ValidationException;
import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;
import hotel.validation.exceptions.DTOAgeRangeException;
import hotel.validation.exceptions.DTOAgeRangeNullException;
import hotel.validation.exceptions.booking.*;
import hotel.validation.exceptions.guest.*;
import hotel.validation.exceptions.room.DTORoomNullException;
import hotel.validation.exceptions.room.DTORoomNumberException;
import hotel.validation.exceptions.roomtype.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Validation {
	
	public static void validate(RoomType roomType) throws ValidationException {
		if(Objects.isNull(roomType))
			throw new DTORoomTypeNullException();
		if(roomType.getTypeId() < Constants.TYPE_MIN_ID || roomType.getTypeId() > Constants.TYPE_MAX_ID)
			throw new DTORoomTypeIdException(roomType.getTypeId(), Constants.TYPE_MIN_ID, Constants.TYPE_MAX_ID);
		if(Objects.isNull(roomType.getCategory()) || roomType.getCategory().isBlank())
			throw new DTORoomTypeNameException(roomType.getCategory());
		if(roomType.getCapacity() < Constants.ROOM_MIN_CAP || roomType.getCapacity() > Constants.ROOM_MAX_CAP)
			throw new DTORoomTypeCapacityException(roomType.getCapacity(), Constants.ROOM_MIN_CAP, Constants.ROOM_MAX_CAP);
		if(roomType.getPricePerNight() < Constants.PRICE_PER_NIGHT_MIN || roomType.getPricePerNight() > Constants.PRICE_PER_NIGHT_MAX)
			throw new DTORoomTypePricePerNightException(roomType.getPricePerNight(), Constants.PRICE_PER_NIGHT_MIN, Constants.PRICE_PER_NIGHT_MAX);
	}
	
	public static void validate(RoomTypeIdRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTORoomTypeNullException();
		if(Objects.isNull(dto.roomTypeId()))
			throw new DTORoomTypeNullException();
		if (dto.roomTypeId() < Constants.TYPE_MIN_ID || dto.roomTypeId() > Constants.TYPE_MAX_ID)
			throw new DTORoomTypeIdException(dto.roomTypeId(), Constants.TYPE_MIN_ID, Constants.TYPE_MAX_ID);
	}
	
	public static void validate(Room room) throws ValidationException{
		if(Objects.isNull(room)) 
			throw new DTORoomNullException();
		if(room.getRoomNumber() < Constants.ROOM_MIN_NUMBER || room.getRoomNumber() > Constants.ROOM_MAX_NUMBER)
			throw new DTORoomNumberException(room.getRoomNumber(), Constants.ROOM_MIN_NUMBER, Constants.ROOM_MAX_NUMBER);
		validate(new RoomTypeIdRequest(room.getType()));
	}
	
	public static void validate(RoomNumberRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTORoomNullException();
		if(Objects.isNull(dto.roomNumber()))
			throw new DTORoomNullException();
		if(dto.roomNumber() < Constants.ROOM_MIN_NUMBER || dto.roomNumber() > Constants.ROOM_MAX_NUMBER)
			throw new DTORoomNumberException(dto.roomNumber(), Constants.ROOM_MIN_NUMBER, Constants.ROOM_MAX_NUMBER);
	}
	
	public static void validate(Guest guest) throws ValidationException {
		if(Objects.isNull(guest))
			throw new DTOGuestNullException();
		if(guest.getId() < Constants.GUEST_MIN_ID || guest.getId() > Constants.GUEST_MAX_ID) 
			throw new DTOGuestIdException(guest.getId(), Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID);
		if(Objects.isNull(guest.getName()) || guest.getName().isBlank())
			throw new DTOGuestNameException(guest.getName());
		if(Objects.isNull(guest.getEmail()) || !guest.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
			throw new DTOGuestEmailFormatException(guest.getEmail());
		if(Objects.isNull(guest.getDateOfBirth()) || guest.getAge() < 18)
			throw new DTOGuestDateOfBirthException(guest.getDateOfBirth());
		if(Objects.isNull(guest.getPassword()))
				throw new DTOGuestPasswordNullException();
	}
	
	public static void validate(GuestIdRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOGuestNullException();
		if(Objects.isNull(dto.guestId()))
			throw new DTOGuestNullException();
		if(dto.guestId() < Constants.GUEST_MIN_ID || dto.guestId() > Constants.GUEST_MAX_ID)
			throw new DTOGuestIdException(dto.guestId(), Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID);
	}
	
	public static void validate(Booking booking) throws ValidationException {
		if(Objects.isNull(booking))
			throw new DTOBookingNullException();
		if(booking.getBookingId() < Constants.BOOKING_MIN_ID || booking.getBookingId() > Constants.BOOKING_MAX_ID) 
			throw new DTOBookingIdException(booking.getBookingId(), Constants.BOOKING_MIN_ID, Constants.BOOKING_MAX_ID);
		validate(new GuestIdRequest(booking.getGuestId()));
		validate(new RoomNumberRequest(booking.getRoomNumber()));
		if(Objects.isNull(booking.getCheckIn()))
			throw new DTOBookingCheckInException();
		if(Objects.isNull(booking.getCheckOut()))
			throw new DTOBookingCheckOutException();
		if(booking.getCheckIn().compareTo(booking.getCheckOut()) > 0)
			throw new DTOBookingDateCollisionException(booking.getCheckIn(), booking.getCheckOut());
	}

	public static void validate(BookingIdRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOBookingNullException();
		if(Objects.isNull(dto.bookingId()))
			throw new DTOBookingNullException();
		if(dto.bookingId() < Constants.BOOKING_MIN_ID || dto.bookingId() > Constants.BOOKING_MAX_ID)
			throw new DTOBookingIdException(dto.bookingId(), Constants.BOOKING_MIN_ID, Constants.BOOKING_MAX_ID);
	}

	public static void validatePassword(String password) throws ValidationException {
		if(Objects.isNull(password))
			throw new DTOGuestPasswordNullException();
		if(!password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}"))
			throw new DTOGuestPasswordFormatException();
	}

	public static void validate(LoginRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOLoginNullException();
		if(Objects.isNull(dto.email()) || Objects.isNull(dto.password()))
			throw new DTOLoginNullException();
		if(!dto.email().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
			throw new DTOGuestEmailFormatException(dto.email());
		validatePassword(dto.password());	
	}

	public static void validate(BookingDatesRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOBookingNullException();
		if(Objects.isNull(dto.checkIn()) || Objects.isNull(dto.checkOut()))
			throw new DTOBookingDatesNullException();
		if (!dto.checkOut().isAfter(dto.checkIn()))
			throw new DTOBookingDateCollisionException(dto.checkIn(), dto.checkOut());
		if(dto.checkIn().isBefore(LocalDate.now()))
			throw new DTOBookingDateCheckInException();
	}

	public static void validate(AgeRangeRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOAgeRangeNullException();
		if (dto.minAge() < Constants.MIN_AGE || dto.minAge() > Constants.MAX_AGE 
				|| dto.maxAge() > Constants.MAX_AGE || dto.maxAge() < dto.minAge())
			throw new DTOAgeRangeException(dto.minAge(), dto.maxAge());
	}

	public static void validate(BookingCreateRequest dto) throws ValidationException{
		if(Objects.isNull(dto))
			throw new DTOBookingNullException();
		validate(new GuestIdRequest(dto.guestId()));
		validate(new RoomNumberRequest(dto.roomNumber()));
		if(Objects.isNull(dto.checkIn()))
			throw new DTOBookingCheckInException();
		if(Objects.isNull(dto.checkOut()))
			throw new DTOBookingCheckOutException();
		if(dto.checkIn().compareTo(dto.checkOut()) > 0)
			throw new DTOBookingDateCollisionException(dto.checkIn(), dto.checkOut());
	}

	public static void validate(RoomTypeCreateRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTORoomTypeNullException();
		if(Objects.isNull(dto.category()) || dto.category().isBlank())
			throw new DTORoomTypeNameException(dto.category());
		if(dto.capacity() < Constants.ROOM_MIN_CAP || dto.capacity() > Constants.ROOM_MAX_CAP)
			throw new DTORoomTypeCapacityException(dto.capacity(), Constants.ROOM_MIN_CAP, Constants.ROOM_MAX_CAP);
		if(dto.pricePerNight() < Constants.PRICE_PER_NIGHT_MIN || dto.pricePerNight() > Constants.PRICE_PER_NIGHT_MAX)
			throw new DTORoomTypePricePerNightException(dto.pricePerNight(), Constants.PRICE_PER_NIGHT_MIN, Constants.PRICE_PER_NIGHT_MAX);
	}

	public static void validate(RoomCreateRequest dto) throws ValidationException {
		if(Objects.isNull(dto)) 
			throw new DTORoomNullException();
		if(dto.roomNumber() < Constants.ROOM_MIN_NUMBER || dto.roomNumber() > Constants.ROOM_MAX_NUMBER)
			throw new DTORoomNumberException(dto.roomNumber(), Constants.ROOM_MIN_NUMBER, Constants.ROOM_MAX_NUMBER);
		validate(new RoomTypeIdRequest(dto.roomTypeId()));	
	}

	public static void validate(GuestCreateRequest dto) throws ValidationException {
		if(Objects.isNull(dto))
			throw new DTOGuestNullException();
		if(Objects.isNull(dto.name()) || dto.name().isBlank())
			throw new DTOGuestNameException(dto.name());
		if(Objects.isNull(dto.email()) || !dto.email().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
			throw new DTOGuestEmailFormatException(dto.email());
		if(Objects.isNull(dto.dateOfBirth()) || ChronoUnit.YEARS.between(dto.dateOfBirth(), LocalDate.now()) < 18)
			throw new DTOGuestDateOfBirthException(dto.dateOfBirth());
		if(Objects.isNull(dto.password()))
				throw new DTOGuestPasswordNullException();
		
	}
}
