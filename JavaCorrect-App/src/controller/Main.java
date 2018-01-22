package controller;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import db.MysqlConnexion;
import db.MysqlPropertiesParser;
import db.MysqlRequest;
import db.StudentCsvParser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main
extends Application {
	
	public static void main(final String[] args) {
//		final MysqlPropertiesParser properties = MysqlPropertiesParser.getInstance();
//		Connection myqlco = MysqlConnexion.getInstance(properties);
//		
//			myqlco = MysqlConnexion.getInstance(properties);
//				String projectId = UUID.randomUUID()+"";
//				LocalDate dateExpi = LocalDate.now();
//				String projectName = "my awesome project";
//				try {
//					MysqlRequest.insertProfesseur(myqlco, "bonprof", "jeandelafontaine@upmc.fr", "mdp");
//					MysqlRequest.insertProject(myqlco, projectId, dateExpi, projectName);
//					MysqlRequest.getProject(myqlco, projectId);
//					new StudentCsvParser("/home/flo/Documents/JavaCorrect/Javacorrect/JavaCorrect-App/resources/studentTest.csv");
//					MysqlRequest.insertEvaluation(myqlco, projectId, "bonprof", 3514665, 1);
//					MysqlRequest.updateNoteToEvaluation(myqlco, 10.5, projectId, "bonprof", 3514665, 1);
//				} catch (NoSuchAlgorithmException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
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
