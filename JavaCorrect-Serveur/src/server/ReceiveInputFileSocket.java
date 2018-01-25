package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import tools.SocketTools;

public class ReceiveInputFileSocket implements Runnable {

	private String outputfileBase;
	final static String SEPARATOR = "/";

	private int port;
	ServerSocket socket;
	Socket c;

	ReceiveInputFileSocket(int port, String filePath) {
		this.outputfileBase = filePath;
		this.port = port;
		
	}

	@Override
	public void run() {
		try {
			this.socket = new ServerSocket(this.port);
			while(true) {
				System.out.println("Serveur: en attente");
				this.c = this.socket.accept();
				System.out.println("Serveur: Connexion établie");
				receiveFile(c);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		finally {
			try {
				SocketTools.disconnect(this.c, this.socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void receiveFile(Socket c) throws IOException {
		FileOutputStream fos;
		InputStream is = c.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		DataOutputStream dos = new DataOutputStream(c.getOutputStream());

		byte repClientByte[] = new byte[36];
		try {
			is.read(repClientByte, 0, 36);
		} catch (IOException ioe) {

			ioe.printStackTrace();
		}
		String repClient = new String(repClientByte).toString();
		String outputFolder = this.outputfileBase + SEPARATOR + repClient;
		File dir = new File(outputFolder);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String outputFile = outputFolder + SEPARATOR + "output.txt";

		System.out.println(this.outputfileBase);
		int matches = repClient.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}") ? 1 : 0;
		dos.writeInt(matches);
		int sizeExcpected = dis.readInt();
		System.out.println(sizeExcpected);

		dos.writeInt(1);

		fos = new FileOutputStream(outputFile);
		byte[] buffer = new byte[sizeExcpected];

		int filesize = sizeExcpected; // Send file size in separate msg
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

		File file = new File(outputFile);
		int wellTransfered = (file.length() == sizeExcpected) ? 1 : 0;
		dos.writeInt(wellTransfered);

	}
	
}
