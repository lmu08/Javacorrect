package application;

import java.sql.Connection;
import java.sql.SQLException;

import application.MysqlConnexion;
import application.MysqlPropertiesParser;
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
		Connection myqlco = MysqlConnexion.getInstance(properties);
		
    //		System.out.println(properties.getDbname());
    //		String insertStudent =
    //		"INSERT INTO " + properties.getDbname() + ".CLASSE (intituleClasse) values (?);";
    //		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(insertStudent);
    //		preparedstatement.setString(1,"L3 DANT");
    //		try {
    //			preparedstatement.executeUpdate();
    //		}
    //		catch(Exception e) {
    //			System.out.println("Already exists");
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
