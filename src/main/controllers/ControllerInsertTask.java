package main.controllers;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.application.models.Materia;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Effect;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerInsertTask {
	@FXML
	private AnchorPane insertPane;
	
	@FXML
    private StackPane stackPane;

	@FXML
	private JFXSpinner loading;

	@FXML
	private JFXDatePicker datePicker;

	@FXML
	private JFXComboBox<String> tipoBox;
	
    @FXML
    private JFXComboBox<String> materiaBox;
    
	@FXML
	private JFXTextArea commento;

	@FXML
	private JFXListView<File> fileListView;

	@FXML
	private JFXButton removeFilesBtn;

	@FXML
	private JFXButton insertBtn;

	@FXML
	private JFXButton cancelBtn;
	
	HashMap<Integer,Materia> materie;
	
	public void initialize() {
		materie = DataBaseHandler.getInstance().getMaterie();
		initTitleBox();
		initComponents();
	}

	public void initComponents() {
		fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		cancelBtn.setOnAction(e -> {
			cancel();
		});
		
		for(int key : materie.keySet()) {
			materiaBox.getItems().add(materie.get(key).getNome());
		}
		
		tipoBox.getItems().add("Compito");
		tipoBox.getItems().add("Verifica");
		tipoBox.getItems().add("Interrogazione");
		tipoBox.getItems().add("Allegato file");
		tipoBox.getItems().add("Altro");
	}
	
	public void resetFields() {
		tipoBox.getSelectionModel().clearSelection();
		materiaBox.getSelectionModel().clearSelection();
		commento.clear();
		fileListView.getItems().clear();
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
		for (File f : selectedFiles) {
			removeFilesBtn.setDisable(false);
			fileListView.getItems().add(f);
		}
	}

	@FXML
	public void removeFiles() {
		ObservableList<File> selectedItems = fileListView.getSelectionModel().getSelectedItems();
		if (selectedItems.size() > 0) {
			fileListView.getItems().removeAll(selectedItems);
			if (fileListView.getItems().isEmpty()) {
				removeFilesBtn.setDisable(true);
			}
		}
	}
	
	@FXML
	public void insert() {
		SchoolTask task;
		ObservableList<File> selectedItems = fileListView.getItems();
		if (selectedItems.size() > 0) {
			task = new SchoolTask(datePicker.getValue(), tipoBox.getSelectionModel().getSelectedItem(),
					materiaBox.getSelectionModel().getSelectedItem(),commento.getText(), selectedItems);
		} else {
			task = new SchoolTask(datePicker.getValue(), tipoBox.getSelectionModel().getSelectedItem(),
					materiaBox.getSelectionModel().getSelectedItem(),commento.getText());
		}
		insertTask(task); 
	}

	public void insertTask(SchoolTask task) {
		Task<Boolean> insertSTTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				insertPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().insertTaskQuery(task);
			}
		};

		insertSTTask.setOnFailed(event -> {
			loading.setVisible(false);
			insertPane.setDisable(false);
			insertSTTask.getException().printStackTrace();
		});

		insertSTTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			insertPane.setEffect(null);
			if (insertSTTask.getValue()) {
				SimplerSchoolUtil.popUpDialog(stackPane, insertPane, "Message", "New task inserted");
				resetFields();
			} else {
				SimplerSchoolUtil.popUpDialog(stackPane, insertPane, "Error", DataBaseHandler.getInstance().getMsg());
				insertPane.setDisable(false);
			}
		});

		new Thread(insertSTTask).start();
	}
	
	public void cancel() {
		WindowStyle.close((Stage) titleHBox.getScene().getWindow());
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
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleCloseHoverImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});

		titleCloseButton.setOnMouseExited(e -> {
			String img = SimplerSchoolUtil.getFileURIByPath("config", "titleCloseImagePath").toString();
			titleCloseImage.setImage(new Image(img));
		});

		titleCloseButton.setOnMouseClicked(e -> {
			WindowStyle.close((Stage) titleHBox.getScene().getWindow());
		});
	}
	/***********************************************/

}
