package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

import tools.SocketTools;

public class SendOutputFileSocket
implements Callable<Boolean> {
	private final String filePath;
	private final String projName;
	private final int port;
	private final String host;
	private final static int FALSE = 1;
	Socket c;
	boolean isConnected;

	SendOutputFileSocket(final String host, final int port, final String filePath, final String projName) {
		this.filePath = filePath;
		this.projName = projName;
		this.port = port;
		this.host = host;
		this.isConnected = false;
	}

	@Override
	public Boolean call()
	throws Exception {
		boolean res = false;
		final InetSocketAddress server = new InetSocketAddress(this.host, this.port);
		this.c = new Socket();
		try {
			c.connect(server, this.port);
			res = sendFile(c);
			SocketTools.disconnect(this.c);
		} catch (final IOException e) {
			//			this.disconnect();
			return false;
		}
		return res;
	}

	private boolean sendFile(final Socket c)
	throws IOException {
		final File file = new File(this.filePath);
		
		try (final InputStream is = c.getInputStream(); //
		final DataInputStream dis = new DataInputStream(is); //
		OutputStream os = c.getOutputStream(); //
		final DataOutputStream dos = new DataOutputStream(os);) {
			
			os.write(this.projName.getBytes("UTF8"));
			
			if (!SocketTools.getBoolean(FALSE)) {
				//			closeStream(os, is);
				return false;
			}
			
			// creating object to send file
			final byte[] byteArray = new byte[(int) file.length()];
			
			dos.writeInt(byteArray.length);
			dis.readInt();
			
			final FileInputStream fis = new FileInputStream(file);
			while (fis.read(byteArray) > 0) {
				dos.write(byteArray);
			}
			fis.close();
			
			// sending file through socket
			System.out.println("Sending " + this.filePath + "( size: " + byteArray.length + " bytes)");
			os.write(byteArray, 0, byteArray.length); // copying byteArray to socket
			os.flush(); // flushing socket
			System.out.println("Done.");
			
			boolean res = true;
			try {
				Thread.sleep(2500);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!SocketTools.getBoolean(dis.readInt())) {
				res = false;
			}
			return res;
		}
	}

}
