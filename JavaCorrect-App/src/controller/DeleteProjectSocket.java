package controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

import tools.SocketTools;

public class DeleteProjectSocket
implements Callable<Boolean> {
	private final String projName;
	private final int port;
	private final String host;
	private Socket c;
	boolean isConnected;

	DeleteProjectSocket(final String host, final int port, final String projName) {
		this.projName = projName;
		this.port = port;
		this.host = host;
		this.isConnected = false;
	}

	@Override
	public Boolean call() {
		boolean res = false;
		final InetSocketAddress server = new InetSocketAddress(this.host, this.port);
		this.c = new Socket();
		try {
			c.connect(server, this.port);
			System.out.println("Client : Connexion établie");
			res = sendFile(c);
			SocketTools.disconnect(this.c);
		} catch (final IOException e) {
			return false;
		}
		return res;
	}

	private boolean sendFile(final Socket c)
	throws IOException {
		try (final InputStream is = c.getInputStream(); //
		final DataInputStream dis = new DataInputStream(is); //
		final OutputStream os = c.getOutputStream();) {

			os.write(this.projName.getBytes("UTF8"));
			dis.readInt();
			return (dis.readInt() == 1) ? true : false;
		}
	}
}
