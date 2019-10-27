package org.csds.lab2.server.security;

import org.apache.commons.codec.binary.Base64;
import org.csds.lab2.server.dto.PublicKey;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class EncryptionUtils {

    public static String generateSessionKey() {
        SecureRandom r = new SecureRandom();
        byte[] aesKey = new byte[16];
        r.nextBytes(aesKey);
        return Arrays.toString(aesKey);
    }

    public static String encrypt(String value, PublicKey key) {
        try {
            RSAPublicKeyImpl rsaPublicKey =
                    new RSAPublicKeyImpl(new BigInteger(String.valueOf(key.getN())), new BigInteger(String.valueOf(key.getE())));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            return Base64.encodeBase64String(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));

        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Cannot encrypt session key.");
            e.printStackTrace();
            return value;
        }
    }
}
