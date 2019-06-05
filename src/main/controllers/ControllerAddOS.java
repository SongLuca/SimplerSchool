package main.controllers;

import java.util.HashMap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.application.models.MetaData;
import main.application.models.OrarioSettimanale;
import main.database.DataBaseHandler;
import main.utils.Console;
import main.utils.Effect;
import main.utils.LanguageBundle;
import main.utils.Utils;
import main.utils.WindowStyle;

public class ControllerAddOS {
	@FXML
	private AnchorPane subContentPane;
	@FXML
	private JFXSpinner loading;
	@FXML
	private JFXTextField nomeField;
	@FXML
	private JFXButton applyBtn, closeBtn;
	@FXML
	private Label msgLbl;
	
	private String mode;
	public void initialize() {
		mode = "insert";
		initComponents();
	}

	public void initComponents() {
		msgLbl.setText("");
		nomeField.setPromptText(LanguageBundle.get("insertOSPrompt"));
	}
	
	public JFXTextField getNomeField() {
		return nomeField;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	@FXML
	public void apply(MouseEvent event) {
		String nomeOS = nomeField.getText();
		boolean valido = true;
		if (nomeOS.trim().length() == 0) {
			msgLbl.setText(LanguageBundle.get("insertOSError2"));
		} else {
			HashMap<Integer, OrarioSettimanale> orariS = DataBaseHandler.getInstance().getOS();
			for (int key : orariS.keySet()) {
				if (nomeOS.equals(orariS.get(key).getNomeOrario())) {
					valido = false;
					break;
				}
			}
		}
		if(mode.equals("insert")) {
			if (valido) {
				Console.print("nuovo orario settimanale creato : " + nomeOS, "app");
				OrarioSettimanale os = new OrarioSettimanale(nomeOS);
				os.setStato("insert");
				updateOSTask(os);
				cancel(event);
			} else {
				msgLbl.setText(LanguageBundle.get("insertOSError1"));
			}
		}
		else if(mode.equalsIgnoreCase("edit")){
			OrarioSettimanale os = MetaData.cm.getOs();
			if(nomeOS.equals(os.getNomeOrario()))
				valido = true;
			if (valido) {
				Console.print(os.getNomeOrario() + " rinominato a " + nomeOS, "app");
				if(!nomeOS.equals(os.getNomeOrario())) {
					String old = os.getNomeOrario();
					os.setStato("update");
					os.setNomeOrario(nomeOS);
					MetaData.cm.setCurrOS(os.getId());
					MetaData.cm.updateOSTask(old+" "+LanguageBundle.get("renameDone")+" "+nomeOS, false);
				}
				cancel(event);
			} else {
				msgLbl.setText(LanguageBundle.get("insertOSError1"));
			}
		}
	}

	public void updateOSTask(OrarioSettimanale os) {
		Task<Boolean> updateOSTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				loading.setVisible(true);
				subContentPane.setEffect(Effect.blur());
				return DataBaseHandler.getInstance().updateOSTable(os);
			}
		};

		updateOSTask.setOnFailed(event -> {
			loading.setVisible(false);
			subContentPane.setEffect(null);
			Utils.errorMsg("Failed to insert os in db");
		});

		updateOSTask.setOnSucceeded(event -> {
			loading.setVisible(false);
			subContentPane.setEffect(null);
			if (updateOSTask.getValue()) {
				MetaData.cm.updateOSPicker();
				MetaData.cm.setOSPicker(os.getNomeOrario());
			} else {
				Console.print("Error on updating os table", "db");
			}
		});
		new Thread(updateOSTask).start();
	}

	@FXML
	public void cancel(MouseEvent event) {
		WindowStyle.close((Stage) nomeField.getScene().getWindow());
	}

}
