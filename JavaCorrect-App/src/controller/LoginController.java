package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.WindowManager;
import db.MysqlConnexion;
import db.MysqlPropertiesParser;
import db.MysqlRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tools.EncryptingTools;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Hyperlink createAccountLink;
	private MysqlPropertiesParser properties;
	private WindowManager windowManager;
	private Connection mysqlco;
	
	public LoginController() throws ClassNotFoundException{
		this.properties = MysqlPropertiesParser.getInstance();
		this.mysqlco = MysqlConnexion.getInstance(this.properties);
	}

	public void initManager(final WindowManager windowManager) {
		this.windowManager = windowManager;
	}

	@FXML
	public void handleLoginAction() throws SQLException {
		final String username = usernameTextField.getText().toLowerCase();
		final String password = passwordField.getText();
		boolean failed = false;
		String errorMessage = "";
		//TODO check in DB if ok
		System.out.println(username);
		ResultSet rs = MysqlRequest.getProfesseurByLogin(this.mysqlco, username);
		if(!rs.isBeforeFirst()) {
			failed = true;
			errorMessage = "Login inexistant en base de données";
		}
		else {
			System.out.println(username);
			rs.next();
			String loginDb = rs.getString("loginProfesseur");
			String passwordDb = rs.getString("passwdProfesseur");
			String encryptedPassword = EncryptingTools.clearTextToEncrypted(password, "SHA-256");
			
			if (passwordDb.equals(encryptedPassword) && username.equals(loginDb)) {
				windowManager.showMainView(username);
			} else {
				failed = true;
				errorMessage = "Mot de passe incorrect";
			}
		}
		if(failed) {
			
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("L'authentification a échoué");
			final Label label = new Label(errorMessage);
			label.setWrapText(true);
			alert.getDialogPane().setContent(label);
			alert.showAndWait();
		}
		
		
	}
	
	@FXML
	private void handleOpenCreateAccountAction() {
		windowManager.showRegisterView();
	}

}
