package com.iut.banque.controller;

import java.util.logging.Logger;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionSupport;

import com.iut.banque.exceptions.IllegalFormatException;
import com.iut.banque.exceptions.IllegalOperationException;
import com.iut.banque.exceptions.TechnicalException;
import com.iut.banque.facade.BanqueFacade;
import com.iut.banque.modele.Client;
import com.iut.banque.modele.Compte;

public class CreerCompte extends ActionSupport {

	private static Logger logger = Logger.getLogger(CreerCompte.class.getName());
	private static final long serialVersionUID = 1L;
	private String numeroCompte;
	private boolean avecDecouvert;
	private double decouvertAutorise;
	private transient Client client;
	private transient String idClient; // Correction Bug Bloquant N2
	private String message;
	private boolean error;
	private boolean result;
	private transient BanqueFacade banque;
	private transient Compte compte;

	/**
	 * @param compte
	 *            the compte to set
	 */
	public void setCompte(Compte compte) {
		this.compte = compte;
	}

	/**
	 * @return the compte
	 */
	public Compte getCompte() {
		return compte;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Indique si le résultat de l'action précedente avait réussi
	 * 
	 * @return le status de l'action précédente
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * Setter de l'action précédente
	 * 
	 * @param error
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	// Correction Bug Bloquant N2

	/**
	 * @return the idClient
	 */
	public String getIdClient() {
		return idClient;
	}

	/**
	 * @param idClient
	 *            the client to set
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	// Fin Correction Bug Bloquant N2

		/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Constructeur sans paramêtre de CreerCompte
	 */
	public CreerCompte() {
		logger.info("In Constructor from CreerCompte class ");
		ApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
		this.banque = (BanqueFacade) context.getBean("banqueFacade");
	}

	/**
	 * @return the numeroCompte
	 */
	public String getNumeroCompte() {
		return numeroCompte;
	}

	/**
	 * @param numeroCompte
	 *            the numeroCompte to set
	 */
	public void setNumeroCompte(String numeroCompte) {
		this.numeroCompte = numeroCompte;
	}

	/**
	 * @return the avecDecouvert
	 */
	public boolean isAvecDecouvert() {
		return avecDecouvert;
	}

	/**
	 * @param avecDecouvert
	 *            the avecDecouvert to set
	 */
	public void setAvecDecouvert(boolean avecDecouvert) {
		this.avecDecouvert = avecDecouvert;
	}

	/**
	 * @return the decouvertAutorise
	 */
	public double getDecouvertAutorise() {
		return decouvertAutorise;
	}

	/**
	 * @param decouvertAutorise
	 *            the decouvertAutorise to set
	 */
	public void setDecouvertAutorise(double decouvertAutorise) {
		this.decouvertAutorise = decouvertAutorise;
	}

	/**
	 * Getter du message résultant de l'action précédente.
	 * 
	 * @return le message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Choisi le message à enregistrer en fonction du message reçu en paramêtre.
	 * 
	 * @param message
	 *            : le message indiquant le status de l'action précédente.
	 */
	public void setMessage(String message) {
		switch (message) {
			case "NONUNIQUEID":
				this.message = "Ce numéro de compte existe déjà !";
				break;
			case "INVALIDFORMAT":
				this.message = "Ce numéro de compte n'est pas dans un format valide !";
				break;
			case "SUCCESS":
				this.message = "Le compte " + compte.getNumeroCompte() + " a bien été créé.";
				break;
			default:
				this.message = "Une erreur est survenue";
				break;
		}
	}

	/**
	 * Getter du status de l'action précédente. Si vrai, indique qu'une création
	 * de compte a déjà été essayée (elle peut avoir réussi ou non). Sinon, le
	 * client vient d'arriver sur la page.
	 * 
	 * @return le status de l'action précédente
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * Setter du status de l'action précédente.
	 * 
	 * @param result
	 *            : le status
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	// Correction Bug Bloquant N2
	/**
	 * Action créant un compte client ou gestionnaire.
	 * 
	 * @return une chaine déterminant le résultat de l'action
	 */
	public String creationCompte() {
		try {
			client = banque.getClientById(idClient);
			if(client != null) {
				if (avecDecouvert) {
					banque.createAccount(numeroCompte, client, decouvertAutorise);
				} else {
					banque.createAccount(numeroCompte, client);
				}
				this.compte = banque.getCompte(numeroCompte);				
			} else {
				throw new TechnicalException("Client introuvable");
			}
			return "SUCCESS";
		} catch (IllegalOperationException e) {
			e.printStackTrace();
			return "ERROR_ILLEGAL_OPERATION";
		} catch (TechnicalException e) {
			return "NONUNIQUEID";
		} catch (IllegalFormatException e) {
			return "INVALIDFORMAT";
		}
	}
	// Fin Correction Bug Bloquant N2
}
