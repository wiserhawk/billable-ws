package com.indhawk.billable.billablews.utils;

import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils {

    @Value("${hash.salt}")
    private static String salt;

    public static byte[] getSHA(String value) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String text = value + salt;
        byte[] encodedHash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return encodedHash;
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
