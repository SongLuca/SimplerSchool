package main.application;
import javafx.application.Application;
import javafx.stage.Stage;
import main.application.models.Utente;
import main.utils.Console;
import main.utils.Utils;

public class Main extends Application {
	public static Utente utente;
	public static String selectedOsName;
	public final static String CONFIG = "config";
	public final static String DBINFO = "databaseinfo";
	public final static String APPCONFIG = "appconfig";
	public final static String USERCONFIG = "userconfig";
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage){
			Console.print("Initializing application", "app");
			Utils.readConfigProperties();
			Utils.readAppProperties();	
			Utils.readDBInfoProperties();
			Utils.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
		//	Utils.loadWindow("mainFXML", stage, false, "appIconPath", "appName");
		//	Utils.loadWindow("materieFXML", null, false, "appIconPath", "Simpler School");
	}

}
