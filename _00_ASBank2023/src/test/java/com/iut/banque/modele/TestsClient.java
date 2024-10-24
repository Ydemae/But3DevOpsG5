package com.iut.banque.modele;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.Test;


public class TestsClient {

	/**
	 * Tests successifs de la méthode de vérification du format de numéro de
	 * client
	 */

	@Test
	public void testMethodeCheckFormatUserId() {
		String strClient = "a.utilisateur928";
		if (!Client.checkFormatUserIdClient(strClient)) {
			fail("String" + strClient + " refusé dans le test");
		}
		strClient = "32a.abc1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "aaa.abc1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "abc1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "a.138";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "a.a1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "a.bcdé1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "a.abc01";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "a.ab.c1";
		if (Client.checkFormatUserIdClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}
	@Test
	public void testMethodeCheckFormatNumeroClient() {
		String strClient = "1234567890";
		if (!Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " refusé dans le test");
		}
		strClient = "12a456789";
		if (Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "12#456789";
		if (Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "12345678";
		if (Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
		strClient = "12345678901";
		if (Client.checkFormatNumeroClient(strClient)) {
			fail("String " + strClient + " validé dans le test");
		}
	}

	/**
	 * Tests successifs de la méthode de vérification du format du numéro de
	 * client
	 */

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
