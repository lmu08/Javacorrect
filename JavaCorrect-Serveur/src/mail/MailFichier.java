package mail;

public class MailFichier {

	public static void main(String[] args) {

		// while (true) {
		try {
			ReceiveEmail.receiveEmail("java.correct@gmail.com", "789@Upmc");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }

		// SendEmail.sendEmail("java.correct@gmail.com", "789@Upmc",
		// "amichi.katia@gmail.com", "titre", "message test");

	}

}
