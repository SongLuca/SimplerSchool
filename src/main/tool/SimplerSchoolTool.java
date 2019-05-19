package main.tool;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SimplerSchoolTool {
	private Stage stage;

	public SimplerSchoolTool(Stage owner) {
		stage = new Stage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("toolGUI.fxml"));
			Scene scene = new Scene(root);
			if(owner != null)
				stage.initOwner(owner);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setTitle("SimplerSchoolTool");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		
	}

	


}
