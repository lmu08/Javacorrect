package tools;

import java.io.IOException;
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
	
	public static void disconnect(final Socket c)
	throws IOException {
		if (c.isConnected()) {
			c.close();
		}
	}
}
