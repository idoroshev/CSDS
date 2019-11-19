package org.csds.lab2.server.security;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.csds.lab2.server.dto.PublicKey;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class EncryptionUtils {

    public static final String initVector = "RandomInitVector";

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

    public static String generateRandomKey() {
        return generateRandomKey(16);
    }

    public static String generateRandomKey(int length) {
        SecureRandom r = new SecureRandom();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < length; i++) {
            key.append(Character.toString((char) (r.nextInt(26) + 97)));
        }
        return key.toString();
    }

    public static String encryptByRSA(String value, PublicKey key) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (i > 0) {
                encrypted.append(" ");
            }
            encrypted.append(pow((int) value.charAt(i), key.getE(), key.getN()));
        }
        return encrypted.toString();
    }

    public static String encryptByAES(String value, String key) {
        try {
            byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = initVector.getBytes(StandardCharsets.UTF_8);
            KeyParameter keyParam = new KeyParameter(keyBytes);
            CipherParameters params = new ParametersWithIV(keyParam, ivBytes);
            BlockCipherPadding padding = new PKCS7Padding();
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                    new CBCBlockCipher(new AESEngine()), padding);
            cipher.reset();
            cipher.init(true, params);
            byte[] buffer = new byte[cipher.getOutputSize(valueBytes.length)];
            int len = cipher.processBytes(valueBytes, 0, valueBytes.length, buffer, 0);
            len += cipher.doFinal(buffer, len);
            byte[] out = Arrays.copyOfRange(buffer, 0, len);
            return Hex.encodeHexString(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByAES(String value, String key) {
        try {
            byte[] valueBytes = Hex.decodeHex(value.toCharArray());
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = initVector.getBytes(StandardCharsets.UTF_8);
            KeyParameter keyParam = new KeyParameter(keyBytes);
            CipherParameters params = new ParametersWithIV(keyParam, ivBytes);
            BlockCipherPadding padding = new PKCS7Padding();
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                    new CBCBlockCipher(new AESEngine()), padding);
            cipher.reset();
            cipher.init(false, params);
            byte[] buffer = new byte[cipher.getOutputSize(valueBytes.length)];
            int len = cipher.processBytes(valueBytes, 0, valueBytes.length, buffer, 0);
            len += cipher.doFinal(buffer, len);
            byte[] out = Arrays.copyOfRange(buffer, 0, len);
            return new String(out, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
