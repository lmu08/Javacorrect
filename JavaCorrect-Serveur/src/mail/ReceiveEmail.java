package mail;

import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

import db.MysqlRequest;

public class ReceiveEmail {
	private final static String SEPARATOR = "/";
	
	static String saveDirectory;
	
	/**
	 * Reception des mails, récupération des devoirs, suppression de chaque mail,
	 * création d'un thread pour chaque mail
	 * un appel à la fonction notation() pour chaque devoir récupérer 
	 * 
	 * @param login mail où on récupère les devoir envoyer
	 * @param password
	 * @throws Exception qui gère les exceptions du thread 
	 * 
	 */
	public static void receiveEmail(final String login, final String password)
			throws Exception {

		final Properties imapProps = MailPropertiesParser.getInstance().getImapProperties();
		List<Thread> listThread = new ArrayList<>();

		try {
			final IMAPStore emailStore = (IMAPStore) Session.getInstance(imapProps).getStore("imap");
			emailStore.connect(login, password);

			//Création du folder object
			final Folder emailFolder = emailStore.getFolder("INBOX");
			emailFolder.open(Folder.READ_WRITE);

			//Récupération des mail à partir du folder
			final Message[] messages = emailFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {

				final Message message = messages[i];

				//création d'un thread pour chaque mail
				final Thread e = emailcontrol(emailFolder, login, password, message);
				listThread = new LinkedList<>();
				listThread.add(e);
			}

			//Attente des threads
			for (final Thread thread : listThread) {
				thread.join();
			}

			//Fermeture du emailFolder 
			if (emailFolder.isOpen()) {
				emailFolder.close(false);
			}
			emailStore.close();

		} catch (final MessagingException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Pour chaque mail on crée un thread pour gagner du temps 
	 * 
	 * @param emailFolder le folder pour récupérer un mail   
	 * @param login 
	 * @param password
	 * @param message le mail que nous somme en train de traiter 
	 */
	private static Thread emailcontrol(final Folder emailFolder, final String login, final String password, 
			final Message message) {
		
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				if (true) {
					try {
						
						if (!emailFolder.isOpen()) {
							emailFolder.open(Folder.READ_WRITE);
						}

						final SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
						final Date date = format.parse(format.format(message.getReceivedDate()));

						final String subject = message.getSubject();
						System.out.println("sub : " + subject);
						
						final Pattern p = Pattern.compile(SEPARATOR);
						//Séparation du subject en sous-chaînes
						final String[] items = p.split(subject, 10);
						
						// f > chemin home 
						final FileSystemView fsv = FileSystemView.getFileSystemView();
						final File f = fsv.getDefaultDirectory();
						
						//Vérification du subject 
						if (items.length >= 2) {
							final boolean matchSubj = MysqlRequest.checkProjectId(items[0], items[1]);
							
							//Si le idEtudant et idProjet sont dans la même table dans la base de donnée 
							if (matchSubj) {
								
								final String idProjet = items[0];
								final String idEtu = items[1];
								
								//le répertoire du dossier du projet du prof
								saveDirectory = f.toString() + SEPARATOR + idProjet;
								
								final Multipart multipart = (Multipart) message.getContent();
								System.out.println("nb de pièce joint : " + (multipart.getCount() - 1));
								
								//Récupération des pièce joint
								for (int j = 1; j < multipart.getCount(); j++) {
									
									final BodyPart bodyPart = multipart.getBodyPart(j);
									final MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(j);
									
									final String zipFile = Optional.ofNullable(bodyPart.getFileName()).orElse("");
									
									//Vérification de la pièce joint 
									//Compilation de la regex
									final Pattern patternFile = Pattern.compile("^[0-9]{7}+.zip$");
									// Création d'un moteur de recherche
									final Matcher matchNomFileZipe = patternFile.matcher(zipFile);
									
									System.out.println("si c'est le bon .zip : " + matchNomFileZipe.matches());
									
									final String numEtu = zipFile.substring(0, zipFile.indexOf("."));

									final boolean marchIdNumEtu = MysqlRequest.checknumEtiIDEtu(Integer.parseInt(numEtu) , idEtu);
									//le bon format du .zip et que le numEtu correspond à L'idEtu 
									if (matchNomFileZipe.matches() && marchIdNumEtu ) {
										
										//Création du dossier de l'étudiant
										new File(saveDirectory + SEPARATOR + numEtu).mkdir();
										
										final String messageContent = part.getContent().toString();
										System.out.println("Message : " + messageContent);
										
										// move Files
										if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
											// this part is attachment
											final String fileName = part.getFileName();
											part.saveFile(saveDirectory + SEPARATOR + fileName);
										} else {
											// this part may be the message content
											System.out.println(messageContent);
										}
										
										// Unzip
										final File zipFilePath = new File(saveDirectory + SEPARATOR + zipFile);
										final File destDir = new File(saveDirectory + SEPARATOR + numEtu);
										
										MyZip.decompress(zipFilePath, destDir, true);
										final ResultSet rsArgs = MysqlRequest.getArguments(idProjet);
										rsArgs.next();
										final String args = rsArgs.getString("arguments");
										System.out.println(saveDirectory);
										
										//Notation du devoir
										Notation.note(saveDirectory, numEtu, idProjet, args, date);
										
										//Mail de confirmation 
										SendEmail.sendEmail(login, password, message.getFrom()[0].toString(), subject,
											"votre devoir à bien été reçu");
									} else {
										System.out.println("c'est pas le bon format du zip");
									}
								}
							} else {
								System.out.println("c'est pas le bon objet");
							}
						} else {
							System.out.println("c'est pas le bon objet");
						}
						//Suppression du mail
						message.setFlag(Flags.Flag.DELETED, true);
					} catch (final Exception e) {
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
