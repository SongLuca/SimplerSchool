package main.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableRow;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import main.application.Main;
import main.application.models.Config;
import main.application.models.MetaData;
import main.application.models.SchoolTask;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.LanguageBundle;
import main.utils.Utils;

public class ControllerFilesOverview {
	@FXML
    private JFXSpinner loading;
    @FXML
    private AnchorPane content;
    @FXML
    private JFXTreeTableView<FileDetail> filesTableView;
    @FXML
    private Label fileCount;
    @FXML
    private JFXTextField searchField;
    
    private JFXTreeTableColumn <FileDetail, LocalDate> dateCol;

    private JFXTreeTableColumn <FileDetail, String> subjectCol, fileNameCol;

    private JFXTreeTableColumn<FileDetail, DownloadButton> actionCell;
    
    private String lblText;
    
    private ArrayList<FileDetail> fileDetail;
    
    public void initialize() {
    	lblText=LanguageBundle.get("fileViewTitle");
    	getAllInfoFromDb();
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
				toFileDetail();
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
    
    public void toFileDetail() {
    	ArrayList<SchoolTask> allAttivita = DataBaseHandler.getInstance().getAttivita();
    	fileDetail = new ArrayList<FileDetail>();
    	for(SchoolTask task : allAttivita) {
    		if(task.hasAllegato()) {
    			for(String a : task.getAllegati().keySet()) {
    				String path = task.getAllegati().get(a).getFile().getAbsolutePath();
    				fileDetail.add(new FileDetail(task.getData(), task.getMateriaNome(), FilenameUtils.getName(a), path));
    			}
    		}
    	}
    }
    
    private <T> void setupCellValueFactory(JFXTreeTableColumn<FileDetail, T> column, Function<FileDetail, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<FileDetail, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }
    
    public void initColumns() {
    	dateCol = new JFXTreeTableColumn<>(LanguageBundle.get("dataLbl"));
    	subjectCol = new JFXTreeTableColumn<>(LanguageBundle.get("materia"));
    	fileNameCol = new JFXTreeTableColumn<>(LanguageBundle.get("nomeFile"));
    	actionCell = new JFXTreeTableColumn<>(LanguageBundle.get("download"));
    	dateCol.setPrefWidth(100);
    	subjectCol.setPrefWidth(100);
    	fileNameCol.setPrefWidth(275);
    	actionCell.setPrefWidth(80);
    	dateCol.setResizable(false);
    	subjectCol.setResizable(false);
    	fileNameCol.setResizable(false);
    	actionCell.setResizable(false);
    	actionCell.setSortable(false);
    	fileNameCol.setCellFactory(tc -> {
		    JFXTreeTableCell<FileDetail, String> cell = new JFXTreeTableCell<>();
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
    	subjectCol.setStyle("-fx-alignment: CENTER");
    	dateCol.setStyle("-fx-alignment: CENTER");
    	actionCell.setStyle("-fx-alignment: CENTER");
    	setupCellValueFactory(subjectCol, FileDetail::getSubject);
    	setupCellValueFactory(fileNameCol, FileDetail::getFileName);
    	setupCellValueFactory(dateCol, FileDetail::getDataProperty);
    	setupCellValueFactory(actionCell, FileDetail::getDownloadButtonProperty);
    	
    	ObservableList<FileDetail> fileList = FXCollections.observableArrayList(fileDetail);
    	filesTableView.setRowFactory(tv->{
    		JFXTreeTableRow<FileDetail> row = new JFXTreeTableRow<>();
   		    row.setOnMouseClicked(event -> {
   		        if (!row.isEmpty() && event.getClickCount() == 2) {
					TreeItem<FileDetail> file1 = filesTableView.getSelectionModel().getSelectedItem();
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().open(new File(file1.getValue().getFilePath().get()));
						} catch (IOException ex) {
							// no application registered for PDFs
						}
					}
   		        }
   		    });
   		    return row ;
    	});
    	filesTableView.setRoot(new RecursiveTreeItem<>(fileList, RecursiveTreeObject::getChildren));
    	filesTableView.getColumns().add(dateCol);
    	filesTableView.getColumns().add(subjectCol);
    	filesTableView.getColumns().add(fileNameCol);
    	filesTableView.getColumns().add(actionCell);
    	filesTableView.setShowRoot(false);
    	fileCount.textProperty()
           .bind(Bindings.createStringBinding(() -> lblText+" (" + filesTableView.getCurrentItemsCount() + ")",
        		   filesTableView.currentItemsCountProperty()));
    	searchField.textProperty().addListener(setupSearchField(filesTableView));
    	
    }
    
    public ChangeListener<String> setupSearchField(final JFXTreeTableView<ControllerFilesOverview.FileDetail> tableView) {
        return (o, oldVal, newVal) ->
            tableView.setPredicate(detailProp -> {
                final FileDetail detail = detailProp.getValue();
                return detail.subject.get().contains(newVal)
                		||	detail.fileName.get().contains(newVal);
            });
    }
    
    class FileDetail extends RecursiveTreeObject<FileDetail> {
    	SimpleObjectProperty<DownloadButton> downloadButton;
    	ObjectProperty<LocalDate> data;
    	StringProperty subject;
    	StringProperty fileName;
    	StringProperty filePath;
    	
    	FileDetail(LocalDate data, String subject, String fileName, String filePath) {
    		this.data = new SimpleObjectProperty<>(data);
    		this.subject = new SimpleStringProperty(subject);
    		this.fileName = new SimpleStringProperty(fileName);
    		this.filePath = new SimpleStringProperty(filePath);
    		this.downloadButton = new SimpleObjectProperty<>(new DownloadButton(filePath));
    	}
    	
    	ObjectProperty<DownloadButton> getDownloadButtonProperty() {
			return downloadButton;
		}
    	
		ObjectProperty<LocalDate> getDataProperty() {
			return data;
		}
		
		StringProperty getSubject() {
			return subject;
		}
		
		StringProperty getFilePath() {
			return filePath;
		}

		StringProperty getFileName() {
			return fileName;
		}
    }
    
    class DownloadButton extends JFXButton {
        DownloadButton(String filePath) {
        	super();
        	ImageView btnImage = new ImageView();
        	String img = getClass().getResource(Config.getString(Main.CONFIG, "downloadImagePath")).toExternalForm();
        	btnImage.setImage(new Image(img));
			btnImage.setFitWidth(25);
			btnImage.setFitHeight(25);
			setGraphic(btnImage);
			setTooltip(new Tooltip(LanguageBundle.get("download")));
			setOnMouseClicked(e->{
				DirectoryChooser chooser = new DirectoryChooser();
				chooser.setTitle("Choose a directory to save the file");
				File selectedDirectory = chooser.showDialog(content.getScene().getWindow());
				if(selectedDirectory != null) {
					File targetFile = new File(filePath);
					Console.print("Downloading " + targetFile.getName() + " to dir: " + selectedDirectory.getAbsolutePath(), "fileio");
					try {
						File dest = new File(selectedDirectory.getAbsolutePath()+"/"+targetFile.getName());
						FileUtils.copyFile(targetFile, dest);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Dialog");
						alert.setHeaderText(null);
						alert.setContentText("File saved in "+ dest.getAbsolutePath());
						alert.showAndWait();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
        }
    }
}
