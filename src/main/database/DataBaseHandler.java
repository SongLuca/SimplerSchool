package main.database;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mysql.jdbc.Blob;

import java.sql.PreparedStatement;
import main.application.Main;
import main.utils.PasswordHash;
import main.utils.SimplerSchoolUtil;

/***********************************************
 * 	This is a Data Access Object
 * 
 * 
 * 
 * @author Administrator
 *
 **********************************************/
public class DataBaseHandler {
	private static DataBaseHandler DataBaseHandler = null;
	private String msg;
	
	private DataBaseHandler() {
		
	}

	public static DataBaseHandler getInstance() {
		if (DataBaseHandler == null)
			DataBaseHandler = new DataBaseHandler();

		return DataBaseHandler;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void resetMsg() {
		this.msg = "";
	}
	
	public String getMsg() {
		return msg;
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
			String s = toStringResultSet(rs);
			if(s.equals("")) {
				System.out.println("the username is not used");
				conn.close();
				return true;
			}
			else {
				System.out.println("the username is already taken");
				conn.close();
				return false;
			}
			
		} catch (SQLException e) {
			this.setMsg("Can not connect to the SQL database!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean runValidateUserQuery(String username, char[] pass) {
		System.out.println("validating login");
		String query = "SELECT UTENTE.PASS_HASH FROM UTENTE WHERE UTENTE.USERNAME = ?";
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
			String s = toStringResultSet(rs);
			if(s.equals("")) {
				this.setMsg("Incorrect username or password");
				return false;
			}
			else {
				try {
					if(PasswordHash.validatePassword(pass,s)) {
						this.setMsg("Welcome");
						conn.close();
						return true;
					}
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					e.printStackTrace();
				}
				this.setMsg("Incorrect username or password");
				conn.close();
				return false;
			}
			
		} catch (SQLException e) {
			System.out.println("Can not connect to the SQL database!");
			this.setMsg("Can not connect to the SQL database!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		return false;
	}
	
	public void updateUtenteTable(String username, char[] password) {
		System.out.println("inserting user");
		String query = "INSERT INTO UTENTE VALUES(?,?,?,?,?,?)";
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
			stmt.setBlob(6, (Blob) null);
			stmt.execute();
			System.out.println(stmt);
		} catch (SQLException e) {
			SimplerSchoolUtil.errorMsg("Connection lost! Failed to update the user table!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
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
						toString+="|";
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
