package rx.webapp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class Util {
	public static boolean isLogged(HttpServletRequest request) throws IOException {
		String user = (String)request.getSession().getAttribute("user");
		return (user != null && user.length()!=0);
	}
}
