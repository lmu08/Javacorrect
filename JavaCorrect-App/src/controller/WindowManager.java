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

	/**
	 * Shows login view
	 */
	public void showLoginView() {
		stage.setResizable(false);
		loadScene("/ui/Login.fxml", LoginController.class).ifPresent(controller -> controller.initManager(this));
	}
	
	/**
	 * Shows main view
	 * @param login of connected user
	 */
	public void showMainView(final String login) {
		stage.setResizable(true);
		loadScene("/ui/Javacorrect.fxml", MainViewController.class).ifPresent(controller -> controller.initUser(this, login));
	}
	
	/**
	 * Shows register view
	 */
	public void showRegisterView() {
		stage.setResizable(false);
		loadScene("/ui/Register.fxml", RegisterController.class).ifPresent(controller -> controller.initManager(this));
	}
	
	/**
	 * Loads new Scene(JavaFx) 
	 * @param path
	 * @param c
	 * @return
	 */
	private <C> Optional<C> loadScene(final String path, final Class<C> c) {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			stage.getScene().setRoot(loader.load());
			stage.sizeToScene();
			return Optional.ofNullable(loader.getController()).map(c::cast);
		} catch (final IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
