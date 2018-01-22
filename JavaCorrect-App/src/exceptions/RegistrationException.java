package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RegistrationException extends Exception {
		public RegistrationException(String message){
			super(message);
			final Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Echec de l'inscription");
			alert.setContentText(message);
			alert.show();
			
		}
}
