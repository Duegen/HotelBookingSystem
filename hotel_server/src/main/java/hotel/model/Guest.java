package hotel.model;

import hotel.dto.account.input.GuestCreateRequest;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Objects;

public class Guest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int id;
	private String name;
	private String email;
	private LocalDate dateOfBirth; 
	private String password;
	
	public Guest(int id, GuestCreateRequest dto, String algorithm, int keyLength) {
		this.id = id;
		name = dto.name();
		email = dto.email();
		dateOfBirth = dto.dateOfBirth();
		
		byte[] salt = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);

		KeySpec spec = new PBEKeySpec(dto.password().toCharArray(), salt, 65536, keyLength);
		try {
		    SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
		    byte[] hash = factory.generateSecret(spec).getEncoded();
		    this.password = Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			this.password = "Password1";
		}
	}
	
	private Guest(Guest guest) {
		id = guest.getId();
		name = guest.getName();
		email = guest.getEmail();
		dateOfBirth = guest.getDateOfBirth();
		password = guest.getPassword();
	}

	public Guest() {
		id = 0;
	}

	@Override
	public String toString() {
		return "Guest [id=" + id + ", name=" + name + ", email=" + email + ", date of birth=" + dateOfBirth + ", password=" + password + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Guest other)) 
            return false;
		return id == other.id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
	public LocalDate getDateOfBirth(){
		return dateOfBirth;
	}
	
	public String getPassword() {
		return password;
	}
	
	public long getAge() {
		return ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
	}

	public Guest copy() {
		return new Guest(this);
	}
}
