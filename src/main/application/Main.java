package main.application;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;
import main.application.models.Utente;
import main.utils.SimplerSchoolUtil;

public class Main extends Application {
	public static Properties prop;
	public static Utente utente;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException{
			prop = SimplerSchoolUtil.readProperties("src/main/resources/config/config.properties");
		//	SimplerSchoolUtil.loadWindow("mainFXML", stage, false, "appIconPath", "appName");
			SimplerSchoolUtil.loadWindow("backgroundLoginFXML", null, false, "appIconPath", "appName");
	}

}