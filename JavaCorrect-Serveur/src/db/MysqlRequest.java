package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * class containing only static methods.
 * make a request with prepared statement for each method 
 *
 */
public class MysqlRequest {
	private static final Connection myqlco = MysqlConnexion.getInstance(MysqlPropertiesParser.getInstance());

	public static boolean checkProjectId(final String projetId, String studentId) throws SQLException {
		final String request = 
		"SELECT * FROM javacorrectdb.EVALUATION " + 
		"WHERE PROJET_idProjet=? "+
		"AND ETUDIANT_numEtu IN " + 
			"(SELECT * FROM (SELECT numetu " + 
			"FROM javacorrectdb.ETUDIANT "+ 
			"WHERE idEtu=?) " + 
		"AS subquery);";
		final PreparedStatement statement = myqlco.prepareStatement(request);
		statement.setString(1, projetId);
		statement.setString(2, studentId);
		return statement.executeQuery().first(); // returns true if a match is found
	}
	
	public static ResultSet getEmailByidProjet(String idProjet) throws SQLException {
		String getMailByidProjet = "SELECT emailEtu from javacorrectdb.ETUDIANT WHERE numEtu IN (SELECT numEtu from EVALUATION where EVALUATION.PROJET_idProjet= ?);";
		PreparedStatement preparedstatement = myqlco.prepareStatement(getMailByidProjet);
		preparedstatement.setString(1, idProjet);
		return preparedstatement.executeQuery();
	}

	public static ResultSet getDateExpiByidProjet(String idProjet) throws SQLException {
		String getProjectRs = "select dateExpi " + "FROM javacorrectdb.PROJET " + "where idProjet=?;";
		PreparedStatement preparedstatement = myqlco.prepareStatement(getProjectRs);
		preparedstatement.setString(1, idProjet);
		return preparedstatement.executeQuery();
	}

	public static ResultSet getidEtuByEmail(String email) throws SQLException {
		String getProjectRs = "select idEtu " + "FROM javacorrectdb.ETUDIANT " + "where emailEtu=?;";
		PreparedStatement preparedstatement = myqlco.prepareStatement(getProjectRs);
		preparedstatement.setString(1, email);
		return preparedstatement.executeQuery();
	}
	
	public static int updateNote(double note, String projectId, int numEtu)
	throws SQLException {
		String addNoteToEval = "UPDATE EVALUATION " + "SET EVALUATION_note= ?" + "WHERE PROJET_idProjet= ? and" + " ETUDIANT_numEtu = ?;";
		PreparedStatement preparedstatement = myqlco.prepareStatement(addNoteToEval);
		preparedstatement.setBigDecimal(1, BigDecimal.valueOf(note));
		preparedstatement.setString(2, projectId);
		preparedstatement.setInt(3, numEtu);
		return preparedstatement.executeUpdate();
	}

	public static ResultSet getProject(String idProjet) throws SQLException {
		String getProjectRs = "select * " + "FROM PROJET " + "where idProjet = ?;";
		PreparedStatement preparedstatement = myqlco.prepareStatement(getProjectRs);
		preparedstatement.setString(1, idProjet);
		return preparedstatement.executeQuery();
	}
	
	public static ResultSet getArguments(String idProject) throws SQLException {
		String request = "select arguments from PROJET where idProjet=?";
		PreparedStatement preparedStatement = myqlco.prepareStatement(request);
		preparedStatement.setString(1, idProject);
		return preparedStatement.executeQuery();
	}

}
