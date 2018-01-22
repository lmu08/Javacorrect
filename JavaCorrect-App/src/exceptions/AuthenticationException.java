package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

public class AuthenticationException extends Exception{
	
	public AuthenticationException(String message) {
		super(message);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Echec de l'authentification");
		alert.setContentText(message);
		alert.show();
	}
}
