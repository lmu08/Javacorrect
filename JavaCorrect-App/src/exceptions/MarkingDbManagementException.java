package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MarkingDbManagementException extends Exception{
	public MarkingDbManagementException(String message){
		super(message);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Données liées aux évaluations");
		alert.setContentText(message);
		alert.show();
		
	}
}
