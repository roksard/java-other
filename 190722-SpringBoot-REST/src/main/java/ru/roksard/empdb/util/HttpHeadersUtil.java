package ru.roksard.empdb.util;

import org.springframework.http.HttpHeaders;

public class HttpHeadersUtil {
	public static String msgHeader = "empdb-message";

	/**
	 * Convenience factory method for pre-set headers
	 * 
	 * @return instance of HttpHeaders object with default headers set
	 */
	public static HttpHeaders httpHeadersDefault() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Expose-Headers", msgHeader);
		responseHeaders.set(msgHeader, "not-set");
		return responseHeaders;
	}

	public static HttpHeaders httpHeadersMsg(String message) {
		HttpHeaders responseHeaders = httpHeadersDefault();
		responseHeaders.set(msgHeader, message);
		return responseHeaders;
	}
}
