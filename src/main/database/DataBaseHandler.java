package main.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.commons.io.FileUtils;
import com.mysql.jdbc.Statement;
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
	private ArrayList<SchoolTask> attivita;
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
	
	public ArrayList<SchoolTask> getAttivita() {
		return attivita;
	}
	
	public SchoolTask getAttivita(int idTask) {
		if(attivita == null)
			return null;
		else{
			for(SchoolTask task : attivita) {
				if(task.getIdTask() == idTask)
					return task;
			}
		}
		return null;
	}
	
	public SchoolTask getAttivita(String idTask) {
		int id = Integer.parseInt(idTask);
		if(attivita == null)
			return null;
		else{
			for(SchoolTask task : attivita) {
				if(task.getIdTask() == id)
					return task;
			}
		}
		return null;
	}
	
	public boolean addAttivita(SchoolTask task) {
		return attivita.add(task);
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
		getAttivitaS(LocalDate.now());
		Console.print("--------------------------------","app");
	}

	public boolean runResetPassQuery(String username, char[] password) {
		Console.print("Reseting passwod","app");
		String query = "UPDATE UTENTE SET pass_hash = ? WHERE username = ?";
		Connection conn = openConn();
		try {
			String pash_hash = PasswordHash.createHash(password);
			PreparedStatement stmt = conn.prepareStatement(query);
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
			rs = stmt.executeQuery();
			String s = toStringResultSet(rs);
			if (s.equals("")) {
				Console.print("the username is not used","debug");
				return insertUtenteQuery(u, password, conn);
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
			PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, u.getUsername()); 	// username
			stmt.setString(2, u.getNome()); 		// nome
			stmt.setString(3, u.getCognome()); 	// cognome
			stmt.setString(4, pash_hash); 			// pass_hash
			stmt.setString(5, u.getScuola()); 		// scuola
			stmt.setString(6, defaultAvatar);		// avatar path
			stmt.execute();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next())
	                u.setUserid(generatedKeys.getInt(1));
	            else
	                throw new SQLException("Creating user failed, no ID obtained.");
	        }
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
		String query = "UPDATE UTENTE SET SCUOLA = ?, NOME = ?, COGNOME = ?, AVATAR_PATH = ? WHERE USER_ID = ?";
		try{
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, u.getScuola());
			stmt.setString(2, u.getNome());
			stmt.setString(3, u.getCognome());
			stmt.setString(4, u.getAvatar_path());
			stmt.setInt(5, u.getUserid());
			int recordUpdated = stmt.executeUpdate();
			if (recordUpdated != 1)
				throw new IllegalArgumentException("error! multiple record have been updated!");
			Console.print(recordUpdated + " record has been updated!","db");
		}catch (SQLException e) {
			this.setMsg("Failed to update the user table! Check query and connection");
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
		}
		
	}
	
	public void uploadAvatarFile(Utente u, Connection conn) {
		Console.print("Uploading avatar file","db");
		File avatar = new File(u.getAvatar_path());
		File serverAvatar = new File(Config.getString("config", "databaseFolder") + "/users/"+ u.getUserid());
		if (!serverAvatar.exists()) {
			serverAvatar.mkdirs();
		}
		serverAvatar = new File	(serverAvatar.getAbsolutePath() + "/" + avatar.getName());
		u.setAvatar_path("users/" + u.getUserid() + "/" + avatar.getName());
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
			int recordUpdated = stmt.executeUpdate();
			if (recordUpdated != 1)
				throw new IllegalArgumentException("error! multiple record have been updated!");
			Console.print(recordUpdated + " record has been updated!","db");
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
			stmt.execute();
			removeOSXmlFile(os);
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
			return false;
		}
	}
	
	public boolean getAttivitaS(LocalDate data) {
		Console.print("Getting tasks of this week","db");
		String query = "SELECT * FROM task WHERE YEARWEEK(TASK_DATA, 1) = YEARWEEK(?, 1) "
				+ "AND USER_ID = ? "
			//	+ "AND TIPO in ('compito','interrogazione','verifica') " 
				+ "order by TASK_DATA, TIPO desc";
		Connection conn = openConn();
		ResultSet rs = null;
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setDate(1, Date.valueOf(data));
			stmt.setInt(2, Main.utente.getUserid());
			rs = stmt.executeQuery();
			return rsToAttivita(rs);
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}
	
	public boolean insertTaskQuery(SchoolTask task) {
		Console.print("Inserting task","db");
		String query = "INSERT INTO TASK(TIPO,MATERIA,TASK_DATA, COMMENTO, USER_ID) VALUES(?,?,?,?,?)";
		Connection conn = openConn();
		try {
			PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, task.getTipo());
			stmt.setString(2, task.getMateria());
			stmt.setDate(3, java.sql.Date.valueOf(task.getData()));
			stmt.setString(4, task.getComment());
			stmt.setInt(5, Main.utente.getUserid());
			stmt.execute();
			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	            if (generatedKeys.next())
	                task.setIdTask(generatedKeys.getInt(1));
	            else
	                throw new SQLException("Creating task failed, no ID obtained.");
	        }
			if(task.hasAllegato())
				uploadAllegati(task, conn);
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
			return false;
		}
	}
	
	public boolean deleteTaskQuery(SchoolTask task) {
		Console.print("Deleting task "+task.getIdTask(),"db");
		String query = "DELETE FROM TASK WHERE TASK_ID = ? AND USER_ID = ?";
		Connection conn = openConn();
		try {
			PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, task.getIdTask());
			stmt.setInt(2, Main.utente.getUserid());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
			return false;
		}
	}
	
	public boolean updateTaskQuery(SchoolTask task) {
		Console.print("Updating task " + task.getIdTask(),"db");
		String query = "UPDATE TASK SET TASK_DATA = ?, MATERIA = ?, TIPO = ?, COMMENTO = ? WHERE TASK_ID = ? AND USER_ID =?";
		Connection conn = openConn();
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setDate(1, Date.valueOf(task.getData()));
			stmt.setString(2, task.getMateria());
			stmt.setString(3, task.getTipo());
			stmt.setString(4, task.getComment());
			stmt.setInt(5, task.getIdTask());
			stmt.setInt(6, Main.utente.getUserid());
			int recordUpdated = stmt.executeUpdate();
			if (recordUpdated != 1)
				throw new IllegalArgumentException("error! multiple record have been updated!");
			Console.print(recordUpdated + " record has been updated!","db");
			return true;
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
			return false;
		}
	}
	
	public void uploadAllegati(SchoolTask task, Connection conn) {
		Console.print("Uploading allegati to db folder","db");
		LinkedHashMap<String,File> files = task.getAllegati();
		File destFolder = new File(Config.getString("config", "databaseFolder") + 
				"/users/" + Main.utente.getUserid() + "/allegati/");
		if(!destFolder.exists())
			destFolder.mkdirs();
		for(String file : files.keySet()) {
			if(insertAllegatoQuery(file, task.getIdTask(), conn)) {
				File dest = new File(destFolder.getAbsolutePath() + "/" +file);
				try {
					FileUtils.copyFile(files.get(file), dest);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Console.print(files.size() + " allegati uploaded","db");
	}
	
	public ResultSet getAllegatoByTask(int idTask) {
		String query = "SELECT file_path FROM allegato WHERE task_id = ?";
		Connection conn = openConn();
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, idTask);
			return stmt.executeQuery();
		} catch (SQLException e) {
			Console.print("Can not connect to the SQL database! " + e.getMessage(),"db");
			this.setMsg("Can not connect to the SQL database!");
		}
		return null;
	}
	
	public boolean insertAllegatoQuery(String allegato, int taskID, Connection conn) {
		String query = "INSERT INTO ALLEGATO(file_path, task_id) VALUES(?,?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, "users/" + Main.utente.getUserid() + "/allegati/"+ taskID + "/" + allegato);
			stmt.setInt(2, taskID);
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
	
	public boolean rsToAttivita(ResultSet rs) {
		attivita = new ArrayList<SchoolTask>();
		SchoolTask task;
		try {
			while (rs.next()) {
				task = new SchoolTask(rs.getInt("TASK_ID"), 
						rs.getDate("TASK_DATA").toLocalDate(),
						rs.getString("TIPO"), 
						rs.getString("MATERIA"),
						rs.getString("COMMENTO"));
				ResultSet files = getAllegatoByTask(task.getIdTask());
				while(files.next()) {
					String path = files.getString("file_path");
					task.addFile(new File(Config.getString("config", "databaseFolder") + "/" + path));
				}
				attivita.add(task);
			}
			Console.print(attivita.size()+ " tasks loaded", "db");
		} catch (SQLException e) {
			Console.print("Failed to retrive resultset" + e.getMessage(),"db");
		}
		if (attivita.isEmpty())
			return false;
		else
			return true;
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
