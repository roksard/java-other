package rx.webapp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Util {
	public static boolean isLogged(HttpServletRequest request) throws IOException {
		return request.getSession().getAttribute("user") != null;
	}
}
