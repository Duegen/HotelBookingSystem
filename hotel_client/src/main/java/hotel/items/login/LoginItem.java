package hotel.items.login;

import hotel.HotelApplContext;
import hotel.dto.account.input.LoginRequest;
import hotel.dto.account.output.LoginResponse;
import hotel.items.HotelItem;

import java.util.Objects;

public class LoginItem extends HotelItem{

	public LoginItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Login";
	}

	@Override
	public void perform() {
		try {
			LoginRequest loginDTO = login();
			if(Objects.isNull(loginDTO))
				return;
			LoginResponse login = accountService.login(loginDTO);
			context.setUserId(login.userId());
			context.setExcessLevel(login.excessLevel());
			inOut.outputlLine("Welcome " + login.userName());
		} catch (Exception e) {
			context.setUserId(0);
			context.setExcessLevel(-1);
			inOut.outputlLine(e.getMessage());
		}
	}
	
	@Override
	public boolean isExit() {
		if(context.getExcessLevel() != -1)
			return true;
		else return false;
	}
}
