package main.controllers;

import java.util.LinkedHashMap;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import animatefx.animation.FadeOutRight;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.application.customGUI.ConfirmDialog;
import main.application.models.Allegato;
import main.application.models.MetaData;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.LanguageBundle;
import main.utils.Utils;

public class attivitaBoxController {
	@FXML
	private Label materiaLbl, commentoLbl;

	@FXML
	private Label votoLbl;

	@FXML
	private JFXButton  fileBtn, deleteBtn;

	@FXML
	private JFXButton  editBtn;

	@FXML
	private JFXTextArea comment;

	@FXML
	private Pane contentPane;

	@FXML
	private JFXSpinner loading;

	@FXML
	private TitledPane Box;

	private int idTask;

	public void initialize() {
		initLangBindings();
	}

	public void initLangBindings() {
		materiaLbl.setText(LanguageBundle.get("materia")+": ");
		commentoLbl.setText(LanguageBundle.get("commento")+": ");
		fileBtn.setTooltip(new Tooltip(LanguageBundle.get("fileListTooltip")));
		deleteBtn.setTooltip(new Tooltip(LanguageBundle.get("deleteBtn")));
		editBtn.setTooltip(new Tooltip(LanguageBundle.get("edit")));
	}

	public void setIdTask(int idTask) {
		this.idTask = idTask;
	}

	public void setMateria(String materia) {
		materiaLbl.setText(materiaLbl.getText() + materia);
	}

	public void setCommento(String commento) {
		comment.setText(commento);
	}

	public void setAllInfo(SchoolTask task) {
		materiaLbl.setText(materiaLbl.getText() + task.getMateriaNome());
		comment.setText(task.getComment());
	}

	public void setVoto(String voto) {
		votoLbl.setVisible(true);
		votoLbl.setText(LanguageBundle.get("voto")+": "+ voto);
	}

	public void markAsDone() {
		Box.getStyleClass().add("withScore-titled-pane");
	}

	@FXML
	void edit(MouseEvent e) {
		Console.print("Opening edit task " + idTask + " window", "gui");
		ControllerInsertTask cit = (ControllerInsertTask) Utils.loadWindow("insertTaskFXML",
				(Stage) ((Node) e.getSource()).getScene().getWindow(), false, null, null);
		cit.setTitle(LanguageBundle.get("modificaAttivita"));
		cit.setMode("edit");
		cit.setIdTask(idTask);
		cit.setTaskBoxController(this);
		if (!cit.loadEditTask())
			Console.print("Error! Invalid task id!", "ERROR");
	}

	@FXML
	void remove(MouseEvent event) {
		Stage owner = (Stage) contentPane.getScene().getWindow();
		StackPane stack = (StackPane) contentPane.getScene().lookup("#stackPane");
		AnchorPane anchor = (AnchorPane) contentPane.getScene().lookup("#detailsPane");
		ConfirmDialog dialog = new ConfirmDialog(owner, "deleteConfirmLbl");
		if (dialog.getResult()) {
			SchoolTask task = DataBaseHandler.getInstance().getAttivita(idTask);
			switch (task.getTipo()) {
			case "Compiti per casa":
				deleteTask(task, anchor, stack, "compitiBox");
				break;
			case "Verifica":
				deleteTask(task, anchor, stack, "verificheBox");
				break;
			case "Interrogazione":
				deleteTask(task, anchor, stack, "interrBox");
				break;
			case "Allegato file":
				deleteTask(task, anchor, stack, "allegatoBox");
				break;
			default:
				Utils.popUpDialog(stack, anchor, "Error", "Undefined tipo attivita");
				break;
			}

		}
	}

	@FXML
	void showFileList(MouseEvent e) {
		LinkedHashMap<String, Allegato> allegati = DataBaseHandler.getInstance().getAttivita(idTask).getAllegati();
		if (allegati.isEmpty()) {
			StackPane stack = (StackPane) contentPane.getScene().lookup("#stackPane");
			AnchorPane anchor = (AnchorPane) contentPane.getScene().lookup("#detailsPane");
			Utils.popUpDialog(stack, anchor, "Message", LanguageBundle.get("noFile"));
		} else {
			Console.print("Opening file list view of task " + idTask + " window", "gui");
			ControllerFileView cfv = (ControllerFileView) Utils.loadWindow("fileViewFXML",
					(Stage) ((Node) e.getSource()).getScene().getWindow(), false, null, null);
			cfv.setFileList(allegati);
			cfv.populateListView();
		}

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
				Utils.popUpDialog(stack, anchor, "Msg", LanguageBundle.get("taskDeleteOnSuccess"));
				VBox box = (VBox) anchor.getScene().lookup("#" + tipoBox);
				FadeOutRight animation = new FadeOutRight(Box);
				animation.setOnFinished(e -> {
					box.getChildren().remove(Box);
					renameCounts(box);
				});
				animation.play();
				MetaData.cm.loadNoteBoard();
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
		for (Node component : contentBox.getChildren()) {
			if (component instanceof TitledPane) {
				TitledPane pane = (TitledPane) component;
				if (pane.getText().substring(2).equals("" + (count + i))) {
					int currentcount = Integer.parseInt(pane.getText().substring(2));
					pane.setText("N." + (currentcount - 1));
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
