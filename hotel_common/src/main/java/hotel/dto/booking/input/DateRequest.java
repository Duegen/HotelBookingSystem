package hotel.dto.booking.input;

import java.io.Serializable;
import java.time.LocalDate;

public record DateRequest(LocalDate date) implements Serializable{

}
