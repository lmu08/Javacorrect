package mail;

import java.sql.Connection;

import db.MysqlConnexion;
import db.MysqlPropertiesParser;

public class MailFichier {
	static String saveDirectory;

	public static void main(String[] args) throws Exception {
		Connection mysqlco = MysqlConnexion.getInstance(MysqlPropertiesParser.getInstance());

		//while (true) {
			try {
				ReceiveEmail.receiveEmail("java.correct@gmail.com", "789@Upmc");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	//	}
		// SendEmail.sendEmail("java.correct@gmail.com", "789@Upmc",
		// "amichi.katia@gmail.com", "titre", "message test");

	}

}
