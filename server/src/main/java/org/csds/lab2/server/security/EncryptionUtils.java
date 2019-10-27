package org.csds.lab2.server.security;

import org.csds.lab2.server.dto.PublicKey;

import java.security.SecureRandom;
import java.util.Arrays;

public class EncryptionUtils {

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
        for (int i = 0; i < 24; i++) {
            key.append(Character.toString((char) (r.nextInt(26) + 97)));
        }
        return key.toString();
    }

    public static String encrypt(String value, PublicKey key) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (i > 0) {
                encrypted.append(" ");
            }
            encrypted.append(pow((int) value.charAt(i), key.getE(), key.getN()));
        }
        return encrypted.toString();
    }
}
