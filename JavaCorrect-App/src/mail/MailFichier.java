package mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;

import com.sun.mail.imap.IMAPStore;

public class MailFichier {
	static String saveDirectory = "/home/katy";

	public static void main(String[] args) throws Exception {
		receiveEmail("java.correct@mail.com", "789@Upmc");
	}

	public static void receiveEmail(final String login, final String password) throws Exception {
		String subject, zipFile, messageContent;
		String idProjet, numEtu;

		final Properties properties = new Properties();
		properties.put("mail.imap.host", "imap.mail.com");
		properties.put("mail.imap.port", "993");
		properties.put("mail.imap.auth", "true");
		properties.put("mail.imap.ssl.enable", "true");

		try {
			final IMAPStore emailStore = (IMAPStore) Session.getInstance(properties).getStore("imap");
			emailStore.connect(login, password);

			// create the folder object and open it
			final Folder emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			final Message[] messages = emailFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				final Message message = messages[i];
				subject = message.getSubject();
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + subject);
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());

				idProjet = subject.substring(0, subject.indexOf("/"));

				saveDirectory = "/home/katy" + "/" + idProjet; // le répertoire du dossier du projet du prof

				Multipart multipart = (Multipart) messages[i].getContent();

				for (int j = 1; j < multipart.getCount(); j++) {
					String attachFiles = "";

					BodyPart bodyPart = multipart.getBodyPart(j);
					InputStream stream = bodyPart.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(stream));
					MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(j);

					zipFile = bodyPart.getFileName();

					numEtu = zipFile.substring(0, zipFile.indexOf("."));

					// création du dossier de l'étudiant
					new File(saveDirectory + "/" + numEtu).mkdir();

					messageContent = part.getContent().toString();
					System.out.println("Message : " + messageContent);

					// move Files
					if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
						// this part is attachment
						String fileName = part.getFileName();
						attachFiles += fileName + ", ";
						part.saveFile(saveDirectory + File.separator + fileName);
					} else {
						// this part may be the message content
						System.out.println(messageContent);
					}

					// Unzip
					File zipFilePath = new File(saveDirectory + "/" + zipFile);
					File destDir = new File(saveDirectory + "/" + numEtu);

					System.out.println("zipFilePath : " + zipFilePath);
					System.out.println("zipFile : " + zipFile);
					System.out.println("saveDirectory : " + destDir);
					System.out.println("subject : " + subject);

					MyZip.decompress(zipFilePath, destDir, true);

					Notation.note(saveDirectory, numEtu, idProjet);

				}

			}
			emailFolder.close(false);
			emailStore.close();
		} catch (final MessagingException | IOException e) {
			e.printStackTrace();
		}

	}
}
