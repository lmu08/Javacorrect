package tools;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTools {
	private final static int TRUE = 1;

	/**
	 * @param binaryInt : a integer 0 for false, 1 for true
	 * @return boolean value of integer
	 */
	public static boolean getBoolean(final int binaryInt) {
		if (binaryInt == TRUE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * close a connection through a socket from server side and
	 * close associated server socket
	 * @param c specified client side socket to be close
	 * @param socket close server socket releasing socket port
	 * @throws IOException
	 */
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

	/**
	 * Deletes recursively the file specified in argument
	 * @param file to delete
	 * @throws IOException
	 */
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
