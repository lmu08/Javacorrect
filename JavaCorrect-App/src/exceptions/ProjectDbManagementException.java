package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProjectDbManagementException extends Exception{
	public ProjectDbManagementException(String message){
		super(message);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Données liées au projet");
		alert.setContentText(message);
		alert.show();
		
	}
}
