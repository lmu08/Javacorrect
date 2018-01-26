package mail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Notation {

	private final static String SEPARATOR = "/";

	private static final Runtime RUNTIME = Runtime.getRuntime();

	/**
	 * noter les devoir des étudiants
	 *
	 * @param compilDirectory
	 *            répertoire où le résulat de la compilation sera mit
	 * @param numEtu
	 *            numéro de l'étudiant a noté
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
			ecrir_ligne_fichier(compilDirectory, numEtu, 0, "test");
		} else {
			System.out.println("20");
			ecrir_ligne_fichier(compilDirectory, numEtu, 20, "/listeEtu.csv");
		}
	}

	// br2 reçois true si les fichier sont déffirents / false dans le cas contraire
	public static boolean diffFichier(final String cmd) throws Exception {

		ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));
		pb.redirectErrorStream(true);
		Process proc = pb.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = br.readLine();
		System.out.println("line : " + line);
		if (line == null) // pas diff
			return false;
		else
			return true;

	}

	public static void ecrir_ligne_fichier(final String directory, final String numEtu, final int note,
			final String listeEtu) throws FileNotFoundException, IOException {
		final String directory2 = directory + listeEtu;
		final String l = null;
		String ligne = null;
		int compt = 0;

		final FileReader fichiergraph = new FileReader(directory2);
		final BufferedReader br = new BufferedReader(fichiergraph);
		final Path path = Paths.get(directory2);
		final List<String> lines = Files.readAllLines(path);

		String newLigne = "";
		while ((ligne = br.readLine()) != null) {
			compt++;
			if (ligne.endsWith("3603567")) {
				System.out.println("ligne : " + ligne);
				newLigne = ligne + "," + note;
				System.out.println("newLigne : " + newLigne);
				lines.add(3, newLigne); // index 3: between 3rd and 4th line
				Files.write(path, lines);
			}
		}
		br.close();
	}
}
