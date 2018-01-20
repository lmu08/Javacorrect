package controller;

import java.sql.Connection;

import db.MysqlConnexion;
import db.MysqlPropertiesParser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main
extends Application {

	public static void main(final String[] args) {
		MysqlPropertiesParser properties = new MysqlPropertiesParser();
		try {
			Connection myqlco = MysqlConnexion.getInstance(properties);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
//	    MysqlPropertiesParser properties = new MysqlPropertiesParser();
//		Connection myqlco;
//		try {
//			myqlco = MysqlConnexion.getInstance(properties);
//			String projectId = UUID.randomUUID()+"";
//			LocalDate dateExpi = LocalDate.now();
//			String projectName = "my awesome project";
//			try {
//				MysqlRequest.insertProfesseur(myqlco, "bonprof", "jeandelafontaine@upmc.fr", "mdp");
//				MysqlRequest.insertProject(myqlco, projectId, dateExpi, projectName);
//				MysqlRequest.getProject(myqlco, projectId);
//				new StudentCsvParser("/home/flo/Documents/JavaCorrect/Javacorrect/studentTest.csv");
//				MysqlRequest.insertEvaluation(myqlco, projectId, "Quoo2kae", 3514665, 1);
//				MysqlRequest.updateNoteToEvaluation(myqlco, 10.5, projectId, "Quoo2kae", 3514665, 1);
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (ClassNotFoundException e) {
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
