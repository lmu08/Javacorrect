package tools;

import java.io.IOException;
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
	 * close a connection through a socket from client side 
	 * @param c : specified client side socket to be close
	 * @throws IOException
	 */
	public static void disconnect(final Socket c)
	throws IOException {
		System.out.println(c.isConnected());
		if (c.isConnected()) {
			c.close();
		}
	}
}
