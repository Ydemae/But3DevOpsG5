package com.iut.banque.converter;

import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.CompteAvecDecouvert;
import com.iut.banque.modele.CompteSansDecouvert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:TestsClientConverter-context.xml")
@Transactional("transactionManager")
public class TestsClientConverter {

    @Autowired
    ClientConverter clientConverter;

    @Test
    public void testMethodeConvertToString() throws IllegalFormatException {
        Client client = new Client("John", "Doe", "4 rue du Coq", true, "j.doe3", "pass", "", "0000000001");

        Assert.assertEquals("Doe John (0000000001)", clientConverter.convertToString(new HashMap<>(), client));
    }

    @Test
    public void testMethodeConvertFromString(){
        String[] clientsId = new String[1];
        clientsId[0] = "j.doe2";

        Object client = clientConverter.convertFromString(new HashMap<>(), clientsId, Client.class);

        Client clientConverti = (Client)client;

        Assert.assertNotNull(clientConverti);
        Assert.assertEquals("j.doe2", clientConverti.getUserId());
        Assert.assertEquals("0000000001", clientConverti.getNumeroClient());
    }
}
