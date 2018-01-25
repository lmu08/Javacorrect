package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

import db.CSVSavingException;
import db.ProjectCreationException;
import db.RegistrationException;
import tools.EncryptingTools;

public class MysqlRequest {
	private static final Connection myqlco = MysqlConnexion.getInstance(MysqlPropertiesParser.getInstance());

	public static ResultSet getIdPromotionRequest(int promo, String classe) throws CSVSavingException {
		try {
			String getPromotionRequestStr = "select idPromotion " + "FROM PROMOTION INNER JOIN CLASSE "
					+ "where PROMOTION.CLASSE_idClasse = CLASSE.idClasse " + "and anneePromotion= ? "
					+ "and intituleClasse= ?;";
			PreparedStatement preparedstatement = myqlco.prepareStatement(getPromotionRequestStr);
			preparedstatement.setInt(1, promo);
			preparedstatement.setString(2, classe);
			return preparedstatement.executeQuery();
		} catch (SQLException e) {
			throw new CSVSavingException(e);
		}
	}

	public static ResultSet getStudentByNum(int numEtu) throws CSVSavingException {
		try {
			String getidClasse = "select * " + "FROM ETUDIANT " + "where numEtu= ?;";
			PreparedStatement preparedstatement = myqlco.prepareStatement(getidClasse);
			preparedstatement.setInt(1, numEtu);
			return preparedstatement.executeQuery();
		} catch (SQLException e) {
			throw new CSVSavingException(e);
		}
	}

	public static ResultSet getidClasseRequest(String classe) throws CSVSavingException {
		try {
			String getidClasse = "select idClasse " + "FROM CLASSE " + "where intituleClasse= ?;";
			PreparedStatement preparedstatement = myqlco.prepareStatement(getidClasse);
			preparedstatement.setString(1, classe);
			return preparedstatement.executeQuery();
		} catch (SQLException e) {
			throw new CSVSavingException(e);
		}
	}
	
	public static ResultSet getProfesseurByLogin(String login) throws RegistrationException {
		try {
			String getProfByIdQuery = "SELECT * "
					+ "FROM PROFESSEUR "
					+ "WHERE loginProfesseur = ? ; ";
			PreparedStatement preparedstatement = myqlco.prepareStatement(getProfByIdQuery);
			preparedstatement.setString(1, login);
			return preparedstatement.executeQuery();
		} catch (SQLException e) {
			throw new RegistrationException(e);
		}
	}
	
	public static ResultSet getProfesseurByMail(String mail) throws RegistrationException {
		try {
			String getProfByIdQuery = "SELECT * "
					+ "FROM PROFESSEUR "
					+ "WHERE mailProfesseur = ? ; ";
			java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(getProfByIdQuery);
			preparedstatement.setString(1, mail);
			return preparedstatement.executeQuery();
		} catch (SQLException e) {
			throw new RegistrationException(e);
		}
	}
	
	public static ResultSet getProject(String idProjet) throws SQLException {
		String getProjectRs = "select * " +
				"FROM PROJET " + 
				"where idProjet = ?;";
		PreparedStatement preparedstatement = myqlco.prepareStatement(getProjectRs);
		preparedstatement.setString(1, idProjet);
		return preparedstatement.executeQuery();
	}
	
	public static int insertClasse(String classe) throws CSVSavingException {
		try {	
			String insertRequest = "INSERT INTO CLASSE " + "(intituleClasse) VALUES " + "(?);";
			PreparedStatement preparedstatement = myqlco.prepareStatement(insertRequest);
			preparedstatement.setString(1, classe);
			return preparedstatement.executeUpdate();
		} catch (SQLException e) {
			throw new CSVSavingException(e);
		}
	}
	public static int insertPromotion(final int anneePromotion, final int idClasse)
	throws CSVSavingException {
		try {
			String insertRequest = "INSERT INTO PROMOTION " + "(anneePromotion, CLASSE_idClasse) VALUES " + "(?, ?);";
			PreparedStatement preparedstatement = myqlco.prepareStatement(insertRequest);
			preparedstatement.setInt(1, anneePromotion);
			preparedstatement.setInt(2, idClasse);
			return preparedstatement.executeUpdate();
		} catch (SQLException e) {
			throw new CSVSavingException(e);
		}
	}

	public static int insertStudent(final int numEtu, final String nomEtu, final String prenomEtu, final String emailEtu ,final int idPromotion)
	throws SQLException {
		int res;
		ResultSet rs = getStudentByNum(numEtu);
		if(rs.isBeforeFirst()) {
			res = updateStudent(numEtu, nomEtu, prenomEtu, emailEtu,idPromotion);
		}
			final String insertRequest = "INSERT INTO ETUDIANT " + "(numEtu, nomEtu, prenomEtu, emailEtu ,PROMOTION_idPromotion, idEtu) VALUES " + "(?, ?, ?, ?, ?, ?);";
			PreparedStatement preparedstatement = myqlco.prepareStatement(insertRequest);
			preparedstatement.setInt(1, numEtu);
			preparedstatement.setString(2, nomEtu);
			preparedstatement.setString(3, prenomEtu);
			preparedstatement.setString(4, emailEtu);
			preparedstatement.setInt(5, idPromotion);
			preparedstatement.setString(6, String.valueOf(UUID.randomUUID()));
			res = preparedstatement.executeUpdate();
			return res;
	}

	public static int insertProject(String projectId ,LocalDate dateExpi, String projectName, String arguments) throws ProjectCreationException {
		try {
			String insertStudent =
			"INSERT INTO PROJET "
			+ "(idProjet, dateExpi, intituleProjet, arguments) " + 
			"VALUES (?, ?, ?, ?);";
			String dateExpiString = dateExpi.getYear()+"-"+dateExpi.getMonthValue()+"-"+dateExpi.getDayOfMonth();
			System.out.println(dateExpiString);
			PreparedStatement preparedstatement = myqlco.prepareStatement(insertStudent);
			preparedstatement.setString(1,projectId);
			preparedstatement.setDate(2, java.sql.Date.valueOf(dateExpiString));
			preparedstatement.setString(3, projectName);
			preparedstatement.setString(4, arguments);
			return preparedstatement.executeUpdate();
		} catch (SQLException e) {
			throw new ProjectCreationException(e);
		}
	}
	
	public static int insertEvaluation(String projectId, String loginProf, int numEtu, int idPromo) throws CSVSavingException {
		try {
			String insertEval =
			"INSERT INTO EVALUATION "
			+ "(PROJET_idProjet, ETUDIANT_numEtu, ETUDIANT_Promotion_idPromotion, PROFESSEUR_loginProfesseur ) " + 
			"VALUES (?, ?, ?, ?);";
			PreparedStatement preparedstatement = myqlco.prepareStatement(insertEval);
			preparedstatement.setString(1,projectId);
			preparedstatement.setInt(2, numEtu);
			preparedstatement.setInt(3, idPromo);
			preparedstatement.setString(4, loginProf);
			return preparedstatement.executeUpdate();
		} catch (SQLException e) {
			throw new CSVSavingException(e);
		}
	}
	
	public static int updateNoteToEvaluation( double note, String projectId, String loginprof, int numEtu, int idPromo) throws SQLException {
		String addNoteToEval =
		"UPDATE EVALUATION "
		+ "SET EVALUATION_note= ?"
		+ "WHERE PROJET_idProjet= ? and"
		+ " ETUDIANT_numEtu = ? and "
		+ " ETUDIANT_Promotion_idPromotion = ? and"
		+ " PROFESSEUR_loginProfesseur = ?;";
		PreparedStatement preparedstatement = myqlco.prepareStatement(addNoteToEval);
		preparedstatement.setBigDecimal(1,BigDecimal.valueOf(note));
		preparedstatement.setString(2,projectId );
		preparedstatement.setInt(3, numEtu);
		preparedstatement.setInt(4, idPromo);
		preparedstatement.setString(5, loginprof);
		return preparedstatement.executeUpdate();
	}
	
	public static int updateDateEnvoiToEvaluation(LocalDate dateEnvoi, String projectId, String loginprof, int numEtu, int idPromo) throws SQLException {
		String addNoteToEval =
		"UPDATE EVALUATION "
		+ "SET EVALUATION_date_envoi= ? "
		+ "WHERE PROJET_idProjet= ? and"
		+ " ETUDIANT_numEtu = ? and "
		+ " ETUDIANT_Promotion_idPromotion = ? and"
		+ " PROFESSEUR_loginProfesseur = ?;";
		String dateEnvoiString = dateEnvoi.getYear()+"-"+dateEnvoi.getMonthValue()+"-"+dateEnvoi.getDayOfMonth();
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(addNoteToEval);
		preparedstatement.setDate(1, java.sql.Date.valueOf(dateEnvoiString));
		preparedstatement.setString(2,projectId );
		preparedstatement.setInt(3, numEtu);
		preparedstatement.setInt(4, idPromo);
		preparedstatement.setString(5, loginprof);
		System.out.println(preparedstatement.executeUpdate());
		return preparedstatement.executeUpdate();
	}

	public static int insertProfesseur(final String login, final String mailProfesseur, final String password)
	throws RegistrationException {
		try {
			final ResultSet rs = getProfesseurByLogin(login);
			if (rs.isBeforeFirst()) {
				updateProfesseur(login, mailProfesseur, password);
			} else {
				String encryptedPassword = EncryptingTools.clearTextToEncrypted(password, "SHA-256");
				String insertProf =
						"INSERT INTO PROFESSEUR "
						+ "(loginProfesseur, mailProfesseur, passwdProfesseur) "
						+ "VALUES (?, ?, ?);";
				PreparedStatement preparedstatement = myqlco.prepareStatement(insertProf);
				preparedstatement.setString(1, login.toLowerCase());
				preparedstatement.setString(2, mailProfesseur);
				preparedstatement.setString(3, encryptedPassword);
				return preparedstatement.executeUpdate();
			}
			return -1;
		}catch (SQLException e) {
			throw new RegistrationException(e);
		}
	}
	
	
	private static int updateProfesseur(String login, String mailProfesseur, String password) throws SQLException {
		String encryptedPassword = EncryptingTools.clearTextToEncrypted(password, "SHA-256");
		String insertProf =
				"UPDATE PROFESSEUR "
				+ "SET mailProfesseur = ? , passwdProfesseur = ? "
				+ "WHERE loginProfesseur = ?";
		PreparedStatement preparedstatement = myqlco.prepareStatement(insertProf);
		preparedstatement.setString(1, mailProfesseur);
		preparedstatement.setString(2, encryptedPassword);
		preparedstatement.setString(3, login.toLowerCase());
		return preparedstatement.executeUpdate();
	}

	public static int updateStudent(final int numEtu, final String nomEtu, final String prenomEtu, final String emailEtu, final int idPromotion) throws SQLException {
		final String updatetRequest = "UPDATE ETUDIANT " + "SET nomEtu = ?, prenomEtu = ?, emailEtu = ?, PROMOTION_idPromotion = ?" + "WHERE numEtu= ? ;";
		PreparedStatement preparedstatement = myqlco.prepareStatement(updatetRequest);
		preparedstatement.setString(1, nomEtu);
		preparedstatement.setString(2, prenomEtu);
		preparedstatement.setString(3, emailEtu);
		preparedstatement.setInt(4, idPromotion);
		preparedstatement.setInt(5, numEtu);
		return preparedstatement.executeUpdate();
	}
	
	public static ResultSet getProjectNameByTeacher(String loginProfesseur) throws SQLException {
		String request = "select PROJET.intituleProjet "
				+ "FROM EVALUATION INNER JOIN PROFESSEUR INNER JOIN PROJET on "
				+ "EVALUATION.PROFESSEUR_loginProfesseur = PROFESSEUR.loginProfesseur "
				+ "and EVALUATION.PROJET_idProjet = PROJET.idProjet "
				+ "and loginProfesseur= ?;";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(request);
		preparedstatement.setString(1, loginProfesseur);
		return preparedstatement.executeQuery();
		
	}
	
	
	public static ResultSet getEvaluationByLoginProjName(String loginProfesseur, String intituleProjet) throws ProjectCreationException {
		try {
			String request = "SELECT * "
					+ "FROM PROJET as proj "
					+ "INNER JOIN EVALUATION as eval "
					+ "ON proj.idProjet = eval.PROJET_idProjet "
					+ "WHERE eval.PROFESSEUR_loginProfesseur = ? "
					+ "and proj.intituleProjet = ? ;";
			java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(request);
			preparedstatement.setString(1, loginProfesseur);
			preparedstatement.setString(2, intituleProjet);
			return preparedstatement.executeQuery();
		} catch (SQLException e) {
			throw new ProjectCreationException(e);
		}
	}
	
	public static ResultSet getEvaluation(String loginProfesseur, String intituleProjet) throws SQLException {
		String request = "SELECT * from PROJET AS p "
				+ "INNER JOIN EVALUATION as eval "
					+ "ON p.idProjet = eval.PROJET_idProjet "
				+ "INNER JOIN ETUDIANT AS etu "
					+ "ON etu.numEtu = eval.ETUDIANT_numEtu "
				+ "INNER JOIN PROMOTION AS promo "
					+ "ON etu.PROMOTION_idPromotion = promo.idPromotion "
				+ "INNER JOIN CLASSE AS classe "
					+ "ON promo.CLASSE_idClasse = classe.idClasse "
				+ "WHERE eval.PROFESSEUR_loginProfesseur = ? "
				+ "and p.intituleProjet = ? ;";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(request);
		preparedstatement.setString(1, loginProfesseur);
		preparedstatement.setString(2, intituleProjet);
		return preparedstatement.executeQuery();
	}
	
	public static int deleteProjet(String idProjet) throws SQLException {
		String request = "DELETE FROM PROJET where idProjet= ?";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(request);
		preparedstatement.setString(1, idProjet);
		return preparedstatement.executeUpdate();
	}
	
	public static int getProjetByIntitule(String intituleProjet) throws SQLException {
		String request = "DELETE FROM PROJET where intituleProjet= ?";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(request);
		preparedstatement.setString(1, intituleProjet);
		return preparedstatement.executeUpdate();
	}

}
