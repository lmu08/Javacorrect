package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlRequest {
	private static final Connection myqlco = MysqlConnexion.getInstance(MysqlPropertiesParser.getInstance());
	
	public static boolean checkProjectId(final String id) throws SQLException {
		final String request = "SELECT * FROM javacorrectdb.PROJET where idProjet=" + id;
		final Statement statement = myqlco.createStatement();
		statement.execute(request);
		return statement.getResultSet().first(); //returns true if a match is found
	}
}
