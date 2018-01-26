package db;

import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MarkingDbManagementException
extends SQLException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * occurs when there is a problem with interaction with "EVALUATION" table 
	 * @param message
	 * @param cause
	 */
	public MarkingDbManagementException(final String message, final Throwable cause) {
		super(message, cause);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Données liées aux évaluations");
		alert.setContentText(message);
		alert.show();
	}
}
