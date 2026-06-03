package hotel.generator;

import hotel.dto.account.input.GuestCreateRequest;
import hotel.dto.booking.input.BookingCreateRequest;
import hotel.dto.booking.input.RoomCreateRequest;
import hotel.dto.booking.input.RoomTypeCreateRequest;
import hotel.exceptions.ConflictException;
import hotel.exceptions.ValidationException;
import hotel.interfaces.IHotelAccountService;
import hotel.interfaces.IHotelBookingService;
import hotel.model.Booking;
import hotel.model.Guest;
import hotel.model.Room;
import hotel.model.RoomType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class RandomHotelDataGenerator {
	private final IHotelBookingService bookingService;
	private final IHotelAccountService accountService;
	private static final int N_TYPES = 10;
	private final static int N_GUESTS = Constants.NAMES_EMAILS.size();
	private final static int N_BOOKINGS = 20;
	
	private static Random r = new Random();
	
	public RandomHotelDataGenerator(IHotelBookingService bookingService, IHotelAccountService accountService) {
		this.bookingService = bookingService;
		this.accountService = accountService;
	}
	
	private void generateRoomTypes() {
		while(bookingService.getRoomTypes().size() < N_TYPES) {
			try {
				bookingService.createRoomType(getRandomRoomType());
			} catch (Exception e) {
				continue;
			}
		}
	}

	private RoomTypeCreateRequest getRandomRoomType() {
		int randomNameIndex = r.nextInt(0, 3);
		String randomName = Constants.NAMES[randomNameIndex];
		int randomCapacity = Constants.CAPACITY[r.nextInt(0, 4)];
		double randomPrice = Constants.PRICE_PER_PERSON_PER_NIGHT[randomNameIndex];
		RoomTypeCreateRequest dto = new RoomTypeCreateRequest(randomName, randomPrice * randomCapacity, randomCapacity);
		return dto;
	}
	
	private void generateRooms(){
		for (int number : Constants.ROOM_NUMBERS)
			try {
				bookingService.createRoom(getRandomRoom(number));
			} catch (ConflictException | ValidationException e) {
				continue;
			}
	}

	private RoomCreateRequest getRandomRoom(int number) {
		Collection<RoomType> roomTypes = bookingService.getRoomTypes().values(); 
		int randomTypeId = roomTypes.stream().skip(r.nextInt(0, roomTypes.size()))
				.findFirst().orElse(null).getTypeId();
		return new RoomCreateRequest(number, randomTypeId);
	}
	
	private void generateGuests(){
		Collection<Guest> guests = accountService.getGuests().values();
		while(guests.size() < N_GUESTS) { 
			GuestCreateRequest randomGuest = getRandomGuest(guests.size());
			try {
				accountService.createGuest(randomGuest);
			} catch (Exception e) {
				continue;
			}
		}
	}

	private static GuestCreateRequest getRandomGuest(int index) {
		Entry<String, String> randomNameEmail = Constants.NAMES_EMAILS.entrySet().stream()
				.skip(index)
				.findFirst().orElse(null);
		LocalDate randomBD = Constants.MIN_BD.plusDays(r.nextLong(0, ChronoUnit.DAYS.between(Constants.MIN_BD, Constants.MAX_BD)));
		GuestCreateRequest dto = new GuestCreateRequest(randomNameEmail.getKey(),
				randomNameEmail.getValue(), randomBD, "Password1");
		return dto;
	}
	
	private void generateBookings(){
		Collection<Booking> bookings = bookingService.getBookings().values();
		while(bookings.size() < N_BOOKINGS) {
			BookingCreateRequest randomBooking = getRandomBooking();
			try {
				bookingService.createBooking(randomBooking);
			} catch (Exception e) {
				continue;
			}
		}
	}

	private BookingCreateRequest getRandomBooking() {
		Map<Integer, Room> rooms = bookingService.getRooms();
		Map<Integer, Guest> guests = accountService.getGuests();
		int randomRoomId = rooms.values().stream()
				.skip(r.nextInt(0, rooms.size())).findFirst().orElse(null).getRoomNumber();
		int randomGuestId = guests.values().stream()
				.skip(r.nextInt(0, guests.size())).findFirst().orElse(null).getId();
		LocalDate date = LocalDate.now().minusDays(r.nextInt(1, 100));
		LocalDate checkOut = date.plusDays(r.nextInt(1, 20));
		LocalDate checkIn = checkOut.minusDays(r.nextInt(1, 20));
		return new BookingCreateRequest(randomGuestId, randomRoomId, checkIn, checkOut);
	}
	
	public void generate() {
		generateRoomTypes();
		generateRooms();
		generateGuests();
		generateBookings();
	}
}
