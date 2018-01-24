package mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.swing.filechooser.FileSystemView;

import com.sun.mail.imap.IMAPStore;

public class ReceiveEmail {

	static String saveDirectory;

	public static void receiveEmail(final String login, final String password) throws Exception {
		String subject, zipFile, messageContent;
		String idProjet, numEtu, idProjetEtu;

		idProjet = "Projet1";
		idProjetEtu = "Etu1";

		final Properties properties = new Properties();
		properties.put("mail.imap.host", "imap.gmail.com");
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

				Pattern p = Pattern.compile("/");
				// séparation du subject en sous-chaînes
				String[] items = p.split(subject, 10);

				// compilation de la regex
				Pattern patternProjet = Pattern.compile(idProjet + "/" + idProjetEtu);
				// création d'un moteur de recherche
				Matcher matchSubject = patternProjet.matcher(items[0] + "/" + items[1]);

				// System.out.println("---------------------------------");
				// System.out.println("Email Number " + (i + 1));
				// System.out.println("Subject: " + subject);
				// System.out.println("From: " + message.getFrom()[0]);
				// System.out.println("Text: " + message.getContent().toString());
				// System.out.println("................................. \n");

				// f > chemain home (ex içi c'est /home/katy
				FileSystemView fsv = FileSystemView.getFileSystemView();
				File f = fsv.getDefaultDirectory();

				saveDirectory = f.toString() + "/" + idProjet; // le répertoire du dossier du projet du prof

				// System.out.println("saveDirectory : " + saveDirectory);
				// System.out.println("nb mail : " + messages.length);

				if (matchSubject.matches()) {
					Multipart multipart = (Multipart) messages[i].getContent();
					System.out.println("nb de pièce joint : " + (multipart.getCount() - 1));

					for (int j = 1; j < multipart.getCount(); j++) {
						String attachFiles = "";

						BodyPart bodyPart = multipart.getBodyPart(j);
						InputStream stream = bodyPart.getInputStream();
						BufferedReader br = new BufferedReader(new InputStreamReader(stream));
						MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(j);

						zipFile = Optional.ofNullable(bodyPart.getFileName()).orElse("");

						// compilation de la regex
						Pattern patternFile = Pattern.compile("^[0-9]{7}+.zip$");
						// création d'un moteur de recherche
						Matcher matchNomFileZipe = patternFile.matcher(zipFile);

						System.out.println("si c'est le bon .zip : " + matchNomFileZipe.matches());

						if (matchNomFileZipe.matches()) {

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

							// System.out.println("zipFilePath : " + zipFilePath);
							// System.out.println("zipFile : " + zipFile);
							// System.out.println("destDir : " + destDir);
							// System.out.println("subject : " + subject);
							// System.out.println("saveDirectory : " + saveDirectory);

							MyZip.decompress(zipFilePath, destDir, true);
							String args = " ";
							System.out.println(saveDirectory);
							final Thread redChecker = waitForRed(saveDirectory, numEtu, idProjet, args);

							// Notation.note(saveDirectory, numEtu, idProjet, args);
							SendEmail.sendEmail(login, password, message.getFrom()[0].toString(), subject,
									"votre devoir à bien été reçu");
						} else {
							System.out.println("c'est pas le bon format du zip");
						}
					}
				} else {
					System.out.println("c'est pas le bon objet");
				}
				message.setFlag(Flags.Flag.DELETED, true);
			}

			emailFolder.close(true);
			emailStore.close();
		} catch (final MessagingException | IOException e) {
			e.printStackTrace();
		}

	}

	private static Thread waitForRed(String thCompilDirectory, String thNumEtu, String thIdProjet, String thArgs) {

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String thEtuDirectory = thCompilDirectory + "/" + thNumEtu + "/" + thIdProjet;
				final File f = new File(thEtuDirectory);
				if (f.exists()) {
					try {
						Notation.note(thCompilDirectory, thNumEtu, thIdProjet, thArgs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
		return thread;
	}
}
