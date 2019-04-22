package main.application;
import javafx.application.Application;
import javafx.stage.Stage;
import main.application.models.Config;
import main.application.models.Utente;
import main.utils.SimplerSchoolUtil;

public class Main extends Application {
	public static Utente utente;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage){
			Config.config = SimplerSchoolUtil.readProperties("config");
			Config.userConfig = SimplerSchoolUtil.readProperties("userconfig");			
			
			SimplerSchoolUtil.confirmMsg("Cartella database corrente: " + Config.getString("config", "databaseFolder") + 
					"\n\nAssicurati che stai usando la cartella database del tuo computer (modifica 'databaseFolder' nel config.properties)");		
					
		//	SimplerSchoolUtil.loadWindow("mainFXML", stage, false, "appIconPath", "appName");
			SimplerSchoolUtil.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
		//	SimplerSchoolUtil.loadWindow("materieFXML", null, false, "appIconPath", "Simpler School");
	}

}
