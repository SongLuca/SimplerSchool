package main.application;
import java.sql.SQLException;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;
import main.utils.SimplerSchoolUtil;

public class Main extends Application {
	public static Properties prop;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws SQLException{
			prop = SimplerSchoolUtil.readProperties("src/main/resources/config/config.properties");
		//	SimplerSchoolUtil.loadWindow("mainFXML", stage, false, "appIconPath", "appName");
			SimplerSchoolUtil.loadWindow("backgroundLoginFXML", stage, false, "appIconPath", "appName");
			
	}

}