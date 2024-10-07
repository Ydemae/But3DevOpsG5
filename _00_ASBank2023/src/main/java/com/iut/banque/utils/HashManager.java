package com.iut.banque.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashManager {
    public static String[] HashNewPassword(String password) {
        /*Returns an array with two elements
        0 - Hashed password
        1 - Salt used to hash the password
         */
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return HashPassword(password, Base64.getEncoder().encodeToString(salt));
    }


    public static String[] HashPassword(String password, String salt){
         /*Returns an array with two elements
        0 - Hashed password
        1 - Salt used to hash the password
         */

        System.out.println(password);
        try {
            byte[] byteSalt = Base64.getDecoder().decode(salt);

            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(byteSalt);

            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            String[] result = new String[2];
            result[0] = Base64.getEncoder().encodeToString(hashedPassword);
            result[1] = salt;
            System.out.println(result[0]);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public static void main(String[] args) {
        String password = "adminpass";
        String[] results = HashNewPassword(password);
        System.out.println(results[0]);
        System.out.println(results[1]);
    }*/
}
