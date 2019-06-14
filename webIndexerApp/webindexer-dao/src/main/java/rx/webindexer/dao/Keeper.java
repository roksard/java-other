package rx.webindexer.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

public class Keeper {
	static Properties properties = null;
	public static final String PROPERTIES_FILE = "webindexer.properties";
	public static final String DB_HOST_PROPERTY = "db.host";
	public static final String DB_USER_PROPERTY = "db.user";
	public 	static final String DB_PASSWORD_PROPERTY = "db.pw";
	public static final String DB_TABLE = "WIUSERS";

	public static Properties getProperties() throws FileNotFoundException, IOException {
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

	public static String getDBAccessUser() throws Exception {
		return getProperties().getProperty(DB_USER_PROPERTY);
	}

	public static String getDBAccessHost() throws Exception {
		return getProperties().getProperty(DB_HOST_PROPERTY);
	}

	public static String getDBAccessPassword() throws Exception {
		return getProperties().getProperty(DB_PASSWORD_PROPERTY);
	}

	public static Connection getConnection() throws SQLException, Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(getDBAccessHost(), getDBAccessUser(), getDBAccessPassword());
	}

	public static void closeConnection(Connection con) throws SQLException {
		if (con != null)
			con.close();
	}
	
	public static byte[] objectToByteArray(Object obj) throws IOException {
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			) {
			oos.writeObject(obj);
			return baos.toByteArray();
		} 
	}

	public static void addUser(String user, String password, LinkedList<StatsUnit> stats) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		try  {
			con = getConnection();
			stmt = con.prepareStatement("INSERT INTO WIUSERS (user, password, webstats) values (?,?,?)");
			stmt.setString(1, "'"+user+"'");
			stmt.setString(2, "'"+password+"'");
			stmt.setBlob(3, new ByteArrayInputStream( objectToByteArray(stats) ));
			stmt.execute();
		} finally {
			closeConnection(con);
			if(stmt != null) 
				stmt.close();
		}

	}
	
	public static void updateUserStats(String user, LinkedList<StatsUnit> stats) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		try  {
			con = getConnection();
			stmt = con.prepareStatement("UPDATE WIUSERS SET webstats = ? WHERE user = ?");
			stmt.setBlob(1, new ByteArrayInputStream( objectToByteArray(stats) ));
			stmt.setString(2, "'"+user+"'");
			stmt.execute();
		} finally {
			closeConnection(con);
			if(stmt != null) 
				stmt.close();
		}

	}

	public static void main(String[] args) throws Exception {
		String createTable = "CREATE TABLE `WIUSERS` (\r\n" + "  `id` INT NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `user` VARCHAR(64) NOT NULL,\r\n" + "  `password` VARCHAR(64) NOT NULL,\r\n"
				+ "  `webstats` BLOB NULL,\r\n" + "  PRIMARY KEY (`id`),\r\n"
				+ "  UNIQUE INDEX `id_UNIQUE` (`id` ASC),\r\n"
				+ "  UNIQUE INDEX `user_UNIQUE` (`user` ASC))\r\n" + "ENGINE = InnoDB\r\n"
				+ "DEFAULT CHARACTER SET = utf8\r\n" + "COLLATE = utf8_unicode_ci\r\n"
				+ "COMMENT = 'WebIndexer app users and web stats table';\r\n";
		System.out.println(createTable);
	}

	/*
	 * Обращается к базе данных и проверяет создана ли таблица, если нет, создает
	 * Таблица содержит учетные данные пользователей, а также Blob, в котором
	 * хранится LinkedList<Stats> - связанный список, в котором хранятся данные о
	 * проиндексированных пользователем страницах
	 */
	public static void initDataBase() throws Exception {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			stmt = con.createStatement();
			try {
				// запрос, чтобы проверить что таблица существует
				stmt.execute("SELECT 1 FROM WIUSERS");
			} catch (SQLException e) {
				// 42S02: Table or view doesn't exist
				if (e.getSQLState().equalsIgnoreCase("42S02")) {
					String createTable = "CREATE TABLE `WIUSERS` (\r\n" + "  `id` INT NOT NULL AUTO_INCREMENT,\r\n"
							+ "  `user` VARCHAR(64) NOT NULL,\r\n" + "  `password` VARCHAR(64) NOT NULL,\r\n"
							+ "  `webstats` BLOB NULL,\r\n" + "  PRIMARY KEY (`id`),\r\n"
							+ "  UNIQUE INDEX `id_UNIQUE` (`id` ASC),\r\n"
							+ "  UNIQUE INDEX `user_UNIQUE` (`user` ASC))\r\n" + "ENGINE = InnoDB\r\n"
							+ "DEFAULT CHARACTER SET = utf8\r\n" + "COLLATE = utf8_unicode_ci\r\n"
							+ "COMMENT = 'WebIndexer app users and web stats table';\r\n";
					stmt.execute(createTable);

				}
			}

		} finally {
			closeConnection(con);
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
		}
	}
}
