package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InvalidMailAdressException extends Exception{

	public InvalidMailAdressException(String message){
		super(message);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Adresse mail ");
		alert.setContentText(message);
		alert.show();
	}
}
