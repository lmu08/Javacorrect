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
		try {
			final Parent root = FXMLLoader.load(getClass().getResource("/ui/Javacorrect.fxml"));

			stage.setTitle("Javacorrect");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (final NullPointerException e) {
			System.err.println("The application failed to load (resource not found). Error :\n" + e);
		}

		//		How to access the controller :
		//		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Javacorrect.fxml"));
		//		stage.setScene(new Scene(loader.load()));
		//		final Controller controller = loader.getController();
	}
}
