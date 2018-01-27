package mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
	
	/**
	 * Envoie des mails 
	 * @param login de notre boite mail
	 * @param password 
	 * @param toEmail le mail du récepteur
	 * @param subjet m'objet du mail à envoyer 
	 * @param body le texte à envoyer 
	 */
	
	public static void sendEmail(final String login, final String password, final String toEmail,
			final String subject, final String body) {
		
		final Properties smtpProps = MailPropertiesParser.getInstance().getSmtpProperties();
		final MimeMessage msg = new MimeMessage(Session.getInstance(smtpProps));
		
		try {
			final Properties headerProps = MailPropertiesParser.getInstance().getHeaderProperties();
			msg.addHeader("Content-type", headerProps.getProperty("Content-type"));
			msg.addHeader("format", headerProps.getProperty("format"));
			msg.addHeader("Content-Transfer-Encoding",  headerProps.getProperty("Content-Transfer-Encoding"));
			
			final InternetAddress[] dest = InternetAddress.parse(toEmail, false);
			msg.setFrom(new InternetAddress("no_reply@test.com", "no_reply"));
			msg.setReplyTo(dest);
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, dest);
			
			System.out.println("Message is ready");
			Transport.send(msg, login, password);
			
			System.out.println("Email Sent Successfully!!");
		} catch (final MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
