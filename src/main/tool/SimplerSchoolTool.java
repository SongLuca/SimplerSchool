package main.tool;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.models.Config;

public class SimplerSchoolTool {
	private Stage stage;

	public SimplerSchoolTool() {
		stage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("toolGUI.fxml"));
			Scene scene = new Scene(root);
			stage.setTitle("SimplerSchoolTool");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}

	


}
