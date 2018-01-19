package controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class LoginManager {
	private final Stage stage;
	
	public LoginManager(final Stage stage) {
		this.stage = stage;
	}
	
	public void showLoginView() {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
			stage.getScene().setRoot(loader.load());
			stage.sizeToScene();
			final LoginController loginController = (LoginController) loader.getController();
			loginController.initManager(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void showMainView(final String login) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Javacorrect.fxml"));
			stage.getScene().setRoot(loader.load());
			stage.sizeToScene();
			final MainViewController mainViewController = (MainViewController) loader.getController();
			mainViewController.initUser(this, login);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
