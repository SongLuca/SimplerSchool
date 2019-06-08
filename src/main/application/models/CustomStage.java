package main.application.models;
import java.io.IOException;
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
import main.utils.Preferences;
import main.utils.WindowStyle;

public class CustomStage{
	private Stage stage;
	private FXMLLoader fxmlLoader, contentfxmlLoaderm;
	
	public CustomStage(Stage owner) throws Exception {
		fxmlLoader = new FXMLLoader(getClass().getResource(Config.getString(Main.CONFIG, "customStageFXML")));
		Parent root = fxmlLoader.load();
		stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initOwner(owner);
		String cssUrl = getClass().getResource("/main/resources/gui/css/"+Preferences.theme+".css").toExternalForm();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(cssUrl);
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		HBox titleBox = (HBox) fxmlLoader.getNamespace().get("titleHBox");
		WindowStyle.allowDrag(titleBox, stage);
		MetaData.ccs = fxmlLoader.getController();
		root.requestFocus();
	}
	
	public void loadContent(String FXMLKey) {
		try {
			contentfxmlLoaderm = new FXMLLoader(getClass().getResource(Config.getString(Main.CONFIG, FXMLKey)));
			AnchorPane contentPane = contentfxmlLoaderm.load();
			setContent(contentPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public FXMLLoader getContentfxmlLoaderm() {
		return contentfxmlLoaderm;
	}

	public Object getContentController() {
		return contentfxmlLoaderm.getController();
	}
	
	public void setIcon(String imageKey) {
		ImageView icona = (ImageView) fxmlLoader.getNamespace().get("icon");
		String img = getClass().getResource(Config.getString(Main.CONFIG, imageKey)).toExternalForm();
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
	
	public void setTitle (String key, String extra) {
		Label windowTitle = (Label) fxmlLoader.getNamespace().get("title");
		windowTitle.setText(LanguageBundle.get(key)+" "+extra);
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
	
	public Object getComponent(String id) {
		return contentfxmlLoaderm.getNamespace().get(id);
	}
}
