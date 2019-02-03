package main.application;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	private Properties prop;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws IOException{
			readProperties();
			Parent root = FXMLLoader.load(new File(prop.getProperty("mainFXML")).toURI().toURL());
	        Scene scene = new Scene(root);
	    
	        stage.setTitle("Simpler School");
	        stage.getIcons().add(new Image(new File(prop.getProperty("iconPath")).toURI().toString()));
	        stage.setMinHeight(Integer.parseInt(prop.getProperty("minHeight")));
	        stage.setMinWidth(Integer.parseInt(prop.getProperty("minWidth")));
	        scene.getStylesheets().add(new File(prop.getProperty("fileCss")).toURI().toURL().toExternalForm());
	        stage.setScene(scene);
	        stage.show();
	}
	
	public void readProperties() {
		prop = new Properties();
		String propFileName = "src/main/resources/config/config.properties";
		FileInputStream fit = null ;
		try{
			fit = new FileInputStream(propFileName);
			prop.load(fit);
		}
		catch (Exception e) {
			System.out.println("Exception: " + e);
		} 
		try {
			fit.close();
		} catch (IOException e) {
			System.out.println("exception caught while closing config.properties stream");
		}
		System.out.println("config.properties loaded");
	}
	
}
