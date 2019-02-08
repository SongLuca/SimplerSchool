package main.database;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import main.application.Main;
import main.utils.PasswordHash;

public class DataBaseHandler {
	private static DataBaseHandler DataBaseHandler = null;

	private DataBaseHandler() {
	}

	public static DataBaseHandler getInstance() {
		if (DataBaseHandler == null)
			DataBaseHandler = new DataBaseHandler();

		return DataBaseHandler;
	}

	public boolean runRegisterValidateQuery(String username) {
		System.out.println("Validating username");
		String query = "SELECT * FROM UTENTE WHERE UTENTE.USERNAME = ?";
		Connection conn;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Main.prop.getProperty("databasehost"),
					Main.prop.getProperty("usernamesql"), Main.prop.getProperty("passwordsql"));
			PreparedStatement  stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			if(toStringResultSet(rs).equals("")) {
				System.out.println("result is empty");
				return true;
			}
			else {
				System.out.println("not empty");
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void updateUtenteTable(String username, char[] password) {
		System.out.println("Inserting user");
		String query = "INSERT INTO UTENTE VALUES(?,?,?,?,?)";
		Connection conn;
		try {
			String pash_hash = PasswordHash.createHash(password);
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Main.prop.getProperty("databasehost"),
					Main.prop.getProperty("usernamesql"), Main.prop.getProperty("passwordsql"));
			PreparedStatement  stmt = conn.prepareStatement(query);
			stmt.setString(1, username);	// username
			stmt.setString(2, null); 		// nome
			stmt.setString(3, null); 		// cognome
			stmt.setString(4, pash_hash);   // pass_hash
			stmt.setString(5, null); 		// scuola
			stmt.execute();
			
			System.out.println(stmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toStringResultSet(ResultSet rs) {
		ResultSetMetaData rsmd;
		String toString = "";
		try {
			rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1)
						toString+=" | ";
					toString+=rs.getString(i);
				}
				toString+="";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toString;
	}
}
