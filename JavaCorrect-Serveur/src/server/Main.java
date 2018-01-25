package server;

//import mail.ReceiveEmail;

public class Main {
	public static void main(String [] args) {
		try {
			Thread receiveInputFile = new Thread(new ReceiveInputFileSocket(52112, "/home/flo"));
			receiveInputFile.start();
			
			Thread receiveDeleteProjRequest = new Thread(new ReceiveDeleteProjectSocket(52113, "/home/flo"));
			receiveDeleteProjRequest.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
