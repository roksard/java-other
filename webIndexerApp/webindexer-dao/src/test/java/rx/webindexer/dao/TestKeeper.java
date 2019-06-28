package rx.webindexer.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import rx.webindexer.security.Hasher;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKeeper extends Keeper {
	String PROPERTIES_TEST_FILE = "src/test/resources/webindexer_test.properties";
	User testUser = null;
	LinkedList<StatsUnit> stats = null;

	boolean tableExists() throws Exception {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			try {
				// запрос, чтобы проверить что таблица существует
				stmt.execute("SELECT 1 FROM WIUSERS");
			} catch (SQLException e) {
				// 42S02: Table or view doesn't exist
				if (e.getSQLState().equalsIgnoreCase("42S02"))
					return false;
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return true;
	}

	void dropTable() throws Exception {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			stmt.execute("DROP TABLE wiusers");
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
	
	public void createTestUser() {
		testUser = new User("user1", "password1");
		StatsUnit statsUnit = new StatsUnit();
		statsUnit.calculated = true;
		statsUnit.letterFrequency = new LinkedList<TreeMap<Character, Float>>();
		TreeMap<Character, Float> letterFreq = new TreeMap<Character, Float>();
		letterFreq.put('c', 3.0f);
		letterFreq.put('a', 0.5f);
		TreeMap<Character, Float> letterFreq2 = new TreeMap<Character, Float>(letterFreq);
		letterFreq2.put('x', 5.0f);
		statsUnit.letterFrequency.add(letterFreq);
		statsUnit.letterFrequency.add(letterFreq2);
		statsUnit.setUniqueWordsPercent(50.0f);
		stats = new LinkedList<>();
		Collections.addAll(stats, statsUnit);
	}
	
	@Before
	public void beforeTest() {
		try {
			createTestUser();
			PROPERTIES_FILE = PROPERTIES_TEST_FILE;
			establishConnection();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@After
	public void afterTest() {
		if(connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	@Test //@Ignore
	public void _a_shouldInitDB() {
		try {
			if (tableExists())
				dropTable();
			initDataBase();
			Assert.assertTrue(tableExists());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void _b_shouldInsertUser() {
		try {
			PreparedStatement stmt =
			insertUser(testUser.getLogin(), Hasher.hashPasswordRandom(testUser.getPassword()), stats);
			boolean updated1 = stmt.getUpdateCount() == 1;
			Assert.assertTrue(updated1);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void _c_shouldGetUserStats() {
		try {
			List<StatsUnit> stats1 = getUserStats(testUser.getLogin());
			Assert.assertTrue(stats1.equals(stats));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test @Ignore
	public void _d_shouldGetUserPassword() {
		try {
			byte[] pwdRaw = getUserPassword(testUser.getLogin());
			byte[] salt = Hasher.extractSalt(pwdRaw);
			byte[] hash = Hasher.extractHash(pwdRaw);
			byte[] userHash = Hasher.hashPassword(testUser.getPassword(), salt);
			Assert.assertTrue(Arrays.equals(hash, userHash));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
