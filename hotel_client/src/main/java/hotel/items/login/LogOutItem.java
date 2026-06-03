package hotel.items.login;

import hotel.HotelApplContext;
import hotel.items.HotelItem;

public class LogOutItem extends HotelItem{

	public LogOutItem(HotelApplContext context) {
		super(context);
	}

	@Override
	public String displayName() {
		return "Log out";
	}

	@Override
	public void perform() {
		try {
			accountService.logout();
			context.setExcessLevel(-1);
			context.setUserId(0);
		} catch (Exception e) {
			inOut.outputlLine(e.getMessage());
		}
	}

	
	@Override
	public boolean isExit() {
		return true;
	}
}
