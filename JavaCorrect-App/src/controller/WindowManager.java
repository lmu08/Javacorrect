package controller;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class WindowManager {
	private final Stage stage;
	
	public WindowManager(final Stage stage) {
		this.stage = stage;
	}
	
	public void showLoginView() {
		loadScene("/ui/Login.fxml").map(LoginController.class::cast)
			.ifPresent(controller -> controller.initManager(this));
	}

	public void showMainView(final String login) {
		loadScene("/ui/Javacorrect.fxml").map(MainViewController.class::cast)
			.ifPresent(controller -> controller.initUser(this, login));
	}

	public void showRegisterView() {
		loadScene("/ui/Register.fxml").map(RegisterController.class::cast)
			.ifPresent(controller -> controller.initManager(this));
	}

	private <C> Optional<C> loadScene(final String path) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			stage.getScene().setRoot(loader.load());
			stage.sizeToScene();
			return Optional.of(loader.getController());
		} catch (final IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
