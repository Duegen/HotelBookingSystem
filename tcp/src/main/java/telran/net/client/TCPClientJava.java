package telran.net.client;

import telran.net.exceptions.ApiException;
import telran.net.protocol.*;

import java.io.*;
import java.net.Socket;

import static telran.net.protocol.TCPResponseCode.*;

public class TCPClientJava implements Closeable {
	protected ObjectOutputStream outPut;
	protected ObjectInputStream inPut;
	protected Socket socket;

	public TCPClientJava(String hostName, int port) throws Exception {
		super();
		this.socket = new Socket(hostName, port);
		this.outPut = new ObjectOutputStream(socket.getOutputStream());
		this.outPut.flush();
		this.inPut = new ObjectInputStream(socket.getInputStream());

	}

	@Override
	public void close() throws IOException {
		socket.close();

	}

	@SuppressWarnings("unchecked")
	protected <T> T sendRequest(String requestType, Serializable requestData) {
		try {
			RequestJava request = new RequestJava(requestType, requestData);
			outPut.writeObject(request);
			ResponseJava response = (ResponseJava) inPut.readObject();
			if (response.code != OK)
				throw new ApiException(response.code , (String)response.responseData);
			return (T) response.responseData;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
