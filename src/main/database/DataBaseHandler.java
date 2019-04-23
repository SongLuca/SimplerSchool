package main.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.sql.PreparedStatement;
import main.application.Main;
import main.application.models.Config;
import main.application.models.Materia;
import main.application.models.OrarioSettimanale;
import main.application.models.SchoolTask;
import main.application.models.Utente;
import main.utils.Console;
import main.utils.PasswordHash;

/***********************************************
 * This is a Data Access Object
 * 
 * @author Administrator
 *
 **********************************************/
public class DataBaseHandler {
	private static DataBaseHandler DataBaseHandler = null;
	private String msg;
	private String passHash;
	private HashMap<Integer, Materia> materie;
	private HashMap<Integer, OrarioSettimanale> orariS;
	static String mySqlConnClass = "com.mysql.jdbc.Driver";
	private String defaultAvatar = "default/defaultAvatar.jpg";

	private DataBaseHandler() {

	}

	public static DataBaseHandler getInstance() {
		if (DataBaseHandler == null)
			DataBaseHandler = new DataBaseHandler();

		return DataBaseHandler;
	}

	public HashMap<Integer, Materia> getMaterie() {
		return new HashMap<Integer, Materia>(materie);
	}
	
	public HashMap<Integer, OrarioSettimanale> getOS() {
		return orariS;
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
	
	public Connection openConn() {
		Connection conn = null;
		try {
			Class.forName(mySqlConnClass);
			conn = DriverManager.getConnection(Config.getString("config", "databasehost"),
					Config.getString("config", "usernamesql"), Config.getString("config", "passwordsql"));
		} catch (SQLException | ClassNotFoundException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		} 
		return conn;
	}
	
	public void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			Console.print("Error caught while closing connection","db");
			e.printStackTrace();
		}
	}

	public void preLoadUserData() { // prende tutti i dati dal db prima di entrare nel main
		Console.print("---Preloading all user data---","app");
		runGetMaterieQuery();
		getOSQuery();
		Console.print("--------------------------------","app");
	}

	public boolean runResetPassQuery(String username, char[] password) {
		Console.print("Reseting passwod","app");
		String query = "UPDATE UTENTE SET pass_hash = ? WHERE username = ?";
		Connection conn = openConn();
		try {
			String pash_hash = PasswordHash.createHash(password);
			PreparedStatement stmt = conn.prepareStatement(query);
			Console.print(stmt.toString(),"db");
			stmt.setString(1, pash_hash);
			stmt.setString(2, username);
			int recordUpdated = stmt.executeUpdate();
			if (recordUpdated == 1)
				return true;
			else {
				Console.print(recordUpdated + " record has been updated!","db");
				this.setMsg("No such username");
				return false;
			}
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Failed to update the user table! Check query and connection");
			return false;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean runRegisterValidateQuery(Utente u, char[] password) {
		Console.print("Validating username","db");
		String query = "SELECT * FROM UTENTE WHERE UTENTE.USERNAME = ?";
		Connection conn = openConn();
		ResultSet rs = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, u.getUsername());
			Console.print(stmt.toString(),"db");
			rs = stmt.executeQuery();
			String s = toStringResultSet(rs);
			if (s.equals("")) {
				Console.print("the username is not used","debug");
				if (insertUtenteQuery(u, password, conn))
					return true;
				else
					return false;
			} else {
				setMsg("the username is already taken");
				conn.close();
				return false;
			}
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}

	public boolean runValidateUserQuery(String username, char[] pass) {
		Console.print("Validating login","db");
		String query = "SELECT * FROM UTENTE WHERE UTENTE.USERNAME = ?";
		Connection conn = openConn();
		ResultSet rs = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			Console.print(stmt.toString(),"db");
			rs = stmt.executeQuery();
			Utente utente = this.RsToUtente(rs);
			if (utente.getUsername() == null) {
				this.setMsg("Incorrect username or password");
				return false;
			} else {
				try {
					if (PasswordHash.validatePassword(pass, getPassHash())) {
						Main.utente = utente;
						Console.print("logged in user info: " + utente.toString(),"db");
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
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}

	public boolean insertUtenteQuery(Utente u, char[] password, Connection conn) {
		Console.print("Inserting user","db");
		String query = "INSERT INTO UTENTE(username,nome,cognome,pass_hash,scuola,avatar_path) "
				+ "VALUES(?,?,?,?,?,?)";
		try {
			String pash_hash = PasswordHash.createHash(password);
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, u.getUsername()); 	// username
			stmt.setString(2, u.getNome()); 		// nome
			stmt.setString(3, u.getCognome()); 	// cognome
			stmt.setString(4, pash_hash); 			// pass_hash
			stmt.setString(5, u.getScuola()); 		// scuola
			stmt.setString(6, defaultAvatar);		// avatar path
			Console.print(stmt.toString(),"db");
			stmt.execute();
			if (!u.getAvatar_path().equals(defaultAvatar)) {
				uploadAvatarFile(u, conn);
				updateUtenteQuery(u, conn);
			}
			return true;
		} catch (SQLException e) {
			this.setMsg("Failed to update the user table! Check query and connection");
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			return false;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void updateUtenteQuery(Utente u, Connection conn) {
		Console.print("Updating user","db");
		String query = "UPDATE UTENTE SET SCUOLA = ?, NOME = ?, COGNOME = ?, AVATAR_PATH = ? WHERE USERNAME = ?";
		try{
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, u.getScuola());
			stmt.setString(2, u.getNome());
			stmt.setString(3, u.getCognome());
			stmt.setString(4, u.getAvatar_path());
			stmt.setString(5, u.getUsername());
			Console.print(stmt.toString(),"db");
			int recordUpdated = stmt.executeUpdate();
			if (recordUpdated != 1)
				throw new IllegalArgumentException("error! multiple record have been updated!");
			Console.print(recordUpdated + " record has been updated!","db");
		}catch (SQLException e) {
			this.setMsg("Failed to update the user table! Check query and connection");
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
		}
		
	}
	
	public int getUserIdQuery(String username, Connection conn) {
		Console.print("Getting user id","db");
		String query = "SELECT USER_ID FROM UTENTE WHERE USERNAME = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			Console.print(stmt.toString(),"db");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				return rs.getInt("user_id");
			}
			throw new IllegalArgumentException("invalid username: " + username);
		} catch (SQLException e) {
			this.setMsg("Failed to update the user table! Check query and connection");
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
		}
		return 0;
	}
	
	public void uploadAvatarFile(Utente u, Connection conn) {
		Console.print("Uploading avatar file","db");
		int id = getUserIdQuery(u.getUsername(), conn);
		File avatar = new File(u.getAvatar_path());
		File serverAvatar = new File(Config.getString("config", "databaseFolder") + "/users/"+id);
		if (!serverAvatar.exists()) {
			serverAvatar.mkdirs();
		}
		serverAvatar = new File	(serverAvatar.getAbsolutePath() + "/" + avatar.getName());
		u.setUserid(id);
		u.setAvatar_path("users/" + id + "/" + avatar.getName());
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(avatar);
			os = new FileOutputStream(serverAvatar);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean runGetMaterieQuery() {
		Console.print("Getting materie","db");
		String query = "SELECT * FROM MATERIA WHERE USER_ID = ?";
		Connection conn = openConn();
		ResultSet rs = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, Main.utente.getUserid());
			Console.print(stmt.toString(),"db");
			rs = stmt.executeQuery();
			return rsToMaterie(rs);
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}

	public boolean updateMateriaTable(HashMap<Integer, Materia> materieNuove) {
		Console.print("Update materie table","db");
		Connection conn = openConn();
		for (int key : materieNuove.keySet()) {
			Materia m = materieNuove.get(key);
			switch (m.getStato()) {
			case "insert": // inserimento della nuova materia
				Console.print("Inserting materia " + m.getNome(),"db");
				runInsertMateriaQuery(m, conn);
				break;
			case "update": // aggiornamento della materia modificata
				Console.print("updating materia " + m.getNome(),"db");
				runUpdateMateriaQuery(m, conn);
				break;
			case "delete": // cancellamento della materia
				Console.print("deleting materia " + m.getNome(),"db");
				runDeleteMateriaQuery(m, conn);
				break;
			case "fresh":
				// non fare niente...
				break;
			default:
				throw new IllegalArgumentException("Stato invalido nella materia " + m.getId());
			}
		}
		this.closeConn(conn);
		return true;
	}

	public boolean runDeleteMateriaQuery(Materia m, Connection conn) {
		String query = "DELETE FROM MATERIA WHERE MATERIA_ID = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, m.getId());
			Console.print(stmt.toString(),"db");
			stmt.execute();
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}

	public boolean runUpdateMateriaQuery(Materia m, Connection conn) {
		String query = "UPDATE MATERIA SET NOME = ?, COLOR = ? WHERE MATERIA_ID = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, m.getNome());
			stmt.setString(2, m.getColore());
			stmt.setInt(3, m.getId());
			Console.print(stmt.toString(),"db");
			int recourdUpdated = stmt.executeUpdate();
			if (recourdUpdated == 1)
				return true;
			else {
				Console.print(recourdUpdated + " records have been updated!","db");
				return false;
			}
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}

	public boolean runInsertMateriaQuery(Materia m, Connection conn) {
		String query = "INSERT INTO MATERIA (MATERIA_ID, NOME, COLOR, USER_ID) VALUES(?,?,?,?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, m.getId());
			stmt.setString(2, m.getNome());
			stmt.setString(3, m.getColore());
			stmt.setInt(4, Main.utente.getUserid());
			Console.print(stmt.toString(),"db");
			stmt.execute();
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}
	
	public boolean updateOSTable(OrarioSettimanale os) {
		Console.print("Update orariosettimanale table","db");
		Connection conn = openConn();
		switch(os.getStato()) {
			case "insert":
				return insertOSQuery(os, conn);
			case "update": 
				return updateOSQuery(os, conn);
			case "delete": 
				return deleteOSQuery(os, conn);
			case "fresh": 
				Console.print("No action taken on " + os.getNomeOrario(),"db");
				break;
		}
		return false;
	}
	
	public boolean getOSQuery() {
		Console.print("Getting orariosettimanale","db");
		String query = "SELECT * FROM ORARIOSETTIMANALE WHERE USER_ID = ?";
		Connection conn = openConn();
		ResultSet rs = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, Main.utente.getUserid());
			Console.print(stmt.toString(),"db");
			rs = stmt.executeQuery();
			return rsToOS(rs);
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}
	
	public boolean insertOSQuery(OrarioSettimanale os, Connection conn) {
		Console.print("Inserting orariosettimanale" + os.getId(),"db");
		String query = "INSERT INTO ORARIOSETTIMANALE(OS_ID, NOME, FILE_PATH, USER_ID) VALUES(?,?,?,?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, os.getId());
			stmt.setString(2, os.getNomeOrario());
			stmt.setString(3, os.getStoredPath());
			stmt.setInt(4, Main.utente.getUserid());
			Console.print(stmt.toString(),"db");
			stmt.execute();
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
			return false;
		}
	}
	
	public boolean updateOSQuery(OrarioSettimanale os, Connection conn) {
		Console.print("updating orariosettimanale" + os.getId(),"db");
		String query = "UPDATE ORARIOSETTIMANALE SET NOME = ? WHERE OS_ID = ? AND USER_ID =?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, os.getNomeOrario());
			stmt.setInt(2, os.getId());
			stmt.setInt(3, Main.utente.getUserid());
			Console.print(stmt.toString(),"db");
			stmt.execute();
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
			return false;
		}
	}
	
	public boolean deleteOSQuery(OrarioSettimanale os, Connection conn) {
		Console.print("Deleting orariosettimanale " + os.getId(),"db");
		String query = "DELETE FROM ORARIOSETTIMANALE WHERE OS_ID = ? AND USER_ID = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, os.getId());
			stmt.setInt(2, Main.utente.getUserid());
			Console.print(stmt.toString(),"db");
			stmt.execute();
			removeOSXmlFile(os);
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
			return false;
		}
	}
	
	public boolean insertTaskQuery(SchoolTask task) {
		Console.print("Inserting task","db");
		String query = "INSERT INTO TASK(TIPO,MATERIA,TASK_DATA, COMMENTO, USER_ID) VALUES(?,?,?,?,?)";
		Connection conn = openConn();
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, task.getTipo());
			stmt.setString(2, task.getMateria());
			stmt.setDate(3, java.sql.Date.valueOf(task.getData()));
			stmt.setString(4, task.getComment());
			stmt.setInt(5, Main.utente.getUserid());
			Console.print(stmt.toString(),"db");
			stmt.execute();
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
			return false;
		}
	}
	
	
	
	
	
	public void removeOSXmlFile(OrarioSettimanale os) {
		File xml = new File(Config.getString("config", "databaseFolder") + "/" + os.getStoredPath());
		if(xml.delete())
			Console.print(xml.getAbsolutePath() + " deleted","fileio");
		else
			Console.print(xml.getAbsolutePath() + " doesnt exist","fileio");
	}
	
	public boolean rsToOS(ResultSet rs) {
		orariS = new HashMap<Integer, OrarioSettimanale>();
		OrarioSettimanale os;
		try {
			while (rs.next()) {
				os = new OrarioSettimanale();
				os.setId(rs.getInt("os_id"));
				os.setNomeOrario(rs.getString("nome"));
				os.setStoredPath(rs.getString("file_path"));
				os.setUser_id(rs.getInt("user_id"));
				os.loadXML();
				orariS.put(os.getId(), os);
			}
		} catch (SQLException e) {
			Console.print("Failed to retrive resultset" + e.getMessage(),"db");
		}
		if (orariS.isEmpty())
			return false;
		else
			return true;
	}
	
	public boolean rsToMaterie(ResultSet rs) {
		materie = new HashMap<Integer, Materia>();
		Materia ma;
		try {
			while (rs.next()) {
				ma = new Materia();
				ma.setId(rs.getInt("materia_id"));
				ma.setNome(rs.getString("nome"));
				ma.setColore(rs.getString("color"));
				ma.setStato("fresh");
				materie.put(ma.getId(), ma);
			}
		} catch (SQLException e) {
			Console.print("Failed to retrive resultset" + e.getMessage(),"db");
		}
		if (materie.isEmpty())
			return false;
		else
			return true;
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
				utente.setAvatar_path(rs.getString("avatar_path"));
				this.savePassHash(rs.getString("pass_hash"));
			}
		} catch (SQLException e) {
			Console.print("Failed to retrive resultset" + e.getMessage(),"db");
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
						toString += "|";
					toString += rs.getString(i);
				}
				toString += "";
			}
		} catch (SQLException e) {
			Console.print("Failed to retrive resultset" + e.getMessage(),"db");
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
