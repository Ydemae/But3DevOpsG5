package com.iut.banque.modele;

import static org.junit.Assert.fail;

import com.iut.banque.exceptions.IllegalFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class TestsClient {

	//Tests getters-setters

	@Test
	public void testGetSetAccounts() throws IllegalArgumentException, IllegalFormatException {
		Client client = new Client();

		Map<String, Compte> accounts = new HashMap<>();
		accounts.put("XE0000000000", new CompteSansDecouvert("XE0000000000", 100.00, client));

		client.setAccounts(accounts);

		if (client.getAccounts() != accounts){
			Assert.fail("SetAccounts n'a pas set la liste des comptes correctement");
		}
	}

	@Test
	public void testGetIdentity() throws IllegalFormatException {
		Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password",null, "1234567890");

		Assert.assertEquals("Doe John (1234567890)", c.getIdentity());
	}

	@Test
	public void testToString() throws IllegalFormatException {
		Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password",null, "1234567890");

		Assert.assertEquals("Client [userId=j.doe1, nom=John, prenom=Doe, adresse=20 rue Bouvier, male=true, userPwd=password, numeroClient=1234567890, accounts=0]", c.toString());
	}

	/**
	 * Tests successifs de la méthode de vérification du format de numéro de
	 * client
	 */
	@Test
	public void testMethodeCheckFormatUserIdClientCorrect() {
		String strClient = "a.utilisateur928";
		if (!Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " refusé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientCommencantParUnChiffre() {
		String strClient = "32a.abc1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientCommencantParPlusieursLettres() {
		String strClient = "aaa.abc1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientSansPointSeparateur() {
		String strClient = "abc1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientChaineVide() {
		String strClient = "";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientSansLettresApresLePointSeparateur() {
		String strClient = "a.138";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientAvecUneSeuleLettreApresLePointSeparateur() {
		String strClient = "a.a1";
		if (!Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " refusé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientAvecCaractereSpecial() {
		String strClient = "a.bcdé1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientAvecTrailingZeros() {
		String strClient = "a.abc01";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatUserIdClientAvecPlusieursPointsSeparateurs() {
		String strClient = "a.ab.c1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	/**
	 * Tests successifs de la méthode de vérification du format du numéro de
	 * client
	 */
	@Test
	public void testMethodeCheckFormatNumeroClientCorrect() {
		String strClient = "1234567890";
		if (!Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " refusé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatNumeroClientAvecLettre() {
		String strClient = "12a456789";
		if (Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatNumeroClientAvecCaractereSpecial() {
		String strClient = "12#456789";
		if (Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatNumeroClientAvecMoinsDeNeufChiffres() {
		String strClient = "12345678";
		if (Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	@Test
	public void testMethodeCheckFormatNumeroClientAvecPlusDeDixChiffres() {
		String strClient = "12345678901";
		if (Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	/**
	 * Tests de la méthode possedeComptesADecouvert
	 */
	@Test
	public void testMethodePossedeComptesADecouvertPourClientAvecQueDesComptesSansDecouvert() {
		try {
			Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password",null, "1234567890");
			c.addAccount(new CompteSansDecouvert("FR1234567890", 42, c));
			c.addAccount(new CompteSansDecouvert("FR1234567891", 0, c));
			if (c.possedeComptesADecouvert()) {
				fail("La méthode aurait du renvoyer faux");
			}
		} catch (Exception e) {
			fail("Exception récupérée -> " + e.getStackTrace().toString());
		}
	}

	@Test
	public void testMethodePossedeComptesADecouvertPourClientSansComptes() {
		try {
			Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password",null, "1234567890");
			if (c.possedeComptesADecouvert()) {
				fail("La méthode aurait du renvoyer faux");
			}
		} catch (Exception e) {
			fail("Exception récupérée -> " + e.getStackTrace().toString());
		}
	}

	@Test
	public void testMethodePossedeComptesADecouvertPourClientAvecUnCompteADecouvertParmisPlusieursTypesDeComptes() {
		try {
			Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password",null, "1234567890");
			c.addAccount(new CompteSansDecouvert("FR1234567890", 42, c));
			c.addAccount(new CompteSansDecouvert("FR1234567891", 0, c));
			c.addAccount(new CompteAvecDecouvert("FR1234567892", -42, 100, c));
			c.addAccount(new CompteAvecDecouvert("FR1234567893", 1000, 100, c));
			if (!c.possedeComptesADecouvert()) {
				fail("La méthode aurait du renvoyer vrai");
			}
		} catch (Exception e) {
			fail("Exception récupérée -> " + e.getStackTrace().toString());
		}
	}

	@Test
	public void testMethodePossedeComptesADecouvertPourClientAvecPlusieursComptesADecouvertParmisPlusieursTypesDeComptes() {
		try {
			Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password",null, "1234567890");
			c.addAccount(new CompteSansDecouvert("FR1234567890", 42, c));
			c.addAccount(new CompteSansDecouvert("FR1234567891", 0, c));
			c.addAccount(new CompteAvecDecouvert("FR1234567892", -42, 100, c));
			c.addAccount(new CompteAvecDecouvert("FR1234567893", 1000, 100, c));
			c.addAccount(new CompteAvecDecouvert("FR1234567893", -4242, 5000, c));
			c.addAccount(new CompteSansDecouvert("FR1234567891", 1000.01, c));
			if (!c.possedeComptesADecouvert()) {
				fail("La méthode aurait du renvoyer vrai");
			}
		} catch (Exception e) {
			fail("Exception récupérée -> " + e.getStackTrace().toString());
		}
	}

	@Test
	public void testMethodePossedeComptesADecouvertPourClientAvecUnUniqueCompteADecouvert() {
		try {
			Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password",null, "1234567890");
			c.addAccount(new CompteAvecDecouvert("FR1234567892", -42, 100, c));
			if (!c.possedeComptesADecouvert()) {
				fail("La méthode aurait du renvoyer vrai");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception récupérée -> " + e.getStackTrace().toString());
		}
	}
	
	//Tests pour la méthode getCompteAvecSoldeNonNul()

	@Test
	public void testMethodeGetCompteAvecSoldeNonNulAvecDeuxComptesAvecSoldeNul(){
		try {
			Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password",null, "1234567890");
			c.addAccount(new CompteAvecDecouvert("FR1234567890",0,42,c));
			c.addAccount(new CompteSansDecouvert("FR1234567891", 0, c));
			if (c.getComptesAvecSoldeNonNul().size()!=0){
				fail("La méthode a renvoyé un ou plusieurs comptes aveec un solde nul");
			}
		} catch (Exception e) {
			fail("Exception récupérée -> " + e.getStackTrace().toString());
		}
	}
	@Test
	public void testMethodeGetCompteAvecSoldeNonNulAvecUnCompteSansDecouvertAvecSoldeNonNul(){
		try {
			Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password", null,"1234567890");
			c.addAccount(new CompteAvecDecouvert("FR1234567890",0,42,c));
			c.addAccount(new CompteSansDecouvert("FR1234567891", 1, c));
			if (c.getComptesAvecSoldeNonNul().get("FR1234567891")==null){
				fail("La méthode n'a pas renvoyé dans le map le compte avec solde non nul");
			}
		} catch (Exception e) {
			fail("Exception récupérée -> " + e.getStackTrace().toString());
		}
	}
	@Test
	public void testMethodeGetCompteAvecSoldeNonNulAvecUnCompteAvecDecouvertAvecSoldeNonNul(){
		try {
			Client c = new Client("John", "Doe", "20 rue Bouvier", true, "j.doe1", "password", null,"1234567890");
			c.addAccount(new CompteAvecDecouvert("FR1234567890",1,42,c));
			c.addAccount(new CompteSansDecouvert("FR1234567891", 0, c));
			if (c.getComptesAvecSoldeNonNul().get("FR1234567890")==null){
				fail("La méthode n'a pas renvoyé dans le map le compte avec solde non nul");
			}
		} catch (Exception e) {
			fail("Exception récupérée -> " + e.getStackTrace().toString());
		}
	}

}
