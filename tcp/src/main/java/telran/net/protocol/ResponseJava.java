package telran.net.protocol;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResponseJava implements Serializable {
	public TCPResponseCode code;
	public Serializable responseData;

	public ResponseJava(TCPResponseCode code, Serializable responseData) {
		super();
		this.code = code;
		this.responseData = responseData;
	}

	@Override
	public String toString() {
		return "ResponseJava [code=" + code + ", responseData=" + responseData + "]";
	}

}
