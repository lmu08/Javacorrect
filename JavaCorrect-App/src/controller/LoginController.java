package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.MysqlRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tools.EncryptingTools;

public class LoginController {
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Hyperlink createAccountLink;
	private WindowManager windowManager;
	
	public void initManager(final WindowManager windowManager) {
		this.windowManager = windowManager;
	}

	@FXML
	public void handleLoginAction() {
		try {
			final String username = usernameTextField.getText().toLowerCase();
			final String password = passwordField.getText();
			final ResultSet rs = MysqlRequest.getProfesseurByLogin(username);
			if (!rs.isBeforeFirst()) {
				final Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Echec de l'authentification");
				alert.setContentText("Ce login n'existe pas");
				alert.show();
			} else {
				rs.next();
				final String loginDb = rs.getString("loginProfesseur");
				final String passwordDb = rs.getString("passwdProfesseur");
				final String encryptedPassword = EncryptingTools.clearTextToEncrypted(password, "SHA-256");
				if (passwordDb.equals(encryptedPassword) && username.equals(loginDb)) {
					windowManager.showMainView(username);
				} else {
					final Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Echec de l'authentification");
					alert.setContentText("Mot de passe invalide");
					alert.show();
				}
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			System.out.println(e.getSQLState());
		}
	}
	
	@FXML
	private void handleOpenCreateAccountAction() {
		windowManager.showRegisterView();
	}

}
