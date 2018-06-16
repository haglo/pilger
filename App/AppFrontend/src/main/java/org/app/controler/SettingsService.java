package org.app.controler;

import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import org.app.model.dao.SettingsDAO;
import org.app.model.entity.Settings;

/*
 * Managed Bean
 * In MVC the Controler-Part
 */
@RequestScoped
public class SettingsService {

	@EJB
	private SettingsDAO settingsDAO;

	private boolean isEditing = false;

	public boolean getEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	public void toggleEditing() {
		this.isEditing = !this.isEditing;
	}

	public SettingsDAO getSettingsDAO() {
		return settingsDAO;
	}

	public Locale getMyLocale() {
		Locale SPANISH = new Locale("es");

		Settings mySettings = new Settings();
		List<Settings> settings = settingsDAO.findAll();
		for (Settings entry : settings) {
			mySettings = entry;
		}

		switch (mySettings.getDefaultLanguage()) {
		case english:
			return Locale.ENGLISH;
		case german:
			return Locale.GERMAN;
		case spanish:
			return SPANISH;
		case french:
			return  Locale.FRANCE;
		default:
			return Locale.ENGLISH;
		}
	}
	
	public String getMyTheme() {
		Settings mySettings = new Settings();
		List<Settings> settings = settingsDAO.findAll();
		for (Settings entry : settings) {
			mySettings = entry;
		}
		switch (mySettings.getDefaultTheme()) {
		case Standard:
			return "appui";
		case Facebook:
			return "apptheme";
		default:
			return "appui";
		}
	}


}
