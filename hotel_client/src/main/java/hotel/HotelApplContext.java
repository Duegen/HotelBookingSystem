package hotel;
import cli.*;
import hotel.interfaces.*;

public class HotelApplContext {
	private final Inputoutput inOut;
	private final IHotelBookingServiceCommon bookingService;
	private final IHotelAccountServiceCommon accountService;
	private final IAnalyticsCommon analyticsService;
	
	private final String dateFormat;
	private int userId = 0;
	private int excessLevel = -1;
	
	public HotelApplContext(Inputoutput inOut, 
			IHotelBookingServiceCommon bookingService, 
			IHotelAccountServiceCommon accountService,
			IAnalyticsCommon analyticsService,
			String dateFormat) {

		this.inOut = inOut;
		this.bookingService = bookingService;
		this.accountService = accountService;
		this.analyticsService = analyticsService;
		this.dateFormat = dateFormat;
	}

	public Inputoutput getInOut() {
		return inOut;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public IHotelBookingServiceCommon getBookingService() {
		return bookingService;
	}

	public IHotelAccountServiceCommon getAccountService() {
		return accountService;
	}

	public IAnalyticsCommon getAnalyticsService() {
		return analyticsService;
	}

	public int getExcessLevel() {
		return excessLevel;
	}

	public void setExcessLevel(int excessLevel) {
		this.excessLevel = excessLevel;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	

}
