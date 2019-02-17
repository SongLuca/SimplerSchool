package main.application;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.stage.Stage;
import main.application.models.Config;
import main.application.models.OrarioSettimanale;
import main.application.models.Utente;
import main.utils.SimplerSchoolUtil;

public class Main extends Application {
	public static Utente utente;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException{
			new OrarioSettimanale();
			Config.config = SimplerSchoolUtil.readProperties("config");
			Config.userConfig = SimplerSchoolUtil.readProperties("userconfig");
		//	SimplerSchoolUtil.loadWindow("mainFXML", stage, false, "appIconPath", "appName");
			SimplerSchoolUtil.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "Simpler School");
			
		//	SimplerSchoolUtil.loadWindow("materieFXML", null, false, "appIconPath", "Simpler School");
	}

}