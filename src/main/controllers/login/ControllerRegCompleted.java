package main.controllers.login;

import java.io.IOException;
import java.net.MalformedURLException;
import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomOut;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.utils.SimplerSchoolUtil;
import main.utils.WindowStyle;

public class ControllerRegCompleted {
	@FXML
	private AnchorPane regCompletedPane;
	
	@FXML
	void openLogin(MouseEvent e1) {
		try {
			AnchorPane login = FXMLLoader.load(SimplerSchoolUtil.getFileURI("config", "loginFXML").toURL());
			WindowStyle.setAnchorPaneConstraints(login, 50, 50, 275, 275);
			login.setVisible(false);
			AnchorPane backgroundLogin = (AnchorPane) ((Node) e1.getSource()).getScene().lookup("#rootPane");
			backgroundLogin.getChildren().add(login);

			ZoomOut fadeOutLeft = new ZoomOut(regCompletedPane);
			fadeOutLeft.setOnFinished(e -> {
				regCompletedPane.setVisible(false);
			});
			new ZoomIn(login).play();
			login.setVisible(true);
			fadeOutLeft.play();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
