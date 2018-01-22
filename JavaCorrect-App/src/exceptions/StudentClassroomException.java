package exceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class StudentClassroomException extends Exception {

	public StudentClassroomException(String message){
		super(message);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Classe ou promotion");
		alert.setContentText(message);
		alert.show();
		
	}
}
