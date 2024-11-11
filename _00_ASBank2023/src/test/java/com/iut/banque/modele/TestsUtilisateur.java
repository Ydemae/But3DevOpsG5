package com.iut.banque.modele;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.iut.banque.exceptions.IllegalFormatException;

public class TestsUtilisateur {


    //On teste les méthodes de l'Utilisateur en utilisant un client étant donné que la classe est abstraite
    @Test
    public void testGetSetNom(){
        Client client = new Client();
        client.setNom("Jean");

        assertEquals("Jean", client.getNom());
    }

    @Test public void testGetSetPrenom(){
        Client client = new Client();
        client.setPrenom("Renault");

        assertEquals("Renault", client.getPrenom());
    }

    @Test public void testGetSetAdresse(){
        Client client = new Client();
        client.setAdresse("06 rue du Coq");

        assertEquals("06 rue du Coq", client.getAdresse());
    }

    @Test public void testGetSetMotDePasse(){
        Client client = new Client();
        client.setUserPwd("deuaifhuua&23$");

        assertEquals("deuaifhuua&23$", client.getUserPwd());
    }

    @Test public void testGetSetSalt(){
        Client client = new Client();
        client.setSalt("Xrn2WLdKeR/ma9RCE1EXEw==");

        assertEquals("Xrn2WLdKeR/ma9RCE1EXEw==", client.getSalt());
    }

    @Test public void testGetSetMale() throws IllegalFormatException{
        Client c = new Client("John", "Doe", "20 rue Bouvier", false, "j.doe1", "password",null, "1234567890");
        c.setMale(true);

        Assert.assertEquals("Client [userId=j.doe1, nom=John, prenom=Doe, adresse=20 rue Bouvier, male=true, userPwd=password, numeroClient=1234567890, accounts=0]", c.toString());
    }
}
