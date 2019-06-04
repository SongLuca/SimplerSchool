package main.controllers;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.application.models.Allegato;
import main.application.models.Materia;
import main.application.models.MetaData;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.LanguageBundle;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerInsertTask {
	@FXML
	private AnchorPane contentPane;

	@FXML
	private JFXSpinner loading;

	@FXML
	private JFXDatePicker datePicker;

	@FXML
	private JFXComboBox<String> tipoBox, materiaBox;

	@FXML
	private JFXTextArea commento;

	@FXML
	private JFXListView<String> fileListView;

	@FXML
	private JFXButton removeFilesBtn, clearFileBtn, oggiBtn;

	@FXML
	private JFXButton insertBtn, cancelBtn, addFileBtn;

	@FXML
	private Label countLbl, materiaLbl, commentLbl, dragHintLbl;

	@FXML
	private Label tipoLbl, allegareLbl, votoLbl, dataLbl;

	@FXML
	private HBox votoBox;

	@FXML
	private JFXTextField votoField;

	@FXML
	private Pane dragHintPane;

	private ArrayList<Materia> materie;

	private LinkedHashMap<String, Allegato> allegati;

	private String mode;

	private int idTask;

	private SchoolTask editTask;

	private attivitaBoxController boxController;

	private boolean fixedMateria;

	public void initialize() {
		allegati = new LinkedHashMap<String, Allegato>();
		votoBox.setVisible(false);
		this.idTask = 0;
		materie = DataBaseHandler.getInstance().getMaterie();
		fixedMateria = false;
		initComponents();
		initLangBindings();
	}

	public void initLangBindings() {
		removeFilesBtn.setText(LanguageBundle.get("remove"));
		clearFileBtn.setText(LanguageBundle.get("clearBtn"));
		addFileBtn.setText(LanguageBundle.get("add"));
		oggiBtn.setText(LanguageBundle.get("oggi"));
		cancelBtn.setText(LanguageBundle.get("cancel"));
		insertBtn.setText(LanguageBundle.get("insert"));
		tipoLbl.setText(LanguageBundle.get("tipo"));
		allegareLbl.setText(LanguageBundle.get("allegareFile"));
		votoLbl.setText(LanguageBundle.get("voto"));
		materiaLbl.setText(LanguageBundle.get("materia"));
		commentLbl.setText(LanguageBundle.get("commento"));
		dataLbl.setText(LanguageBundle.get("dataLbl"));
		dragHintLbl.setText(LanguageBundle.get("dragHintLbl"));
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
		if (mode.equalsIgnoreCase("edit"))
			insertBtn.setText("Apply");
	}

	public void setIdTask(int idTask) {
		this.idTask = idTask;
	}

	public void setTaskBoxController(attivitaBoxController boxController) {
		this.boxController = boxController;
	}

	public void setMateriaBox(String materia) {
		materiaBox.getSelectionModel().select(materia);
		materiaBox.setDisable(true);
	}

	public void setDatePicker(LocalDate date) {
		datePicker.setValue(date);
		datePicker.setDisable(true);
		oggiBtn.setDisable(true);
	}

	public void setTipoBox(String tipo) {
		tipoBox.getSelectionModel().select(tipo);
	}
	
	public void fixedMateria() {
		fixedMateria = true;
	}

	public boolean loadEditTask() {
		if (idTask != 0) {
			editTask = DataBaseHandler.getInstance().getAttivita(idTask);
			datePicker.setValue(editTask.getData());
			switch(editTask.getTipo()) {
				case "Compiti per casa":
					tipoBox.getSelectionModel().select(LanguageBundle.get("compitiPerCasa"));
					break;
				case "Verifica":
					tipoBox.getSelectionModel().select(LanguageBundle.get("verifica"));
					break;
				case "Interrogazione":
					tipoBox.getSelectionModel().select(LanguageBundle.get("interrogazione"));
					break;
				case "Allegato file":
					tipoBox.getSelectionModel().select(LanguageBundle.get("allegatoFile"));
					break;
			}
			materiaBox.getSelectionModel().select(editTask.getMateriaNome());
			commento.setText(editTask.getComment());
			if (editTask.getTipo().equalsIgnoreCase("Verifica")
					|| editTask.getTipo().equalsIgnoreCase("Interrogazione")) {
				votoBox.setVisible(true);
				if (editTask.getVoto() > -1)
					votoField.setText(editTask.getVoto() + "");
			}

			if (editTask.hasAllegato()) {
				fileListView.getItems().clear();
				removeFilesBtn.setDisable(false);
				clearFileBtn.setDisable(false);
				for (String file : editTask.getAllegati().keySet()) {
					fileListView.getItems().add(file);
					allegati.put(file, editTask.getAllegati().get(file));
				}
			}
			return true;
		}
		return false;
	}

	public void initComponents() {
		fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		fileListView.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				if (event.getDragboard().hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				event.consume();
			}
		});

		fileListView.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					removeFilesBtn.setDisable(false);
					clearFileBtn.setDisable(false);
					for (File f : db.getFiles()) {
						if(!f.isDirectory()) {
							if (!fileListView.getItems().contains(f.getName())) {
								fileListView.getItems().add(f.getName());
								allegati.put(f.getName(), new Allegato(idTask, f));
								dragHintPane.setVisible(false);
							}
						}
					}
					success = true;
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

		cancelBtn.setOnAction(e -> {
			cancel();
		});

		for (Materia m : materie) {
			materiaBox.getItems().add(m.getNome());
		}

		datePicker.setValue(LocalDate.now());

		tipoBox.getItems().addAll(LanguageBundle.get("compitiPerCasa"), LanguageBundle.get("verifica"),
				LanguageBundle.get("interrogazione"), LanguageBundle.get("allegatoFile"));

		tipoBox.setOnAction(e -> {
			if (tipoBox.getSelectionModel().getSelectedItem() != null
					&& (tipoBox.getSelectionModel().getSelectedItem().equals(LanguageBundle.get("verifica")) || tipoBox
							.getSelectionModel().getSelectedItem().equals(LanguageBundle.get("interrogazione")))) {
				votoBox.setVisible(true);
				new FadeIn(votoBox).play();
			} else {
				new FadeOut(votoBox).play();
				votoBox.setVisible(false);
			}
		});

		commento.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				if (commento.getText().length() < 200) {
					countLbl.setText("Charcaters: " + commento.getText().length() + " (max 200)");
					countLbl.setTextFill(Color.GREEN);
				} else {
					countLbl.setText("Charcaters: " + commento.getText().length() + " (max 200)");
					countLbl.setTextFill(Color.RED);
				}

			}
		});

		votoField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					votoField.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}

	public void resetFields() {
		if (fixedMateria) {
			commento.clear();
			fileListView.getItems().clear();
		} else {
			tipoBox.getSelectionModel().clearSelection();
			materiaBox.getSelectionModel().clearSelection();
			commento.clear();
			fileListView.getItems().clear();
		}
	}

	@FXML
	public void setToToday() {
		datePicker.setValue(LocalDate.now());
	}

	@FXML
	public void addFiles() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Allegare file");
		List<File> selectedFiles = fileChooser.showOpenMultipleDialog(tipoBox.getScene().getWindow());
		if (selectedFiles != null) {
			for (File f : selectedFiles) {
				removeFilesBtn.setDisable(false);
				clearFileBtn.setDisable(false);
				if (!fileListView.getItems().contains(f.getName())) {
					fileListView.getItems().add(f.getName());
					allegati.put(f.getName(), new Allegato(idTask, f));
					dragHintPane.setVisible(false);
				}
			}
		}
	}

	@FXML
	public void removeFiles() {
		ObservableList<String> selectedItems = fileListView.getSelectionModel().getSelectedItems();
		if (selectedItems.size() > 0) {
			allegati.keySet().removeAll(selectedItems);
			fileListView.getItems().removeAll(selectedItems);
			if (fileListView.getItems().isEmpty()) {
				removeFilesBtn.setDisable(true);
				clearFileBtn.setDisable(true);
				dragHintPane.setVisible(true);
			}
		}
	}

	@FXML
	public void clear() {
		fileListView.getItems().clear();
		allegati.clear();
		clearFileBtn.setDisable(true);
		removeFilesBtn.setDisable(true);
		dragHintPane.setVisible(true);
	}

	public String getTaskTipo() {
		String tipo = tipoBox.getSelectionModel().getSelectedItem();
		if (tipo.equals(LanguageBundle.get("compitiPerCasa"))) {
			return "Compiti per casa";
		} else if (tipo.equals(LanguageBundle.get("verifica"))) {
			return "Verifica";
		} else if (tipo.equals(LanguageBundle.get("interrogazione"))) {
			return "Interrogazione";
		} else if (tipo.equals(LanguageBundle.get("allegatoFile"))) {
			return "Allegato file";
		}
		return "error";
	}

	@FXML
	public void insert() {
		if (mode.equalsIgnoreCase("insert")) {
			String nomeM = materiaBox.getSelectionModel().getSelectedItem();
			int idMateria = getMateriaIdByName(nomeM);
			if (validateInputs(idMateria)) {
				int voto = ((votoField.getText().length() != 0) ? Integer.parseInt(votoField.getText()) : -1);
				if (fileListView.getItems().size() > 0) {
					insertTask(new SchoolTask(idMateria, datePicker.getValue(), getTaskTipo(), voto, commento.getText(),
							allegati));
				} else {
					insertTask(
							new SchoolTask(idMateria, datePicker.getValue(), getTaskTipo(), voto, commento.getText()));
				}
			}
		}

		else if (mode.contains("edit")) {
			String nomeM = materiaBox.getSelectionModel().getSelectedItem();
			int idMateria = getMateriaIdByName(nomeM);
			if (validateInputs(idMateria)) {
				SchoolTask newTask;
				int voto = ((votoField.getText().length() != 0) ? Integer.parseInt(votoField.getText()) : -1);
				if (fileListView.getItems().size() > 0) {
					newTask = new SchoolTask(idMateria, datePicker.getValue(), getTaskTipo(), voto, commento.getText(),
							allegati);
				} else {
					newTask = new SchoolTask(idMateria, datePicker.getValue(), getTaskTipo(), voto, commento.getText());
				}
				newTask.setIdTask(idTask);
				if (!newTask.equals(editTask)) {
					updateTask(newTask, getAddedAllegati(newTask.getAllegati(), editTask.getAllegati()),
							getRemoveAllegati(newTask.getAllegati(), editTask.getAllegati()));
				}
			}
		}

	}

	public boolean validateInputs(int idMateria) {
		StackPane stackDialog = (StackPane) contentPane.getScene().lookup("#dialogStack");
		if (tipoBox.getSelectionModel().getSelectedItem() == null) {
			Utils.popUpDialog(stackDialog, contentPane, "Error", LanguageBundle.get("insertTaskErrorMsg1"));
			return false;
		}

		if (materiaBox.getSelectionModel().getSelectedItem() == null) {
			Utils.popUpDialog(stackDialog, contentPane, "Error", LanguageBundle.get("insertTaskErrorMsg2"));
			return false;
		}

		if (tipoBox.getSelectionModel().getSelectedItem().equals(LanguageBundle.get("allegatoFile"))
				&& fileListView.getItems().size() == 0) {
			Utils.popUpDialog(stackDialog, contentPane, "Error", LanguageBundle.get("insertTaskErrorMsg3"));
			return false;
		}

		if (commento.getText().length() >= 200) {
			Utils.popUpDialog(stackDialog, contentPane, "Error", LanguageBundle.get("insertTaskErrorMsg4"));
			return false;
		}

		if (!validateDateMateria(idMateria)) {
			Utils.popUpDialog(stackDialog, contentPane, "Error", materiaBox.getValue() + " " + LanguageBundle.get("insertTaskErrorMsg5"));
			return false;
		}

		return true;
	}

	public boolean validateDateMateria(int idMateria) {
		int day = datePicker.getValue().getDayOfWeek().getValue();
		return MetaData.cm.getOs().validateMateriaByGiorno(day - 1, idMateria);
	}

	public int getMateriaIdByName(String nome) {
		for (Materia m : materie) {
			if (m.getNome().equals(nome))
				return m.getId();
		}
		return 0;
	}

	public void insertTask(SchoolTask task) {
		StackPane stackDialog = (StackPane) contentPane.getScene().lookup("#dialogStack");
		Task<Boolean> insertSTTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				contentPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().insertTaskQuery(task, MetaData.cm.getSelectedDate());
			}
		};

		insertSTTask.setOnFailed(event -> {
			loading.setVisible(false);
			contentPane.setDisable(false);
			insertSTTask.getException().printStackTrace();
		});

		insertSTTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			contentPane.setEffect(null);
			if (insertSTTask.getValue()) {
				Utils.popUpDialog(stackDialog, contentPane, LanguageBundle.get("message"),LanguageBundle.get("insertSucc"));
				resetFields();
				if (!fixedMateria) {
					LocalDate data = task.getData();
					if (data.isBefore(MetaData.cm.getSelectedDate().with(DayOfWeek.MONDAY))
							|| data.isAfter(MetaData.cm.getSelectedDate().with(DayOfWeek.SUNDAY))) {
						Console.print("Input task does not belong to this week", "");
					} else {
						Console.print("Input task belongs to this week", "");
						MetaData.cm.loadNoteBoard();
					}
				} else {
					MetaData.cod.reloadAttivita();
					MetaData.cod.populatePanes();
					MetaData.cm.loadNoteBoard();
				}
			} else {
				Utils.popUpDialog(stackDialog, contentPane, "Error", DataBaseHandler.getInstance().getMsg());
				contentPane.setDisable(false);
			}
		});

		new Thread(insertSTTask).start();
	}

	public void updateTask(SchoolTask task, List<Allegato> added, List<Allegato> removed) {
		StackPane stackDialog = (StackPane) contentPane.getScene().lookup("#dialogStack");
		Task<Boolean> updateTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				contentPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().updateTaskQuery(task, added, removed);
			}
		};

		updateTask.setOnFailed(event -> {
			loading.setVisible(false);
			contentPane.setDisable(false);
			updateTask.getException().printStackTrace();
		});

		updateTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			contentPane.setEffect(null);
			if (updateTask.getValue()) {
				Utils.popUpDialog(stackDialog, contentPane, LanguageBundle.get("message"),LanguageBundle.get("updateSucc"));
				loadEditTask();
				MetaData.cod.populatePanes();
				this.boxController.setAllInfo(editTask);
				MetaData.cm.loadNoteBoard();
			} else {
				Utils.popUpDialog(stackDialog, contentPane, "Error", DataBaseHandler.getInstance().getMsg());
				contentPane.setDisable(false);
			}
		});

		new Thread(updateTask).start();
	}

	public List<Allegato> getRemoveAllegati(LinkedHashMap<String, Allegato> newM,
			LinkedHashMap<String, Allegato> oldM) {
		List<Allegato> removed = new LinkedList<Allegato>();
		for (String key : oldM.keySet()) {
			if (!newM.containsKey(key)) {
				removed.add(new Allegato(oldM.get(key).getIdAllegato(), idTask, oldM.get(key).getFile()));
			}

		}
		return removed;
	}

	public List<Allegato> getAddedAllegati(LinkedHashMap<String, Allegato> newM, LinkedHashMap<String, Allegato> oldM) {
		List<Allegato> added = new LinkedList<Allegato>();
		for (String key : newM.keySet()) {
			if (!oldM.containsKey(key)) {
				added.add(new Allegato(idTask, newM.get(key).getFile()));
			}
		}
		return added;
	}

	public void cancel() {
		WindowStyle.close((Stage) contentPane.getScene().getWindow());
	}

}
