package rx.webindexer.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class Users implements Serializable {
	private static final long serialVersionUID = -3396868729201063863L;
	protected static ConcurrentHashMap<String,User> map = new ConcurrentHashMap<>();
	
	static {
		try {
			map = (ConcurrentHashMap<String,User>)Keeper.loadFromExternal(Keeper.LOCAL_DEFAULT_DIR + 
					Keeper.USERS_DEFAULT_FILE);
		} catch (FileNotFoundException e) {
			try {
				//Файл не найден, значит он еще не сохранялся на этом сервере, загружаем стандартный из ресурсов
				//в котором есть единственный пользователь "admin" с паролем "password"
				map = (ConcurrentHashMap<String,User>)Keeper.loadAsResource("/"+Keeper.USERS_DEFAULT_FILE);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static ConcurrentHashMap<String,User> getUsers() {
		return map;
	}
	
	public static User getUser(String userName) {
		return map.get(userName);
	}
	
	public static boolean addUser(User user) {
		if(map.containsKey(user.getLogin()))
			return false;
		else
			map.put(user.getLogin(), user);
		return true;
	}
	
	public static boolean saveToExternalDefault() {
		
		try {
			Keeper.saveToExternal(Keeper.LOCAL_DEFAULT_DIR + Keeper.USERS_DEFAULT_FILE, map);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}