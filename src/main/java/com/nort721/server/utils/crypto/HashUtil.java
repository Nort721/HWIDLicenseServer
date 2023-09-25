package com.nort721.server.utils.crypto;

import com.nort721.server.Main;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    /**
     * Hash a string
     * @param str the string
     * @param algorithm the hash algorithm
     * @return Returns a hashed string
     */
    public static String hashString(String str, String algorithm) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            return toHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            Main.logger.logMessage(" the hash algorithm that have been provided does not exists", true);
        }
        return null;
    }

    /**
     * Converts a byte array to a string
     * @param bytes the byte array
     * @return Returns the converted string
     */
    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }


}
