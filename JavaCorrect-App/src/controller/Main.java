package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(final String[] args) {
		// Thread receiveInputFile = new Thread(new ReceiveInputFileSocket(52112,
		// "/home/flo"));
		// receiveInputFile.start();
		Application.launch(args);
		System.out.println();
	}

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Javacorrect");
		primaryStage.setScene(new Scene(new Pane()));
		primaryStage.show();

		final WindowManager windowManager = new WindowManager(primaryStage);
		windowManager.showLoginView();
	}
}