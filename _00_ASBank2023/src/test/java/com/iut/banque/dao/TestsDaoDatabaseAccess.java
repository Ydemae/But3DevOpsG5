package com.iut.banque.dao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.iut.banque.exceptions.IllegalFormatException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.iut.banque.modele.Gestionnaire;

public class TestsDaoDatabaseAccess {

    @Mock
    private DaoHibernate daoHibernate;

    @InjectMocks
    private DaoDatabaseAccess daoDatabaseAccess;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAccessDataWithAdmin() throws IllegalFormatException {
        Gestionnaire admin = new Gestionnaire("NomAdmin", "PrenomAdmin", "AdresseAdmin", true, "admin", "adminPwd", "salt123");
        when(daoHibernate.getUserById(anyString())).thenReturn(admin);
        String result = daoDatabaseAccess.accessData("0");
        assertEquals("Accès autorisé", result);
    }
}