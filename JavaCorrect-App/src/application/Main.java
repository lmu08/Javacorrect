package application;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
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
	private static String userLogin;
	
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
	public void start(final Stage primaryStage)
	throws Exception {
		try {
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Login.fxml"));
			final Stage stage = new Stage();
			stage.setTitle("Javacorrect");
			stage.setScene(new Scene(loader.load()));
			final LoginController loginController = (LoginController) loader.getController();
			loginController.setStage(stage);
			stage.showAndWait();
			if (!(userLogin = loginController.getLogin()).isEmpty()) {
				final Parent parent = FXMLLoader.load(getClass().getResource("/ui/Javacorrect.fxml"));
				primaryStage.setScene(new Scene(parent));
				primaryStage.show();
			}
		} catch (final NullPointerException e) {
			System.err.println("The application failed to load (resource not found). Error :\n" + e);
		}
	}
	
	public static String getUserLogin() {
		return userLogin;
	}
}
