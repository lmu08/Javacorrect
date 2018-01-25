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
	
	// static String saveDirectory = "/home/katy/projet/";
	
	/**
	 * noter les devoir des étudiants
	 *
	 * @param compilDirectory
	 *        répertoire où le résulat de la compilation sera mit
	 * @param numEtu
	 *        numéro de l'étudiant a noté
	 * @throws IOException
	 */
	static Runtime runtime = Runtime.getRuntime();
	
	public static void note(final String compilDirectory, final String numEtu, final String idProjet, final String args)
	throws Exception {
		
		// le répertoi de l'étudiant
		final String etuDirectory = compilDirectory + "/" + numEtu + "/" + idProjet;
		runtime.exec("./javacShell " + etuDirectory + " " + compilDirectory);
		
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
	public static boolean diffFichier(final String cmd)
	throws Exception {
		final Process proc = runtime.exec(cmd);
		final BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		final String line;
		return br.ready();
	}
	
	public static void ecrir_ligne_fichier(String Directory, final String numEtu, final int note, final String listeEtu)
	throws FileNotFoundException, IOException {
		
		Directory = Directory + listeEtu;
		final String l = null;
		String ligne = null;
		
		int compt = 0;
		
		final FileReader fichiergraph = new FileReader(Directory);
		
		final BufferedReader br = new BufferedReader(fichiergraph);
		
		final Path path = Paths.get(Directory);
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
