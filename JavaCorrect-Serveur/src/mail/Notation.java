package mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import db.MysqlRequest;

public class Notation {

	private final static String SEPARATOR = "/";

	private static final Runtime RUNTIME = Runtime.getRuntime();

	/**
	 * Noter les devoir des étudiants
	 *
	 * @param compilDirectory répertoire où le résultat de la compilation sera mit
	 * @param numEtu numéro de l'étudiant à noté
	 * @param date la date de réception d'un devoir
	 * @throws IOException qui gère les exceptions qui nous permet d'accéder à la base de donnée
	 * 			et de lancer des commande avec exec()
	 */
	
	public static void note(final String compilDirectory, final String numEtu, final String idProjet, final String args, final Date date)
			throws Exception {
		//Le répertoire de l'étudiant
		final String etuDirectory = compilDirectory + SEPARATOR + numEtu;
		final ResultSet projectDateRs = MysqlRequest.getDateExpiByidProjet(idProjet);
		projectDateRs.next();
		final java.sql.Date projectDate = projectDateRs.getDate("dateExpi");
		final boolean malus = projectDate.before(date);

		//La compilation et l'éxécution du devoir, redirection du résultat dans un fichier
		RUNTIME.exec("./javacShell " + etuDirectory + " " + args);
		final String output = Files.list(Paths.get(compilDirectory)).map(Path::getFileName).map(Path::toString).filter(path -> path.endsWith(".txt")).findAny().orElse("");
		System.out.println("Comparaison avec le fichier du prof : " + output);
		
		//Commande qui sert à comparer entre le fichier du professeur et la sorti du devoir d'un étudiant
		final String cmd = "diff -qEbB " + compilDirectory + "/" + output + " " + etuDirectory + "/testEtu";
		System.out.println(cmd);
		TimeUnit.SECONDS.sleep(5);
		
		//Sauvegarde des notes dans la base de donnée
		if (diffFichier(cmd)) {
			System.out.println("0");
			MysqlRequest.updateNote(0.0, idProjet, Integer.valueOf(numEtu));
		} else {
			System.out.println("20");
			MysqlRequest.updateNote(malus ? 15.0 : 20.0, idProjet, Integer.valueOf(numEtu));
		}
	}

	/**
	 * Comparaison de deux fichiers en prenant en compte les tabulations, les blancs,
	 *  et les passages à la ligne
	 * 
	 * @param cmd la commande à exécuter pour effectuer la comparaison
	 * @return true si les fichier sont différents / false dans le cas contraire
	 * @throws IOException qui gére les exception produit par ProcessBuilder et BufferedReader
	 */
	
	public static boolean diffFichier(final String cmd) throws IOException
	 {
		final ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));
		pb.redirectErrorStream(true);
		final Process proc = pb.start();
		final BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		final String line = br.readLine();
		System.out.println("line : " + line);
		return line != null;
	}
}
