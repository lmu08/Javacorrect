package server;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.MysqlRequest;
import mail.SendEmail;

public class MailCreation {

	public void mailCreation(String idProjet) throws SQLException {

		final ArrayList<String> listEmail = (ArrayList<String>) MysqlRequest.getEmailByidProjet(idProjet);
		final Date dateExpi = (Date) MysqlRequest.getDateExpiByidProjet(idProjet);

		for (String email : listEmail) {
			final ResultSet idEtu = MysqlRequest.getidEtuByEmail(email);
			SendEmail.sendEmail("java.correct@gmail.com", "789@Upmc", email, "Creation du Projet" + idProjet,
					"L'id du Projet est " + idProjet + "dateExpi : " + dateExpi.toString() + "\n" + "Votre id est "
							+ idEtu);
		}
	}
}
