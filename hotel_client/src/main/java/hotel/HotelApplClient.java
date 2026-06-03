package hotel;

import cli.*;
import hotel.interfaces.IAnalyticsCommon;
import hotel.interfaces.IHotelAccountServiceCommon;
import hotel.interfaces.IHotelBookingServiceCommon;
import hotel.items.accountant.ShowAllStatisticsItem;
import hotel.items.accountant.ShowBookingsByCheckInItem;
import hotel.items.accountant.ShowMostPopularRoomForRangeAgeItem;
import hotel.items.accountant.ShowOccupancyForDateItem;
import hotel.items.guest.CancelMyBookingItem;
import hotel.items.guest.CreateBookingItem;
import hotel.items.guest.ShowAvailableRoomsItem;
import hotel.items.guest.ShowMyBookingsItem;
import hotel.items.login.LogOutItem;
import hotel.items.login.LoginItem;
import hotel.items.login.RegisterGuestItem;
import hotel.items.manager.*;
import hotel.proxy.HotelTCPProxy;


public class HotelApplClient {

	public static void main(String[] args) {
		Inputoutput inOut = new ConsoleInputOutput();
		inOut.outputlLine("Welcome to booking service!");
		try {
			
			HotelTCPProxy proxy = new HotelTCPProxy(HotelApplConstants.HOST, HotelApplConstants.PORT);
			IHotelBookingServiceCommon bookingService = proxy;
			IHotelAccountServiceCommon accountService = proxy;
			IAnalyticsCommon analyticsService = proxy;
			HotelApplContext context = new HotelApplContext(inOut, bookingService, accountService, analyticsService, HotelApplConstants.DATE_FORMAT);
			
			while (true) {
				Menu loginMenu = new Menu(getLoginMenuItems(context), inOut);
				loginMenu.runMenu();
				switch (context.getExcessLevel()) {
					case -1: {
						inOut.outputlLine("Service is closed");
						return;
					}
					case 0: {
						Menu guestMenu = new Menu(getGuestMenuItems(context), inOut);
						guestMenu.runMenu();
						break;
					}
					case 1: {
						Menu managerMenu = new Menu(getManagerMenu(context), inOut);
						managerMenu.runMenu();
						break;
					}
					case 2: {
						Menu accountantMenu = new Menu(getAccountantMenu(context), inOut);
						accountantMenu.runMenu();
						break;
					}
				}
				if(context.getExcessLevel() != -1) {
					inOut.outputlLine("Service is closed");
					break;
				}
			}
			
		} catch (Exception e) {
			inOut.outputlLine("fatal error");
			inOut.outputlLine(e.getMessage());
		}
	}
	
	private static Item[] getAccountantMenu(HotelApplContext context) {
		return new Item[] { new ShowAllStatisticsItem(context), new ShowBookingsByCheckInItem(context),
				new ShowMostPopularRoomForRangeAgeItem(context), new ShowOccupancyForDateItem(context),
				new LogOutItem(context), new ExitItem() };
	}

	private static Item[] getManagerMenu(HotelApplContext context) {
		return new Item[] { new AddRoomTypeItem(context), new AddRoomItem(context), new RemoveRoomTypeItem(context),
				new RemoveRoomItem(context), new RemoveGuestItem(context), new RemoveBookingItem(context),
				new ShowRoomTypesItem(context), new ShowRoomsItem(context), new ShowGuestsItem(context),
				new ShowBookingsItem(context), new LogOutItem(context), new ExitItem() };
	}

	private static Item[] getGuestMenuItems(HotelApplContext context) {
		return new Item[] { new CreateBookingItem(context), new CancelMyBookingItem(context),
				new ShowAvailableRoomsItem(context), new ShowMyBookingsItem(context), new LogOutItem(context), new ExitItem() };
	}

	private static Item[] getLoginMenuItems(HotelApplContext context) {
		return new Item[] { new LoginItem(context), new RegisterGuestItem(context), new ExitItem() };
	}
	
}