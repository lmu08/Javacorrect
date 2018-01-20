package application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ToolsMethods {

	public static String clearTextToEncrypted(String clearText, String algorithm) {
		StringBuffer result = new StringBuffer();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(algorithm);
			md.update(clearText.getBytes());
	        byte[] bytes =  md.digest();
		    for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
       
	    return result.toString();
	}
}
