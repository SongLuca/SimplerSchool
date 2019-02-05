package main.application;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Main extends Application {
	public static Properties prop;
	public static Stage primaryStage;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws IOException{
			Main.primaryStage = stage;
			readProperties();
			Parent root = FXMLLoader.load(new File(prop.getProperty("mainFXML")).toURI().toURL());
			primaryStage.setTitle("Simpler School");
			primaryStage.getIcons().add(new Image(new File(prop.getProperty("appIconPath")).toURI().toString()));
			primaryStage.setMinHeight(Integer.parseInt(prop.getProperty("minHeightMain")));
			primaryStage.setMinWidth(Integer.parseInt(prop.getProperty("minWidthMain")));
	        //scene.getStylesheets().add(new File(prop.getProperty("fileCss")).toURI().toURL().toExternalForm());
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
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