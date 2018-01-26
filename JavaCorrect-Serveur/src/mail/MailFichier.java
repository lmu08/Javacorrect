package mail;

import java.util.Properties;

import server.ReceiveDeleteProjectSocket;
import server.ReceiveInputFileSocket;

public class MailFichier {
	private static final String USER_HOME = System.getProperty("user.home");
	
	public static void main(final String[] args) {
		new Thread(new ReceiveInputFileSocket(52112, USER_HOME)).start();
		new Thread(new ReceiveDeleteProjectSocket(52113, USER_HOME)).start();
		
		while (true) {
			try {
				final Properties props = MailPropertiesParser.getInstance().getMailAdressProperties();
				ReceiveEmail.receiveEmail(props.getProperty("address"), props.getProperty("password"));
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
	}
}
