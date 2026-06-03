package hotel.interfaces;

import hotel.dto.account.input.GuestCreateRequest;
import hotel.dto.account.input.GuestIdRequest;
import hotel.dto.account.input.LoginRequest;
import hotel.dto.account.output.GuestResponse;
import hotel.dto.account.output.LoginResponse;
import hotel.exceptions.ConflictException;
import hotel.exceptions.ValidationException;

import java.util.List;

public interface IHotelAccountServiceCommon {
	void createGuest(GuestCreateRequest dto) throws ConflictException, ValidationException;
	GuestResponse removeGuest(GuestIdRequest dto) throws ConflictException, ValidationException;
	List<GuestResponse> getGuestsResponse();
	LoginResponse login(LoginRequest dto) throws ValidationException, ConflictException;
	void logout();
}
