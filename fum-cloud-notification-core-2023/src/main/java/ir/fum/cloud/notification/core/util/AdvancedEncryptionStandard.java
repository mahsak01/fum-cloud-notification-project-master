package ir.fum.cloud.notification.core.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author Ali Mojahed on 11/22/2022
 * @project notise
 **/

public class AdvancedEncryptionStandard {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final String MY_KEY = BeanUtil.getProperty("encryption.secret.key", String.class);
    private static final String TRANSFORMATION_INFO = "AES/GCM/NoPadding";
    private static final String ALGORITHM = "AES";
    private static final String CHAR_SET_NAME = "UTF-8";
    private static Logger logger = LogManager.getLogger(AdvancedEncryptionStandard.class);

    public static SecretKey getKey() {
        SecretKey secretKey = null;

        try {
            byte[] key = MY_KEY.getBytes(CHAR_SET_NAME);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            secretKey = new SecretKeySpec(key, ALGORITHM);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.log(Level.WARN, "An error at getting key: {}", e.getMessage());
        }

        return secretKey;
    }


    public static String encrypt(String strToEncrypt) {
        String encoded = null;

        try {
            SecretKey secretKey = getKey();

            byte[] iv = new byte[GCM_IV_LENGTH];
            (new SecureRandom()).nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION_INFO);
            GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes(CHAR_SET_NAME));
            byte[] encrypted = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encrypted, 0, iv.length);
            System.arraycopy(cipherText, 0, encrypted, iv.length, cipherText.length);

            encoded = Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            logger.log(Level.DEBUG, "Error while encrypting: {}", e.getMessage());
        }

        return encoded;
    }


    public static String decrypt(String strToDecrypt) {
        String result = null;

        try {
            SecretKey secretKey = getKey();

            byte[] decoded = Base64.getDecoder().decode(strToDecrypt);

            byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION_INFO);
            GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] cipherText = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);

            result = new String(cipherText, CHAR_SET_NAME);

        } catch (Exception e) {
            logger.log(Level.DEBUG, "Error while decrypting: {}", e.getMessage());
        }

        return result;
    }
}
