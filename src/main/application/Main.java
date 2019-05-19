package main.application;
import javafx.application.Application;
import javafx.stage.Stage;
import main.application.models.Config;
import main.application.models.Utente;
import main.tool.SimplerSchoolTool;
import main.utils.Console;
import main.utils.DBFolderChecker;
import main.utils.Utils;

public class Main extends Application {
	public static Utente utente;
	public final static String CONFIG = "config";
	public final static String DBINFO = "databaseinfo";
	public final static String USERCONFIG = "userconfig";
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage){
			Console.print("Initializing application", "app");
			Config.config = Utils.readProperties(CONFIG);
			Config.userConfig = Utils.readProperties(USERCONFIG);	
			Config.databaseinfo = Utils.readProperties(DBINFO);
			new SimplerSchoolTool();
			//Utils.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
		/*	if(DBFolderChecker.doCheck())		
				Utils.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
			else
				Console.print("Terminated. Database folder not found", "fileio");*/
		//	Utils.loadWindow("mainFXML", stage, false, "appIconPath", "appName");
		//	Utils.loadWindow("materieFXML", null, false, "appIconPath", "Simpler School");
	}

}
