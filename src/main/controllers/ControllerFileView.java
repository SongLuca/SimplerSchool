package main.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import org.apache.commons.io.FileUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.application.Main;
import main.application.models.Allegato;
import main.application.models.Config;
import main.utils.Console;
import main.utils.LanguageBundle;
import main.utils.WindowStyle;

public class ControllerFileView {
    @FXML
    private Label hintLbl, title;
    
    @FXML
    private JFXButton closeBtn;
    
	@FXML
	private JFXListView<String> fileListView;

	LinkedHashMap<String, Allegato> allegati;

	public void initialize() {
		fileListView.setCellFactory(param -> new customCell());
		fileListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click) {
				if (click.getClickCount() == 2) {
					String file = fileListView.getSelectionModel().getSelectedItem();
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().open(allegati.get(file).getFile());
						} catch (IOException ex) {
							// no application registered for PDFs
						}
					}
				}
			}
		});
		initLangBindings();
	}
	
	public void initLangBindings() {
		hintLbl.setText(LanguageBundle.get("fileViewHint"));
		closeBtn.setText(LanguageBundle.get("close"));
	}
	
	public void populateListView() {
		for (String key : allegati.keySet()) {
			fileListView.getItems().add(key);
		}
	}

	public void setFileList(LinkedHashMap<String, Allegato> allegati) {
		this.allegati = allegati;
	}

	@FXML
	public void close() {
		WindowStyle.close((Stage) fileListView.getScene().getWindow());
	}

	class customCell extends ListCell<String> {
		HBox hbox = new HBox();
		JFXButton btn = new JFXButton("");
		Pane pane = new Pane();
		Label label = new Label("");
		ImageView btnImage = new ImageView();
		
		public customCell() {
			String img = getClass().getResource(Config.getString(Main.CONFIG, "downloadImagePath")).getFile();
			btnImage.setImage(new Image(img));
			btnImage.setFitWidth(15);
			btnImage.setFitHeight(15);
			btn.setGraphic(btnImage);
			btn.setTooltip(new Tooltip(LanguageBundle.get("download")));
			btn.setOnMouseClicked(e->{
				DirectoryChooser chooser = new DirectoryChooser();
				chooser.setTitle("Choose a directory to save the file");
				File selectedDirectory = chooser.showDialog(hbox.getScene().getWindow());
				if(selectedDirectory != null) {
					File targetFile = allegati.get(label.getText()).getFile();
					Console.print("Downloading " + targetFile.getName() + " to dir: " + selectedDirectory.getAbsolutePath(), "fileio");
					try {
						File dest = new File(selectedDirectory.getAbsolutePath()+"/"+label.getText());
						FileUtils.copyFile(targetFile, dest);
						Console.print("File saved in "+ dest.getAbsolutePath(), "fileio");
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			hbox.getChildren().addAll(label, pane, btn);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}

		@Override
		protected void updateItem(String text, boolean empty) {
			super.updateItem(text, empty);
			setText(null); // No text in label of super class
			if (empty) {
				setGraphic(null);
			} else {
				label.setText(text != null ? text : "<null>");
				setGraphic(hbox);
			}
		}
	}
}
