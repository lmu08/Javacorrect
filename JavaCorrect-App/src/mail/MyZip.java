package mail;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MyZip {

	/**
     * décompresse le fichier zip dans le répertoire donné
     * @param file le fichier zip à décompresser
     * @param folder le répertoire où les fichiers seront extraits
     * @param deleteZipAfter un boolean
     * @throws FileNotFoundException
     * @throws IOException
     */
	
	// Décompresse un fichier zip à l'adresse indiquée par le dossier
	 	public static void decompress(final File file, final File folder,final boolean deleteZipAfter)
	     throws IOException {
	         final ZipInputStream zis = new ZipInputStream(new BufferedInputStream(
	         		new FileInputStream(file.getCanonicalFile())));
	         ZipEntry ze;
	         try {    
	 	        // Parcourt tous les fichiers
	 	        while (null != (ze = zis.getNextEntry())) {
	 	            final File f = new File(folder.getCanonicalPath(), ze.getName());
	 	            if (f.exists())
	 	            	f.delete();
	 	            
	 	            // Création des dossiers
	 	            if (ze.isDirectory()) {
	 	                f.mkdirs();
	 	                continue;
	 	            }
	 	            f.getParentFile().mkdirs();
	 	            final OutputStream fos = new BufferedOutputStream(
	 	            		new FileOutputStream(f));
	 	            
	 	            // Ecriture des fichiers
	 	            try {
	 	                try {
	 	                    final byte[] buf = new byte[8192];
	 	                    int bytesRead;
	 	                    while (-1 != (bytesRead = zis.read(buf)))
	 	                        fos.write(buf, 0, bytesRead);
	 	                } finally {
	 	                    fos.close();
	 	                }
	 	            } catch (final IOException ioe) {
	 	                f.delete();
	 	                throw ioe;
	 	            }
	 	        }
	         } finally {
	         	zis.close();
	         }
	         if (deleteZipAfter)
	         	file.delete();
	     }
}
