package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextField emailTextField;
	private WindowManager windowManager;

	public void initManager(final WindowManager windowManager) {
		this.windowManager = windowManager;
	}
	
	@FXML
	private void handleRegisterAction() {
		//TODO Create account using username and password (+ send confirmation email ?)
	}

	@FXML
	private void handleOpenSignInAction() {
		windowManager.showLoginView();
	}
}
