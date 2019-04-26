package main.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.application.models.Config;
import main.application.models.MetaData;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Utils;
import main.utils.WindowStyle;
import javafx.scene.control.TitledPane;

public class ControllerOreDetails {
	@FXML
	private VBox compitiBox;

	@FXML
	private VBox verificheBox;

	@FXML
	private VBox interrBox;

	@FXML
	private VBox allegatoBox;

	@FXML
	private JFXSpinner loading;

	private ArrayList<SchoolTask> attivita;

	private int compitiCount;

	private int verificheCount;

	private int interrCount;

	private int allegatoCount;

	private String materia;

	public void initialize() {
		attivita = DataBaseHandler.getInstance().getAttivita();
		setMateria(MetaData.materiaSelected);
		initTitleBox();
		populatePanes();
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public void populatePanes() {
		Console.print(attivita.toString(), "");
		if (attivita != null) {
			this.compitiCount = 0;
			this.verificheCount = 0;
			this.interrCount = 0;
			for (SchoolTask task : attivita) {
				if (task.getMateria().equalsIgnoreCase(materia)) {
					if (task.getTipo().equalsIgnoreCase("Compito")) {
						compitiCount++;
						attivitaBoxController c = loadTaskBox(compitiBox, compitiCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateria());
						c.setCommento(task.getComment());
					}

					if (task.getTipo().equalsIgnoreCase("Verifica")) {
						verificheCount++;
						attivitaBoxController c = loadTaskBox(verificheBox, verificheCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateria());
						c.setCommento(task.getComment());
					}

					if (task.getTipo().equalsIgnoreCase("Interrogazione")) {
						interrCount++;
						attivitaBoxController c = loadTaskBox(interrBox, interrCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateria());
						c.setCommento(task.getComment());
					}

					if (task.getTipo().equalsIgnoreCase("Allegato file")) {
						allegatoCount++;
						attivitaBoxController c = loadTaskBox(allegatoBox, allegatoCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateria());
						c.setCommento(task.getComment());
					}
				}
			}
		}
	}

	public attivitaBoxController loadTaskBox(VBox pane, int count) {
		try {
			URL fxmlURL = new File(Config.getString("config", "attivitaBoxFXML")).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			TitledPane content = fxmlLoader.load();
			content.setText("N." + count);
			pane.getChildren().add(content);
			return (attivitaBoxController)fxmlLoader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@FXML
	public void close() {
		WindowStyle.close((Stage) compitiBox.getScene().getWindow());
	}

	/*********** Custom Window title bar ************/
	@FXML
	private HBox titleHBox;

	@FXML
	private Label title;

	@FXML
	private JFXButton titleCloseButton;

	@FXML
	private ImageView titleCloseImage;

	public void initTitleBox() {
		titleCloseButton.setOnMouseEntered(e -> {
			String img = Utils.getFileURIByPath("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = Utils.getFileURIByPath("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) titleHBox.getScene().getWindow());
		});
	}
	/***********************************************/

}
