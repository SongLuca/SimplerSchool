package main.controllers;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import animatefx.animation.FadeOutRight;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.application.customGUI.ConfirmDialog;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Effect;
import main.utils.Utils;

public class attivitaBoxController {
	@FXML
	private Label idLbl;

	@FXML
	private Label materiaLbl;

	@FXML
	private JFXTextArea comment;

	@FXML
	private TitledPane titlePane;

	@FXML
	private Pane contentPane;
	
	@FXML
	private JFXSpinner loading;
	
	@FXML
	private TitledPane Box;
	 
	public void initialize() {

	}

	@FXML
	void edit(MouseEvent event) {

	}

	@FXML
	void remove(MouseEvent event) {
		Stage owner = (Stage) contentPane.getScene().getWindow();
		StackPane stack = (StackPane) contentPane.getScene().lookup("#stackPane");
		AnchorPane anchor = (AnchorPane) contentPane.getScene().lookup("#detailsPane");
		ConfirmDialog dialog = new ConfirmDialog(owner, "Are you sure to delete this task?");
		if (dialog.getResult()) {
			SchoolTask task = DataBaseHandler.getInstance().getAttivita(idLbl.getText());
			switch(task.getTipo()) {
				case "Compito":
					deleteTask(task,anchor,stack,"compitiBox");
					break;
				case "Verifica":
					deleteTask(task,anchor,stack,"verificheBox");
					break;
				case "Interrogazione":
					deleteTask(task,anchor,stack,"interrBox");
					break;
				case "Allegato file":
					deleteTask(task,anchor,stack,"allegatoBox");
					break;
				default:
					Utils.popUpDialog(stack, anchor, "Error", "Undefined tipo attivita");
					break;
			}
			
		}
	}

	@FXML
	void showFileList(MouseEvent event) {

	}
	
	public void deleteTask(SchoolTask task, AnchorPane anchor, StackPane stack, String tipoBox) {
		JFXSpinner loading = (JFXSpinner) anchor.getScene().lookup("#loading");
		Task<Boolean> deleteTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				anchor.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().deleteTaskQuery(task);
			}
		};

		deleteTask.setOnFailed(event -> {
			loading.setVisible(false);
			anchor.setDisable(false);
			deleteTask.getException().printStackTrace();
		});

		deleteTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			anchor.setEffect(null);
			if (deleteTask.getValue()) {
				Utils.popUpDialog(stack, anchor, "Msg", "Task deleted!");
				VBox box = (VBox) anchor.getScene().lookup("#"+tipoBox);
				FadeOutRight animation = new FadeOutRight(Box);
				animation.setOnFinished(e->{
					box.getChildren().remove(Box);
					renameCounts(box);
				});
				animation.play();
				
			} else {
				Utils.popUpDialog(stack, anchor, "Error", DataBaseHandler.getInstance().getMsg());
				anchor.setDisable(false);
			}
		});
		
		new Thread(deleteTask).start();
	}
	
	public void renameCounts(VBox contentBox) {
		int count = getTitlePaneCount();
		int i = 1;
		for(Node component : contentBox.getChildren()) {
			if(component instanceof TitledPane) {
				TitledPane pane = (TitledPane) component;
				if(pane.getText().substring(2).equals(""+(count+i))) {
					int currentcount = Integer.parseInt(pane.getText().substring(2));
					pane.setText("N."+(currentcount-1));
					i++;
				}
			}
		}
	}
	
	public int getTitlePaneCount() {
		String number = Box.getText().substring(2);
		return Integer.parseInt(number);
	}
}
