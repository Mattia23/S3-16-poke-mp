package cryptography;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AesEncryptor implements Encryptor {

	public static final int REQUIRED_KEY_LENGTH = 16;
	public static final String ALGORITHM = "AES";
	
	private Cipher encryptor;
	private Cipher decryptor;

	public AesEncryptor(final SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		this.encryptor = Cipher.getInstance(ALGORITHM);
		this.decryptor = Cipher.getInstance(ALGORITHM);
		this.encryptor.init(Cipher.ENCRYPT_MODE, key);
		this.decryptor.init(Cipher.DECRYPT_MODE, key);
	}

	@Override
	public String decrypt(final String s) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		byte[] dec = Base64.getDecoder().decode(s);
		byte[] utf8 = this.decryptor.doFinal(dec);
		return new String(utf8, "UTF8");
	}

	@Override
	public String encrypt(final String s) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		final byte[] utf8 = s.getBytes("UTF8");
		byte[] enc = this.encryptor.doFinal(utf8);
		return Base64.getEncoder().encodeToString(enc);
	}
}