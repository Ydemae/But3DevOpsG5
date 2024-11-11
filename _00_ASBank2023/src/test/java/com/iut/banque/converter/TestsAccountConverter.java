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
@ContextConfiguration("classpath:TestsAccountConverter-context.xml")
@Transactional("transactionManager")
public class TestsAccountConverter {

    @Autowired
    AccountConverter accountConverter;

    @Test
    public void testMethodeConvertToString() throws IllegalFormatException {
        CompteSansDecouvert compte1 = new CompteSansDecouvert("XE0000000001", 100.00, new Client());

        Assert.assertEquals("XE0000000001", accountConverter.convertToString(new HashMap<>(), compte1));
    }

    @Test
    public void testMethodeConvertFromString(){
        String[] numerosComptes = new String[1];
        numerosComptes[0] = "AB7328887341";

        Object compte = accountConverter.convertFromString(new HashMap<>(), numerosComptes, CompteAvecDecouvert.class);

        CompteAvecDecouvert compteConverti = (CompteAvecDecouvert)compte;

        Assert.assertNotNull(compte);
        Assert.assertEquals("AB7328887341", compteConverti.getNumeroCompte());
        Assert.assertEquals("j.doe2", compteConverti.getOwner().getUserId());
        Assert.assertEquals(4242.00, compteConverti.getSolde(), 0);
        Assert.assertEquals(123.00, compteConverti.getDecouvertAutorise(), 0);
    }
}
