package server;

//import mail.ReceiveEmail;

public class Main {
	private static final String USER_HOME = System.getProperty("user.home");
	
	public static void main(final String[] args) {
		try {
			final Thread receiveInputFile = new Thread(new ReceiveInputFileSocket(52112, USER_HOME));
			receiveInputFile.start();
			
			final Thread receiveDeleteProjRequest = new Thread(new ReceiveDeleteProjectSocket(52113, USER_HOME));
			receiveDeleteProjRequest.start();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
}
