package hotel.service.account;

import hotel.app.HotelApplConstants;
import hotel.dto.account.input.GuestCreateRequest;
import hotel.dto.account.input.GuestIdRequest;
import hotel.dto.account.input.LoginRequest;
import hotel.dto.account.output.GuestResponse;
import hotel.dto.account.output.LoginResponse;
import hotel.exceptions.ConflictException;
import hotel.exceptions.ValidationException;
import hotel.interfaces.IHotelAccountService;
import hotel.interfaces.IHotelBookingService;
import hotel.model.Guest;
import hotel.service.account.exceptions.*;
import hotel.validation.Constants;
import hotel.validation.Validation;
import telran.net.server.LoginData;
import telran.net.server.ServerClientJava;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HotelAccountService implements IHotelAccountService{
	private Map<Integer, Guest> guests;
	private IHotelBookingService bookingService;
	private final AtomicBoolean databaseIsModified;
	private final ReentrantReadWriteLock accountReadWriteLock;
	private ReentrantReadWriteLock bookingReadWriteLock = new ReentrantReadWriteLock();
	
	@Override
	public AtomicBoolean getDatabaseIsModified() {
		return databaseIsModified;
	}

	@Override
	public void setBookingService(IHotelBookingService bookingService) {
		this.bookingService = bookingService;
		bookingReadWriteLock = bookingService.getBookingreadwritelock();
	}

	public HotelAccountService() {
		this.guests = new TreeMap<>();
		databaseIsModified = new AtomicBoolean();
		databaseIsModified.set(false);
		accountReadWriteLock = new ReentrantReadWriteLock();
	}
	
	@Override
	public Map<Integer, Guest> getGuests() {
		try {
			accountReadWriteLock.readLock().lock();
			return Collections.unmodifiableMap(guests);
		} finally {
			accountReadWriteLock.readLock().unlock();
		}
	}
	
	@Override
	public List<GuestResponse> getGuestsResponse() {
		try {
			accountReadWriteLock.readLock().lock();
			return guests.values().stream()
					.map(g -> new GuestResponse(g.getId(), g.getName(), g.getEmail(), g.getDateOfBirth()))
					.toList();
		} finally {
			accountReadWriteLock.readLock().unlock();
		}
	}
	
	@Override
	public void createGuest(GuestCreateRequest dto)
			throws GuestException, ValidationException {
		Validation.validate(dto);
		Validation.validatePassword(dto.password());
		try {
			accountReadWriteLock.writeLock().lock();
			int guestId = findFreeId(guests, Constants.GUEST_MIN_ID, Constants.GUEST_MAX_ID);
			if (guestId == -1)
				throw new NoFreeGuestIdException();
			Guest guest = new Guest(guestId, dto, HotelApplConstants.ALGORITHM, HotelApplConstants.KEY_LENGTH);
			if (guests.values().stream().anyMatch(g -> g.getEmail().equals(dto.email())))
				throw new DuplicateEmailException(dto.email());
			guests.put(guestId, guest);
			databaseIsModified.set(true);
		} finally {
			accountReadWriteLock.writeLock().unlock();
		}
	}

	@Override
	public void addGuest(Guest guest)
			throws ConflictException, ValidationException {
		Validation.validate(guest);
		try {
			accountReadWriteLock.writeLock().lock();
			if (guests.containsKey(guest.getId()))
				throw new GuestAlreadyExistsException(guest.getId());
			if (guests.values().stream().anyMatch(g -> g.getEmail().equals(guest.getEmail())))
				throw new DuplicateEmailException(guest.getEmail());
			guests.put(guest.getId(), guest.copy());
			databaseIsModified.set(true);
		} finally {
			accountReadWriteLock.writeLock().unlock();
		}
	}

	@Override
	public GuestResponse removeGuest(GuestIdRequest dto) throws GuestException, ValidationException {
		Validation.validate(dto);
		if(Objects.isNull(bookingService))
			throw new IllegalArgumentException("booking service is null");
		try {
			accountReadWriteLock.writeLock().lock();
			bookingReadWriteLock.readLock().lock();
			if (!guests.containsKey(dto.guestId()))
				throw new GuestNotFoundException(dto.guestId());
			boolean hasRelatedBookings = bookingService.getBookings().values().stream()
					.anyMatch(booking -> booking.getGuestId() == dto.guestId());
			if (hasRelatedBookings)
				throw new RemoveGuestAssignException(dto.guestId());
			if(ServerClientJava.alreadyLogin("guest" + dto.guestId()))
				throw new RemoveGuestLoginException(dto.guestId());
			Guest removed = guests.remove(dto.guestId());
			databaseIsModified.set(true);
			return new GuestResponse(removed.getId(), removed.getName(), removed.getEmail(),
					removed.getDateOfBirth());
		} finally {
			accountReadWriteLock.writeLock().unlock();
			bookingReadWriteLock.readLock().unlock();
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
	public LoginResponse login(LoginRequest dto) throws ValidationException, GuestException {
		byte[] hash;
		byte[] inputHash;
		String password;
		String name;
		int excessLevel = -1;
		int guestId = 0;
		String userId;
		
		Validation.validate(dto);
		if(dto.email().equals(HotelApplConstants.ACCOUNTANT_LOGIN)) {
			excessLevel = 2;
			password = HotelApplConstants.ACCOUNTANT_PASSWORD;
			name = "Accountant";
			userId = "accountant";
		}
		else if(dto.email().equals(HotelApplConstants.MANAGER_LOGIN)) {
			excessLevel = 1;
			password = HotelApplConstants.MANAGER_PASSWORD;
			name = "Manager";
			userId = "manager";
		}
		else {
			excessLevel = 0;
			Guest guest = findGuestByEmail(dto.email());
			if(Objects.isNull(guest))
				throw new GuestLoginException();
			guestId = guest.getId();
			password = guest.getPassword();
			name = "guest " + guest.getName();
			userId = "guest" + guestId;
		}
		try {
			String[] parts = password.split(":");
			byte[] salt = Base64.getDecoder().decode(parts[0]);
			hash = Base64.getDecoder().decode(parts[1]);	
			KeySpec spec = new PBEKeySpec(dto.password().toCharArray(), salt, 65536, HotelApplConstants.KEY_LENGTH);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(HotelApplConstants.ALGORITHM);
			inputHash = factory.generateSecret(spec).getEncoded();
			if(!Arrays.equals(hash, inputHash))
				throw new GuestLoginException();
			if(ServerClientJava.alreadyLogin(userId))
				throw new AccountAlreadyLoginException();
			ServerClientJava.setLocal(new LoginData(userId, excessLevel, guestId));
			ServerClientJava.addUserSession(userId, 0);
			return new LoginResponse(excessLevel, guestId, name);		
		} catch(GuestLoginException | AccountAlreadyLoginException e) {
			throw new GuestException(e.getMessage());
		} catch (Exception e) {
			throw new GuestLoginErrorException();
		}
        
	}

	@Override
	public Guest findGuestById(int guestId) {
		return guests.get(guestId);
	}
	
	@Override
	public Guest findGuestByEmail(String email) {
		if(Objects.isNull(email))
			return null;
		return guests.values().stream().filter(g -> email.equals(g.getEmail())).findFirst().orElse(null);
	}

	@Override
	public void logout() {
		ServerClientJava.clearUserSession(ServerClientJava.getLocal().userId());
		ServerClientJava.setLocal(new LoginData("anonimous", -1, 0));
		
	}
}
