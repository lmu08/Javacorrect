package controller;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.MysqlConnexion;
import db.MysqlPropertiesParser;
import db.MysqlRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import tools.RegexTools;

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
	private MysqlPropertiesParser properties;
	private Connection mysqlco;
	
	public RegisterController(){
		this.properties = MysqlPropertiesParser.getInstance();
		this.mysqlco = MysqlConnexion.getInstance(this.properties);
	}
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
		String username = usernameTextField.getText();
		String email = emailTextField.getText();
		String password = passwordField.getText();
		String errorMessage = "";
		String mailPattern ="^[^\\W][a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$";
		if(!RegexTools.pregMatch(mailPattern, email)) {
			errorMessage = "Adresse mail invalide";
		}
		if(errorMessage.equals("")) {
			try {
				ResultSet rsMail = MysqlRequest.getProfesseurByMail(mysqlco,email.toLowerCase());
				if(rsMail.isBeforeFirst()) {
					errorMessage = "Adresse mail déjà existante";
				}
				ResultSet rsLogin = MysqlRequest.getProfesseurByLogin(mysqlco,username.toLowerCase());
				if(rsLogin.isBeforeFirst()) {
					errorMessage = "Login déjà existant";
				}
				if(errorMessage.equals("")) {
					MysqlRequest.insertProfesseur(mysqlco, username, email, password);
				}
				
			} catch (NoSuchAlgorithmException nsae) {
				// TODO Auto-generated catch block
				nsae.printStackTrace();
			}
			catch(SQLException sqle) {
				sqle.printStackTrace();
				errorMessage ="erreur inconnue au niveau de la base de données";
			}
		}
		if(!errorMessage.equals("")){
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("L'inscription a échoué");
			final Label label = new Label(errorMessage);
			label.setWrapText(true);
			alert.getDialogPane().setContent(label);
			alert.showAndWait();
		}else {
			windowManager.showLoginView();
		}
		
			
		
	}
	
	@FXML
	private void handleOpenSignInAction() {
		windowManager.showLoginView();
	}
	
	private void updateRegisterButton() {
		registerButton.setDisable(usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() || emailTextField.getText().isEmpty());
	}
}
