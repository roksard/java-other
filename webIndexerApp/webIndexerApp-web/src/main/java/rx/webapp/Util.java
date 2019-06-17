package rx.webapp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import rx.webindexer.dao.User;

public class Util {
	public static boolean isLogged(HttpServletRequest request) throws IOException {
		User user = null;
		try {
			user = (User)request.getSession().getAttribute("user");
		} catch (Exception e) {
			return false;
		}
		return (user != null && user.getLogin().length()!=0);
	}
}
