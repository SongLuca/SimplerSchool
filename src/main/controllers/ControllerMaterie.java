package main.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.FadeIn;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.application.models.Config;
import main.application.models.Materia;
import main.database.DataBaseHandler;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerMaterie {

	@FXML
	private JFXButton addButton;

	@FXML
	private JFXSpinner loading;

	@FXML
	private VBox materieBox;
	
	@FXML
	private HBox subjectBox;
	
	private HashMap<Integer, Materia> materie;
	
	private List<Integer> toRemove;
	
	public void initialize() {
		materie = DataBaseHandler.getInstance().getMaterie();
		System.out.println("opening materie gui");
		initMaterieBox();
		initTitleBox();
	}
	
	public void apply() {
		toRemove = new ArrayList<Integer>();
		for(int key : materie.keySet()) {
			Materia m = materie.get(key);
			HBox materia = (HBox)materieBox.lookup("#materiaBox"+key);  // prende il materiabox a seconda dell id
			if(materia == null) {			// se il tale box non esiste allora vuol dire che e' stato cancellato dall utente
				m.setStato("delete");	
			}
			else {
				JFXTextField nome = (JFXTextField)materia.lookup("#nomeMateria");
				JFXColorPicker colore = (JFXColorPicker)materia.lookup("#coloreMateria");
				String hexColor = SimplerSchoolUtil.toRGBCode(colore.getValue());
				if(nome.getText().trim().equals("") && !m.getStato().equals("insert")) {  // cancella materia con nome vuoto
					m.setStato("delete");
				}
				if(nome.getText().trim().equals("") && m.getStato().equals("insert")) {  // cancella materia con nome vuoto
					toRemove.add(key);
				}
				if(m.getStato().equals("fresh")) {
					if(!m.getNome().equals(nome.getText())) { // se ha modificato il nome materia
						m.setNome(nome.getText());
						m.setStato("update");
					}
					if(!m.getColore().equalsIgnoreCase(hexColor)) {  // se ha modificato il colore materia
						m.setColore(hexColor);
						m.setStato("update");
					}
				}
				if(m.getStato().equals("insert")) {
					m.setNome(nome.getText());
					m.setColore(hexColor);
				}
			}
		}
		for(int i = 0 ; i < toRemove.size() ; i ++) {
			materie.remove(toRemove.get(i));
		}
		materieBox.getChildren().clear();  	// svuota tutti i materiabox
		updateMaterieInDB();
		
	}
	
	public void exit() {
		WindowStyle.close((Stage)materieBox.getScene().getWindow());
	}
	
	@FXML
	public void newMateria() {  // aggiunge un nuovo materiabox con nome = "" e colore = ffffff
		HBox box = loadMateriaBox();
		int id = 100;
		Label idLbl = (Label)box.lookup("#idMateria");
		if(materie.size() != 0) {
			for(int key : materie.keySet()) {
				if(id == key)
					id++;
			}
		}
		materie.put(id, new Materia(id));
		idLbl.setText(""+id);
		box.setId(box.getId()+id);
	}
	
	public void addMateria(Materia m) {  // aggiunge i materiabox a seconda di materia dato passato in parametro
		HBox box = loadMateriaBox();
		box.setId(box.getId()+m.getId());
		JFXTextField nome = (JFXTextField)box.lookup("#nomeMateria");
		JFXColorPicker colore = (JFXColorPicker)box.lookup("#coloreMateria");
		Label idLbl = (Label)box.lookup("#idMateria");
		nome.setText(m.getNome());
		colore.setValue(Color.valueOf(m.getColore()));
		idLbl.setText(""+m.getId());
	}
	
	public void getMaterieFromDB() {   // un attivita in background per rivecere tutte le materie dal db
		Task<Boolean> getMaterieTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				return DataBaseHandler.getInstance().runGetMaterieQuery();
			}
		};

		getMaterieTask.setOnFailed(event -> {
			loading.setVisible(false);
			SimplerSchoolUtil.errorMsg("Failed to get Materie from db");
			getMaterieTask.getException().printStackTrace();
		});

		getMaterieTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			if (getMaterieTask.getValue()) {
				materie = DataBaseHandler.getInstance().getMaterie();
				initMaterieBox();
				System.out.println(materie);
			} else {
				materie = new HashMap<Integer, Materia>();
			}
		});
		new Thread(getMaterieTask).start();
	}
	
	public void updateMaterieInDB() {   // un attivita in background per aggiornare tutte le materie nel db
		Task<Boolean> updateMaterieTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				return DataBaseHandler.getInstance().updateMateriaTable(materie);
			}
		};

		updateMaterieTask.setOnFailed(event -> {
			loading.setVisible(false);
			SimplerSchoolUtil.errorMsg("Failed to get Materie from db");
			updateMaterieTask.getException().printStackTrace();
		});

		updateMaterieTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			if (updateMaterieTask.getValue()) {
				getMaterieFromDB();
			} else {
				System.out.println("Error");
			}
		});
		new Thread(updateMaterieTask).start();
	}
	
	public HBox loadMateriaBox() {  // legge il materiabox dal file fxml
		try {
			URL fxmlURL = new File(Config.getString("config", "materiaBoxFXML")).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(fxmlURL);
			HBox box = fxmlLoader.load();
			materieBox.getChildren().add(box);
			new FadeIn(box).play();
			return box;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void initMaterieBox() {   //aggiunge i materiabox a seconda della collezione delle materie
		for(int key : materie.keySet()) {
			addMateria(materie.get(key));
		}
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
			String img = SimplerSchoolUtil.getFileURI("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseExited(e -> {
			String img = SimplerSchoolUtil.getFileURI("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});
		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) titleHBox.getScene().getWindow());
		});
	}
	/***********************************************/
}
