package com.iut.banque.facade;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import com.iut.banque.constants.LoginConstants;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsBanqueManager-context.xml")
@Transactional("transactionManager")
public class TestsBanqueFacade {

    @Autowired
    private BanqueFacade bf;

    @Test
    public void testGetUserConnectWhenUnconnected() {
        try {
            bf.logout();
            if(bf.getConnectedUser() != null) {
                fail("Aucun utilisateur ne devrait être connecté");
            }
        }  catch (Exception te) {
            fail("Exception inattendue: " + te.getMessage());
        }
    }

    @Test
    public void testLoginManager() {
        try {
            int result = bf.tryLogin("admin", "adminpass");
            if (result != LoginConstants.MANAGER_IS_CONNECTED) {
                fail("Un manager devrait être connecté");
            }
        }  catch (Exception te) {
            fail("Exception inattendue: " + te.getMessage());
        }
    }

     @Test
     public void testLoginUser() {
             int result = bf.tryLogin("a.lidell1", "toto");
             if (result != LoginConstants.USER_IS_CONNECTED) {
                 fail("Un user devrait être connecté");
             }
     }

    @Test
    public void testLoginUserWrongPassword() {
            int result = bf.tryLogin("a.lidell1", "mama");
            assertEquals(LoginConstants.LOGIN_FAILED, result);
    }

    @Test
    public void testLoginUserWrongUserId() {
        try {
            int result = bf.tryLogin("a.aldi9", "toto");
            if (result != LoginConstants.LOGIN_FAILED) {
                fail("Aucun user ne devrait être connecté");
            }
        }  catch (Exception te) {
            if(!(te instanceof NullPointerException)){
                fail(te.getMessage());
            }

        }
    }

    @Test
    public void testLoginUserEmptyUserId() {
        try{
            int result = bf.tryLogin("", "toto");
            if (result != LoginConstants.LOGIN_FAILED) {
                fail("Aucun user ne devrait être connecté");
            }
        }  catch (Exception te) {
            if(!(te instanceof NullPointerException)){
                fail(te.getMessage());
            }
        }
    }

    @Test
    public void testLoginUserEmptyPassword() {
            int result = bf.tryLogin("a.lidell1", "");
            if (result != LoginConstants.LOGIN_FAILED) {
                fail("Aucun user ne devrait être connecté");
            }
    }
}
