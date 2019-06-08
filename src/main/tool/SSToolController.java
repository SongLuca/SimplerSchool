package main.tool;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.application.Main;
import main.application.models.Config;
import main.utils.Utils;

public class SSToolController {
	private final String NEW_LINE = "\n";
	private final String CHECK = "O";
	private final String CROSS = "X";
	private final int INTERVAL = 500;
	@FXML
	private AnchorPane contentPane;

	@FXML
	private Label dbFolderPath;

	@FXML
	private Button copyBtn;

	@FXML
	private Button startBtn;

	@FXML
	private Button skipBtn;

	@FXML
	private TextArea log;

	@FXML
	private HBox workingBox;

	@FXML
	private HBox idleBox;

	private File dbFolder;

	public void initialize() {
		initComponents();
		dbFolder = (dbFolderPath.getText().isEmpty()) ? null : new File(dbFolderPath.getText());
	}

	public void initComponents() {
		String db = Config.getString(Main.DBINFO, "databaseFolder");
		dbFolder = new File(db.substring(0, db.lastIndexOf("xampp") + 6));
		dbFolderPath.setText(Config.getString(Main.DBINFO, "databaseFolder"));
		log.setWrapText(true);
	}

	@FXML
	public void skip() {
		Stage stage = (Stage) contentPane.getScene().getWindow();
		if (stage.getOwner() != null) {
			stage.close();
		} else {
			stage.close();
			Utils.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
		}

	}

	@FXML
	public void start() {
		Task<Boolean> loginValidateTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				startBtn.setDisable(true);
				skipBtn.setDisable(true);
				idleBox.setVisible(false);
				workingBox.setVisible(true);
				log.appendText("------Start checking------\n");
				return startCheck();
			}
		};

		loginValidateTask.setOnFailed(event -> {
			startBtn.setDisable(false);
			skipBtn.setDisable(false);
			idleBox.setVisible(true);
			workingBox.setVisible(false);
			log.appendText("rip");
			loginValidateTask.getException().printStackTrace();
		});

		loginValidateTask.setOnSucceeded(event -> {
			startBtn.setDisable(false);
			skipBtn.setDisable(false);
			idleBox.setVisible(true);
			workingBox.setVisible(false);
			if (loginValidateTask.getValue()) {
				log.appendText(CHECK + CHECK + "All checked. Ready to go " + CHECK + CHECK + "\n");
			} else {
				log.appendText(CROSS + CROSS + "Problem encounterd. Checking stopped " + CROSS + CROSS + "\n");
			}
			log.appendText("------ End checking ------\n");
		});
		new Thread(loginValidateTask).start();

	}

	public boolean startCheck() throws InterruptedException {
		if (checkIfDBFolderExists()) {
			if (!checkDBFolderStructure()) {
				updateDBInfo();
				return checkDBStructure();
			}
		}
		return false;
	}

	public boolean checkDBStructure() {
		Connection conn = null;
		String query = "";
		boolean valid = true;
		List<dbTable> tables = Arrays.asList(new dbTable("insegna", Arrays.asList("prof_id", "materia_id", "user_id")),
				new dbTable("docente", Arrays.asList("prof_id", "nome", "cognome", "user_id")),
				new dbTable("allegato", Arrays.asList("ALLEGATO_ID", "file_path", "TASK_ID")),
				new dbTable("task",
						Arrays.asList("TASK_ID", "TASK_DATA", "TIPO", "COMMENTO", "voto", "MATERIA_ID", "user_id", "os_id")),
				new dbTable("orariosettimanale", Arrays.asList("os_id", "nome", "file_path", "user_id")),
				new dbTable("utente",
						Arrays.asList("user_id", "username", "nome", "cognome", "pass_hash", "scuola", "avatar_path")),
				new dbTable("materia", Arrays.asList("materia_id", "nome", "color", "user_id", "prof_id", "prof2_id")));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(Config.getString(Main.DBINFO, "databasehost"),
					Config.getString(Main.DBINFO, "usernamesql"), Config.getString(Main.DBINFO, "passwordsql"));

			for (int i = 0; i < tables.size(); i++) {
				query = "show COLUMNS from " + tables.get(i).getNome();
				Statement stmt = conn.createStatement();
				ResultSet rs = null;
				try {
					rs = stmt.executeQuery(query);
					ArrayList<String> colonne = new ArrayList<String>();
					while (rs.next()) {
						colonne.add(rs.getString(1));
					}
					List<String> result = tables.get(i).getColonne().stream().filter(aObject -> {
						return !colonne.contains(aObject);
					}).collect(Collectors.toList());

					if (result.size() == 0)
						log.appendText(CHECK + "Table " + tables.get(i).getNome() + NEW_LINE);
					else {
						log.appendText(
								CROSS + "Table " + tables.get(i).getNome() + " missing column " + result + NEW_LINE);
						valid = false;
					}

				} catch (SQLException e) {
					log.appendText(CROSS + "SQL error: " + e.getMessage() + NEW_LINE);
					valid = false;
				}
				Thread.sleep(INTERVAL);
			}
			conn.close();
			if (valid) {
				log.appendText(CHECK + "Database structure" + NEW_LINE);
			} else {
				log.appendText(CROSS + "Database structure is invalid! "
						+ "Please recreate database using simpler_school.sql in the project folder" + NEW_LINE);
			}
		} catch (SQLException se) {
			int count = 1;
			while (se != null) {
				log.appendText("SQLException " + count + NEW_LINE);
				log.appendText("Code: " + se.getErrorCode() + NEW_LINE);
				log.appendText("SqlState: " + se.getSQLState() + NEW_LINE);
				log.appendText("Error Message: " + se.getMessage() + NEW_LINE);
				se = se.getNextException();
				count++;
			}
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public void updateDBInfo() {
		String completePath = dbFolder.getAbsolutePath() + "/htdocs/Simpler_School";
		completePath = completePath.replace("\\", "/");
		Config.databaseinfo.setProperty("databaseFolder", completePath);
		Utils.saveDBInfoProperties(true);
		log.appendText(CHECK + "databaseinfo.properties updated" + NEW_LINE);
	}

	public boolean checkIfDBFolderExists() {
		if (!dbFolderPath.getText().contains("xampp")) {
			log.appendText(CROSS + "Invalid folder path (must be the xampp root path)\n");
			return false;
		} else {
			String db = dbFolderPath.getText();
			dbFolder = new File(db.substring(0, db.lastIndexOf("xampp") + 5));
			if (dbFolder.exists())
				log.appendText(CHECK + "Xampp folder" + NEW_LINE);
			else {
				log.appendText(CROSS + "Folder does not exist" + NEW_LINE);
				return false;
			}
		}
		return true;
	}

	public boolean checkDBFolderStructure() {
		File defaultFolder, userFolder, defaultAvatar, dbDefaultAvatar;
		boolean change = false;
		try {
			defaultFolder = new File(dbFolder.getAbsolutePath() + "/htdocs/Simpler_School/default");
			userFolder = new File(dbFolder.getAbsolutePath() + "/htdocs/Simpler_School/users");
			defaultAvatar = new File(getClass().getResource("defaultAvatar.jpg").toURI());
			dbDefaultAvatar = new File(dbFolder.getAbsolutePath() + "/htdocs/Simpler_School/default/defaultAvatar.jpg");
			if (!userFolder.exists()) {
				userFolder.mkdirs();
				log.appendText(CHECK + "default folder missing. Created new one." + NEW_LINE);
				change = true;
			} else
				log.appendText(CHECK + "default folder" + NEW_LINE);
			if (!defaultFolder.exists()) {
				defaultFolder.mkdirs();
				log.appendText(CHECK + "user folder missing. Created new one." + NEW_LINE);
				change = true;
			} else
				log.appendText(CHECK + "user folder" + NEW_LINE);

			if (!dbDefaultAvatar.exists()) {
				FileUtils.copyFile(defaultAvatar, dbDefaultAvatar);
				log.appendText(CHECK + "defaultAvatar.jpg missing. Created new one." + NEW_LINE);
				change = true;
			} else
				log.appendText(CHECK + "defaultAvatar.jpg" + NEW_LINE);
			log.appendText(CHECK + "Database folder structure" + NEW_LINE);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return change;
	}

	@FXML
	public void editDBPath() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose a directory to save the file");
		File selectedDirectory = chooser.showDialog(contentPane.getScene().getWindow());
		if (selectedDirectory != null) {
			dbFolderPath.setText(selectedDirectory.getAbsolutePath());
		}
	}

	@FXML
	public void copyDBPath(MouseEvent event) {
		if (!dbFolderPath.getText().isEmpty()) {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(dbFolderPath.getText());
			clipboard.setContent(content);
		}
	}

	class dbTable {
		private String nome;
		private List<String> colonne;

		dbTable(String nome, List<String> colonne) {
			this.nome = nome;
			this.colonne = colonne;
		}

		String getNome() {
			return nome;
		}

		List<String> getColonne() {
			return colonne;
		}
	}
}
