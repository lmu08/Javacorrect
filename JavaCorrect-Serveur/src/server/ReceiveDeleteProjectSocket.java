package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import tools.SocketTools;

public class ReceiveDeleteProjectSocket
implements Runnable {

	private final String outputfileBase;
	final static String SEPARATOR = "/";

	private final int port;
	private ServerSocket socket;
	private Socket c;

	ReceiveDeleteProjectSocket(final int port, final String filePath) {
		this.outputfileBase = filePath;
		this.port = port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.socket = new ServerSocket(this.port);
			while (true) {
				System.out.println("Serveur: en attente");
				this.c = this.socket.accept();
				System.out.println("Serveur: Connexion établie");
				receiveFile(c);
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				SocketTools.disconnect(this.c, this.socket);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void receiveFile(final Socket c)
	throws IOException {
		
		boolean deleted = false;
		final InputStream is = c.getInputStream();
		final DataOutputStream dos = new DataOutputStream(c.getOutputStream());

		final byte repClientByte[] = new byte[36];
		try {
			is.read(repClientByte, 0, 36);
		} catch (final IOException ioe) {

			ioe.printStackTrace();
		}
		final String projName = new String(repClientByte).toString();
		if (projName.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
			final String outputFolder = this.outputfileBase + SEPARATOR + projName;
			System.out.println(outputFolder);
			final File dir = new File(outputFolder);
			if (dir.exists()) {
				SocketTools.delete(dir);
				if (!dir.exists()) {
					System.out.println("Successfully deleted");
					deleted = true;
				}
			}
		}
		dos.writeInt((deleted) ? 1 : 0);

	}
}
