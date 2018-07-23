package cc.protea.util.http;


public class TimedRequest extends Request {

    /**
     *
     * @param url The URL to connect to
     * @param connectTimeout connect timeout in ms
     * @param readTimeout read timeout in ms
     */
    public TimedRequest(final String url, final int connectTimeout, final int readTimeout) {
        super(url);
        if (connectTimeout > 0) {
            connection.setConnectTimeout(connectTimeout);
        }
        if (readTimeout > 0) {
            connection.setReadTimeout(readTimeout);
        }
    }

}
