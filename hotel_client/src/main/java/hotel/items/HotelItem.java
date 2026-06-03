package hotel.items;

import cli.Inputoutput;
import cli.Item;
import hotel.HotelApplContext;
import hotel.dto.account.input.GuestCreateRequest;
import hotel.dto.account.input.GuestIdRequest;
import hotel.dto.account.input.LoginRequest;
import hotel.dto.account.output.GuestResponse;
import hotel.dto.booking.input.*;
import hotel.dto.booking.output.BookingFullResponse;
import hotel.dto.booking.output.BookingResponse;
import hotel.dto.booking.output.RoomResponse;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.interfaces.IAnalyticsCommon;
import hotel.interfaces.IHotelAccountServiceCommon;
import hotel.interfaces.IHotelBookingServiceCommon;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public abstract class HotelItem implements Item {
	protected final Inputoutput inOut;
	protected final IHotelBookingServiceCommon bookingService;
	protected final IHotelAccountServiceCommon accountService;
	protected final IAnalyticsCommon analyticsService;
	protected final String dateFormat;
	protected final HotelApplContext context;

	protected HotelItem(HotelApplContext context) {
		inOut = context.getInOut();
		bookingService = context.getBookingService();
		accountService = context.getAccountService();
		analyticsService = context.getAnalyticsService();
		dateFormat = context.getDateFormat();
		this.context = context;
	}

	protected LoginRequest login() {
		String email = inOut.inputString("Enter email");
		if(Objects.isNull(email))
			return null;
		String password = inOut.inputString("Enter password");
		if(Objects.isNull(password))
			return null;
		return new LoginRequest(email, password);
	}
	
	protected GuestIdRequest getExistingGuest() {
		Integer guestId = inOut.inputInteger("Enter guest id");
		if (guestId == null)
			return null;
		return new GuestIdRequest(guestId);
	}

	protected RoomNumberRequest getExistingRoom() {
		Integer roomNumber = inOut.inputInteger("Enter room number");
		if (roomNumber == null)
			return null;
		return new RoomNumberRequest(roomNumber);
	}

	protected RoomTypeIdRequest getExistingRoomType() {
		Integer roomTypeId = inOut.inputInteger("Enter room type id");
		if (roomTypeId == null)
			return null;
		return new RoomTypeIdRequest(roomTypeId);
	}
	
	protected BookingIdRequest getExistingBooking() {
		Integer bookingId = inOut.inputInteger("Enter booking id");
		if (bookingId == null)
			return null;
		return new BookingIdRequest(bookingId);
	}

	protected BookingDatesRequest inputCheckInCheckOut() {
		LocalDate checkIn = inOut.inputDate("Enter check in date in format", dateFormat);
		if (checkIn == null)
			return null;
		LocalDate checkOut = inOut.inputDate("Enter check out date in format", dateFormat);
		if (checkOut == null)
			return null;
		if (!checkOut.isAfter(checkIn)) {
			inOut.outputlLine("Check out must be after check in");
			return null;
		}
		return new BookingDatesRequest(checkIn, checkOut);
	}
	
	protected RoomTypeCreateRequest addRoomType() {
		String name = inOut.inputString("Enter room type category");
		if(Objects.isNull(name))
			return null;
		Double pricePerNight = inOut.inputDouble("Enter price per night");
		if(Objects.isNull(pricePerNight))
			return null;
		Integer capacity = inOut.inputInteger("Enter room capacity");
		if(Objects.isNull(capacity))
			return null;
		return new RoomTypeCreateRequest(name, pricePerNight, capacity);
	}
	
	protected RoomTypeIdRequest removeRoomType() {
		Integer roomTypeId = inOut.inputInteger("Enter room type id");
		if(Objects.isNull(roomTypeId))
			return null;
		return new RoomTypeIdRequest(roomTypeId);
	}
	
	protected RoomCreateRequest addRoom() {
		Integer roomNumber = inOut.inputInteger("Enter room number");
		if(Objects.isNull(roomNumber))
			return null;
		Integer roomTypeId = inOut.inputInteger("Enter room type id");
		if(Objects.isNull(roomTypeId))
			return null;
		return new RoomCreateRequest(roomNumber, roomTypeId);
	}
	
	protected RoomTypeIdRequest removeRoom() {
		Integer roomId = inOut.inputInteger("Enter room id");
		if(Objects.isNull(roomId))
			return null;
		return new RoomTypeIdRequest(roomId);
	}
	
	protected GuestCreateRequest inputNewGuest() {
		String name = inOut.inputString("Enter guest name");
		if(Objects.isNull(name))
			return null;
		String email = inOut.inputString("Enter guest email");
		if(Objects.isNull(email))
			return null;
		String password = inOut.inputString("Enter guest password");
		if(Objects.isNull(password))
			return null;
		LocalDate birthDate = inOut.inputDate("Enter birth date in format", dateFormat);
		if(Objects.isNull(birthDate))
			return null;
		return new GuestCreateRequest(name, email, birthDate, password);
	}
	
	protected GuestIdRequest removeGuest() {
		Integer guestId = inOut.inputInteger("Enter guest id");
		if(Objects.isNull(guestId))
			return null;
		return new GuestIdRequest(guestId);
	}

	protected void showBookings(List<BookingResponse> bookings, String newMessage) {
		if (bookings == null || bookings.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		bookings.stream().sorted((bk1, bk2) -> bk1.BookingId() - bk2.BookingId())
		.forEach(bk -> inOut.outputlLine("booking id - %d, room number - %d, category - %s, price per night - %.2f, capacity - %d, check in - %s, check out - %s"
				.formatted(bk.BookingId(), bk.roomNumber(), bk.caregory(), bk.pricePerNight(), bk.capacity(), bk.checkIn().toString(), bk.checkOut().toString())));
	}
	
	protected void showFullBookings(List<BookingFullResponse> bookings, String newMessage) {
		if (bookings == null || bookings.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		bookings.stream().sorted((bk1, bk2) -> bk1.BookingId() - bk2.BookingId())
		.forEach(bk -> {
			GuestResponse guest = bk.guest();
			RoomTypeResponse rt = bk.roomType();
			inOut.outputlLine("booking id - %d, guest id - %d, guest name - %s, guest email - %s, room number - %d, category - %s, price per night - %.2f, capacity - %d, check in - %s, check out - %s"
					.formatted(bk.BookingId(), guest.guestId(), guest.name(), guest.email(), bk.roomNumber(), rt.category(), rt.pricePerNight(), rt.capacity(), bk.checkIn().toString(), bk.checkOut().toString()));
		});
				
	}

	protected void showRooms(List<RoomResponse> rooms, String newMessage) {
		if (rooms == null || rooms.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		rooms.stream().sorted((r1, r2) -> r1.RoomNumber() - r2.RoomNumber())
		.forEach(room -> inOut.outputlLine("room number - %d, category - %s, price per night - %.2f, capacity - %d"
				.formatted(room.RoomNumber(), room.category(), room.pricePerNight(), room.capacity())));
	}
	
	protected void showRoomTypes(List<RoomTypeResponse> roomTypes, String newMessage) {
		if (roomTypes == null || roomTypes.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		roomTypes.stream().sorted((rt1, rt2) -> rt1.roomTypeId() - rt2.roomTypeId())
		.forEach(rt -> inOut.outputlLine("room type id - %d, category - %s, price per night - %.2f, capacity - %d"
				.formatted(rt.roomTypeId(), rt.category(), rt.pricePerNight(), rt.capacity())));
	}
	
	protected void showGuests(List<GuestResponse> guests, String newMessage) {
		if (guests == null || guests.isEmpty()) {
			inOut.outputlLine(newMessage);
			return;
		}
		guests.stream().sorted((g1, g2) -> g1.guestId() - g2.guestId())
		.forEach(g -> inOut.outputlLine("guest id - %d, guest name - %s, guest email - %s, guest date of birth - %s"
				.formatted(g.guestId(), g.name(), g.email(), g.dateOfBirth().toString())));
	}
}
