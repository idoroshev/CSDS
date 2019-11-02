package org.csds.lab2.server.security;

import org.csds.lab2.server.dto.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionUtils {

    private static SecretKeySpec secretKey;

    private static int pow(int a, int b, int m) {
        int ans = 1;
        while (b > 0) {
            if ((b & 1) == 0) {
                a = (a * a) % m;
                b >>= 1;
            } else {
                ans = (ans * a) % m;
                b--;
            }
        }
        return ans;
    }

    public static String generateSessionKey() {
        SecureRandom r = new SecureRandom();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            key.append(Character.toString((char) (r.nextInt(26) + 97)));
        }
        return key.toString();
    }

    public static String encryptSessionKey(String value, PublicKey key) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (i > 0) {
                encrypted.append(" ");
            }
            encrypted.append(pow((int) value.charAt(i), key.getE(), key.getN()));
        }
        return encrypted.toString();
    }

    private static void setKey(String key) {
        byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        secretKey = new SecretKeySpec(byteKey, "AES");
    }

    @SuppressWarnings("SameParameterValue")
    private static byte[][] generateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {

        int digestLength = md.getDigestLength();
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        byte[] generatedData = new byte[requiredLength];
        int generatedLength = 0;

        try {
            md.reset();

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0)
                    md.update(generatedData, generatedLength - digestLength, digestLength);
                md.update(password);
                if (salt != null)
                    md.update(salt, 0, 8);
                md.digest(generatedData, generatedLength, digestLength);

                // additional rounds
                for (int i = 1; i < iterations; i++) {
                    md.update(generatedData, generatedLength, digestLength);
                    md.digest(generatedData, generatedLength, digestLength);
                }

                generatedLength += digestLength;
            }

            // Copy key and IV into separate byte arrays
            byte[][] result = new byte[2][];
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
            if (ivLength > 0)
                result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

            return result;

        } catch (DigestException e) {
            throw new RuntimeException(e);

        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, (byte) 0);
        }
    }

    public static String encryptByAES(String value, String key) {
        setKey(key);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByAES(String value, String key) {
        try {
            byte[] cipherData = Base64.getDecoder().decode(value);
            byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, key.getBytes(StandardCharsets.UTF_8), md5);
            SecretKeySpec secretKey = new SecretKeySpec(keyAndIV[0], "AES");
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

            byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
            Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCBC.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] decryptedData = aesCBC.doFinal(encrypted);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
