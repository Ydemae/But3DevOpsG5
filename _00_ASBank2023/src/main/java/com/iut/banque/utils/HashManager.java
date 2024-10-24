package com.iut.banque.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

public class HashManager {

    private static Logger logger = Logger.getLogger(HashManager.class.getName());
    private HashManager() {
        throw new IllegalStateException("Utility class");
    }

    public static String[] hashNewPassword(String password) {
        /*Returns an array with two elements
        0 - Hashed password
        1 - Salt used to hash the password
         */
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return hashPassword(password, Base64.getEncoder().encodeToString(salt));
    }


    public static String[] hashPassword(String password, String salt){
         /*Returns an array with two elements
        0 - Hashed password
        1 - Salt used to hash the password
         */

        logger.info(password);
        try {
            byte[] byteSalt = Base64.getDecoder().decode(salt);

            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(byteSalt);

            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            String[] result = new String[2];
            result[0] = Base64.getEncoder().encodeToString(hashedPassword);
            result[1] = salt;
            logger.info(result[0]);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

}
