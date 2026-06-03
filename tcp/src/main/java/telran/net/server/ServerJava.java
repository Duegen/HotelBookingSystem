package telran.net.server;

import telran.net.protocol.ProtocolJava;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerJava implements Runnable {
	ServerSocket serverSocket;
	ProtocolJava protocol;
	int port;

	public ServerJava(ProtocolJava protocol, int port) throws Exception {
		super();
		this.port = port;
		this.serverSocket = new ServerSocket(port);
		this.protocol = protocol;

	}

	@Override
	public void run() throws RuntimeException{
		System.out.println("listen on port " + port);

		try {
			while (!serverSocket.isClosed()) {
				try {
					Socket socket = serverSocket.accept();
					Thread client = new Thread(new ServerClientJava(socket, protocol));
					client.start();
				} catch (Exception e) {
					System.out.println("Client error " + e.getMessage());
					throw new Exception(e.getMessage());
				}

			}
		} catch (Exception e) {
			if (!serverSocket.isClosed())
				throw new RuntimeException("server error " + e.getMessage());
		}

	}

	public void stop() {
		try {
			serverSocket.close();
		} catch (Exception ignored) {

		}
	}

}
