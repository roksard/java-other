package rx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

public class JDBC1 {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/mydb1?serverTimezone=UTC", "root", "6696");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from customers");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + 
				rs.getString(3) + rs.getInt(4));
			
			stmt.addBatch("insert into customers (Id,Name,Address,Age) values "+
					"(4,'Rob','Dresden',22);");
			stmt.addBatch("update customers set Name='Edward' where id=2;");
			int[] batchRes = stmt.executeBatch();			
			System.out.println(Arrays.toString(batchRes));
			ResultSet rs2 = stmt.executeQuery("select Name,Age,Address from customers");
			while(rs2.next()) {
				System.out.println(rs2.getString(1) + rs2.getString(2) + 
						rs2.getString(3));
			}
			
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
