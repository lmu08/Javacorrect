package server;

//import mail.ReceiveEmail;

public class Main {
	public static void main(final String[] args) {
		try {
			final Thread receiveInputFile = new Thread(new ReceiveInputFileSocket(52112, "/home/flo"));
			receiveInputFile.start();
			
			final Thread receiveDeleteProjRequest = new Thread(new ReceiveDeleteProjectSocket(52113, "/home/flo"));
			receiveDeleteProjRequest.start();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
}
