package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main
extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage stage)
	throws Exception {
		final Parent root = FXMLLoader.load(getClass().getResource("/resources/Javacorrect.fxml"));

		stage.setTitle("Javacorrect");
		stage.setScene(new Scene(root));
		stage.show();
	}
}
