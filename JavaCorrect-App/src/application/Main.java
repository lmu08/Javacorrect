package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main
extends Application {
	
	public static void main(final String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(final Stage primaryStage)
	throws Exception {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
			final Stage stage = new Stage();
			stage.setTitle("Javacorrect");
			stage.setScene(new Scene(loader.load()));
			final LoginController loginController = (LoginController) loader.getController();
			loginController.setStage(stage);
			stage.showAndWait();
			if (loginController.loginOK()) {
				final Parent parent = FXMLLoader.load(getClass().getResource("/ui/Javacorrect.fxml"));
				primaryStage.setScene(new Scene(parent));
				primaryStage.show();
			}
		} catch (final NullPointerException e) {
			System.err.println("The application failed to load (resource not found). Error :\n" + e);
		}
	}
}
