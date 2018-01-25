package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlRequest {
	private static final Connection myqlco = MysqlConnexion.getInstance(MysqlPropertiesParser.getInstance());
	
	public static boolean checkProjectId(final String projetId, String studentId) throws SQLException {
		final String request = "SELECT * FROM javacorrectdb.EVALUATION "
		+ "WHERE PROJET_idProjet=? "
		+ "AND ETUDIANT_numEtu IN "
			+ "(SELECT * FROM (SELECT numetu "
				+ "FROM javacorrectdb.ETUDIANT "
				+ "WHERE idEtu=?) "
			+ "AS subquery);";
		final PreparedStatement statement = myqlco.prepareStatement(request);
		statement.setString(1, projetId);
		statement.setString(2, studentId);
		return statement.executeQuery().first(); //returns true if a match is found
	}
}
