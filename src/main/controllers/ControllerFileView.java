package main.controllers;

import java.awt.Desktop;
import java.io.IOException;
import java.util.LinkedHashMap;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.application.models.Allegato;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerFileView {
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXSpinner loading;

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
			String img = Utils.getFileURIByPath("config", "downloadImagePath").toString();
			btnImage.setImage(new Image(img));
			btnImage.setFitWidth(15);
			btnImage.setFitHeight(15);
			btn.setGraphic(btnImage);
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
