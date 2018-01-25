package controller;

import java.io.BufferedInputStream;
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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SendOutputFileSocket implements Callable<Boolean> {

	private String filePath;
	private String projName;
	private int port;
	private String host;
	private final static int TRUE = 1;
	private final static int FALSE = 1;
	Socket c;
	boolean isConnected;

	SendOutputFileSocket(String host, int port, String filePath, String projName) {
		this.filePath = filePath;
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
			res = sendFile(c);
			this.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			this.disconnect();
			return false;
		}

		return res;
	}

	private boolean sendFile(Socket c) throws IOException {
		File file = new File(this.filePath);
		InputStream is = c.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		OutputStream os = c.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);

		os.write(this.projName.getBytes("UTF8"));

		if (!getBoolean(FALSE)) {
//			closeStream(os, is);
			return false;
		}

		// creating object to send file
		byte[] byteArray = new byte[(int) file.length()];

		dos.writeInt(byteArray.length);
		dis.readInt();

		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		while (fis.read(byteArray) > 0) {
			dos.write(byteArray);
		}
		fis.close();

		// sending file through socket
		os = c.getOutputStream();
		System.out.println("Sending " + this.filePath + "( size: " + byteArray.length + " bytes)");
		os.write(byteArray, 0, byteArray.length); // copying byteArray to socket
		os.flush(); // flushing socket
		System.out.println("Done.");

		boolean res = true;
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!getBoolean(dis.readInt())) {
			res = false;
		}


		return res;

	}

	public static boolean getBoolean(int binaryInt) {
		if (binaryInt == TRUE) {
			return true;
		} else {
			return false;
		}
	}

	public void disconnect() throws IOException {
		if (c.isConnected()) {
			c.close();
		}
	}

}
