package tools;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTools {
	private final static int TRUE = 1;
	
	public static boolean getBoolean(final int binaryInt) {
		if (binaryInt == TRUE) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void disconnect(final Socket c, final ServerSocket socket)
	throws IOException {
		if (c.isClosed()) {
			c.close();
			System.out.println("Serveur : Closing client socket");
		}
		if (!socket.isClosed()) {
			socket.close();
			System.out.println("Serveur : Closing server socket");
		}
	}
	
	public static void delete(final File file)
	throws IOException {
		for (final File childFile : file.listFiles()) {
			
			if (childFile.isDirectory()) {
				delete(childFile);
			} else {
				if (!childFile.delete()) {
					throw new IOException();
				}
			}
		}
		if (!file.delete()) {
			throw new IOException();
		}
	}
}
