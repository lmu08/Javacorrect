package mail;

public class MailFichier {

	public static void main(String[] args) throws Exception {
		// ReceiveEmail.receiveEmail("java.correct@mail.com", "789@Upmc");
		SendEmail.sendEmail("java.correct@mail.com", "789@Upmc", "amichi.katia@gmail.com", "titre", "message test");

	}

}
