package mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	 * @throws IOException
	 */
	public static void note(final String compilDirectory, final String numEtu, final String idProjet, final String args)
	throws Exception {
		// le répertoi de l'étudiant
		final String etuDirectory = compilDirectory + SEPARATOR + numEtu + SEPARATOR + idProjet;
		RUNTIME.exec("./javacShell " + etuDirectory + SEPARATOR + compilDirectory + args);

		final String cmd = "diff -q " + compilDirectory + "/test" + compilDirectory + "/testEtu";

		if (diffFichier(cmd)) {
			System.out.println("0");
			//TODO save dans la db
		} else {
			System.out.println("20");
			//TODO save dans la bd
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
		if (line == null) {
			return false;
		} else {
			return true;
		}

	}
}
