package cryptography;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * MyEncryptor allows to encrypt and decrypt the password stored in the database
 */
public class MyEncryptor {

	public static final String ALGORITHM = "AES";
	public static final int REQUIRED_KEY_LENGTH = AesEncryptor.REQUIRED_KEY_LENGTH;
	private static final String DEFAULT_PASSWORD = "yxc537y90wFEeQLz";
	private static Encryptor encryptor;

	private MyEncryptor() {
		try {
			final SecretKey secretKey = new SecretKeySpec(DEFAULT_PASSWORD.getBytes(), ALGORITHM);
			encryptor = new AesEncryptor(secretKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	public static void init() {
		if(encryptor == null) {
			new MyEncryptor();
		}
	}
	
	public static String encrypt(final String s) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		return encryptor.encrypt(s);
	}

	public static String decrypt(final String s) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		return encryptor.decrypt(s);
	}


}