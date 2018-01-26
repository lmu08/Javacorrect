package server;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import db.MysqlRequest;
import mail.SendEmail;

public class MailCreation {
	
	@SuppressWarnings("resource")
	public static void mailCreation(final String idProjet)
	throws SQLException {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		// FIXME check this
		final ResultSet listEmail = MysqlRequest.getEmailByidProjet(idProjet);
		final ResultSet res = MysqlRequest.getDateExpiByidProjet(idProjet);
		res.next();
		final Date dateExpi = res.getDate("dateExpi");
		System.out.println("envoi de mails...");
		if (!listEmail.isBeforeFirst()) {
			System.out.println("0 result set");
		}
		while (listEmail.next()) {
			final String email = listEmail.getString("emailEtu");
			final ResultSet rs = MysqlRequest.getidEtuByEmail(email);
			rs.next();
			final String idEtu = rs.getString("idEtu");
			System.out.println("envoi à " + idEtu + ", date :" + dateExpi + ", email:" + email);
			SendEmail.sendEmail("java.correct@gmail.com", "789@Upmc", email, "Creation du Projet " + idProjet,
				"L'id du Projet est " + idProjet + "\n" + "La date limite est : " + dateExpi + ", veuillez à ne pas l'envoyer 5 min avant." + "\n" + "Votre id est " + idEtu + "\n" + "\n" + "L'objet du mail doit être au format [idProjet]/[idEtu]" + "\n" + "Le dossier .zip doit être au format [numéroEtudiant].zip" + "\n" + "\n" + "Veuillez respécter toutes les consignes ! .");
		}
	}
}
