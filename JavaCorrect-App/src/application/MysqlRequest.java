package application;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class MysqlRequest {

	public static ResultSet getIdPromotionRequest(Connection myqlco, int promo, String classe) throws SQLException {
		String getPromotionRequestStr = "select idPromotion " + "FROM PROMOTION INNER JOIN CLASSE "
				+ "where PROMOTION.CLASSE_idClasse = CLASSE.idClasse " + "and anneePromotion= ? "
				+ "and intituleClasse= ?;";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(getPromotionRequestStr);
		preparedstatement.setInt(1, promo);
		preparedstatement.setString(2, classe);
		return preparedstatement.executeQuery();
	}

	public static ResultSet getStudentByNum(Connection myqlco, int numEtu) throws SQLException {
		String getidClasse = "select * " + "FROM ETUDIANT " + "where numEtu= ?;";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(getidClasse);
		preparedstatement.setInt(1, numEtu);
		return preparedstatement.executeQuery();
	}

	public static ResultSet getidClasseRequest(Connection myqlco, String classe) throws SQLException {
		String getidClasse = "select idClasse " + "FROM CLASSE " + "where intituleClasse= ?;";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(getidClasse);
		preparedstatement.setString(1, classe);
		return preparedstatement.executeQuery();
	}
	
	public static ResultSet getProject(Connection myqlco, String idProjet) throws SQLException {
		String getProjectRs = "select * " +
				"FROM PROJET " + 
				"where idProjet = ?;";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(getProjectRs);
		preparedstatement.setString(1, idProjet);
		return preparedstatement.executeQuery();
	}

	public static int insertClasse(Connection myqlco, String classe) throws SQLException {
		String insertRequest = "INSERT INTO CLASSE " + "(intituleClasse) VALUES " + "(?);";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(insertRequest);
		preparedstatement.setString(1, classe);
		return preparedstatement.executeUpdate();
	}

	public static int insertPromotion(Connection myqlco, int anneePromotion, int idClasse) throws SQLException {
		String insertRequest = "INSERT INTO PROMOTION " + "(anneePromotion, CLASSE_idClasse) VALUES " + "(?, ?);";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(insertRequest);
		preparedstatement.setInt(1, anneePromotion);
		preparedstatement.setInt(2, idClasse);
		return preparedstatement.executeUpdate();
		
	}

	public static int insertStudent(Connection myqlco, int numEtu, String nomEtu, String prenomEtu,int  idPromotion) throws SQLException {
		String insertRequest = "INSERT INTO ETUDIANT " +
				"(numEtu, nomEtu, prenomEtu, PROMOTION_idPromotion) VALUES "
				+ "(?, ?, ?, ?);";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(insertRequest);
		preparedstatement.setInt(1, numEtu);
		preparedstatement.setString(2, nomEtu);
		preparedstatement.setString(3, prenomEtu);
		preparedstatement.setInt(4, idPromotion);
		return preparedstatement.executeUpdate();
	}

	public static int insertProject(Connection myqlco,String projectId ,LocalDate dateExpi, String projectName) throws SQLException {
		String insertStudent =
		"INSERT INTO PROJET "
		+ "(idProjet, dateExpi, intituleProjet) " + 
		"VALUES (?, ?, ?);";
		String dateExpiString = dateExpi.getYear()+"-"+dateExpi.getMonthValue()+"-"+dateExpi.getDayOfMonth();
		System.out.println(dateExpiString);
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(insertStudent);
		preparedstatement.setString(1,projectId);
		preparedstatement.setDate(2, java.sql.Date.valueOf(dateExpiString));
		preparedstatement.setString(3, projectName);
		
		return preparedstatement.executeUpdate();
		
	}
	
	public static int insertEvaluation(Connection myqlco, String projectId, int profId, int numEtu, int idPromo) throws SQLException {
		String insertEval =
		"INSERT INTO EVALUATION "
		+ "(PROJET_idProjet, ETUDIANT_numEtu, ETUDIANT_Promotion_idPromotion, PROFESSEUR_idProfesseur ) " + 
		"VALUES (?, ?, ?, ?);";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(insertEval);
		preparedstatement.setString(1,projectId);
		preparedstatement.setInt(2, numEtu);
		preparedstatement.setInt(3, idPromo);
		preparedstatement.setInt(4, profId);
		
		return preparedstatement.executeUpdate();
	}
	
	public static int updateNoteToEvaluation(Connection myqlco, double note, String projectId, int profId, int numEtu, int idPromo) throws SQLException {
		String addNoteToEval =
		"UPDATE EVALUATION "
		+ "SET EVALUATION_note= ?"
		+ "WHERE PROJET_idProjet= ? and"
		+ " ETUDIANT_numEtu = ? and "
		+ " ETUDIANT_Promotion_idPromotion = ? and"
		+ " PROFESSEUR_idProfesseur = ?";
		java.sql.PreparedStatement preparedstatement = myqlco.prepareStatement(addNoteToEval);
		preparedstatement.setBigDecimal(1,BigDecimal.valueOf(note));
		preparedstatement.setString(2,projectId );
		preparedstatement.setInt(3, numEtu);
		preparedstatement.setInt(4, idPromo);
		preparedstatement.setInt(5, profId);
		return preparedstatement.executeUpdate();
	}
	
	
}
