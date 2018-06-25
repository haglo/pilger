package org.app.controler;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.app.model.entity.Account;

/*
 * Managed Bean
 * In MVC the Controler-Part
 */
@SessionScoped
public class SessionService implements Serializable {

	@Inject
	SettingsService settingsService;
	
	private static final long serialVersionUID = 1L;
	
	private Account currentUser = new Account();
	private Locale currentLocale;
	private String currentTheme;

	public Account getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Account currentUser) {
		this.currentUser = currentUser;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

	public String getCurrentTheme() {
		return currentTheme;
	}

	public void setCurrentTheme(String currentTheme) {
		this.currentTheme = currentTheme;
	}

}
