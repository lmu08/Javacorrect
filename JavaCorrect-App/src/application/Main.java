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

	public static void main(final String[] args) throws ClassNotFoundException, SQLException {
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
