package server;

//import mail.ReceiveEmail;

public class Main {
	public static void main(String [] args) {
		try {
			Thread receiveInputFile = new Thread(new ReceiveInputFileSocket(52112, "/home/flo"));
			receiveInputFile.start();
//			ReceiveEmail.receiveEmail("java.correct@gmail.com", "789@Upmc");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
