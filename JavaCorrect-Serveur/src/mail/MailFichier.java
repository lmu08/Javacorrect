package mail;

public class MailFichier {
	
	public static void main(final String[] args) {
		while (true) {
			try {
				ReceiveEmail.receiveEmail("java.correct@gmail.com", "789@Upmc");
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
