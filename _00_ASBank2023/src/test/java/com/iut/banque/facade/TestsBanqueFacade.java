package com.iut.banque.facade;


import com.iut.banque.constants.LoginConstants;
import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.InsufficientFundsException;
import com.iut.banque.exceptions.TechnicalException;
import com.iut.banque.modele.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test/resources/TestsBanqueFacade-context.xml")
@Transactional("transactionManager")
public class TestsBanqueFacade {

    @Autowired
    private BanqueFacade banqueFacade;

    public void loginManager(){
        banqueFacade.tryLogin("admin", "adminpass");
    }

    /*
    * Série de tests pour la fonction tryLogin
    * */
    @Test
    public void testMethodeTryLoginInvalidUserCode(){
        int result = banqueFacade.tryLogin("Test", "Test");

        Assert.assertEquals(LoginConstants.LOGIN_FAILED, result);
    }

    @Test
    public void testMethodeTryLoginInvalidPassword(){
        int result = banqueFacade.tryLogin("a.lidell1", "toto1");

        Assert.assertEquals(LoginConstants.LOGIN_FAILED, result);
    }

    @Test
    public void testMethodeTryLoginValidUser(){
        int result = banqueFacade.tryLogin("a.lidell1", "toto");

        Assert.assertEquals(LoginConstants.USER_IS_CONNECTED, result);
        banqueFacade.logout();
    }

    @Test
    public void testMethodeTryLoginValidManager(){
        int result = banqueFacade.tryLogin("admin", "adminpass");

        Assert.assertEquals(LoginConstants.MANAGER_IS_CONNECTED, result);
    }

    //Test getConnectedUser

    @Test
    public void testMethodeGetConnectedUserNoUserLogged(){
        banqueFacade.logout();
        Utilisateur user = banqueFacade.getConnectedUser();

        Assert.assertNull(user);
    }

    @Test
    public void testMethodeGetConnectedUserCasSimple() {
        banqueFacade.tryLogin("admin", "adminpass");

        Assert.assertEquals("admin", banqueFacade.getConnectedUser().getUserId());
        Assert.assertEquals("Smith", banqueFacade.getConnectedUser().getNom());
        Assert.assertEquals("Joe", banqueFacade.getConnectedUser().getPrenom());
    }

    //Test crediter
    @Test
    public void testMethodeCrediterCasSimple() throws IllegalFormatException {
        CompteSansDecouvert compte = new CompteSansDecouvert("FR0123456789", 100, new Client());

        banqueFacade.crediter(compte, 100);

        Assert.assertEquals(200, (int)compte.getSolde());
    }

    @Test
    public void testMethodeCrediterCasDecouvert() throws IllegalFormatException, IllegalOperationException {
        CompteAvecDecouvert compte = new CompteAvecDecouvert("FR0123456789", -50, 100, new Client());

        banqueFacade.crediter(compte, 100);

        Assert.assertEquals(50, (int)compte.getSolde());
    }


    //Tests debiter
    @Test
    public void testMethodeDebiterCasSimple() throws IllegalFormatException, InsufficientFundsException {
        CompteSansDecouvert compte = new CompteSansDecouvert("FR0123456789", 100, new Client());

        banqueFacade.debiter(compte, 50);

        Assert.assertEquals(50, (int)compte.getSolde());
    }

    @Test
    public void testMethodeDebiterCasInsufficientFunds() throws IllegalFormatException {
        CompteSansDecouvert compte = new CompteSansDecouvert("FR0123456789", 100, new Client());

        try {
            banqueFacade.debiter(compte, 150);

            Assert.fail("Should have launched an InsufficientFundsException");
        }
        catch (InsufficientFundsException e) {
            //Expected exception
        }
    }

    @Test
    public void testMethodeDebiterCasDecouvert() throws IllegalFormatException, InsufficientFundsException, IllegalOperationException {
        CompteAvecDecouvert compte = new CompteAvecDecouvert("FR0123456789", 100,100, new Client());

        banqueFacade.debiter(compte, 150);

        Assert.assertEquals(-50, (int)compte.getSolde());
    }

    //Tests getAllClients et loadClients
    @Test
    public void testMethodeLoadClientsCasSimple(){
        loginManager();

        banqueFacade.loadClients();
        Map<String, Client> clients = banqueFacade.getAllClients();
        Assert.assertNotNull(clients);
        Assert.assertEquals("Alice Lidell (9865432100)", clients.get("a.lidell1").getIdentity());
    }

    //Test logout
    @Test
    public void testMethodeLogoutCasSimple() {
        loginManager();

        Assert.assertEquals("admin", banqueFacade.getConnectedUser().getUserId());

        banqueFacade.logout();

        Assert.assertNull(banqueFacade.getConnectedUser());
    }

    //Tests createAccount
    @Test
    public void testMethodeCreateAccountSansDecouvert() throws IllegalFormatException, TechnicalException, IllegalOperationException {
        loginManager();

        banqueFacade.createClient("j.pierre3", "pwd", "Jean", "Pierre", "une adresse", true, "1000000032");
        banqueFacade.loadClients();
        Client client = banqueFacade.getAllClients().get("j.pierre3");

        Compte compte = banqueFacade.getCompte("ZE1023940539");

        Assert.assertNull(compte);

        banqueFacade.createAccount("ZE1023940539", client);

        compte = banqueFacade.getCompte("ZE1023940539");

        Assert.assertNotNull(compte);
        Assert.assertEquals("j.pierre3", compte.getOwner().getUserId());
    }

    @Test
    public void testMethodeCreateAccountAvecDecouvert() throws IllegalFormatException, TechnicalException, IllegalOperationException {
        loginManager();

        banqueFacade.createClient("j.pierre3", "pwd", "Jean", "Pierre", "une adresse", true, "1000000032");
        banqueFacade.loadClients();
        Client client = banqueFacade.getAllClients().get("j.pierre3");

        Compte compte = banqueFacade.getCompte("ZE1023940539");

        Assert.assertNull(compte);

        banqueFacade.createAccount("ZE1023940539", client, 200);

        CompteAvecDecouvert compteAvecDecouvert = (CompteAvecDecouvert) banqueFacade.getCompte("ZE1023940539");

        Assert.assertNotNull(compteAvecDecouvert);
        Assert.assertEquals("j.pierre3", compteAvecDecouvert.getOwner().getUserId());
        Assert.assertEquals(200, (int)compteAvecDecouvert.getDecouvertAutorise());
    }

    //Test deleteAccount
    @Test
    public void testMethodeDeleteAccount() throws IllegalFormatException, TechnicalException, IllegalOperationException {
        loginManager();

        Compte compte = banqueFacade.getCompte("KL4589219196");

        Assert.assertNotNull(compte);

        banqueFacade.deleteAccount(compte);

        compte = banqueFacade.getCompte("KL4589219196");

        Assert.assertNull(compte);
    }


    //Test getCompte
    @Test
    public void testMethodeGetCompteCasSimple() {
        Compte compte = banqueFacade.getCompte("AB7328887341");

        Assert.assertEquals("AB7328887341", compte.getNumeroCompte());
        Assert.assertEquals(4242, (int)compte.getSolde());
        Assert.assertEquals("j.doe2", compte.getOwner().getUserId());
    }

    //Test createClient
    @Test
    public void testMethodeCreateClientCasSimple() throws IllegalFormatException, TechnicalException, IllegalOperationException {
        loginManager();

        banqueFacade.createClient("j.pierre4", "pwd", "Jean", "Pierre", "une adresse", true, "1000000031");
        banqueFacade.loadClients();
        Client client = banqueFacade.getAllClients().get("j.pierre4");

       Assert.assertNotNull(client);
       Assert.assertEquals("j.pierre4", client.getUserId());
    }


}

