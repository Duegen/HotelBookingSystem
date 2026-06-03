package hotel.app;

import hotel.generator.RandomHotelDataGenerator;
import hotel.interfaces.*;
import hotel.persistence.BookingsPersistenceEmbeddedImp;
import hotel.persistence.GuestsPersistenceEmbeddedImp;
import hotel.persistence.RoomTypesPersistenceEmbeddedImp;
import hotel.persistence.RoomsPersistenceEmbeddedImp;
import hotel.protocol.HotelTcpProtocol;
import hotel.service.AnalyticsService;
import hotel.service.DaemonPersistence;
import hotel.service.HotelPersistenceService;
import hotel.service.account.HotelAccountService;
import hotel.service.booking.HotelBookingService;
import telran.net.server.ServerJava;

import java.nio.file.Files;
import java.nio.file.Path;

public class HotelApplServer {
	public static void main(String[] args) {
		final long persistenceTimeOut = 1000;
		IHotelAccountService accountService = new HotelAccountService();
		IHotelBookingService bookingService = new HotelBookingService(accountService);
		
		IAnalyticsCommon analyticsService = new AnalyticsService(bookingService, accountService);
		IRoomTypesPersistence roomTypePersistence = RoomTypesPersistenceEmbeddedImp.getInstance();
		IRoomsPersistence roomPersistence = RoomsPersistenceEmbeddedImp.getInstance();
		IGuestsPersistence guestPersistence = GuestsPersistenceEmbeddedImp.getInstance();
		IBookingsPersistence bookingPersistence = BookingsPersistenceEmbeddedImp.getInstance();
		IHotelPersistenceService hotelPersistence = new HotelPersistenceService(bookingService, accountService,
				roomTypePersistence, roomPersistence, guestPersistence, bookingPersistence);
		HotelTcpProtocol protocol = new HotelTcpProtocol(bookingService, accountService, analyticsService);
		try {
			
			boolean filesExist = Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.ROOMTYPE_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.ROOM_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.GUEST_FILE))
					&& Files.exists(Path.of(HotelApplConstants.DIR, HotelApplConstants.BOOKING_FILE));
			if (filesExist) {
				hotelPersistence.loadHotelData();
				System.out.println("- Hotel data is loaded from file");
			} else {
				System.out.println("- Data files not found");
				RandomHotelDataGenerator randomHotelData = new RandomHotelDataGenerator(bookingService,
						accountService);
				randomHotelData.generate();
				System.out.println("- Hotel data is generated");
				hotelPersistence.saveHotelData();
				hotelPersistence.saveUserData();
				System.out.println("- Generated hotel data is saved to file");
			}
			bookingService.getDatabaseIsModified().set(false);
			accountService.getDatabaseIsModified().set(false);
			System.out.println();
			
			DaemonPersistence daemonPersistence = new DaemonPersistence(bookingService, accountService, hotelPersistence, persistenceTimeOut);
			daemonPersistence.start();
			ServerJava server = new ServerJava(protocol, HotelApplConstants.PORT);
			server.run();
		} catch (Exception e) {
			accountService.logout();
			System.out.println(e.getMessage());
		}
	}

}
