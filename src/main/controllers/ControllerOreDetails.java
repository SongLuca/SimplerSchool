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
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.application.Main;
import main.application.models.Config;
import main.application.models.CustomStage;
import main.application.models.Docente;
import main.application.models.Insegna;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.LanguageBundle;
import main.utils.Utils;
import main.utils.WindowStyle;
import javafx.scene.control.TitledPane;

public class ControllerOreDetails {
	@FXML
	private VBox compitiBox, verificheBox;

	@FXML
	private VBox interrBox, allegatoBox;

	@FXML
	private Tab compitiTab, verificheTab;
	
	@FXML
	private Tab interrTab, allegatiTab;
	
	@FXML
    private JFXButton newTaskBtn, expandBtn;

    @FXML
    private JFXButton collapseBtn, closeBtn;
	
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
		initInfoBox();
		populatePanes();
		initLangBindings();
	}
	
	public void initLangBindings() {
		compitiTab.setText(LanguageBundle.get("compitiPerCasa"));
		verificheTab.setText(LanguageBundle.get("verifica"));
		interrTab.setText(LanguageBundle.get("interrogazione"));
		allegatiTab.setText(LanguageBundle.get("allegatoFile"));
		
		newTaskBtn.setText(LanguageBundle.get("newTask"));
		closeBtn.setText(LanguageBundle.get("close"));
		collapseBtn.setText(LanguageBundle.get("collapseAll"));
		expandBtn.setText(LanguageBundle.get("expandAll"));
	}
	
	public void setMateria(String materia) {
		this.materia = materia;
	}

	public String getMateria() {
		return materia;
	}

	public void setDate(LocalDate data) {
		this.data = data;
	}

	public void reloadAttivita() {
		attivita = DataBaseHandler.getInstance().getAttivita();
	}

	public Materia getMateriaByNome() {
		ArrayList<Materia> materie = DataBaseHandler.getInstance().getMaterie();
		for (Materia m : materie) {
			if (m.getNome().equals(materia))
				return m;
		}
		return null;
	}

	public Docente getDocenteById(int idD) {
		ArrayList<Docente> docenti = DataBaseHandler.getInstance().getDocenti();
		for (Docente d : docenti) {
			if (d.getIdDocente() == idD)
				return d;
		}
		return null;
	}

	public void initInfoBox() {
		ArrayList<Insegna> insegna = DataBaseHandler.getInstance().getInsegna();
		boolean ceDocente = false;
		for (Insegna i : insegna) {
			if (i.getMateriaId() == getMateriaByNome().getId()) {
				Docente d = getDocenteById(i.getProfId());
				Label lbl = new Label(LanguageBundle.get("docente") + ": " + d.getNomeCognome());
				lbl.setPrefHeight(20);
				infoVBox.getChildren().add(lbl);
				ceDocente = true;
			}
		}
		if (!ceDocente) {
			Label lbl = new Label(LanguageBundle.get("noDocente"));
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
					if (task.getTipo().equalsIgnoreCase("Compiti per casa")) {
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
						c.setVoto((task.getVoto() > -1) ? task.getVoto() + "" : LanguageBundle.get("noVoto2"));
						if (task.getVoto() > -1)
							c.markAsDone();
					}

					if (task.getTipo().equalsIgnoreCase("Interrogazione")) {
						interrCount++;
						attivitaBoxController c = loadTaskBox(interrBox, interrCount);
						c.setIdTask(task.getIdTask());
						c.setMateria(task.getMateriaNome());
						c.setCommento(task.getComment());
						c.setVoto((task.getVoto() > -1) ? task.getVoto() + "" : LanguageBundle.get("noVoto2"));
						if (task.getVoto() > -1)
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
			URL fxmlURL = new File(Config.getString(Main.CONFIG, "attivitaBoxFXML")).toURI().toURL();
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
		String tabTitle = tabPane.getSelectionModel().getSelectedItem().getText();
		if(tabTitle.equals(compitiTab.getText()))
			setExpandTitlePanes(false, compitiBox);
		else if(tabTitle.equals(verificheTab.getText()))
			setExpandTitlePanes(false, verificheBox);
		else if(tabTitle.equals(interrTab.getText()))
			setExpandTitlePanes(false, interrBox);
		else if(tabTitle.equals(allegatiTab.getText()))
			setExpandTitlePanes(false, allegatoBox);
		else if(tabTitle.equals(verificheTab.getText()))
			setExpandTitlePanes(false, verificheBox);
		else
			Console.print("Error! undefined tab!", "Error");
	}

	@FXML
	void expandAll(MouseEvent event) {
		String tabTitle = tabPane.getSelectionModel().getSelectedItem().getText();
		if(tabTitle.equals(compitiTab.getText()))
			setExpandTitlePanes(true, compitiBox);
		else if(tabTitle.equals(verificheTab.getText()))
			setExpandTitlePanes(true, verificheBox);
		else if(tabTitle.equals(interrTab.getText()))
			setExpandTitlePanes(true, interrBox);
		else if(tabTitle.equals(allegatiTab.getText()))
			setExpandTitlePanes(true, allegatoBox);
		else if(tabTitle.equals(verificheTab.getText()))
			setExpandTitlePanes(true, verificheBox);
		else
			Console.print("Error! undefined tab!", "Error");
	}

	@FXML
	void newTask(MouseEvent e) {
		Console.print("Opening insert window materia: " + materia, "gui");
		try {
			CustomStage window = new CustomStage((Stage) ((Node) e.getSource()).getScene().getWindow());
			FXMLLoader fxmlLoader = new FXMLLoader(Utils.getFileURIByPath(Main.CONFIG, "insertTaskFXML").toURL());
			AnchorPane contentPane = fxmlLoader.load();
			window.setContent(contentPane);
			window.setSize(Config.getDouble(Main.CONFIG, "minWidthInsertTask"), 
					Config.getDouble(Main.CONFIG, "minHeightInsertTask"));
			window.bindTitleLanguage("insertTaskTitle");
			window.setResizable(false);
			window.setModality(Modality.WINDOW_MODAL);
			window.setIcon("taskImagePath");
			ControllerInsertTask cit = (ControllerInsertTask) fxmlLoader.getController();
			cit.setMode("insert");
			cit.setMateriaBox(materia);
			cit.setDatePicker(data);
			cit.setTipoBox(tabPane.getSelectionModel().getSelectedItem().getText());
			cit.fixedMateria();
			window.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

	@FXML
	public void close() {
		WindowStyle.close((Stage) compitiBox.getScene().getWindow());
	}

}
