package com.iut.banque.dao;

import com.iut.banque.modele.Utilisateur;

public class DaoDatabaseAccess {
    private DaoHibernate daoHibernate;

    public DaoDatabaseAccess(DaoHibernate daoHibernate) {
        this.daoHibernate = daoHibernate;
    }

    public String accessData(String userId) {
        Utilisateur user = daoHibernate.getUserById(userId);
        if (user != null && "admin".equals(user.getUserId())) {
            return "Accès autorisé";
        }
        throw new SecurityException("Accès refusé");
    }
}
