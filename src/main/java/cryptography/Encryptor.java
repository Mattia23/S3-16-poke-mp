package cryptography;
import java.io.UnsupportedEncodingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public interface Encryptor {
	String encrypt(String s) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException;
	String decrypt(String s) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException;
}
