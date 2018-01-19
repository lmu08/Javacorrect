package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main
extends Application {

	public static void main(final String[] args) {
		//    MysqlPropertiesParser properties = new MysqlPropertiesParser();
		//		Connection myqlco = MysqlConnexion.getInstance(properties);
		//			MysqlRequest.getProject(myqlco, projectId);
		////			new StudentCsvParser("/home/flo/Documents/JavaCorrect/Javacorrect/studentTest.csv");
		//			MysqlRequest.insertEvaluation(myqlco, projectId, 1, 3514665, 1);
		//			MysqlRequest.updateNoteToEvaluation(myqlco, 10.5, projectId, 1, 3514665, 1);
		//
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//
		//		}
		Application.launch(args);
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
