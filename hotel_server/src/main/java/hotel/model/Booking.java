package hotel.model;

import hotel.dto.booking.input.BookingCreateRequest;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


public class Booking implements Serializable{
	private static final long serialVersionUID = 1L;
	private final int bookingId;
	private int guestId;
	private int roomNumber;
	private LocalDate checkIn;
	private LocalDate checkOut;
	
	public Booking(int bookingId, BookingCreateRequest dto) {
		this.bookingId = bookingId;
		this.guestId = dto.guestId();
		this.roomNumber = dto.roomNumber();
		this.checkIn = dto.checkIn();
		this.checkOut = dto.checkOut();
	}

	private Booking(Booking booking) {
		bookingId = booking.bookingId;
		guestId = booking.guestId;
		roomNumber = booking.roomNumber;
		checkIn = booking.checkIn;
		checkOut = booking.checkOut;
	}

	public Booking() {
		this.bookingId = 0;
	}

	@Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", guestId=" + guestId + ", roomId=" + roomNumber + ", checkIn=" + checkIn
				+ ", checkOut=" + checkOut + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookingId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Booking other)) {
            return false;
        }
		return bookingId == other.bookingId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public int getGuestId() {
		return guestId;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}

	public LocalDate getCheckOut() {
		return checkOut;
	}
	
	public boolean isActiveOn(LocalDate date) {
        return (date.isEqual(checkIn) || date.isAfter(checkIn)) && date.isBefore(checkOut);
    }
	
	public boolean overlaps(LocalDate requestedCheckIn, LocalDate requestedCheckOut) {
        return requestedCheckIn.isBefore(checkOut) && requestedCheckOut.isAfter(checkIn);
    }

	public Booking copy() {
		return new Booking(this);
	}

}
