package hotel.interfaces;

import hotel.exceptions.ConflictException;
import hotel.exceptions.ValidationException;
import hotel.model.Booking;
import hotel.model.Room;
import hotel.model.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface IHotelBookingService extends IHotelBookingServiceCommon{
	void addRoomType(RoomType roomType) throws ConflictException, ValidationException;
	void addRoom(Room dto) throws ConflictException, ValidationException;
	void addBooking(Booking booking) throws ConflictException, ValidationException;
	
	boolean isRoomAvailable(int roomNumber, LocalDate checkIn, LocalDate checkOut);
	
	List<Booking> getBookingsStartOn(LocalDate checkInDate);
	
	Room findRoomByNumber(int roomNumber);
	RoomType findRoomTypeById(int roomTypeId);
	Booking findBookingById(int bookingId);
	
	Map<Integer, Booking> getBookings();
	Map<Integer, RoomType> getRoomTypes();
	Map<Integer, Room> getRooms();
	
	AtomicBoolean getDatabaseIsModified();
	ReentrantReadWriteLock getBookingreadwritelock();
	ReentrantReadWriteLock getRoomReadWritelock();
	ReentrantReadWriteLock getRoomTypeReadWritelock();
}
