package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main
extends Application {

	public static void main(final String[] args) {
		//		final String projectId = UUID.randomUUID() + "";
		//		final LocalDate dateExpi = LocalDate.now();
		//		final String projectName = "my awesome project";
		//		try {
		//			MysqlRequest.insertProfesseur("bonprof", "jeandelafontaine@upmc.fr", "mdp");
		//			MysqlRequest.insertProject(projectId, dateExpi, projectName);
		//			MysqlRequest.getProject(projectId);
		//			new StudentCsvParser("/home/lucas/git/Javacorrect/JavaCorrect-App/resources/studentTest.csv");
		//			MysqlRequest.insertEvaluation(projectId, "bonprof", 3514665, 1);
		//			MysqlRequest.updateNoteToEvaluation(10.5, projectId, "bonprof", 3514665, 1);
		//		} catch (final NoSuchAlgorithmException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} catch (final SQLException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
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
