package telran.net.server;

import telran.net.protocol.*;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static telran.net.protocol.TCPResponseCode.*;


public class ServerClientJava implements Runnable {

	private static final ThreadLocal<LoginData> loginData = new ThreadLocal<>();
	private static final ConcurrentMap<String, Integer> sessionStore = new ConcurrentHashMap<String, Integer>();
	
	private Socket socket;
	private ProtocolJava protocol;

	public ServerClientJava(Socket socket, ProtocolJava protocol) {
		super();
		this.socket = socket;
		this.protocol = protocol;
	}

	@Override
	public void run() {
		loginData.set(new LoginData("anonimous", -1, 0));
		try(Socket s=this.socket;
				ObjectOutputStream outPut = new ObjectOutputStream(s.getOutputStream());
						ObjectInputStream inPut = new ObjectInputStream(s.getInputStream());){
			outPut.flush();
			while (true) {
				Object obj = inPut.readObject();
				ResponseJava response;
				if( !(obj instanceof RequestJava request)) {
					response = new ResponseJava(WRONG_REQUEST, null);
				}else {
					
					try {
						response = protocol.getResponse(request);
						if(response==null) {
							response = new ResponseJava(UNKNOWN, null);
						}
					} catch (Exception e) {
						response = new ResponseJava(WRONG_REQUEST, null);
					}
				}
				outPut.writeObject(response);
				outPut.flush();
			}	
		}catch (EOFException e) {
			System.out.println("client closed connection");
		}catch (Exception e) {
			System.out.println("Error "+ e.getMessage());
		} finally {
			clearUserSession(loginData.get().userId());
			clearLocal();
		}

	}
	
	public static LoginData getLocal() {
		return loginData.get();
	}
	
	public static void setLocal(LoginData data) {
		loginData.set(data);
	}
	
	public static void clearLocal() {
		loginData.remove();
	}
	
	public static void addUserSession(String userId, Integer userSession) {
		sessionStore.put(userId, userSession);
	}
	
	public static void clearUserSession(String userId) {
		sessionStore.remove(userId);
	}
	
	public static boolean alreadyLogin(String userId) {
		return sessionStore.containsKey(userId);
	}
}
