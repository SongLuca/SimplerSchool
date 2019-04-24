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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.application.models.Materia;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.Utils;
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
	
	@FXML
	private Label countLbl;
	
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
		datePicker.setValue(LocalDate.now());
		
		tipoBox.getItems().add("Compito");
		tipoBox.getItems().add("Verifica");
		tipoBox.getItems().add("Interrogazione");
		tipoBox.getItems().add("Allegato file");
		tipoBox.getItems().add("Altro");
		
		commento.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
		    	if(commento.getText().length() < 200) {
		    		countLbl.setText("Charcaters: "+ commento.getText().length() +" (max 200)");
		    		countLbl.setTextFill(Color.GREEN);
		    	}
		    	else {
		    		countLbl.setText("Charcaters: "+ commento.getText().length() +" (max 200)");
		    		countLbl.setTextFill(Color.RED);
		    	}
		    	
		    }
		});
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
		if(validateInputs()) {
			if (fileListView.getItems().size() > 0) {
				insertTask(new SchoolTask(datePicker.getValue(), tipoBox.getSelectionModel().getSelectedItem(),
						materiaBox.getSelectionModel().getSelectedItem(),commento.getText(), fileListView.getItems()));
			} else {
				insertTask(new SchoolTask(datePicker.getValue(), tipoBox.getSelectionModel().getSelectedItem(),
						materiaBox.getSelectionModel().getSelectedItem(),commento.getText()));
			}
		}
	}
	
	public boolean validateInputs() {
		if(tipoBox.getSelectionModel().getSelectedItem() == null) {
			Utils.popUpDialog(stackPane, insertPane, "Error", "Sceglie il tipo dell'attivita!");
			return false;
		}
		
		if(materiaBox.getSelectionModel().getSelectedItem() == null) {
			Utils.popUpDialog(stackPane, insertPane, "Error", "Sceglie la materia dell'attivita!");
			return false;
		}
		
		if(tipoBox.getSelectionModel().getSelectedItem().equals("Allegato file") &&
				fileListView.getItems().size() == 0) {
			Utils.popUpDialog(stackPane, insertPane, "Error", "Inserire almeno un file!");
			return false;
		}
		Console.print(""+commento.getText().length(), "");
		if(commento.getText().length() >= 200) {
			Utils.popUpDialog(stackPane, insertPane, "Error", "Il commento e' troppo lungo!");
			return false;
		}
		
		return true;
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
				Utils.popUpDialog(stackPane, insertPane, "Message", "New task inserted");
				resetFields();
			} else {
				Utils.popUpDialog(stackPane, insertPane, "Error", DataBaseHandler.getInstance().getMsg());
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