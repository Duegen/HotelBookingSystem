package hotel.service.booking;

import hotel.dto.account.input.*;
import hotel.dto.account.output.*;
import hotel.dto.booking.input.*;
import hotel.dto.booking.output.*;
import hotel.exceptions.ValidationException;
import hotel.interfaces.IHotelAccountService;
import hotel.interfaces.IHotelBookingService;
import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;
import hotel.service.account.exceptions.GuestException;
import hotel.service.account.exceptions.GuestNotFoundException;
import hotel.service.booking.exceptions.BookingException;
import hotel.service.booking.exceptions.RoomException;
import hotel.service.booking.exceptions.RoomTypeException;
import hotel.service.booking.exceptions.booking.*;
import hotel.service.booking.exceptions.room.RemoveRoomAssignException;
import hotel.service.booking.exceptions.room.RoomAlreadyExistsException;
import hotel.service.booking.exceptions.room.RoomNotFoundException;
import hotel.service.booking.exceptions.room.RoomUnAvailableException;
import hotel.service.booking.exceptions.roomtype.*;
import hotel.validation.Constants;
import hotel.validation.Validation;
import hotel.validation.exceptions.roomtype.DTORoomTypeNullException;
import telran.net.server.ServerClientJava;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HotelBookingService implements IHotelBookingService {
	private IHotelAccountService accountService;
	private AtomicBoolean databaseIsModified;
	private final ReentrantReadWriteLock bookingReadWritelock;
	private final ReentrantReadWriteLock roomReadWritelock;
	private final ReentrantReadWriteLock roomTypeReadWritelock;

	@Override
	public ReentrantReadWriteLock getBookingreadwritelock() {
		return bookingReadWritelock;
	}

	private final Map<Integer, RoomType> roomTypes;
	private final Map<Integer, Booking> bookings;
	private final Map<Integer, Room> rooms;

	public HotelBookingService(IHotelAccountService accountService) {
		roomTypes = new TreeMap<>();
		rooms = new TreeMap<>();
		bookings = new TreeMap<>();
		databaseIsModified = new AtomicBoolean();
		databaseIsModified.set(false);
		bookingReadWritelock = new ReentrantReadWriteLock();
		roomReadWritelock = new ReentrantReadWriteLock();
		roomTypeReadWritelock = new ReentrantReadWriteLock();
		this.accountService = accountService;
		accountService.setBookingService(this);
	}
	
	@Override
	public AtomicBoolean getDatabaseIsModified() {
		return databaseIsModified;
	}
	
	@Override
	public Map<Integer, RoomType> getRoomTypes() {
		try {
			roomTypeReadWritelock.readLock().lock();
			return Collections.unmodifiableMap(roomTypes);
		} finally {
			roomTypeReadWritelock.readLock().unlock();
		}
	}

	@Override
	public List<RoomTypeResponse> getRoomTypesDTO() {
		try {
			roomTypeReadWritelock.readLock().lock();
			return roomTypes.values().stream().map(rt -> new RoomTypeResponse(rt.getTypeId(), rt.getCategory(),
					rt.getPricePerNight(), rt.getCapacity())).toList();
		} finally {
			roomTypeReadWritelock.readLock().unlock();
		}
	}

	@Override
	public Map<Integer, Booking> getBookings() {
		try {
			bookingReadWritelock.readLock().lock();
			return Collections.unmodifiableMap(bookings);
		} finally {
			bookingReadWritelock.readLock().unlock();
		}
	}

	@Override
	public List<BookingFullResponse> getBookingsFullResponse() {
		try {
			bookingReadWritelock.readLock().lock();
			roomReadWritelock.readLock().lock();
			roomTypeReadWritelock.readLock().lock();
			return bookings.values().stream().map(bk -> {
				Guest guest = accountService.getGuests().get(bk.getGuestId());
				GuestResponse guestRes = new GuestResponse(guest.getId(), guest.getName(), guest.getEmail(),
						guest.getDateOfBirth());
				RoomType rt = roomTypes.get(rooms.get(bk.getRoomNumber()).getType());
				RoomTypeResponse rtRes = new RoomTypeResponse(rt.getTypeId(), rt.getCategory(), rt.getPricePerNight(),
						rt.getCapacity());
				return new BookingFullResponse(bk.getBookingId(), guestRes, bk.getRoomNumber(), rtRes, bk.getCheckIn(),
						bk.getCheckOut());
			}).toList();
		} finally {
			bookingReadWritelock.readLock().unlock();
			roomReadWritelock.readLock().unlock();
			roomTypeReadWritelock.readLock().unlock();
		}

	}

	@Override
	public Map<Integer, Room> getRooms() {
		try {
			roomReadWritelock.readLock().lock();
			return Collections.unmodifiableMap(rooms);
		} finally {
			roomReadWritelock.readLock().unlock();
		}
	}

	@Override
	public List<RoomResponse> getRoomsDTO() {
		try {
			roomReadWritelock.readLock().lock();
			return rooms.values().stream().map(r -> {
				RoomType rt = roomTypes.get(r.getType());
				return new RoomResponse(r.getRoomNumber(), rt.getCategory(), rt.getPricePerNight(), rt.getCapacity());
			}).toList();
		} finally {
			roomReadWritelock.readLock().unlock();
		}
	}

	@Override
	public RoomTypeResponse createRoomType(RoomTypeCreateRequest dto) throws RoomTypeException, ValidationException {
		Validation.validate(dto);
		try {
			roomReadWritelock.readLock().lock();
			if (Objects.isNull(dto))
				throw new DTORoomTypeNullException();
			int roomTypeId = findFreeId(roomTypes, Constants.TYPE_MIN_ID, Constants.TYPE_MAX_ID);
			if (roomTypeId == -1)
				throw new NoFreeRoomTypeIdException();
			RoomType roomType = new RoomType(roomTypeId, dto);
			if (roomTypes.values().stream().anyMatch(rt -> rt.isDuplicate(roomType)))
				throw new RoomTypeDuplicateException(roomType);
			roomTypes.put(roomTypeId, roomType);
			databaseIsModified.set(true);
			return new RoomTypeResponse(roomType.getTypeId(), roomType.getCategory(), roomType.getPricePerNight(),
					roomType.getCapacity());
		} finally {
			roomReadWritelock.readLock().unlock();
		}
	}

	@Override
	public void addRoomType(RoomType roomType) throws RoomTypeException, ValidationException {
		Validation.validate(roomType);
		try {
			roomTypeReadWritelock.writeLock().lock();
			if (roomTypes.containsKey(roomType.getTypeId()))
				throw new RoomTypeAlreadyExistsException(roomType.getTypeId());
			if (roomTypes.values().stream().anyMatch(rt -> rt.isDuplicate(roomType)))
				throw new RoomTypeDuplicateException(roomType);
			roomTypes.put(roomType.getTypeId(), roomType.copy());
			databaseIsModified.set(true);
		} finally {
			roomTypeReadWritelock.writeLock().unlock();
		}
	}

	@Override
	public RoomTypeResponse removeRoomType(RoomTypeIdRequest dto) throws RoomTypeException, ValidationException {
		Validation.validate(dto);
		try {
			roomTypeReadWritelock.writeLock().lock();
			if (!roomTypes.containsKey(dto.roomTypeId()))
				throw new RoomTypeNotFoundException(dto.roomTypeId());
			RoomType roomType = roomTypes.get(dto.roomTypeId());
			boolean typeIsUsedByRooms = rooms.values().stream().map(Room::getType)
					.anyMatch(type -> type.equals(roomType.getTypeId()));
			if (typeIsUsedByRooms)
				throw new RemoveRoomTypeAssignException(dto.roomTypeId());
			RoomType removedRoomType = roomTypes.remove(dto.roomTypeId());
			databaseIsModified.set(true);
			return new RoomTypeResponse(removedRoomType.getTypeId(), removedRoomType.getCategory(),
					removedRoomType.getPricePerNight(), removedRoomType.getCapacity());
		} finally {
			roomTypeReadWritelock.writeLock().unlock();
		}
	}

	@Override
	public RoomResponse createRoom(RoomCreateRequest dto) throws RoomException, RoomTypeException, ValidationException {
		
		Validation.validate(dto);
		try {
			roomReadWritelock.writeLock().lock();
			Room room = new Room(dto);
			if (rooms.containsKey(dto.roomNumber()))
				throw new RoomAlreadyExistsException(room.getRoomNumber());
			if (!roomTypes.containsKey(dto.roomTypeId()))
				throw new RoomTypeNotFoundException(room.getType());
			rooms.put(dto.roomNumber(), room);
			RoomType roomType = roomTypes.get(room.getType());
			databaseIsModified.set(true);
			return new RoomResponse(room.getRoomNumber(), roomType.getCategory(), roomType.getPricePerNight(),
					roomType.getCapacity());
		} finally {
			roomReadWritelock.writeLock().unlock();
		}
	}

	@Override
	public void addRoom(Room room) throws RoomException, RoomTypeException, ValidationException {
		Validation.validate(room);
		try {
			roomReadWritelock.writeLock().lock();
			if (rooms.containsKey(room.getRoomNumber()))
				throw new RoomAlreadyExistsException(room.getRoomNumber());
			if (!roomTypes.containsKey(room.getType()))
				throw new RoomTypeNotFoundException(room.getType());
			databaseIsModified.set(true);
			rooms.put(room.getRoomNumber(), room.copy());
		} finally {
			roomReadWritelock.writeLock().unlock();
		}
	}

	@Override
	public RoomResponse removeRoom(RoomNumberRequest dto) throws RoomException, ValidationException {
		Validation.validate(dto);
		try {
			roomReadWritelock.writeLock().lock();
			if (!rooms.containsKey(dto.roomNumber()))
				throw new RoomNotFoundException(dto.roomNumber());
			boolean hasRelatedBookings = bookings.values().stream()
					.anyMatch(booking -> booking.getRoomNumber() == dto.roomNumber());
			if (hasRelatedBookings)
				throw new RemoveRoomAssignException(dto.roomNumber());
			Room remove = rooms.remove(dto.roomNumber());
			RoomType rt = roomTypes.get(remove.getType());
			databaseIsModified.set(true);
			return new RoomResponse(remove.getRoomNumber(), rt.getCategory(), rt.getPricePerNight(),
					rt.getCapacity());
		} finally {
			roomReadWritelock.writeLock().unlock();
		}
	}

	@Override
	public void addBooking(Booking booking)
			throws BookingException, GuestException, RoomException, ValidationException {
		Validation.validate(booking);
		try {
			bookingReadWritelock.writeLock().lock();
			roomReadWritelock.readLock().lock();
			roomTypeReadWritelock.readLock().lock();
			if (!accountService.getGuests().containsKey(booking.getGuestId()))
				throw new GuestNotFoundException(booking.getBookingId());
			if (!rooms.containsKey(booking.getRoomNumber()))
				throw new RoomNotFoundException(booking.getRoomNumber());
			if (!isRoomAvailable(booking.getRoomNumber(), booking.getCheckIn(), booking.getCheckOut()))
				throw new RoomUnAvailableException(booking.getRoomNumber(), booking.getCheckIn(),
						booking.getCheckOut());
			if (bookings.containsKey(booking.getBookingId()))
				throw new BookingAlreadyExistsException(booking.getBookingId());
			bookings.put(booking.getBookingId(), booking.copy());
		} finally {
			bookingReadWritelock.writeLock().unlock();
			roomReadWritelock.readLock().unlock();
			roomTypeReadWritelock.readLock().unlock();
		}

	}

	@Override
	public BookingResponse createBooking(BookingCreateRequest dto)
			throws BookingException, GuestException, RoomException, ValidationException {
		int bookingId;
		Booking booking;
		RoomType roomType;
		Validation.validate(dto);
		try {
			roomReadWritelock.readLock().lock();
			roomTypeReadWritelock.readLock().lock();
			bookingReadWritelock.writeLock().lock();
			bookingId = findFreeId(bookings, Constants.BOOKING_MIN_ID, Constants.BOOKING_MAX_ID);
			if (bookingId == -1)
				throw new NoFreeBookingIdException();
			booking = new Booking(bookingId, dto);
			if (!accountService.getGuests().containsKey(dto.guestId()))
				throw new GuestNotFoundException(dto.guestId());
			if (!rooms.containsKey(dto.roomNumber()))
				throw new RoomNotFoundException(dto.roomNumber());
			if (!isRoomAvailable(dto.roomNumber(), dto.checkIn(), dto.checkOut()))
				throw new RoomUnAvailableException(dto.guestId(), dto.checkIn(), dto.checkOut());
			bookings.put(bookingId, booking);
			roomType = roomTypes.get(rooms.get(booking.getRoomNumber()).getType());
			databaseIsModified.set(true);
			return new BookingResponse(bookingId, booking.getRoomNumber(), roomType.getCategory(),
					roomType.getPricePerNight(), roomType.getCapacity(), booking.getCheckIn(), booking.getCheckOut());
		} finally {
			bookingReadWritelock.writeLock().unlock();
			roomReadWritelock.readLock().unlock();
			roomTypeReadWritelock.readLock().unlock();
		}
		
	}

	@Override
	public BookingResponse cancelBooking(BookingIdRequest dto) throws BookingException, ValidationException {
		Validation.validate(dto);
		try {
			roomReadWritelock.readLock().lock();
			roomTypeReadWritelock.readLock().lock();
			bookingReadWritelock.writeLock().lock();
			if (!bookings.containsKey(dto.bookingId()))
				throw new BookingNotFoundException(dto.bookingId());
			Booking booking = bookings.get(dto.bookingId());
			boolean isBookingActive = LocalDate.now().isAfter(booking.getCheckIn())
					&& LocalDate.now().isBefore(booking.getCheckOut());
			if (isBookingActive)
				throw new RemoveBookingActiveException(booking.getBookingId());
			if (booking.getCheckOut().isBefore(LocalDate.now()))
				throw new RemoveExpireBookingException(booking.getBookingId());
			booking = bookings.remove(dto.bookingId());
			RoomType roomType = roomTypes.get(rooms.get(booking.getRoomNumber()).getType());
			databaseIsModified.set(true);
			return new BookingResponse(booking.getBookingId(), booking.getRoomNumber(), roomType.getCategory(),
					roomType.getPricePerNight(), roomType.getCapacity(), booking.getCheckIn(),
					booking.getCheckOut());
		} finally {
			bookingReadWritelock.writeLock().unlock();
			roomReadWritelock.readLock().unlock();
			roomTypeReadWritelock.readLock().unlock();
		}
	}

	@Override
	public BookingFullResponse removeBooking(BookingIdRequest dto) throws BookingException, ValidationException {
		Validation.validate(dto);
		try {
			bookingReadWritelock.writeLock().lock();
			roomReadWritelock.readLock().lock();
			roomTypeReadWritelock.readLock().lock();
			if (!bookings.containsKey(dto.bookingId()))
				throw new BookingNotFoundException(dto.bookingId());
			Booking booking = bookings.get(dto.bookingId());
			boolean isBookingActive = LocalDate.now().isAfter(booking.getCheckIn())
					&& LocalDate.now().isBefore(booking.getCheckOut());
			if (isBookingActive)
				throw new RemoveBookingActiveException(booking.getBookingId());
			booking = bookings.remove(dto.bookingId());
			RoomType roomType = roomTypes.get(rooms.get(booking.getRoomNumber()).getType());
			Guest guest = accountService.getGuests().get(booking.getGuestId());
			GuestResponse guestRes = new GuestResponse(guest.getId(), guest.getName(), guest.getEmail(),
					guest.getDateOfBirth());
			RoomTypeResponse rtRes = new RoomTypeResponse(roomType.getTypeId(), roomType.getCategory(),
					roomType.getPricePerNight(), roomType.getCapacity());
			databaseIsModified.set(true);
			return new BookingFullResponse(booking.getBookingId(), guestRes, booking.getRoomNumber(), rtRes,
					booking.getCheckIn(), booking.getCheckOut());
		} finally {
			bookingReadWritelock.writeLock().unlock();
			roomReadWritelock.readLock().unlock();
			roomTypeReadWritelock.readLock().unlock();
		}
	}

	private <R> int findFreeId(Map<Integer, R> sourceMap, int minId, int maxId) {
		int freeId = minId;
		for (int id : sourceMap.keySet()) {
			if (id != freeId)
				break;
			freeId++;
		}
		return freeId > maxId ? -1 : freeId;
	}

	@Override
	public boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut) {
		if (Objects.isNull(checkIn) || Objects.isNull(checkOut))
			throw new IllegalArgumentException("arg can not be null");
		if (!checkOut.isAfter(checkIn)) {
			throw new IllegalArgumentException("check-out must be after check-in");
		}
		try {
			bookingReadWritelock.readLock().lock();
			return bookings.values().stream().filter(booking -> booking.getRoomNumber() == roomNumber)
					.noneMatch(booking -> booking.overlaps(checkIn, checkOut));
		} finally {
			bookingReadWritelock.readLock().unlock();
		}
	}

	@Override
	public List<Booking> getBookingsStartOn(LocalDate checkInDate) {
		return bookings.values().stream().filter(bk -> bk.getCheckIn().equals(checkInDate)).toList();
	}

	@Override
	public List<BookingResponse> getBookingsForGuest() {
		try {
			roomReadWritelock.readLock().lock();
			roomTypeReadWritelock.readLock().lock();
			bookingReadWritelock.readLock().lock();
			List<Booking> guestBookings = bookings.values().stream()
					.filter(bk -> bk.getGuestId() == ServerClientJava.getLocal().guestId()).toList();
			return guestBookings.stream().map(bk -> {
				RoomType roomType = roomTypes.get(rooms.get(bk.getRoomNumber()).getType());
				return new BookingResponse(bk.getBookingId(), bk.getRoomNumber(), roomType.getCategory(),
						roomType.getPricePerNight(), roomType.getCapacity(), bk.getCheckIn(), bk.getCheckOut());
			}).toList();
		} finally {
			roomReadWritelock.readLock().unlock();
			roomTypeReadWritelock.readLock().unlock();
			bookingReadWritelock.readLock().unlock();
		}
	}

	@Override
	public Room findRoomByNumber(int roomNumber) {
		return rooms.get(roomNumber);
	}

	@Override
	public RoomType findRoomTypeById(int roomTypeId) {
		return roomTypes.get(roomTypeId);
	}

	@Override
	public Booking findBookingById(int bookingId) {
		return bookings.get(bookingId);
	}

	@Override
	public List<RoomResponse> getAvailableRooms(BookingDatesRequest dto) throws ValidationException {
		Validation.validate(dto);

		try {
			roomReadWritelock.readLock().lock();
			return rooms.values().stream()
					.filter(room -> isRoomAvailable(room.getRoomNumber(), dto.checkIn(), dto.checkOut())).map(room -> {
						RoomType roomType = roomTypes.get(room.getType());
						return new RoomResponse(room.getRoomNumber(), roomType.getCategory(),
								roomType.getPricePerNight(), roomType.getCapacity());
					}).toList();
		} finally {
			roomReadWritelock.readLock().unlock();
		}
	}
	
	@Override
	public ReentrantReadWriteLock getRoomReadWritelock() {
		return roomReadWritelock;
	}

	@Override
	public ReentrantReadWriteLock getRoomTypeReadWritelock() {
		return roomTypeReadWritelock;
	}
}
