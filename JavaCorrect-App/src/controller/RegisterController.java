package controller;

import java.awt.Button;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.MysqlConnexion;
import db.MysqlPropertiesParser;
import db.MysqlRequest;
import exceptions.InvalidMailAdressException;
import exceptions.RegistrationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
	private void handleRegisterAction() throws RegistrationException, InvalidMailAdressException {
		//TODO Create account using username and password (+ send confirmation email ?)
		String username = usernameTextField.getText();
		String email = emailTextField.getText();
		String password = passwordField.getText();
		String mailPattern ="^[^\\W][a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$";
		if(!RegexTools.pregMatch(mailPattern, email)) {
			throw new InvalidMailAdressException("vérifiez votre adresse mail");
		}
			try {
				ResultSet rsMail = MysqlRequest.getProfesseurByMail(email.toLowerCase());
				if(rsMail.isBeforeFirst()) {
					throw new InvalidMailAdressException("Adresse mail déjà existante");
				}
				ResultSet rsLogin = MysqlRequest.getProfesseurByLogin(username.toLowerCase());
				if(rsLogin.isBeforeFirst()) {
					throw new RegistrationException("Login déjà existant");
				}
				MysqlRequest.insertProfesseur(username, email, password);
				
			} catch (NoSuchAlgorithmException nsae) {
				// TODO Auto-generated catch block
				nsae.printStackTrace();
			}
			catch(SQLException sqle) {
				sqle.printStackTrace();
				throw new RegistrationException("erreur inconnue au niveau de la base de données");
			}
			windowManager.showLoginView();

	}
	
	@FXML
	private void handleOpenSignInAction() {
		windowManager.showLoginView();
	}
	
	private void updateRegisterButton() {
		registerButton.setDisable(usernameTextField.getText().isEmpty() || passwordField.getText().isEmpty() || emailTextField.getText().isEmpty());
	}
}
