package hotel.service;

import hotel.dto.booking.input.AgeRangeRequest;
import hotel.dto.booking.input.DateRequest;
import hotel.dto.booking.output.BookingResponse;
import hotel.dto.booking.output.RoomTypeResponse;
import hotel.exceptions.ValidationException;
import hotel.interfaces.IAnalyticsCommon;
import hotel.interfaces.IHotelAccountService;
import hotel.interfaces.IHotelBookingService;
import hotel.model.Booking;
import hotel.model.Room;
import hotel.model.RoomType;
import hotel.validation.Validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyticsService implements IAnalyticsCommon {
	private final IHotelBookingService bookingService;
	private final IHotelAccountService accountService;
	
	public AnalyticsService(IHotelBookingService bookingService, IHotelAccountService accountService) {
		this.accountService = accountService;
		this.bookingService = bookingService;
	}

	@Override
	public int bookingsNumber() {
		return bookingService.getBookings().size();
	}

	@Override
	public double averageBookingPrice() {
		Collection<Booking> bookings = bookingService.getBookings().values(); 
		if (bookings.size() == 0)
			return 0.;
		OptionalDouble result = bookings.stream()
				.mapToDouble(booking -> ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut())
						* bookingService.getRoomTypes().get(bookingService.getRooms().get(booking.getRoomNumber()).getType()).getPricePerNight())
				.average();
		return result.getAsDouble();
	}

	@Override
	public List<RoomTypeResponse> mostPopularRoomTypes() {
		return getMostPopularRoomStream(bookingService.getBookings().values().stream());
	}

	@Override
	public List<RoomTypeResponse> getMostPopularRoomTypesForAgeRange(AgeRangeRequest ageRange) throws ValidationException {
		Validation.validate(ageRange);
		return getMostPopularRoomStream(
				(bookingService.getBookings().values().stream().filter(bk -> accountService.getGuests().get(bk.getGuestId()).getAge() >= ageRange.minAge()
						&& accountService.getGuests().get(bk.getGuestId()).getAge() <= ageRange.maxAge())));
	}

	private List<RoomTypeResponse> getMostPopularRoomStream(Stream<Booking> bookingStream) {
		Map<Integer, Long> counts = bookingStream.collect(
				Collectors.groupingBy(booking -> bookingService.getRooms().get(booking.getRoomNumber()).getType(), Collectors.counting()));
		long max = counts.values().stream().mapToLong(Long::longValue).max().orElse(0);
		return counts.entrySet().stream().filter(entry -> entry.getValue() == max)
				.map(entry -> {
					RoomType rt = bookingService.getRoomTypes().get(entry.getKey());
					return new RoomTypeResponse(rt.getTypeId(), rt.getCategory(), rt.getPricePerNight(), rt.getCapacity());
				}).toList();
				
	}
	
	@Override
	public int getAvailableRoomsCount(DateRequest date) {
		return (int) bookingService.getRooms().values().stream().filter(room -> isAvailable(room, date.date())).count();
	}

	@Override
	public int getOccupiedRoomsCount(DateRequest date) {
		return (int) bookingService.getRooms().values().stream().filter(room -> !isAvailable(room, date.date())).count();
	}

	private boolean isAvailable(Room room, LocalDate date) {
		if (bookingService.getBookings() == null || bookingService.getBookings().isEmpty()) {
			return true;
		}
		return bookingService.getBookings().values().stream().noneMatch(booking -> booking.getRoomNumber() == room.getRoomNumber() && booking.isActiveOn(date));
	}

	@Override
	public List<BookingResponse> showBookingsByCheckIn(DateRequest date) {
		return bookingService.getBookings().entrySet().stream()
				.filter(entry -> entry.getValue().getCheckIn().equals(date.date()))
				.map(entry -> {
					Booking bk = entry.getValue();
					RoomType rt = bookingService.getRoomTypes().get(bookingService.getRooms().get(bk.getRoomNumber()).getType());
					return new BookingResponse(bk.getBookingId(), bk.getRoomNumber(), rt.getCategory(), 
							rt.getPricePerNight(), rt.getCapacity(), bk.getCheckIn(), bk.getCheckOut());
				})
				.toList();
	}

	
}
