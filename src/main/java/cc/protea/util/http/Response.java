package cc.protea.util.http;

import java.util.List;
import java.util.Map;

/*

 Modified with modifications copyright Richard Stanford

 -- Original portions by Joe Ernst --

 * Copyright 2012 Joe J. Ernst
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This class represents an HTTP Response message.
 */
public class Response extends Message<Response> {

	int responseCode;
	String responseMessage;

	/**
	 * The default constructor is a no-op
	 */
	public Response() {
		// no-op
	}

	/**
	 * Gets the HTTP Response Code from this Response instace.
	 *
	 * @return The HTTP Response Code that was sent from the server
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * Sets the HTTP Response Code on this object.
	 *
	 * @param responseCode
	 *            Any of the standard HTTP Response Codes
	 * @return This object, to support chained method calls
	 */
	public Response setResponseCode(final int responseCode) {
		this.responseCode = responseCode;
		return this;
	}

	/**
	 * Returns a message pertaining to the Response Code.
	 *
	 * @return Any response message that may have been returned by the server. This message should be related to the and should not be confused with the Response Body.
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * Sets the Response Message, which should pertain to the Response Code
	 *
	 * @param responseMessage
	 *            Any message which was sent back from the server, pertaining to the Response Code
	 * @return this Response, to support chained method calls
	 */
	public Response setResponseMessage(final String responseMessage) {
		this.responseMessage = responseMessage;
		return this;
	}

	/**
	 * Returns a String representation of this Response. Helpful for debugging.
	 *
	 * @return Returns a String representation of this Response. Helpful for debugging.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String newline = System.getProperty("line.separator");

		builder.append("Response Code: ").append(this.responseCode).append(newline).append("Response Message: ").append(newline).append(newline).append("Headers: ").append(newline);

		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			List<String> values = entry.getValue();
			for (String value : values) {
				builder.append(entry.getKey()).append(" = ").append(value).append(newline);
			}
		}

		builder.append(newline).append("Body: ").append(newline).append(body);

		return builder.toString();
	}
}