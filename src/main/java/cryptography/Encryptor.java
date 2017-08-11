package cryptography;
import java.io.UnsupportedEncodingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public interface Encryptor {
	/**
	 * Get in input the password and return the string encrypted
	 * @param s the password
	 * @return the password encrypted
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	String encrypt(String s) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException;

	/**
	 * Get in input the password encrypted and return the string decrypted
	 * @param s the password encrypter
	 * @return the password
	 * @throws UnsupportedEncodingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	String decrypt(String s) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException;
}
