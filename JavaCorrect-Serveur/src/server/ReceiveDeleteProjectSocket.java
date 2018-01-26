package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import tools.SocketTools;

public class ReceiveDeleteProjectSocket
implements Runnable {
	
	private final String outputBasePath;
	private final static String SEPARATOR = "/";
	
	private final int port;
	private ServerSocket socket;
	private Socket c;
	
	/**
	 * Receive order to delete files related to a project sent by a client through a socket 
	 * delete project with idSent
	 * 
	 * @param port of the socket
	 * @param outputBasePath : base path of the output
	 */
	public ReceiveDeleteProjectSocket(final int port, final String outputBasePath) {
		this.outputBasePath = outputBasePath;
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			this.socket = new ServerSocket(this.port);
			while (true) {
				System.out.println("Serveur reception de requête de suppresion de projet: en attente");
				this.c = this.socket.accept();
				System.out.println("Serveur: Connexion établie");
				deleteProjectFile(c);
				TimeUnit.SECONDS.sleep(1);
			}
		} catch (IOException | InterruptedException e) {
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
	 * Receive a order to delete all files related to a project
	 * through socket send by a client and delete it
	 * Step 1: Pulls idProjet
	 * Step 2: Deletes recursively the folder containing all data about a project
	 * Step 3: Sends a boolean answer to the server indicating if project has been successfully deleted
	 * 
	 * @param socket with the client
	 * @throws IOException
	 */
	private void deleteProjectFile(final Socket c)
	throws IOException {

		boolean deleted = false;
		final InputStream is = c.getInputStream();
		final DataOutputStream dos = new DataOutputStream(c.getOutputStream());
		
		final byte idProjetByte[] = new byte[36];
		try {
			is.read(idProjetByte, 0, 36);
		} catch (final IOException ioe) {
			
			ioe.printStackTrace();
		}
		final String idProjet = new String(idProjetByte).toString();
		if (idProjet.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
			final String outputFolder = this.outputBasePath + SEPARATOR + idProjet;
			System.out.println(outputFolder);
			final File dir = new File(outputFolder);
			if (dir.exists()) {
				SocketTools.delete(dir);
				if (!dir.exists()) {
					System.out.println("Successfully deleted");
					deleted = true;
				}
			} else {
				deleted = true;
			}
		}
		dos.writeInt((deleted) ? 1 : 0);
		
	}
}
