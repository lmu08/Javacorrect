package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController
implements Initializable {
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextField emailTextField;
	@FXML
	private Button registerButton;
	private WindowManager windowManager;
	
	public void initManager(final WindowManager windowManager) {
		this.windowManager = windowManager;
	}

	@Override
	public void initialize(final URL arg0, final ResourceBundle arg1) {
		usernameTextField.textProperty().addListener(event -> updateRegisterButton());
		passwordField.textProperty().addListener(event -> updateRegisterButton());
		emailTextField.textProperty().addListener(event -> updateRegisterButton());
		updateRegisterButton();
	}
	
	@FXML
	private void handleRegisterAction() {
		//TODO Create account using username and password (+ send confirmation email ?)
	}
	
	@FXML
	private void handleOpenSignInAction() {
		windowManager.showLoginView();
	}
	
	private void updateRegisterButton() {
		registerButton.setDisable(usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() || emailTextField.getText().isEmpty());
	}
}
