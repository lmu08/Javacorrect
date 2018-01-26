package server;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.MysqlRequest;
import mail.SendEmail;

public class MailCreation {

	public static void mailCreation(final String idProjet) throws SQLException {
		// FIXME check this
		final ResultSet listEmail = MysqlRequest.getEmailByidProjet(idProjet);
		final Date dateExpi = (Date) MysqlRequest.getDateExpiByidProjet(idProjet);

		while (listEmail.next()) {
			final String email = listEmail.getString("emailEtu");
			final ResultSet idEtu = MysqlRequest.getidEtuByEmail(email);

			SendEmail.sendEmail("java.correct@gmail.com", "789@Upmc", email, "Creation du Projet" + idProjet,
					"L'id du Projet est " + idProjet + "\n" + "La date limite est : " + dateExpi
							+ ", veuillez à ne pas l'envoyer 5 min avant." + "\n" + "Votre id est " + idEtu + "\n"
							+ "\n" + "L'objet du mail doit être au format [idProjet]/[idEtu]" + "\n"
							+ "Votre devoir doit avoir comme nom le idProjet (" + idProjet + ")" + "\n"
							+ "Le dossier .zip doit être au format [numéroEtudiant].zip" + "\n" + "\n"
							+ "Veuillez respécter toutes les consignes ! .");
		}
	}
}
