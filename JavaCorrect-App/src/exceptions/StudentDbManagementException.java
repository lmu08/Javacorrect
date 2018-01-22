package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class StudentDbManagementException extends Exception{

	public StudentDbManagementException(String message){
		super(message);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Données liées aux étudiants");
		alert.setContentText(message);
		alert.show();
		
	}
}
