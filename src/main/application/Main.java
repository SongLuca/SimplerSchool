package main.application;
import javafx.application.Application;
import javafx.stage.Stage;
import main.application.models.Config;
import main.application.models.Utente;
import main.utils.Console;
import main.utils.DBFolderChecker;
import main.utils.Utils;

public class Main extends Application {
	public static Utente utente;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage){
			Console.print("Initializing application", "app");
			Config.config = Utils.readProperties("config");
			Config.userConfig = Utils.readProperties("userconfig");				
			if(DBFolderChecker.doCheck())		
				Utils.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
			else
				Console.print("Terminated. Database folder not found", "fileio");
		//	Utils.loadWindow("mainFXML", stage, false, "appIconPath", "appName");
		//	Utils.loadWindow("materieFXML", null, false, "appIconPath", "Simpler School");
	}

}
