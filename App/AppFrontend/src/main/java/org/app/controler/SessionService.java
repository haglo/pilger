package org.app.controler;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;

import org.app.model.entity.Account;

/*
 * Managed Bean
 * In MVC the Controler-Part
 */
@SessionScoped
public class SessionService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Account currentUser = new Account();
	private String currentLocale;

	public Account getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Account currentUser) {
		this.currentUser = currentUser;
	}

}
