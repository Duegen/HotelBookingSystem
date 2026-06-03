package hotel.service;

import hotel.interfaces.IHotelAccountService;
import hotel.interfaces.IHotelBookingService;
import hotel.interfaces.IHotelPersistenceService;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DaemonPersistence extends Thread {
	private long timeOut;
	private final IHotelBookingService bookingService;
	private final IHotelPersistenceService persistenceService;
	private final IHotelAccountService accountService;
	
	private final ReentrantReadWriteLock bookingReadWritelock;
	private final ReentrantReadWriteLock roomReadWritelock;
	private final ReentrantReadWriteLock roomTypeReadWritelock;
	
	{
		setDaemon(true);
	}
	
	public DaemonPersistence(IHotelBookingService bookingService, IHotelAccountService accountService, 
			IHotelPersistenceService persistenceService, long timeOut) {
		super();
		this.timeOut = timeOut;
		this.bookingService = bookingService;
		this.persistenceService = persistenceService;
		this.accountService = accountService;
		this.bookingReadWritelock = bookingService.getBookingreadwritelock();
		this.roomReadWritelock = bookingService.getRoomReadWritelock();
		this.roomTypeReadWritelock = bookingService.getRoomTypeReadWritelock();
	}

	@Override
	public void run() {
		while(true) {
			
				
				if (bookingService.getDatabaseIsModified().get()) {
					try {
						bookingReadWritelock.readLock().lock();
						roomReadWritelock.readLock().lock();
						roomTypeReadWritelock.readLock().lock();
						bookingService.getDatabaseIsModified().set(false);
						try {
							persistenceService.saveHotelData();
							System.out.println("Booking data saved to file");
						} catch (IOException e) {
							System.out.println("Booking data can't be saved: " + e.getMessage());
						}
					} finally {
						bookingReadWritelock.readLock().unlock();
						roomReadWritelock.readLock().unlock();
						roomTypeReadWritelock.readLock().unlock();
					}
				} 
			
			if(accountService.getDatabaseIsModified().get()) {
				accountService.getDatabaseIsModified().set(false);
				try {
					persistenceService.saveUserData();
					System.out.println("User data saved to file");
				} catch (IOException e) {
					System.out.println("User data can't be saved: " + e.getMessage());
				}
			}
			try {
				sleep(timeOut);
			} catch (InterruptedException e) {
				System.out.println("exc"+Thread.currentThread().isInterrupted());
				Thread.currentThread().interrupt();
			}
			
		}
	}
	
}
