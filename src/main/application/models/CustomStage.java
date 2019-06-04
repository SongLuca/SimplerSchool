package main.application.models;

import java.io.File;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.application.Main;
import main.utils.LanguageBundle;
import main.utils.Utils;
import main.utils.WindowStyle;

public class CustomStage{
	private Stage stage;
	private FXMLLoader fxmlLoader;
	
	public CustomStage(Stage owner) throws Exception {
		URL fxmlURL = new File(Config.getString(Main.CONFIG, "customStageFXML")).toURI().toURL();
		fxmlLoader = new FXMLLoader(fxmlURL);
		Parent root = fxmlLoader.load();
		stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initOwner(owner);
		Scene scene = new Scene(root);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		HBox titleBox = (HBox) fxmlLoader.getNamespace().get("titleHBox");
		WindowStyle.allowDrag(titleBox, stage);
		root.requestFocus();
	}
	
	public void setIcon(String imageKey) {
		ImageView icona = (ImageView) fxmlLoader.getNamespace().get("icon");
		String img = Utils.getFileURIByPath(Main.CONFIG, imageKey).toString();
		icona.setImage(new Image(img));
		icona.setFitHeight(25);
		icona.setFitWidth(25);
	}
	
	public void bindTitleLanguage(String key) {
		Label windowTitle = (Label) fxmlLoader.getNamespace().get("title");
		LanguageBundle.labelForValue(windowTitle, ()->LanguageBundle.get(key, 0));
	}
	
	public void setTitle (String title) {
		Label windowTitle = (Label) fxmlLoader.getNamespace().get("title");
		windowTitle.setText(title);
	}
	
	public void setMinSize(double width, double height) {
		stage.setMinWidth(width);
		stage.setMinHeight(height);
	}
	
	public void setSize(double width, double height) {
		stage.setWidth(width);
		stage.setHeight(height);
	}
	
	public void show() {
		stage.show();
	}
	
	public void setResizable(boolean value) {
		stage.setResizable(value);
	}
	
	public String getSize() {
		return "Width: "+stage.getWidth()+" Height: "+stage.getHeight();
	}
	
	public void setModality(Modality modality) {
		stage.initModality(modality);
	}
	
	public void setContent(AnchorPane content) {
		AnchorPane contentPane = (AnchorPane) fxmlLoader.getNamespace().get("content");
		contentPane.getChildren().add(content);
	}

}
