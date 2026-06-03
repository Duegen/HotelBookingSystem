package hotel.dto.booking.input;

import java.io.Serializable;

public record AgeRangeRequest(int minAge, int maxAge) implements Serializable{

}
