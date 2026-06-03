package telran.net.exceptions;

import telran.net.protocol.TCPResponseCode;

public class ApiException extends Exception {
	private static final long serialVersionUID = 1L;
	TCPResponseCode responceCode;
	
	public ApiException(TCPResponseCode responceCode, String message) {
		super(message);
		this.responceCode = responceCode;
	}
	
	
}
