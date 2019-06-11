package rx.webindexer.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Keeper {
	static Properties properties = null;
	static final String PROPERTIES_FILE = "webindexer.properties";
	static final String DB_HOST_PROPERTY = "db.host";
	static final String DB_USER_PROPERTY = "db.user";
	static final String DB_PASSWORD_PROPERTY = "db.pw";

	public static Properties getProperties() throws FileNotFoundException, IOException {
		// настройки properties используются в течение работы программы
		// доступ к ним осуществляется с помощью текущего метода
		if (properties != null)
			return properties;
		try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE);) {
			Properties prop = new Properties();
			prop.load(fis);
			return prop;
		}
	}

	public static String getDBUser() throws FileNotFoundException, IOException {
		return getProperties().getProperty(DB_USER_PROPERTY);
	}

	public static String getDBHost() throws FileNotFoundException, IOException {
		return getProperties().getProperty(DB_HOST_PROPERTY);
	}

	public static String getDBPassword() throws FileNotFoundException, IOException {
		return getProperties().getProperty(DB_PASSWORD_PROPERTY);
	}

	public static void initDataBase() throws Exception {
		Connection con = null; 
		Statement stmt = null; 
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(getDBHost(), getDBUser(), getDBPassword());
			stmt = con.createStatement();
			stmt.execute("SELECT 1 FROM WI-USERS");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if(con != null)
				con.close();
			if(stmt != null) 
				stmt.close();
			if(rs != null)
				rs.close();
		}
	}
}
