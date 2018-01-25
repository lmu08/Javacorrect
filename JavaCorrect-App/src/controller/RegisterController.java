package controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import db.MysqlRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController
implements Initializable {
	private static final String MAIL_PATTERN = "^[^\\W][a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$";
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
	public void initialize(final URL url, final ResourceBundle resourceBundle) {
		usernameTextField.textProperty().addListener(event -> updateRegisterButton());
		passwordField.textProperty().addListener(event -> updateRegisterButton());
		emailTextField.textProperty().addListener(event -> updateRegisterButton());
		updateRegisterButton();
	}
	
	@FXML
	private void handleRegisterAction() {
		//TODO Create account using username and password (+ send confirmation email ?)
		final String username = usernameTextField.getText();
		final String email = emailTextField.getText();
		final String password = passwordField.getText();
		if (!Pattern.matches(MAIL_PATTERN, email)) {
			showAlert(AlertType.WARNING, "Mail invalide", "Veuillez vérifier votre adresse mail.");
		} else {
			try {
				final ResultSet rsMail = MysqlRequest.getProfesseurByMail(email.toLowerCase());
				if (rsMail.isBeforeFirst()) {
					showAlert(AlertType.WARNING, "Mail invalide", "Cette adresse mail existe déjà.");
				} else {
					final ResultSet rsLogin = MysqlRequest.getProfesseurByLogin(username.toLowerCase());
					if (rsLogin.isBeforeFirst()) {
						showAlert(AlertType.WARNING, "Login invalide", "Ce login existe déjà.");
					} else {
						MysqlRequest.insertProfesseur(username, email, password);
						showAlert(AlertType.INFORMATION, "Inscription réussie", "Le compte a été créé avec succès.");
						windowManager.showLoginView();
					}
				}
			} catch (final SQLException e) {
				e.printStackTrace();
				showAlert(AlertType.ERROR, "Echec de l'inscription", "Une erreur serveur s'est produite lors de la création du compte.");
			}
		}
	}
	
	public void showAlert(final AlertType alertType, final String title, final String message) {
		final Alert alert = new Alert(alertType);
		alert.setHeaderText(title);
		alert.setContentText(message);
		alert.show();
	}
	
	@FXML
	private void handleOpenSignInAction() {
		windowManager.showLoginView();
	}
	
	private void updateRegisterButton() {
		registerButton.setDisable(usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() || emailTextField.getText().isEmpty());
	}
}
