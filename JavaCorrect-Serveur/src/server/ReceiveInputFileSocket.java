package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import tools.SocketTools;

public class ReceiveInputFileSocket
implements Runnable {

	private final String outputBasePath;
	private final static String SEPARATOR = "/";

	private final int port;
	private ServerSocket socket;
	private Socket c;
	private String idProjet;

	/**
	 * Receive file sent by a client through a socket
	 * @param port of the socket
	 * @param outputBasePath : base path of the output
	 */
	public ReceiveInputFileSocket(final int port, final String outputBasePath) {
		this.outputBasePath = outputBasePath;
		this.port = port;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		try {
			this.socket = new ServerSocket(this.port);
			while (true) {
				System.out.println("Reception de fichier d'entrée: en attente");
				this.c = this.socket.accept();
				System.out.println("Serveur: Connexion établie");
				receiveFile(c);
				TimeUnit.SECONDS.sleep(1);
				MailCreation.mailCreation(idProjet);
			}
		} catch (IOException | InterruptedException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				SocketTools.disconnect(this.c, this.socket);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Receive a file through socket send by a client
	 * Step 1: Pulls idProjet send by client
	 * Step 2: Creates a directory with idProjet as name like this <BASEPATH>/<idProjet>
	 * Step 3: Sends a boolean to client indicating if works
	 * Step 4: Pulls size of file
	 * Step 5: Sends a boolean to client indicating if works
	 * Step 6: Pulls file and create it at this path <BASEPATH>/<idProjet>
	 * Step 7: Sends a boolean to client indicating if file has been successfully transfered
	 * 
	 * @param c : socket with client
	 * @throws IOException
	 */
	private void receiveFile(final Socket c)
	throws IOException {
		FileOutputStream fos;
		try (final InputStream is = c.getInputStream(); //
		final DataInputStream dis = new DataInputStream(is); //
		final DataOutputStream dos = new DataOutputStream(c.getOutputStream());) {
			
			final byte idProjetByte[] = new byte[36];
			try {
				is.read(idProjetByte, 0, 36);
			} catch (final IOException ioe) {
				
				ioe.printStackTrace();
			}
			final String idProjet = new String(idProjetByte).toString();
			this.idProjet = idProjet;
			final String outputFolder = this.outputBasePath + SEPARATOR + idProjet;
			final File dir = new File(outputFolder);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			final String outputFile = outputFolder + SEPARATOR + "output.txt";
			
			System.out.println(this.outputBasePath);
			final int matches = idProjet.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}") ? 1 : 0;
			dos.writeInt(matches);
			final int sizeExcpected = dis.readInt();
			System.out.println(sizeExcpected);
			
			dos.writeInt(1);
			
			fos = new FileOutputStream(outputFile);
			final byte[] buffer = new byte[sizeExcpected];
			
			final int filesize = sizeExcpected; // Send file size in separate msg
			int read = 0;
			int totalRead = 0;
			int remaining = filesize;
			while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
				totalRead += read;
				remaining -= read;
				System.out.println("read " + totalRead + " bytes.");
				fos.write(buffer, 0, read);
			}
			fos.close();
			
			final File file = new File(outputFile);
			final int wellTransfered = (file.length() == sizeExcpected) ? 1 : 0;
			dos.writeInt(wellTransfered);
		}
	}
	
}
