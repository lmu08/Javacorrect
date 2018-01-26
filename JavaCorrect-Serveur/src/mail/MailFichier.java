package mail;

import server.ReceiveDeleteProjectSocket;
import server.ReceiveInputFileSocket;

public class MailFichier {
	private static final String USER_HOME = System.getProperty("user.home");
	
	public static void main(final String[] args) {
		new Thread(new ReceiveInputFileSocket(52112, USER_HOME)).start();
		new Thread(new ReceiveDeleteProjectSocket(52113, USER_HOME)).start();
		
		while (true) {
			try {
				ReceiveEmail.receiveEmail("java.correct@gmail.com", "789@Upmc");
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
