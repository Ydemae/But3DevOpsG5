package com.iut.banque.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class EncryptionManager {
    
    public static String encryptPassword(String password){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);


        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);

            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : hashedPassword) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void main(String[] args) {
        String input = "test";
        String oldHashed = "7f26022fec748bd5f500236a672f8b9f22249f4d69e4801a5b8c27cc8313896befe9740c56e10297913bacab2fc8c17e8a690763dfbb2ef08a6c6e34037016ca";
        String hashed = EncryptionManager.encryptPassword(input);
        System.out.println(hashed);
    }
}
