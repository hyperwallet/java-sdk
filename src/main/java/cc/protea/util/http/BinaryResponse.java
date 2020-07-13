package cc.protea.util.http;

public class BinaryResponse extends Response {

	byte[] body;

	public byte[] getBinaryBody() {
		return body;
	}

	public void setBinaryBody(byte[] body) {
		this.body = body;
	}
	
	
}
