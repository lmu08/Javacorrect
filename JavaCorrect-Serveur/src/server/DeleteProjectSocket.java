package server;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DeleteProjectSocket implements Runnable {

	private String outputfileBase;
	final static String SEPARATOR = "/";

	private int port;
	ServerSocket socket;
	Socket c;

	DeleteProjectSocket(int port, String filePath) {
		this.outputfileBase = filePath;
		this.port = port;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.socket = new ServerSocket(this.port);
			while(true) {
				System.out.println("Serveur: en attente");
				this.c = this.socket.accept();
				System.out.println("Serveur: Connexion établie");
				receiveFile(c);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				this.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void receiveFile(Socket c) throws IOException {
		FileOutputStream fos;
		BufferedOutputStream bos;
		// TODO Auto-generated method stub
		InputStream is = c.getInputStream();
		OutputStream os = c.getOutputStream();
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
		

	}
	public void disconnect() throws IOException {
		if(this.c.isClosed()) {
			this.c.close();
			System.out.println("Serveur : Closing client socket");
		}
		if(!this.socket.isClosed()) {
			this.socket.close();
			System.out.println("Serveur : Closing server socket");
		}
	}
}
