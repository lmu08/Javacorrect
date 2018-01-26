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
	 * noter les devoir des étudiants
	 *
	 * @param compilDirectory
	 *        répertoire où le résulat de la compilation sera mit
	 * @param numEtu
	 *        numéro de l'étudiant a noté
	 * @param date
	 * @throws IOException
	 */
	public static void note(final String compilDirectory, final String numEtu, final String idProjet, final String args, final Date date)
	throws Exception {
		// le répertoi de l'étudiant
		final String etuDirectory = compilDirectory + SEPARATOR + numEtu;
		final ResultSet projectDateRs = MysqlRequest.getDateExpiByidProjet(idProjet);
		projectDateRs.next();
		final java.sql.Date projectDate = projectDateRs.getDate("dateExpi");
		final boolean malus = projectDate.before(date);

		RUNTIME.exec("./javacShell " + etuDirectory + " " + args);
		final String output = Files.list(Paths.get(compilDirectory)).map(Path::getFileName).map(Path::toString).filter(path -> path.endsWith(".txt")).findAny().orElse("");
		System.out.println("Comparaison avec le fichier du prof : " + output);
		final String cmd = "diff -qEbB " + compilDirectory + "/" + output + " " + etuDirectory + "/testEtu";
		System.out.println(cmd);
		TimeUnit.SECONDS.sleep(5);
		if (diffFichier(cmd)) {
			System.out.println("0");
			MysqlRequest.updateNote(0.0, idProjet, Integer.valueOf(numEtu));
		} else {
			System.out.println("20");
			MysqlRequest.updateNote(malus ? 15.0 : 20.0, idProjet, Integer.valueOf(numEtu));
		}
	}

	// br2 reçois true si les fichier sont déffirents / false dans le cas contraire
	public static boolean diffFichier(final String cmd)
	throws Exception {
		final ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));
		pb.redirectErrorStream(true);
		final Process proc = pb.start();
		final BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		final String line = br.readLine();
		System.out.println("line : " + line);
		return line != null;
	}
}
