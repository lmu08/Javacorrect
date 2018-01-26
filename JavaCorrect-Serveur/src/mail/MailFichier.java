package mail;

import java.util.Properties;

public class MailFichier {
	
	public static void main(final String[] args) {
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
