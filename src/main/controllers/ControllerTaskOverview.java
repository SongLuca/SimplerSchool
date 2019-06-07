package main.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.application.models.MetaData;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Effect;
import main.utils.LanguageBundle;
import main.utils.Utils;

public class ControllerTaskOverview {
	@FXML
	private AnchorPane content;

	@FXML
	private JFXTreeTableView<TaskDetail> taskView;

	@FXML
	private JFXSpinner loading;

	@FXML
	private JFXTextField searchField;

	@FXML
	private Label taskCount;

	@FXML
	private JFXComboBox<String> tipoBox;

	private JFXTreeTableColumn<TaskDetail, LocalDate> dateCol;

	private JFXTreeTableColumn<TaskDetail, String> tipoCol, materiaCol, commentoCol, votoCol;

	private String countLblTxt;

	private ArrayList<TaskDetail> taskDetail;

	public void initialize() {
		countLblTxt = LanguageBundle.get("taskViewTitle");
		initComponents();
		getAllInfoFromDb();
	}

	public void initComponents() {
		tipoBox.getItems().addAll("All", LanguageBundle.get("compitiPerCasa"), LanguageBundle.get("verifica"),
				LanguageBundle.get("interrogazione"), LanguageBundle.get("allegatoFile"));
		tipoBox.getSelectionModel().selectFirst();
	}

	public void getAllInfoFromDb() {
		Task<Boolean> getAllInfoTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				content.setEffect(Effect.blur());
				content.setDisable(true);
				return DataBaseHandler.getInstance().getAttivitaQuery();
			}
		};

		getAllInfoTask.setOnFailed(event -> {
			loading.setVisible(false);
			content.setDisable(false);
			getAllInfoTask.getException().printStackTrace();
		});

		getAllInfoTask.setOnSucceeded(event -> {
			StackPane root = (StackPane) content.getScene().lookup("#dialogStack");
			if (getAllInfoTask.getValue()) {
				toTaskDetail();
				initColumns();
				DataBaseHandler.getInstance().getAttivitaSettimanaleQuery(MetaData.cm.getSelectedDate(), false);
			} else {
				Utils.popUpDialog(root, content, "Error", DataBaseHandler.getInstance().getMsg());
			}
			loading.setVisible(false);
			content.setEffect(null);
			content.setDisable(false);
		});

		new Thread(getAllInfoTask).start();
	}

	private <T> void setupCellValueFactory(JFXTreeTableColumn<TaskDetail, T> column,
			Function<TaskDetail, ObservableValue<T>> mapper) {
		column.setCellValueFactory((TreeTableColumn.CellDataFeatures<TaskDetail, T> param) -> {
			if (column.validateValue(param)) {
				return mapper.apply(param.getValue().getValue());
			} else {
				return column.getComputedValue(param);
			}
		});
	}

	public void toTaskDetail() {
		ArrayList<SchoolTask> allAttivita = DataBaseHandler.getInstance().getAttivita();
		taskDetail = new ArrayList<TaskDetail>();
		for (SchoolTask task : allAttivita) {
			Double voto = task.getVoto();
			String votoS = "";
			if (voto == -1)
				votoS = "NaN";
			else
				votoS = task.getVoto() + "";
			String tipoT = task.getTipo();
			if(tipoT.equals("Compiti per casa")) {
				tipoT = LanguageBundle.get("compitiPerCasa");
			} else if (tipoT.equals("Verifica")) {
				tipoT = LanguageBundle.get("verifica");
			} else if (tipoT.equals("Interrogazione")) {
				tipoT = LanguageBundle.get("interrogazione");
			} else if (tipoT.equals("Allegato file")) {
				tipoT = LanguageBundle.get("allegatoFile");
			}
			taskDetail.add(
					new TaskDetail(task.getData(), tipoT, task.getMateriaNome(), task.getComment(), votoS));
		}
	}

	public void initColumns() {
		tipoCol = new JFXTreeTableColumn<>(LanguageBundle.get("tipo"));
		votoCol = new JFXTreeTableColumn<>(LanguageBundle.get("voto"));
		dateCol = new JFXTreeTableColumn<>(LanguageBundle.get("dataLbl"));
		materiaCol = new JFXTreeTableColumn<>(LanguageBundle.get("materia"));
		commentoCol = new JFXTreeTableColumn<>(LanguageBundle.get("commento"));
		tipoCol.setPrefWidth(100);
		votoCol.setPrefWidth(60);
		dateCol.setPrefWidth(100);
		materiaCol.setPrefWidth(100);
		commentoCol.setPrefWidth(195);
		tipoCol.setResizable(false);
		votoCol.setResizable(false);
		dateCol.setResizable(false);
		materiaCol.setResizable(false);
		commentoCol.setResizable(false);
		tipoCol.setStyle("-fx-alignment: CENTER;-fx-text-alignment:justify;");
		votoCol.setStyle("-fx-alignment: CENTER");
		dateCol.setStyle("-fx-alignment: CENTER");
		materiaCol.setStyle("-fx-alignment: CENTER");
		commentoCol.setCellFactory(tc -> {
		    JFXTreeTableCell<TaskDetail, String> cell = new JFXTreeTableCell<>();
		    Tooltip tt = new Tooltip();
		    tt.setMaxWidth(300);
		    tt.setWrapText(true);
		    tt.textProperty().bind(cell.itemProperty());
		    Text text = new Text();
		    cell.setGraphic(text);
		    text.textProperty().bind(cell.itemProperty());
		    Tooltip.install(cell, tt);
		    return cell ;
		});
		tipoCol.setCellFactory(tc -> {
		    JFXTreeTableCell<TaskDetail, String> cell = new JFXTreeTableCell<>();
		    Label text = new Label();
		    cell.setGraphic(text);
		    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
		    text.setWrapText(true);
		    text.setTextAlignment(TextAlignment.CENTER);
		    text.textProperty().bind(cell.itemProperty());
		    return cell ;
		});
		setupCellValueFactory(tipoCol, TaskDetail::tipoProperty);
		setupCellValueFactory(votoCol, TaskDetail::votoProperty);
		setupCellValueFactory(dateCol, TaskDetail::getDataProperty);
		setupCellValueFactory(materiaCol, TaskDetail::materiaProperty);
		setupCellValueFactory(commentoCol, TaskDetail::commentoProperty);

		ObservableList<TaskDetail> fileList = FXCollections.observableArrayList(taskDetail);
		taskView.setRoot(new RecursiveTreeItem<>(fileList, RecursiveTreeObject::getChildren));
		taskView.getColumns().add(dateCol);
		taskView.getColumns().add(tipoCol);
		taskView.getColumns().add(materiaCol);
		taskView.getColumns().add(commentoCol);
		taskView.getColumns().add(votoCol);
		taskView.setShowRoot(false);
		taskCount.textProperty()
				.bind(Bindings.createStringBinding(() -> countLblTxt + " (" + taskView.getCurrentItemsCount() + ")",
						taskView.currentItemsCountProperty()));
		searchField.textProperty().addListener(setupSearchField(taskView));
		tipoBox.valueProperty().addListener(setupTipoBox(taskView));
	}

	public ChangeListener<String> setupTipoBox(final JFXTreeTableView<ControllerTaskOverview.TaskDetail> tableView) {
		return (o, oldVal, newVal) -> tableView.setPredicate(detailProp -> {
			final TaskDetail detail = detailProp.getValue();
			if(newVal.equals("All"))
				return true;
			else
				return detail.tipo.get().contains(newVal);
			
		});
	}
	
	public ChangeListener<String> setupSearchField(final JFXTreeTableView<ControllerTaskOverview.TaskDetail> tableView) {
		return (o, oldVal, newVal) -> tableView.setPredicate(detailProp -> {
			final TaskDetail detail = detailProp.getValue();
			return detail.materia.get().contains(newVal) || detail.commento.get().contains(newVal);
		});
	}

	class TaskDetail extends RecursiveTreeObject<TaskDetail> {
		ObjectProperty<LocalDate> data;
		StringProperty tipo;
		StringProperty materia;
		StringProperty commento;
		StringProperty voto;

		TaskDetail(LocalDate data, String tipo, String materia, String commento, String voto) {
			this.data = new SimpleObjectProperty<>(data);
			this.tipo = new SimpleStringProperty(tipo);
			this.materia = new SimpleStringProperty(materia);
			this.commento = new SimpleStringProperty(commento);
			this.voto = new SimpleStringProperty(voto);
		}

		ObjectProperty<LocalDate> getDataProperty() {
			return data;
		}

		StringProperty tipoProperty() {
			return tipo;
		}

		StringProperty materiaProperty() {
			return materia;
		}

		StringProperty commentoProperty() {
			return commento;
		}

		StringProperty votoProperty() {
			return voto;
		}
	}
}
