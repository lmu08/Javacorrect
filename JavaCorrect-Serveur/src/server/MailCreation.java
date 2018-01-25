package server;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.MysqlRequest;
import mail.SendEmail;

public class MailCreation {

	public static void mailCreation(final String idProjet)
	throws SQLException {
		//FIXME check this
		final ResultSet listEmail = MysqlRequest.getEmailByidProjet(idProjet);
		final Date dateExpi = (Date) MysqlRequest.getDateExpiByidProjet(idProjet);

		while (listEmail.next()) {
			final String email = listEmail.getString("emailEtu");
			final ResultSet idEtu = MysqlRequest.getidEtuByEmail(email);
			SendEmail.sendEmail("java.correct@gmail.com", "789@Upmc", email, "Creation du Projet" + idProjet,
				"L'id du Projet est " + idProjet + "dateExpi : " + dateExpi.toString() + "\n" + "Votre id est " + idEtu);
		}
	}
}
