package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

<<<<<<< HEAD
public class Main extends Application {

=======
public class Main
extends Application {
	
>>>>>>> 3113121cfba84831ce2510dbd373c4671e34cceb
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
