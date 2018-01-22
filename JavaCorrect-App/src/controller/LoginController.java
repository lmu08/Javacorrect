package controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.MysqlRequest;
import exceptions.AuthenticationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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
	public void handleLoginAction() throws AuthenticationException {
		try {
			final String username = usernameTextField.getText().toLowerCase();
			final String password = passwordField.getText();
			//TODO check in DB if ok
			System.out.println(username);
			final ResultSet rs = MysqlRequest.getProfesseurByLogin(username);
			if (!rs.isBeforeFirst()) {
				throw new AuthenticationException("Ce login n'existe pas");
			} else {
				System.out.println(username);
				rs.next();
				final String loginDb = rs.getString("loginProfesseur");
				final String passwordDb = rs.getString("passwdProfesseur");
				final String encryptedPassword = EncryptingTools.clearTextToEncrypted(password, "SHA-256");
				
				if (passwordDb.equals(encryptedPassword) && username.equals(loginDb)) {
					windowManager.showMainView(username);
				} else {
					throw new AuthenticationException("Mot de passe invalide");
				}
			}
		} catch (final SQLException e) {
			// TODO: handle exception
			System.out.println(e.getSQLState());
		}
	}

	@FXML
	private void handleOpenCreateAccountAction() {
		windowManager.showRegisterView();
	}
	
}
