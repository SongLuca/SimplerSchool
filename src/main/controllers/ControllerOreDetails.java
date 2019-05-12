package main.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTabPane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.application.models.Config;
import main.application.models.Docente;
import main.application.models.Insegna;
import main.application.models.Materia;
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
	
	@FXML
    private JFXTabPane tabPane;
	
	@FXML
    private VBox infoVBox;

	private ArrayList<SchoolTask> attivita;

	private int compitiCount;

	private int verificheCount;

	private int interrCount;

	private int allegatoCount;

	private String materia;
	
	private LocalDate data;
	
	public void initialize() {
		MetaData.cod = this;
		setMateria(MetaData.materiaSelected);
		initTitleBox();
		initInfoBox();
		populatePanes();
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}
	
	public String getMateria() {
		return materia;
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	public void setDate(LocalDate data) {
		this.data = data;
	}
	
	public void reloadAttivita() {
		attivita = DataBaseHandler.getInstance().getAttivita();
	}
	
	public Materia getMateriaByNome() {
		ArrayList<Materia> materie = DataBaseHandler.getInstance().getMaterie();
		for(Materia m : materie) {
			if(m.getNome().equals(materia))
				return m;
		}
		return null;
	}
	
	public Docente getDocenteById(int idD) {
		ArrayList<Docente> docenti = DataBaseHandler.getInstance().getDocenti();
		for(Docente d : docenti) {
			if(d.getIdDocente() == idD)
				return d;
		}
		return null;
	}
	
	public void initInfoBox() {
		ArrayList<Insegna> insegna = DataBaseHandler.getInstance().getInsegna();
		boolean ceDocente = false;
		for(Insegna i : insegna) {
			if(i.getMateriaId() == getMateriaByNome().getId()) {
				Docente d = getDocenteById(i.getProfId());
				Label lbl = new Label("Docente: " + d.getNomeCognome());
				lbl.setPrefHeight(20);
				infoVBox.getChildren().add(lbl);
				ceDocente = true;
			}
		}
		if(!ceDocente) {
			Label lbl = new Label("Non ce nessun docente");
			lbl.setPrefHeight(20);
			infoVBox.getChildren().add(lbl);
		}
	}
	
	public void populatePanes() {
		attivita = DataBaseHandler.getInstance().getAttivita();
		if (attivita != null) {
			Console.print("Populating details pane", "gui");
			this.compitiCount = 0;
			this.verificheCount = 0;
			this.interrCount = 0;
			compitiBox.getChildren().clear();
			verificheBox.getChildren().clear();
			interrBox.getChildren().clear();
			allegatoBox.getChildren().clear();
			for (SchoolTask task : attivita) {
				if (task.getMateriaNome().equalsIgnoreCase(materia)) {
					if (task.getTipo().equalsIgnoreCase("Compito")) {
						compitiCount++;
						attivitaBoxController c = loadTaskBox(compitiBox, compitiCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateriaNome());
						c.setCommento(task.getComment());
					}

					if (task.getTipo().equalsIgnoreCase("Verifica")) {
						verificheCount++;
						attivitaBoxController c = loadTaskBox(verificheBox, verificheCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateriaNome());
						c.setCommento(task.getComment());
						c.setVoto((task.getVoto()>-1) ? task.getVoto()+"" : "Nessun voto");
						if(task.getVoto()>-1)
							c.markAsDone();
					}

					if (task.getTipo().equalsIgnoreCase("Interrogazione")) {
						interrCount++;
						attivitaBoxController c = loadTaskBox(interrBox, interrCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateriaNome());
						c.setCommento(task.getComment());
						c.setVoto((task.getVoto()>-1) ? task.getVoto()+"" : "Nessun voto");
						if(task.getVoto()>-1)
							c.markAsDone();
					}

					if (task.getTipo().equalsIgnoreCase("Allegato file")) {
						allegatoCount++;
						attivitaBoxController c = loadTaskBox(allegatoBox, allegatoCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateriaNome());
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
			return (attivitaBoxController) fxmlLoader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setExpandTitlePanes(boolean expand, VBox box) {
		for (Node component : box.getChildren()) {
			if (component instanceof TitledPane) {
				TitledPane pane = (TitledPane) component;
				pane.setExpanded(expand);
			}
		}
	}
	
	@FXML
	void collapseAll(MouseEvent event) {
		switch(tabPane.getSelectionModel().getSelectedItem().getText()) {
			case "Compiti":
				setExpandTitlePanes(false,compitiBox);
				break;
			case "Verifiche":
				setExpandTitlePanes(false,verificheBox);
				break;
			case "Interrogazioni":
				setExpandTitlePanes(false,interrBox);
				break;
			case "Allegato file":
				setExpandTitlePanes(false,allegatoBox);
				break;
			default:
				Console.print("Error! undefined tab!", "Error");
				break;
		}
	}
		
	@FXML
	void expandAll(MouseEvent event) {
		switch(tabPane.getSelectionModel().getSelectedItem().getText()) {
		case "Compiti":
			setExpandTitlePanes(true,compitiBox);
			break;
		case "Verifiche":
			setExpandTitlePanes(true,verificheBox);
			break;
		case "Interrogazioni":
			setExpandTitlePanes(true,interrBox);
			break;
		case "Allegato file":
			setExpandTitlePanes(true,allegatoBox);
			break;
		default:
			Console.print("Error! undefined tab!", "Error");
			break;
		}
	}

	@FXML
	void newTask(MouseEvent e) {
		Console.print("Opening insert window materia: " + materia, "gui");
		ControllerInsertTask cit = (ControllerInsertTask)Utils.loadWindow("insertTaskFXML", 
				(Stage) ((Node) e.getSource()).getScene().getWindow(), false, null, null);
		cit.setTitle("Inserimento attivita");
		cit.setMode("insert");
		cit.setMateriaBox(materia);
		cit.setDatePicker(data);
		cit.fixedMateria();
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
