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
import main.application.models.Config;
import main.application.models.Utente;
import main.utils.PasswordHash;

/***********************************************
 * 	This is a Data Access Object
 * 
 * @author Administrator
 *
 **********************************************/
public class DataBaseHandler {
	private static DataBaseHandler DataBaseHandler = null;
	private String msg;
	private String passHash;
	
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
	
	public boolean runRegisterValidateQuery(String username, char[] password) {
		System.out.println("validating username");
		String query = "SELECT * FROM UTENTE WHERE UTENTE.USERNAME = ?";
		Connection conn;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.getString("config", "databasehost"),
					Config.getString("config", "usernamesql"), Config.getString("config", "passwordsql"));
			PreparedStatement  stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			String s = toStringResultSet(rs);
			if(s.equals("")) {
				System.out.println("the username is not used");
				updateUtenteTable(username, password, conn);
				return true;
			}
			else {
				setMsg("the username is already taken");
				conn.close();
				return false;
			}
			
		} catch (SQLException e) {
			this.setMsg("Can not connect to the SQL database!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean runValidateUserQuery(String username, char[] pass) {
		System.out.println("validating login");
		String query = "SELECT * FROM UTENTE WHERE UTENTE.USERNAME = ?";
		Connection conn;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.getString("config", "databasehost"),
					Config.getString("config", "usernamesql"), Config.getString("config", "passwordsql"));
			PreparedStatement  stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			Utente utente = this.RsToUtente(rs);
			System.out.println(utente.toString());
			if(utente.getUsername() == null) {
				this.setMsg("Incorrect username or password");
				return false;
			}
			else {
				try {
					if(PasswordHash.validatePassword(pass,getPassHash())) {
						Main.utente = utente;
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
	
	public void updateUtenteTable(String username, char[] password, Connection conn) {
		System.out.println("inserting user");
		String query = "INSERT INTO UTENTE(username,nome,cognome,pass_hash,scuola,avatar) "
				+ "VALUES(?,?,?,?,?,?)";
		try {
			String pash_hash = PasswordHash.createHash(password);
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.getString("config", "databasehost"),
					Config.getString("config", "usernamesql"), Config.getString("config", "passwordsql"));
			PreparedStatement  stmt = conn.prepareStatement(query);
			stmt.setString(1, username);	// username
			stmt.setString(2, null); 		// nome
			stmt.setString(3, null); 		// cognome
			stmt.setString(4, pash_hash);   // pass_hash
			stmt.setString(5, null); 		// scuola
			stmt.setString(6, null);		// avatar path
			stmt.execute();
			System.out.println(stmt);
		} catch (SQLException e) {
		//	SimplerSchoolUtil.errorMsg("Connection lost! Failed to update the user table!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
	
	public Utente RsToUtente(ResultSet rs) {
		Utente utente = new Utente();
		try {
			while (rs.next()) {
				utente.setUserid(rs.getInt("user_id"));
				utente.setUsername(rs.getString("username"));
				utente.setNome(rs.getString("nome"));
				utente.setCognome(rs.getString("cognome"));
				utente.setScuola(rs.getString("scuola"));
				this.savePassHash(rs.getString("pass_hash"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return utente;
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
	
	public void savePassHash(String passHash) {
		this.passHash = passHash;
	}
	
	public String getPassHash() {
		String pass = this.passHash;
		this.passHash = "";
		return pass;
	}
	
}
