package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProjectNameException extends Exception{
	public ProjectNameException(String message){
		super(message);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Nom de projet");
		alert.setContentText(message);
		alert.show();
		
	}
}
