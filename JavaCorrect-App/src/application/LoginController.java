package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordField;
	private boolean loginOK = false;
	private Stage stage;
	
	public void initialize() {
		// Mandatory initialize method
	}
	
	public void setStage(final Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void handleLoginAction() {
		final String username = usernameTextField.getText();
		final String password = passwordField.getText();
		//TODO check in DB if ok
		if (password.equals("password") && username.equals("login")) {
			loginOK = true;
			stage.hide();
		} else {
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Login failed");
			final Label label = new Label("Unable to login, either username or password is incorrect.");
			label.setWrapText(true);
			alert.getDialogPane().setContent(label);
			alert.showAndWait();
		}
	}

	public boolean loginOK() {
		return loginOK;
	}

}
