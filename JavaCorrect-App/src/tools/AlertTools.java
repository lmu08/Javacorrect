package tools;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertTools {
	
	/**
	 * makes a warning alert
	 * @param title
	 * @param message
	 */
	public static void showWarning(final String title, final String message) {
		showAlert(AlertType.WARNING, title, message);
	}
	
	/**
	 * make a simple alert
	 * @param alertType
	 * @param title
	 * @param message
	 */
	public static Alert showAlert(final AlertType alertType, final String title, final String message) {
		final Alert alert = new Alert(alertType);
		alert.setHeaderText(title);
		alert.setContentText(message);
		if(!alertType.equals(AlertType.CONFIRMATION)) {
			alert.show();
		}
		return alert;
	}
	
	/**
	 * make a show confirmation alert
	 * @param title
	 * @param message
	 * @return Optional<ButtonType> result that could be use to know the choice of current user
	 */
	public static Optional<ButtonType> showConfirmation(final String title, final String message) {
		Alert alert = showAlert(AlertType.CONFIRMATION, title, message);
		return alert.showAndWait();
	}

}
