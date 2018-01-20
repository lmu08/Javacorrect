package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	private String login;
	private Stage stage;
	private MysqlPropertiesParser properties;
	private Connection mysqlco;
	
	public LoginController() throws ClassNotFoundException{
		this.properties = MysqlPropertiesParser.getInstance();
		this.mysqlco = MysqlConnexion.getInstance(this.properties);
	}
	public void initialize() {
		// Mandatory initialize method
	}
	
	public void setStage(final Stage stage) {
		this.stage = stage;
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
			String encryptedPassword = ToolsMethods.clearTextToEncrypted(password, "SHA-256");
			
			if (passwordDb.equals(encryptedPassword) && username.equals(loginDb)) {
				login = username;
				stage.hide();
			} else {
				failed = true;
				errorMessage = "Mot de passe incorrect";
			}
		}
		if(!failed) {
			login = username;try {
				Connection myqlco = MysqlConnexion.getInstance(properties);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stage.hide();
		}
		else {
			
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("L'authentification a échoué");
			final Label label = new Label(errorMessage);
			label.setWrapText(true);
			alert.getDialogPane().setContent(label);
			alert.showAndWait();
		}
		
		
	}

	public String getLogin() {
		return login;
	}

}
