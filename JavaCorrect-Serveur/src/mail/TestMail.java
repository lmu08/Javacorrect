package mail;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.imap.IMAPStore;

public class TestMail {
	
	public static void main(final String[] args) {
		System.out.println("Start");
		
		receiveEmail("java.correct@mail.com", "789@Upmc");
		//sendEmail("3603567", "", "java.correct@mail.com", "titre", "message sans pièce joint");
	}
	
	public static void receiveEmail(final String login, final String password) {
		final Properties properties = new Properties();
		properties.put("mail.imap.host", "imap.mail.com");
		properties.put("mail.imap.port", "993");
		properties.put("mail.imap.auth", "true");
		properties.put("mail.imap.ssl.enable", "true");
		
		try {
			final IMAPStore emailStore = (IMAPStore) Session.getInstance(properties).getStore("imap");
			emailStore.connect(login, password);
			
			//create the folder object and open it
			final Folder emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			
			//retrieve the messages from the folder in an array and print it
			final Message[] messages = emailFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				final Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());
			}
			emailFolder.close(false);
			emailStore.close();
		} catch (final MessagingException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendEmail(final String login, final String password, final String toEmail, final String subject, final String body) {
		final Properties props = new Properties();
		props.put("mail.smtp.host", "smtps.upmc.fr");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		final MimeMessage msg = new MimeMessage(Session.getInstance(props));
		
		try {
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			
			final InternetAddress[] dest = InternetAddress.parse(toEmail, false);
			msg.setFrom(new InternetAddress("no_reply@test.com", "no_reply"));
			msg.setReplyTo(dest);
			msg.setSubject(subject, "UTF-8");
			msg.setText(body, "UTF-8");
			msg.setSentDate(new Date());
			msg.setRecipients(Message.RecipientType.TO, dest);
			
			System.out.println("Message is ready");
			Transport.send(msg, login, password);
			
			System.out.println("EMail Sent Successfully!!");
		} catch (final MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
