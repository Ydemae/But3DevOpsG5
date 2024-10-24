package com.iut.banque.utils;

import org.junit.Assert;
import org.junit.Test;


public class TestsHashManager {
    @Test
    public void testHashNewPasswordCasSimple(){
        String[] testPasswordAndSalt = HashManager.hashNewPassword("testPassword");
        if (testPasswordAndSalt[0].equals("") || testPasswordAndSalt[0] == null){
            Assert.fail("Le mot de passe retourné par HashManager.hashNewPassword() est vide");
        }
        if (testPasswordAndSalt[1].equals("") || testPasswordAndSalt[1] == null){
            Assert.fail("le salt retourné par HashManager.hashNewPassword() est vide");
        }
    }

    @Test
    public void testHashPasswordCasSimple(){
        String password = "aTestPassword";
        String salt = "a7qOvL4kKfU9vKT8mHnbYQ==";

        String hashedPass = "6iDZOZpVathpr+WNIu5ex/21fClJtf5FQq9sU5KtpRdcloqoRSnGmqQCrTXx0jtTJOsmGxBlQQS4cbGZ4tJVNg==";

        String[] passwordAndSalt = HashManager.hashPassword(password, salt);
        Assert.assertEquals(passwordAndSalt[0],hashedPass);
        Assert.assertEquals(passwordAndSalt[1],salt);
    }

    @Test
    public void testHashPasswordCaracteresSpeciaux(){
        String password = "a!ès!Aasswwrd";
        String salt = "9i7o/jcDUsSmG7b2AXDsUQ==";

        String hashedPass = "LZO0/TclevRiYK1xEbD9hGaGvz8S3z3Glybr5FR9UgIGxIymcrd8rNm8uBs7+m71pt/YA0wFhV509yqSeee7WQ==";

        String[] passwordAndSalt = HashManager.hashPassword(password, salt);
        Assert.assertEquals(passwordAndSalt[0],hashedPass);
        Assert.assertEquals(passwordAndSalt[1],salt);
    }
}

