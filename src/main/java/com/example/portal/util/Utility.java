package com.example.portal.util;

import com.example.portal.model.exception.WebException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Utility {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSeed() {
        return byteArrayToBase64(secureRandom.generateSeed(32));
    }

    public static String sha256(byte[] input) throws WebException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            //logger.error(e.getMessage());
            throw new WebException("error in hash algorithm");
        }
        return byteArrayToBase64(digest.digest(input));
    }

    public static String byteArrayToBase64(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static boolean verifyPassword(String salt, String userPasswordHash, String password) throws WebException {
        String passwordHash = sha256(password.concat(salt).getBytes());
        return passwordHash.equals(userPasswordHash);
    }


}
