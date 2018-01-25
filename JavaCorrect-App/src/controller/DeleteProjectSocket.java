package controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

import tools.SocketTools;


public class DeleteProjectSocket implements Callable<Boolean> {

	private String projName;
	private int port;
	private String host;
	Socket c;
	boolean isConnected;

	DeleteProjectSocket(String host, int port, String projName) {
		this.projName = projName;
		this.port = port;
		this.host = host;
		this.isConnected = false;

	}

	@Override
	public Boolean call() throws Exception {
		boolean res = false;
		InetSocketAddress server = new InetSocketAddress(this.host, this.port);
		this.c = new Socket();
		try {
			c.connect(server, this.port);
			System.out.println("Client : Connexion établie");
			res = sendFile(c);
			SocketTools.disconnect(this.c);
		} catch (IOException e) {
			return false;
		}

		return res;
	}

	private boolean sendFile(Socket c) throws IOException {
		InputStream is = c.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		OutputStream os = c.getOutputStream();
		System.out.println("Envoi de au serveur");
		os.write(this.projName.getBytes("UTF8"));
		return (dis.readInt() == 1) ? true : false;
	}

}
