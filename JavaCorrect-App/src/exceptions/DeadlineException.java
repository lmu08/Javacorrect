package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DeadlineException extends Exception{

	public DeadlineException(String message){
		super(message);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Date du projet");
		alert.setContentText(message);
		alert.show();
		
	}
}
