package mail;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
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

	private String currentUser;

	static String saveDirectory;

	public static void receiveEmail(final String login, final String password)
	throws Exception {
		String idProjet, idProjetEtu;

		idProjet = "Projet1";
		idProjetEtu = "Etu1";

		final Properties properties = new Properties();
		properties.put("mail.imap.host", "imap.gmail.com");
		properties.put("mail.imap.port", "993");
		properties.put("mail.imap.auth", "true");
		properties.put("mail.imap.ssl.enable", "true");

		List<Thread> listThread = null;

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

				System.out.println("message1 : " + message);
				final Thread e = waitForRed(emailFolder, login, password, message, idProjet, idProjetEtu);
				// message.setFlag(Flags.Flag.DELETED, false);
				listThread = new LinkedList<>();
				listThread.add(e);

			}

			// attente des threads
			for (final Thread thread : listThread) {
				thread.join();
			}

			if (emailFolder.isOpen()) {
				emailFolder.close(false);
			}
			emailStore.close();

		} catch (final MessagingException e) {
			e.printStackTrace();
		}

	}

	private static Thread waitForRed(final Folder emailFolder, final String login, final String password, final Message message, final String idProjet, final String idProjetEtu) {

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				if (true) {
					try {

						if (!emailFolder.isOpen()) {
							emailFolder.open(Folder.READ_WRITE);
						}

						System.out.println("message2 : " + message);

						final String subject = message.getSubject();
						System.out.println("sub : " + subject);

						final Pattern p = Pattern.compile("/");
						// séparation du subject en sous-chaînes
						final String[] items = p.split(subject, 10);

						// compilation de la regex
						final Pattern patternProjet = Pattern.compile(idProjet + "/" + idProjetEtu);
						// création d'un moteur de recherche
						final Matcher matchSubject = patternProjet.matcher(items[0] + "/" + items[1]);

						// f > chemin home (ex içi c'est /home/katy
						final FileSystemView fsv = FileSystemView.getFileSystemView();
						final File f = fsv.getDefaultDirectory();

						saveDirectory = f.toString() + "/" + idProjet; // le répertoire du dossier du projet du prof

						if (matchSubject.matches() /* && que le idProjet correspond à L'idEtu */) {
							final Multipart multipart = (Multipart) message.getContent();
							System.out.println("nb de pièce joint : " + (multipart.getCount() - 1));

							for (int j = 1; j < multipart.getCount(); j++) {

								final BodyPart bodyPart = multipart.getBodyPart(j);
								// InputStream stream = bodyPart.getInputStream();
								// BufferedReader br = new BufferedReader(new InputStreamReader(stream));
								final MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(j);

								final String zipFile = Optional.ofNullable(bodyPart.getFileName()).orElse("");

								// compilation de la regex
								final Pattern patternFile = Pattern.compile("^[0-9]{7}+.zip$");
								// création d'un moteur de recherche
								final Matcher matchNomFileZipe = patternFile.matcher(zipFile);

								System.out.println("si c'est le bon .zip : " + matchNomFileZipe.matches());

								if (matchNomFileZipe.matches() /* && le numEtu correspond à L'idEtu */) {

									final String numEtu = zipFile.substring(0, zipFile.indexOf("."));

									// création du dossier de l'étudiant
									new File(saveDirectory + "/" + numEtu).mkdir();

									final String messageContent = part.getContent().toString();
									System.out.println("Message : " + messageContent);

									// move Files
									if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
										// this part is attachment
										final String fileName = part.getFileName();
										part.saveFile(saveDirectory + File.separator + fileName);
									} else {
										// this part may be the message content
										System.out.println(messageContent);
									}

									// Unzip
									final File zipFilePath = new File(saveDirectory + "/" + zipFile);
									final File destDir = new File(saveDirectory + "/" + numEtu);

									MyZip.decompress(zipFilePath, destDir, true);
									final String args = " ";
									System.out.println(saveDirectory);

									Notation.note(saveDirectory, numEtu, idProjet, args);
									SendEmail.sendEmail(login, password, message.getFrom()[0].toString(), subject,
										"votre devoir à bien été reçu");
								} else {
									System.out.println("c'est pas le bon format du zip");
								}
							}
						} else {
							System.out.println("c'est pas le bon objet");
						}
						if (emailFolder.isOpen()) {
							emailFolder.close(false);
						}
					} catch (final Exception e) {
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
