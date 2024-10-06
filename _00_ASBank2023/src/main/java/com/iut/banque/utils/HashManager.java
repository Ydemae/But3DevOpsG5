package com.iut.banque.utils;

import com.iut.banque.dao.DaoHibernate;
import com.iut.banque.interfaces.IDao;
import com.iut.banque.modele.Utilisateur;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashManager {
    private static IDao dao ;

    HashManager() {
        super();
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }


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
        try {
            byte[] byteSalt = Base64.getDecoder().decode(salt);

            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(byteSalt);

            byte[] hashedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));


            String[] result = new String[2];
            result[0] = Base64.getEncoder().encodeToString(hashedPassword);
            result[1] = salt;

            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void HashUserPassword(String userId){
        //For development only, shouldn't ever be used in the code

        Utilisateur user = dao.getUserById(userId);

        System.out.println(user.toString());

        if (user.getSalt() == null) {
            //On ne veut surtout pas utiliser cette méthode sur un utilisateur ayant déjà un mot de passe hashé
            String[] passwordAndSalt = HashNewPassword(user.getUserPwd());
            user.setUserPwd(passwordAndSalt[0]);
            user.setSalt(passwordAndSalt[1]);
            dao.updateUser(user);
        }
    }
}
