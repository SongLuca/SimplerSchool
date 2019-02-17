package main.database;

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
import main.application.models.Utente;
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

	private DataBaseHandler() {

	}

	public static DataBaseHandler getInstance() {
		if (DataBaseHandler == null)
			DataBaseHandler = new DataBaseHandler();

		return DataBaseHandler;
	}

	public HashMap<Integer, Materia> getMaterie() {
		return materie;
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
	
	public void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Error caught while closing connection");
			e.printStackTrace();
		}
	}
	
	public boolean runResetPassQuery(String username, char[] password){
		System.out.println("reseting passwod");
		String query = "UPDATE UTENTE SET pass_hash = ? WHERE username = ?";
		Connection conn;
		try {
			String pash_hash = PasswordHash.createHash(password);
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.getString("config", "databasehost"),
					Config.getString("config", "usernamesql"), Config.getString("config", "passwordsql"));
			PreparedStatement stmt = conn.prepareStatement(query);
			System.out.println(stmt);
			stmt.setString(1, pash_hash);
			stmt.setString(2, username);
			int recordUpdated = stmt.executeUpdate();
			if(recordUpdated == 1)
				return true;
			else {
				System.out.println(recordUpdated + " have been updated!");
				this.setMsg("No such username");
				return false;
			}
		} catch (SQLException e) {
			this.setMsg("Failed to update the user table! Check query and connection");
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
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
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			String s = toStringResultSet(rs);
			if (s.equals("")) {
				System.out.println("the username is not used");
				if (updateUtenteTable(username, password, conn))
					return true;
				else
					return false;
			} else {
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
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			Utente utente = this.RsToUtente(rs);
			System.out.println(utente.toString());
			if (utente.getUsername() == null) {
				this.setMsg("Incorrect username or password");
				return false;
			} else {
				try {
					if (PasswordHash.validatePassword(pass, getPassHash())) {
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

	public boolean updateUtenteTable(String username, char[] password, Connection conn) {
		System.out.println("inserting user");
		String query = "INSERT INTO UTENTE(username,nome,cognome,pass_hash,scuola,avatar_path) "
				+ "VALUES(?,?,?,?,?,?)";
		try {
			String pash_hash = PasswordHash.createHash(password);
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.getString("config", "databasehost"),
					Config.getString("config", "usernamesql"), Config.getString("config", "passwordsql"));
			PreparedStatement stmt = conn.prepareStatement(query);
			System.out.println(stmt);
			stmt.setString(1, username); // username
			stmt.setString(2, null); // nome
			stmt.setString(3, null); // cognome
			stmt.setString(4, pash_hash); // pass_hash
			stmt.setString(5, null); // scuola
			stmt.setString(6, null); // avatar path
			stmt.execute();
			return true;
		} catch (SQLException e) {
			this.setMsg("Failed to update the user table! Check query and connection");
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean runGetMaterieQuery() {
		System.out.println("getting materie ");
		String query = "SELECT * FROM MATERIA";
		Connection conn;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.getString("config", "databasehost"),
					Config.getString("config", "usernamesql"), Config.getString("config", "passwordsql"));
			PreparedStatement stmt = conn.prepareStatement(query);
			System.out.println(stmt);
			rs = stmt.executeQuery();
			return rsToMaterie(rs);
		} catch (SQLException e) {
			System.out.println("Can not connect to the SQL database!");
			this.setMsg("Can not connect to the SQL database!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateMateriaTable(HashMap<Integer, Materia> materieNuove) {
		System.out.println("updating materie");
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.getString("config", "databasehost"),
					Config.getString("config", "usernamesql"), Config.getString("config", "passwordsql"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Can not connect to the SQL database!");
			this.setMsg("Can not connect to the SQL database!");
		}
		for (int key : materie.keySet()) {
			Materia m = materie.get(key);
			switch (m.getStato()) {
				case "insert":  // inserimento della nuova materia
					runInsertMateria(m, conn); 
					break;
				case "update":  // aggiornamento della materia modificata
					runUpdateMateria(m, conn);
					break;
				case "delete":	// cancellamento della materia
					runDeleteMateria(m, conn);
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
	
	public boolean runDeleteMateria(Materia m, Connection conn) {
		String query = "DELETE FROM MATERIA WHERE MATERIA_ID = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			System.out.println(stmt);
			stmt.setInt(1, m.getId());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			System.out.println("Can not connect to the SQL database!");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}
	
	public boolean runUpdateMateria(Materia m, Connection conn) {
		String query = "UPDATE MATERIA SET NOME = ?, COLOR = ? WHERE MATERIA_ID = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			System.out.println(stmt);
			stmt.setString(1, m.getNome());
			stmt.setString(2, m.getColore());
			stmt.setInt(3, m.getId());
			int recourdUpdated = stmt.executeUpdate();
			if(recourdUpdated == 1)
				return true;
			else {
				System.out.println(recourdUpdated + " records have been updated!");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("Can not connect to the SQL database!");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
	}
	
	public boolean runInsertMateria(Materia m, Connection conn) {
		String query = "INSERT INTO MATERIA VALUES(?,?,?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			System.out.println(stmt);
			stmt.setInt(1, m.getId());
			stmt.setString(2, m.getNome());
			stmt.setString(3, m.getColore());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			System.out.println("Can not connect to the SQL database!");
			this.setMsg("Can not connect to the SQL database!");
		}
		return false;
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
			e.printStackTrace();
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
						toString += "|";
					toString += rs.getString(i);
				}
				toString += "";
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
