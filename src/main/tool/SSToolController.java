package main.tool;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import main.application.models.Config;

public class SSToolController {
	private final String NEW_LINE ="\n";
	@FXML
	private AnchorPane contentPane;
	
	@FXML
	private Label dbFolderPath;
	
	@FXML
	private Button copyBtn;
	
	@FXML
	private Button startBtn;
	
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
		String db = Config.getString("config", "databaseFolder");
		dbFolder = new File(db.substring(0,db.lastIndexOf("xampp")+6));
		dbFolderPath.setText(Config.getString("config", "databaseFolder"));
	}
	
	@FXML
	public void startCheck() {
		startBtn.setDisable(true);
		idleBox.setVisible(false);
		workingBox.setVisible(true);
		log.appendText("------Start checking------\n");
		checkIfDBFolderExists();
		checkDBFolderStructure();
		log.appendText("------ End checking ------\n");
		startBtn.setDisable(false);
		idleBox.setVisible(true);
		workingBox.setVisible(false);
	}
	
	public void checkIfDBFolderExists() {
		if(!dbFolderPath.getText().contains("xampp")) {
			log.appendText("Invalid folder path (must be the xampp root path)\n");
		}
		else {
			String db = dbFolderPath.getText();
			dbFolder = new File(db.substring(0,db.lastIndexOf("xampp")+5));
			log.appendText("Xampp folder OK"+NEW_LINE);
		}
	}
	
	public void checkDBFolderStructure() {
		File defaultFolder, userFolder , defaultAvatar, dbDefaultAvatar;
		try {
			defaultFolder = new File(dbFolder.getAbsolutePath()+"/htdocs/Simpler_School/default");
			userFolder = new File(dbFolder.getAbsolutePath()+"/htdocs/Simpler_School/users");			
			defaultAvatar = new File(getClass().getResource("defaultAvatar.jpg").toURI());
			dbDefaultAvatar = new File(dbFolder.getAbsolutePath()+"/htdocs/Simpler_School/default/defaultAvatar.jpg");
			if(!userFolder.exists()) {
				userFolder.mkdirs();
				log.appendText("default folder created"+NEW_LINE);
			}
			else
				log.appendText("default folder OK"+NEW_LINE);
			
			if(!defaultFolder.exists()) {
				defaultFolder.mkdirs();
				log.appendText("user folder created"+NEW_LINE);
			}
			else
				log.appendText("user folder OK"+NEW_LINE);
			
			if(!dbDefaultAvatar.exists()) {
				FileUtils.copyFile(defaultAvatar, dbDefaultAvatar);
				log.appendText("copied defaultAvatar.jpg to default folder"+NEW_LINE);
			}
			else
				log.appendText("defaultAvatar.jpg OK"+NEW_LINE);	
			log.appendText("Database folder structure OK"+NEW_LINE);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void checkDBStructure() {
		
	}
	
	@FXML
	public void editDBPath(){
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose a directory to save the file");
		File selectedDirectory = chooser.showDialog(contentPane.getScene().getWindow());
		if(selectedDirectory != null) {
			dbFolderPath.setText(selectedDirectory.getAbsolutePath());
		}
	}
	
	@FXML
	public void copyDBPath(MouseEvent event) {
		if(!dbFolderPath.getText().isEmpty()) {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(dbFolderPath.getText());
			clipboard.setContent(content);
		}
	}
	
}
