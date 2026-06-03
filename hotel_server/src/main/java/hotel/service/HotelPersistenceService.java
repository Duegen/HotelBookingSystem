package hotel.service;

import hotel.app.HotelApplConstants;
import hotel.exceptions.ValidationException;
import hotel.interfaces.*;
import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;
import hotel.validation.Validation;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class HotelPersistenceService implements IHotelPersistenceService {
	private final IHotelBookingService service;
	private final IHotelAccountService accountService;
	private final IRoomTypesPersistence roomTypesPersistenceImp;
	private final IRoomsPersistence roomsPersistenceImp;
	private final IGuestsPersistence guestsPersistenceImp;
	private final IBookingsPersistence bookingsPersistenceImp;

	public HotelPersistenceService(IHotelBookingService service, IHotelAccountService accountService,
			IRoomTypesPersistence roomTypesPersistenceImp,
			IRoomsPersistence roomsPersistenceImp,
			IGuestsPersistence guestsPersistenceImp,
			IBookingsPersistence bookingsPersistenceImp) {
		this.service = service;
		this.accountService = accountService;
		this.roomTypesPersistenceImp = roomTypesPersistenceImp;
		this.roomsPersistenceImp = roomsPersistenceImp;
		this.guestsPersistenceImp = guestsPersistenceImp;
		this.bookingsPersistenceImp = bookingsPersistenceImp;
	}
	
	@Override
	public void saveHotelData() throws IOException {
		saveRoomTypes(service.getRoomTypes().values(), Paths.get(HotelApplConstants.DIR, HotelApplConstants.ROOMTYPE_FILE));
		saveRooms(service.getRooms().values(), Paths.get(HotelApplConstants.DIR, HotelApplConstants.ROOM_FILE));
		saveBookings(service.getBookings().values(), Paths.get(HotelApplConstants.DIR, HotelApplConstants.BOOKING_FILE));
	}
	
	@Override
	public void saveUserData() throws IOException{
		saveGuests(accountService.getGuests().values(), Paths.get(HotelApplConstants.DIR, HotelApplConstants.GUEST_FILE));
	}
	
	@Override
	public boolean loadHotelData() throws IOException {
		List<RoomType> roomTypesLoaded = loadRoomTypes(Paths.get(HotelApplConstants.DIR, HotelApplConstants.ROOMTYPE_FILE));
		List<Room> roomsLoaded = loadRooms(Paths.get(HotelApplConstants.DIR, HotelApplConstants.ROOM_FILE));
		List<Guest> guestsLoaded = loadGuests(Paths.get(HotelApplConstants.DIR, HotelApplConstants.GUEST_FILE));
		List<Booking> bookingsLoaded = loadBookings(Paths.get(HotelApplConstants.DIR, HotelApplConstants.BOOKING_FILE));
		
		roomTypesLoaded.stream().forEach(rt -> {
			try {
				service.addRoomType(rt);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		roomsLoaded.stream().forEach(r -> {
			try {
				service.addRoom(r);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		guestsLoaded.stream().forEach(g -> {
			try {
				accountService.addGuest(g);
			} catch (Exception e) {
				System.out.println("loading error: " + e.getMessage());
			}
		});
		bookingsLoaded.stream().forEach(bk -> {
			try {
				service.addBooking(bk);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
		return true;
	}

	private void saveRoomTypes(Collection<RoomType> roomTypes, Path dataFile) throws IOException {
		if (Objects.isNull(roomTypes))
			throw new IllegalArgumentException("List of room types is null");
		List<RoomType> validated = roomTypes.stream().filter(roomType -> {
			try {
				Validation.validate(roomType);
				return true;
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
		roomTypesPersistenceImp.saveRoomTypes(validated, dataFile);
	}

	private List<RoomType> loadRoomTypes(Path dataFile) throws IOException {
		List<RoomType> roomTypes = roomTypesPersistenceImp.loadRoomTypes(dataFile);
		return roomTypes.stream().filter(roomType -> {
			try {
				Validation.validate(roomType);
				return true;
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
	}

	private void saveRooms(Collection<Room> rooms, Path dataFile) throws IOException {
		if (Objects.isNull(rooms))
			throw new IllegalArgumentException("List of rooms is null");
		List<Room> validated = rooms.stream().filter(room -> {
			try {
				Validation.validate(room);
				return true;
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
		roomsPersistenceImp.saveRooms(validated, dataFile);
	}

	private List<Room> loadRooms(Path dataFile) throws IOException {
		List<Room> rooms = roomsPersistenceImp.loadRooms(dataFile);
		return rooms.stream().filter(room -> {
			try {
				Validation.validate(room);
				return true;
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
	}

	private void saveGuests(Collection<Guest> guests, Path dataFile) throws IOException {
		if (Objects.isNull(guests))
			throw new IllegalArgumentException("List of guests is null");
		List<Guest> validated = guests.stream().filter(guest -> {
			try {
				Validation.validate(guest);
				return true;
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
		guestsPersistenceImp.saveGuests(validated, dataFile);
	}

	private List<Guest> loadGuests(Path dataFile) throws IOException {
		List<Guest> guests = guestsPersistenceImp.loadGuests(dataFile);
		return guests.stream().filter(guest -> {
			try {
				Validation.validate(guest);
				return true;
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
	}

	private void saveBookings(Collection<Booking> bookings, Path dataFile) throws IOException {
		if (Objects.isNull(bookings))
			throw new IllegalArgumentException("List of bookings is null");
		List<Booking> validated = bookings.stream().filter(booking -> {
			try {
				Validation.validate(booking);
				return true;
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
		bookingsPersistenceImp.saveBookings(validated, dataFile);
	}

	private List<Booking> loadBookings(Path dataFile) throws IOException {
		List<Booking> bookings = bookingsPersistenceImp.loadBookings(dataFile);
		return bookings.stream().filter(booking -> {
			try {
				Validation.validate(booking);
				return true;
			} catch (ValidationException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}).toList();
	}

}
