package rx.webindexer.dao;

import java.util.concurrent.ConcurrentHashMap;

public class Users {
	public static ConcurrentHashMap<String,User> map = new ConcurrentHashMap<>();
	
	static {
		map.put("admin", new User("admin", "password"));
	}
}