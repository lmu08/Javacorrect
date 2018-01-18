package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import application.MysqlConnexion;
import application.MysqlPropertiesParser;
import application.MysqlRequest;
import application.StudentCsvParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main
extends Application {

	public static void main(final String[] args) {
//		MysqlPropertiesParser properties = new MysqlPropertiesParser();
//		try {
//			Connection myqlco = MysqlConnexion.getInstance(properties);
//			LocalDate today = LocalDate.now();
//			String projectId = UUID.randomUUID()+"";
//			MysqlRequest.insertProject(myqlco, projectId ,today, "toto" );
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
		
		launch(args);
	}

	@Override
	public void start(final Stage stage)
	throws Exception {
		try {
			final Parent root = FXMLLoader.load(getClass().getResource("/ui/Javacorrect.fxml"));

			stage.setTitle("Javacorrect");
			stage.setScene(new Scene(root));
			stage.show();
		} catch (final NullPointerException e) {
			System.err.println("The application failed to load (resource not found). Error :\n" + e);
		}

		//		How to access the controller :
		//		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Javacorrect.fxml"));
		//		stage.setScene(new Scene(loader.load()));
		//		final Controller controller = loader.getController();
	}
}
