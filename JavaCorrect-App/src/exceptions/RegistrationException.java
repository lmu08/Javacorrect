package exceptions;

import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RegistrationException
extends SQLException {
	private static final long serialVersionUID = 1L;
	
	public RegistrationException(final Throwable cause) {
		super(cause);
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Echec de l'inscription");
		alert.setContentText("Ube erreur serveur s'est produite lors de l'inscription.");
		alert.show();
	}
}
