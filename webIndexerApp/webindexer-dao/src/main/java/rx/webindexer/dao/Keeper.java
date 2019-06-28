package rx.webindexer.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class Keeper {
	static Properties properties = null;
	String PROPERTIES_FILE = System.getenv("HOME")+"/webindexer.properties";
	String DB_HOST_PROPERTY = "db.host";
	String DB_USER_PROPERTY = "db.user";
	String DB_PASSWORD_PROPERTY = "db.pw";
	String DB_TABLE = "WIUSERS";
	Connection connection = null;

	public Keeper() {
	}

	public Keeper(boolean connectNow) throws SQLException, Exception {
		if (connectNow) {
			establishConnection();
		}
	}

	public Properties getProperties() throws FileNotFoundException, IOException {
		// настройки properties используются в течение работы программы
		// доступ к ним осуществляется с помощью текущего метода
		if (properties == null) {
			try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE);) {
				properties = new Properties();
				properties.load(fis);
			}
		}
		return properties;
	}

	public String getDBAccessUser() throws Exception {
		return getProperties().getProperty(DB_USER_PROPERTY);
	}

	public String getDBAccessHost() throws Exception {
		return getProperties().getProperty(DB_HOST_PROPERTY);
	}

	public String getDBAccessPassword() throws Exception {
		return getProperties().getProperty(DB_PASSWORD_PROPERTY);
	}

	public Connection establishConnection() throws SQLException, Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		if (!isConnected())
			connection = DriverManager.getConnection(getDBAccessHost(), getDBAccessUser(), getDBAccessPassword());
		return connection;
	}

	public void closeConnection() throws SQLException {
		if (connection != null)
			connection.close();
	}
	
	public boolean isConnected() {
		try {
			return (connection != null && (!connection.isClosed()));
		} catch (SQLException e) {
			return false;
		} 
	}

	public static byte[] objectToByteArray(Object obj) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);) {
			oos.writeObject(obj);
			return baos.toByteArray();
		}
	}

	public PreparedStatement insertUser(String user, byte[] password, List<StatsUnit> stats) throws Exception {
		boolean closeConnection = false; // нужно ли закрывать соединение по окончании метода
		if (isConnected()); 
		else { // если соединение не установлено, то мы сами его устанавливаем,
			establishConnection();
			closeConnection = true; // но также сами и закрываем
		} 
		try {
			PreparedStatement stmt = null;
			stmt = connection.prepareStatement("INSERT INTO WIUSERS (user, password, webstats) values (?,?,?)");
			stmt.setString(1, user);
			stmt.setBytes(2, password);
			stmt.setBlob(3, new ByteArrayInputStream(objectToByteArray(stats)));
			stmt.execute();
			return stmt;
		} finally {
			if (closeConnection)
				closeConnection();
		}
	}

	public PreparedStatement updateUserStats(String user, List<StatsUnit> stats) throws Exception {
		boolean closeConnection = false; 
		if (isConnected()); 
		else { // если соединение не установлено, то мы сами его устанавливаем,
			establishConnection();
			closeConnection = true; // но также сами и закрываем
		}
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement("UPDATE WIUSERS SET webstats = ? WHERE user = ?");
			stmt.setBlob(1, new ByteArrayInputStream(objectToByteArray(stats)));
			stmt.setString(2, user);
			stmt.execute();
			return stmt;
		} finally {
			if (stmt != null)
				stmt.close();
			if (closeConnection)
				closeConnection();
		}

	}

	public byte[] getUserPassword(String user) throws Exception {
		boolean closeConnection = false; 
		if (isConnected()); 
		else { // если соединение не установлено, то мы сами его устанавливаем,
			establishConnection();
			closeConnection = true; // но также сами и закрываем
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		byte[] result = null;
		try {
			stmt = connection.prepareStatement("SELECT password FROM WIUSERS WHERE user = ?");
			stmt.setString(1, user);
			stmt.execute();
			rs = stmt.getResultSet();
			if (rs != null) {
				if (rs.next())
					result = rs.getBytes(1);
			}
			return result;
		} finally {
			if (stmt != null)
				stmt.close();
			if (closeConnection)
				closeConnection();
		}

	}

	public List<StatsUnit> getUserStats(String user) throws Exception {
		boolean closeConnection = false; 
		if (isConnected()); 
		else { // если соединение не установлено, то мы сами его устанавливаем,
			establishConnection();
			closeConnection = true; // но также сами и закрываем
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement("SELECT webstats FROM WIUSERS WHERE user = ?");
			stmt.setString(1, user);
			stmt.execute();
			rs = stmt.getResultSet();
			if (rs != null) {
				if (rs.next()) {
					Blob blob = rs.getBlob(1);
					if(blob == null)
						return null;
					else {
						ObjectInputStream ois = new ObjectInputStream(blob.getBinaryStream());
						return (List<StatsUnit>) ois.readObject();
					}
				}
			}
			return null;
		} finally {
			if (stmt != null)
				stmt.close();
			if (closeConnection)
				closeConnection();
		}

	}

	/*
	 * Обращается к базе данных и проверяет создана ли таблица, если нет, создает
	 * Таблица содержит учетные данные пользователей, а также Blob, в котором
	 * хранится LinkedList<Stats> - связанный список, в котором хранятся данные о
	 * проиндексированных пользователем страницах
	 */
	public void initDataBase() throws Exception {
		boolean closeConnection = false; 
		if (isConnected()); 
		else { // если соединение не установлено, то мы сами его устанавливаем,
			establishConnection();
			closeConnection = true; // но также сами и закрываем
		}
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
			try {
				// запрос, чтобы проверить что таблица существует
				stmt.execute("SELECT 1 FROM WIUSERS");
			} catch (SQLException e) {
				// 42S02: Table or view doesn't exist
				if (e.getSQLState().equalsIgnoreCase("42S02")) {
					String createTable = "CREATE TABLE `WIUSERS` (\r\n" + "  `id` INT NOT NULL AUTO_INCREMENT,\r\n"
							+ "  `user` VARCHAR(64) NOT NULL,\r\n" + "  `password` BINARY(128) NULL,\r\n"
							+ "  `webstats` BLOB NULL,\r\n" + "  PRIMARY KEY (`id`),\r\n"
							+ "  UNIQUE INDEX `id_UNIQUE` (`id` ASC),\r\n"
							+ "  UNIQUE INDEX `user_UNIQUE` (`user` ASC))\r\n" + "ENGINE = InnoDB\r\n"
							+ "DEFAULT CHARACTER SET = utf8\r\n" + "COLLATE = utf8_unicode_ci\r\n"
							+ "COMMENT = 'WebIndexer app users and web stats table';\r\n";
					stmt.execute(createTable);

				}
			}

		} finally {
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
			if (closeConnection)
				closeConnection();
		}
	}
}
