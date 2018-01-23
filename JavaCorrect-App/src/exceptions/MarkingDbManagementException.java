package exceptions;

import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MarkingDbManagementException
extends SQLException {
	private static final long serialVersionUID = 1L;
	
	public MarkingDbManagementException(final String message, final Throwable cause) {
		super(message, cause);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Données liées aux évaluations");
		alert.setContentText(message);
		alert.show();
	}
}
