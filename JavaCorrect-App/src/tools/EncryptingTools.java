package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptingTools {

	/**
	 * Encrypts a message with specified algorithm
	 * @param clearText : a message to be encrypted
	 * @param algorithm : algorithm encryption ex: "SHA-256"
	 * @return encrypted message
	 */
	public static String clearTextToEncrypted(final String clearText, final String algorithm) {
		final StringBuffer result = new StringBuffer();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(algorithm);
			md.update(clearText.getBytes());
			final byte[] bytes = md.digest();
			for (final byte b : bytes) {
				result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result.toString();
	}
}
