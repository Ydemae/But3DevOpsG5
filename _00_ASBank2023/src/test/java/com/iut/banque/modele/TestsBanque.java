package com.iut.banque.modele;

import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.InsufficientFundsException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestsBanque {

    Banque banque;

    //Tests débiter

    @Test
    public void testDebiterCasSimple() throws IllegalFormatException {
        banque = new Banque();

        Compte testCompte = new CompteSansDecouvert("WU1234567890", 50, new Client());
        try{
            banque.debiter(testCompte, 30);
        }
        catch (InsufficientFundsException e){
            Assert.fail("L'exception InsufficientFundsException a été lancée alors que le compte possédait plus de solde que le montant débité");
        }

        if (testCompte.getSolde() != 20){
            Assert.fail("Le compte n'a pas été débité correctement");
        }
    }

    @Test
    public void testDebiterCompteSansDecouvertSolde0() throws IllegalFormatException {
        banque = new Banque();

        Compte testCompte = new CompteSansDecouvert("WU1234567890", 0, new Client());

        try {
            banque.debiter(testCompte, 100);
            Assert.fail("Un compte sans découvert est passé en découvert");
        }
        catch (InsufficientFundsException e){
            return;
        }

    }

    @Test
    public void testDebiterCompteAvecDecouvertSolde0() throws IllegalOperationException, IllegalFormatException {
        banque = new Banque();

        Compte testCompte = new CompteAvecDecouvert("WU1234567890", 0, 100, new Client());

        try {
            banque.debiter(testCompte, 100);
        }
        catch (InsufficientFundsException e){
            Assert.fail("Un compte a renvoyé une exception InsufficientFundsException alors que le découvert n'a pas été dépassé");
        }

        if (testCompte.getSolde() !=  -100){
            Assert.fail("Le compte n'a pas été débité correctement");
        }
    }

    //Tests créditer

    @Test
    public void testCrediterCompte() throws IllegalFormatException {
        banque = new Banque();

        Compte testCompte = new CompteSansDecouvert("WU1234567890", 0, new Client());

        banque.crediter(testCompte, 100);


        if (testCompte.getSolde() !=  100){
            Assert.fail("Le compte n'a pas été crédité correctement");
        }
    }

    //Tests deleteUser

    @Test
    public void testDeleteUser() {
        banque = new Banque();
        Map<String, Client> listeClients = new HashMap<String, Client>();

        if (banque.getClients() != null){
            Assert.fail("La liste des clients de la Banque est initialisée à la création d'une nouvelle banque");
        }

        Client testClient = new Client();
        testClient.setNom("Fransisco Testeur");

        Client testClient2 = new Client();
        testClient2.setNom("Francis Testeur");

        Client testClient3 = new Client();
        testClient3.setNom("Francis Testeur");

        listeClients.put("t.test", testClient);
        listeClients.put("t.test2", testClient2);
        listeClients.put("t.test3", testClient3);

        banque.setClients(listeClients);

        Map<String, Client> returnedListeClients = banque.getClients();

        Assert.assertNotNull(returnedListeClients);

        if (returnedListeClients.size() != listeClients.size()){
            Assert.fail("La taille de la liste des clients est incorrecte");
        }
        if (returnedListeClients.get("t.test") != testClient){
            Assert.fail("Le client renvoyé n'est pas égal au client passé à la banque");
        }

        banque.deleteUser("t.test2");
        returnedListeClients = banque.getClients();

        if (returnedListeClients.get("t.test2") != null){
            Assert.fail("La suppression du client de la banque n'a pas été effectué");
        }

    }

    //Tests changeDecouvert

    @Test
    public void testChangeDecouvert() throws IllegalOperationException, IllegalFormatException {
        banque = new Banque();

        CompteAvecDecouvert testCompte = new CompteAvecDecouvert("WU1234567890", 0, 1, new Client());
        banque.changeDecouvert(testCompte, 100);
        try {
            banque.debiter(testCompte, 100);
        }
        catch (InsufficientFundsException e){
            Assert.fail("Le changement du découvert ne s'est pas effectué");
        }
    }

}
