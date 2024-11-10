package com.iut.banque.modele;

import com.google.gwt.dom.client.DListElement;
import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.InsufficientFundsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestsBanque {

    private Banque banque;

    private static final String NUMERO_COMPTE_SANS_DECOUVERT = "WU1234567890";

    @Before
    public void setUp() {
        banque = new Banque();
    }

    //Test getters-setters

    @Test
    public void testGetSetClients() throws IllegalFormatException {
        Map<String, Client> listeClients = new HashMap<>();
        Client client1 = new Client();
        Client client2 = new Client();

        client1.setUserId("a.test1");
        client1.setPrenom("Jean");

        client2.setUserId("a.test2");
        client2.setPrenom("Michael");

        listeClients.put(client1.getUserId(), client1);
        listeClients.put(client2.getUserId(), client2);

        this.banque.setClients(listeClients);

        Map<String, Client> retrievedListeClients = banque.getClients();

        if (retrievedListeClients != listeClients) {
            Assert.fail("La liste des clients n'a pas été set correctement.");
        }
    }

    @Test
    public void testGetSetGestionnaires() throws IllegalFormatException {
        Map<String, Gestionnaire> listeGestios = new HashMap<>();
        Gestionnaire gestionnaire1 = new Gestionnaire();
        Gestionnaire gestionnaire2 = new Gestionnaire();

        gestionnaire1.setUserId("a.test1");
        gestionnaire1.setPrenom("Jean");

        gestionnaire2.setUserId("a.test2");
        gestionnaire2.setPrenom("Michael");

        listeGestios.put(gestionnaire1.getUserId(), gestionnaire1);
        listeGestios.put(gestionnaire2.getUserId(), gestionnaire2);

        this.banque.setGestionnaires(listeGestios);

        Map<String, Gestionnaire> retrievedListeGestios = banque.getGestionnaires();

        if (retrievedListeGestios != listeGestios) {
            Assert.fail("La liste des gestionnaires n'a pas été set correctement.");
        }
    }

    @Test
    public void testGetSetAccounts() throws IllegalFormatException, IllegalOperationException {
        Map<String, Compte> listAccounts = new HashMap<>();
        Compte compte1 = new CompteSansDecouvert("TE0000000000", 100.00, new Client());
        Compte compte2 = new CompteAvecDecouvert("TE0000000001", 99.99, 10.00, new Client());

        listAccounts.put(compte1.getNumeroCompte(), compte1);
        listAccounts.put(compte2.getNumeroCompte(), compte2);

        this.banque.setAccounts(listAccounts);

        Map<String, Compte> retrievedListeAccounts = banque.getAccounts();

        if (retrievedListeAccounts != listAccounts) {
            Assert.fail("La liste des comptes n'a pas été set correctement.");
        }
    }

    // Tests débiter

    @Test
    public void testDebiterCasSimple() throws IllegalFormatException {
        Compte testCompte = new CompteSansDecouvert(NUMERO_COMPTE_SANS_DECOUVERT, 50, new Client());
        try {
            banque.debiter(testCompte, 30);
        } catch (InsufficientFundsException e) {
            Assert.fail("L'exception InsufficientFundsException a été lancée alors que le compte possédait plus de solde que le montant débité");
        }

        Assert.assertEquals("Le compte n'a pas été débité correctement", 20, testCompte.getSolde(), 0);
    }

    @Test
    public void testDebiterCompteSansDecouvertSolde0() throws IllegalFormatException {
        Compte testCompte = new CompteSansDecouvert(NUMERO_COMPTE_SANS_DECOUVERT, 0, new Client());

        try {
            banque.debiter(testCompte, 100);
            Assert.fail("Un compte sans découvert est passé en découvert");
        } catch (InsufficientFundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testDebiterCompteAvecDecouvertSolde0() throws IllegalOperationException, IllegalFormatException {
        Compte testCompte = new CompteAvecDecouvert(NUMERO_COMPTE_SANS_DECOUVERT, 0, 100, new Client());

        try {
            banque.debiter(testCompte, 100);
        } catch (InsufficientFundsException e) {
            Assert.fail("Un compte a renvoyé une exception InsufficientFundsException alors que le découvert n'a pas été dépassé");
        }

        Assert.assertEquals("Le compte n'a pas été débité correctement", -100, testCompte.getSolde(), 0);
    }

    // Tests créditer

    @Test
    public void testCrediterCompte() throws IllegalFormatException {
        Compte testCompte = new CompteSansDecouvert(NUMERO_COMPTE_SANS_DECOUVERT, 0, new Client());

        banque.crediter(testCompte, 100);

        Assert.assertEquals("Le compte n'a pas été crédité correctement", 100, testCompte.getSolde(), 0);
    }

    // Tests deleteUser

    @Test
    public void testDeleteUser() {
        Map<String, Client> listeClients = new HashMap<>();
        final String TEST1_T = "t.test";
        final String TEST2_T = "t.test2";
        final String TEST3_T = "t.test3";
        Assert.assertNull("La liste des clients de la Banque est initialisée à la création d'une nouvelle banque", banque.getClients());

        Client testClient = new Client();
        testClient.setNom("Fransisco Testeur");

        Client testClient2 = new Client();
        testClient2.setNom("Francis Testeur");

        Client testClient3 = new Client();
        testClient3.setNom("Francis Testeur");

        listeClients.put(TEST1_T, testClient);
        listeClients.put(TEST2_T , testClient2);
        listeClients.put(TEST3_T, testClient3);

        banque.setClients(listeClients);

        Map<String, Client> returnedListeClients = banque.getClients();

        Assert.assertNotNull("La liste des clients de la Banque est vide après l'initialisation", returnedListeClients);
        Assert.assertEquals("La taille de la liste des clients est incorrecte", listeClients.size(), returnedListeClients.size());
        Assert.assertEquals("Le client renvoyé n'est pas égal au client passé à la banque", testClient, returnedListeClients.get(TEST1_T));

        banque.deleteUser(TEST2_T);
        returnedListeClients = banque.getClients();

        Assert.assertNull("La suppression du client de la banque n'a pas été effectuée", returnedListeClients.get(TEST2_T));
    }

    // Tests changeDecouvert

    @Test
    public void testChangeDecouvert() throws IllegalOperationException, IllegalFormatException {
        CompteAvecDecouvert testCompte = new CompteAvecDecouvert(NUMERO_COMPTE_SANS_DECOUVERT, 0, 1, new Client());
        banque.changeDecouvert(testCompte, 100);

        try {
            banque.debiter(testCompte, 100);
        } catch (InsufficientFundsException e) {
            Assert.fail("Le changement du découvert ne s'est pas effectué");
        }
    }
}
