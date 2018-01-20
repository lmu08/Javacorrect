package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.MysqlConnexion;
import db.MysqlPropertiesParser;
import db.MysqlRequest;
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
	private final MysqlPropertiesParser properties;
	private WindowManager windowManager;
	private final Connection mysqlco;
	
	public LoginController() {
		this.properties = MysqlPropertiesParser.getInstance();
		this.mysqlco = MysqlConnexion.getInstance(this.properties);
	}

	public void initManager(final WindowManager windowManager) {
		this.windowManager = windowManager;
	}

	@FXML
	public void handleLoginAction() {
		try {
			final String username = usernameTextField.getText().toLowerCase();
			final String password = passwordField.getText();
			boolean failed = false;
			String errorMessage = "";
			//TODO check in DB if ok
			System.out.println(username);
			final ResultSet rs = MysqlRequest.getProfesseurByLogin(this.mysqlco, username);
			if (!rs.isBeforeFirst()) {
				failed = true;
				errorMessage = "Ce login n'existe pas.";
			} else {
				System.out.println(username);
				rs.next();
				final String loginDb = rs.getString("loginProfesseur");
				final String passwordDb = rs.getString("passwdProfesseur");
				final String encryptedPassword = EncryptingTools.clearTextToEncrypted(password, "SHA-256");

				if (passwordDb.equals(encryptedPassword) && username.equals(loginDb)) {
					windowManager.showMainView(username);
				} else {
					failed = true;
					errorMessage = "Ce mot de passe est invalide.";
				}
			}
			if (failed) {
				final Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("L'authentification a échoué");
				final Label label = new Label(errorMessage);
				label.setWrapText(true);
				alert.getDialogPane().setContent(label);
				alert.showAndWait();
			}
		} catch (final SQLException e) {
			// TODO: handle exception
		}
	}
	
	@FXML
	private void handleOpenCreateAccountAction() {
		windowManager.showRegisterView();
	}

}
