package ru.roksard.jdbc2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import ru.roksard.access.Access;

public class JDBC2 {
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void writeBinaryObjectToDB(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (baos.toByteArray().length > 255) {
			System.out.println("cannot write large objects (>255 bytes) using"
					+ " writeBinary. Use writeBlob instead.");
			return;
		}
		try {
			Connection con = DriverManager.getConnection(Access.dbURL, Access.user, 
					Access.password);
			PreparedStatement stmt = 
					con.prepareStatement("insert into mydata (data) values (?)");
			stmt.setBytes(1, baos.toByteArray());
			System.out.println(stmt.toString());
			if (stmt.execute()) {
				System.out.println(stmt.getResultSet());
			} else {
				System.out.println("Updated lines: " +stmt.getUpdateCount());
			}
			con.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Object readBinaryObjectFromDB(int id) {
		byte[] objBytes = null;
		try {
			Connection con = DriverManager.getConnection(Access.dbURL, Access.user, 
					Access.password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select data from mydata where id=" + id);
			if(rs.next()) {
				objBytes = rs.getBytes(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(objBytes); 
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object result = ois.readObject();
			ois.close();
			bais.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeBlobToDB(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Connection con = DriverManager.getConnection(Access.dbURL, Access.user, 
					Access.password);
			PreparedStatement stmt = 
					con.prepareStatement("insert into mydata (bigdata) values (?)");
			stmt.setBytes(1, baos.toByteArray());
			stmt.setBlob(1, new ByteArrayInputStream(baos.toByteArray()));
			System.out.println(stmt.toString());
			if (stmt.execute()) {
				System.out.println(stmt.getResultSet());
			} else {
				System.out.println("Updated lines: " +stmt.getUpdateCount());
			}
			con.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Object readBlobFromDB(int id) {
		Blob blob = null;
		try {
			Connection con = DriverManager.getConnection(Access.dbURL, Access.user, 
					Access.password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select bigdata from mydata where id=" + id);
			if(rs.next()) {
				blob = rs.getBlob(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		ByteArrayInputStream bais;
		try {
			bais = new ByteArrayInputStream(blob.getBytes(1L, (int)blob.length()));
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object result = ois.readObject();
			ois.close();
			bais.close();
			return result;
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	}
	public static void main(String[] args) {
		MyData data = new MyData();
		data.setName("Test binary");
		data.setDescription("testing ");
		data.getWordsCount().put("binary", 111);
		data.getWordsCount().put("hello", 2);
		System.out.println("data to be written: \n" + data);
		//writeBlobToDB(data);
		MyData data1 = (MyData)readBlobFromDB(4);
		System.out.println(data1);
		System.out.println("data equals data1?: " +data.equals(data1));
		//writeBinaryObjectToDB("<<<<<im some other string that should be read>>>>");
		//System.out.println((String)readBinaryObjectFromDB(1));
		//System.out.println((String)readBinaryObjectFromDB(2));
		
	}
}
