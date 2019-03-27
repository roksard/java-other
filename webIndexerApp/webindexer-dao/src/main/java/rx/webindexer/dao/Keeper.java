package rx.webindexer.dao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Keeper {
	public static final String STATS_DEFAULT_FILE = "/stats_data.dat";
	public static final String USERS_DEFAULT_FILE = "/users.dat";
	
	public static <T extends Serializable> void saveToExternal(String fileName, T object) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(object);
		out.close();
	}
	
	public static <T extends Serializable> T loadFromExternal(String fileName) throws ClassNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		@SuppressWarnings("unchecked")
		T result = (T)in.readObject();
		in.close();
		return result;
	}
	
	public static <T extends Serializable> T loadAsResource(String resourceName) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(Keeper.class.getResourceAsStream(resourceName));
		@SuppressWarnings("unchecked")
		T result = (T)in.readObject();
		in.close();
		return result;
	}
	
}
