package rx.webuserbase;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Users {
	public static ConcurrentHashMap<String,User> map = new ConcurrentHashMap<>();
	public static AtomicInteger count = new AtomicInteger(0);
}
