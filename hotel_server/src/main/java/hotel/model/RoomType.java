package hotel.model;

import hotel.dto.booking.input.RoomTypeCreateRequest;

import java.io.Serializable;
import java.util.Objects;

public class RoomType implements Serializable{
	private static final long serialVersionUID = 1L;
	private int typeId;
	private String category;
	private double pricePerNight;
	private int capacity;
	
	public RoomType(int typeId, RoomTypeCreateRequest dto) {
		this.typeId = typeId;
		category = dto.category();
		pricePerNight = dto.pricePerNight();
		capacity = dto.capacity();
	}

	private RoomType(RoomType roomType) {
		typeId = roomType.typeId;
		category = roomType.category;
		pricePerNight = roomType.pricePerNight;
		capacity = roomType.capacity;
	}

	public RoomType() {

	}

	@Override
	public String toString() {
		return "RoomType [typeId=" + typeId + ", category=" + category + ", pricePerNight=" + pricePerNight + ", capacity=" + capacity + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(typeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RoomType other)) 
            return false;
		return typeId == other.typeId;
	}
	
	public boolean isDuplicate(RoomType other) {
		if(Objects.isNull(other))
			return false;
		return capacity == other.capacity
                && Double.doubleToLongBits(pricePerNight) == Double.doubleToLongBits(other.pricePerNight)
                && Objects.equals(category, other.category);
	}
	
	public int getTypeId() {
		return typeId;
	}
	
	public String getCategory() {
		return category;
	}

	public double getPricePerNight() {
		return pricePerNight;
	}

	public int getCapacity() {
		return capacity;
	}

	public RoomType copy() {
		return new RoomType(this);
	}
}
