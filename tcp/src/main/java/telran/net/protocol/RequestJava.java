package telran.net.protocol;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RequestJava implements Serializable {
	public String requestType;
	public Serializable requestData;

	public RequestJava(String requestType, Serializable requestData) {
		super();
		this.requestType = requestType;
		this.requestData = requestData;
	}

	@Override
	public String toString() {
		return "RequestJava [requestType=" + requestType + ", requestData=" + requestData + "]";
	}

}
