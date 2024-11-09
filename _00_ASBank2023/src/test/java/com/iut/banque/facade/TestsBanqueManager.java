package com.iut.banque.facade;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.iut.banque.exceptions.IllegalOperationException;

//@RunWith indique à JUnit de prendre le class runner de Spirng
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration permet de charger le context utilisé pendant les tests.
// Par défault (si aucun argument n'est précisé), cherche le fichier
/// src/com/iut/banque/test/TestsDaoHibernate-context.xml
@ContextConfiguration("/test/resources/TestsBanqueManager-context.xml")
@Transactional("transactionManager")
public class TestsBanqueManager {

	@Autowired
	private BanqueManager bm;

	private static final String ILLEGAL_OP_EXEP = "Une IllegalOperationException aurait dû être récupérée";

	// Tests de par rapport à l'ajout d'un client
	@Test
	public void testCreationDunClient() {
		try {
			bm.loadAllClients();
			bm.createClient("t.test1", "password", "test1nom", "test1prenom", "test town", true, "4242424242");
		} catch (IllegalOperationException e) {
			failIllegalOperationExeption(e);
		} catch (Exception te) {
			failExeption(te);
		}
	}

	@Test
	public void testCreationDunClientAvecDeuxNumerosDeCompteIdentiques() {
		try {
			bm.loadAllClients();
			bm.createClient("t.test1", "password", "test1nom", "test1prenom", "test town", true, "0101010101");
			fail(ILLEGAL_OP_EXEP);
		} catch (Exception te) {
			if(!(te instanceof IllegalOperationException)){
				failExeption(te);
			}
		}
	}

	// Tests par rapport à la suppression de comptes
	@Test
	public void testSuppressionDunCompteAvecDecouvertAvecSoldeZero() {
		try {

			bm.deleteAccount(bm.getAccountById("CADV000000"));
		} catch (IllegalOperationException e) {
			failIllegalOperationExeption(e);
		} catch (Exception te) {
			failExeption(te);
		}
	}

	@Test
	public void testSuppressionDunCompteAvecDecouvertAvecSoldeDifferentDeZero() {
		try {
			bm.deleteAccount(bm.getAccountById("CADNV00000"));
			fail(ILLEGAL_OP_EXEP);
		} catch (Exception te) {
			if(!(te instanceof IllegalOperationException)){
				failExeption(te);
			}
		}
	}

	@Test
	public void testSuppressionDunCompteSansDecouvertAvecSoldeZero() {
		try {
			bm.deleteAccount(bm.getAccountById("CSDV000000"));
		} catch (IllegalOperationException e) {
			failIllegalOperationExeption(e);
		} catch (Exception te) {
			failExeption(te);
		}
	}

	@Test
	public void testSuppressionDunCompteSansDecouvertAvecSoldeDifferentDeZero() {
		try {
			bm.deleteAccount(bm.getAccountById("CSDNV00000"));
			fail(ILLEGAL_OP_EXEP);
		} catch (Exception te) {
			if(!(te instanceof IllegalOperationException)){
				failExeption(te);
			}
		}
	}

	// Tests en rapport avec la suppression d'utilisateurs
	@Test
	public void testSuppressionDunUtilisateurSansCompte() {
		try {
			bm.loadAllClients();
			bm.deleteUser(bm.getUserById("g.pasdecompte"));
		} catch (IllegalOperationException e) {
			failIllegalOperationExeption(e);
		} catch (Exception te) {
			failExeption(te);
		}
	}

	@Test
	public void testSuppressionDuDernierManagerDeLaBaseDeDonnees() {
		bm.loadAllGestionnaires();
		try {
			bm.deleteUser(bm.getUserById("admin"));
			fail(ILLEGAL_OP_EXEP);
		} catch (Exception te) {
			if(!(te instanceof IllegalOperationException)){
				failExeption(te);
			}
		}
	}

	@Test
	public void testSuppressionDunClientAvecComptesDeSoldeZero() {
		try {
			bm.loadAllClients();
			bm.deleteUser(bm.getUserById("g.descomptesvides"));
			if (bm.getAccountById("KL4589219196") != null || bm.getAccountById("KO7845154956") != null) {
				fail("Les comptes de l'utilisateur sont encore présents dans la base de données");
			}
		} catch (IllegalOperationException e) {
			failIllegalOperationExeption(e);
		} catch (Exception te) {
			failExeption(te);
		}
	}

	@Test
	public void testSuppressionDunClientAvecUnCompteDeSoldePositif() {
		try {
			bm.deleteUser(bm.getUserById("j.doe1"));
			fail(ILLEGAL_OP_EXEP);
		} catch (Exception te) {
			if(!(te instanceof IllegalOperationException)){
				failExeption(te);
			}
		}
	}

	@Test
	public void testSuppressionDunClientAvecUnCompteAvecDecouvertDeSoldeNegatif() {
		try {
			bm.deleteUser(bm.getUserById("j.doe1"));
			fail(ILLEGAL_OP_EXEP);
		} catch (Exception te) {
			if(!(te instanceof IllegalOperationException)){
				failExeption(te);
			}
		}
	}

	private void failExeption(Exception te) {
		te.printStackTrace();
		fail("Une Exception " + te.getClass().getSimpleName() + " a été récupérée");
	}

	private void failIllegalOperationExeption(IllegalOperationException e) {
		e.printStackTrace();
		fail("IllegalOperationException récupérée : " + e.getStackTrace());
	}

}
