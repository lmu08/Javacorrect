package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main
extends Application {
	
	public static void main(final String[] args) {
//		Thread receiveInputFile = new Thread(new ReceiveInputFileSocket(52112, "/home/flo"));
//		receiveInputFile.start();
//				final String projectId = UUID.randomUUID() + "";
//				final LocalDate dateExpi = LocalDate.now();
//				final String projectName = "my awesome project";
//		
//				try {
//					MysqlRequest.insertProfesseur("bonprof", "jeandelafontaine@upmc.fr", "mdp");
//					MysqlRequest.insertProject(projectId, dateExpi, projectName, "arg0 arg1");
//					MysqlRequest.getProject(projectId);
//					final StudentCsvParser st = new StudentCsvParser();
//					st.parse("/home/lucas/git/Javacorrect/JavaCorrect-App/resources/studentTest.csv");
//		
//					MysqlRequest.insertEvaluation(projectId, "bonprof", 3514665, 1);
//					MysqlRequest.updateNoteToEvaluation(11.5, projectId, "bonprof", 3514665, 1);
//					MysqlRequest.updateDateEnvoiToEvaluation(LocalDate.now(), projectId, "bonprof", 3514665, 1);
//				} catch (final SQLException e) {
//					e.printStackTrace();
//				}
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
