package org.app.model.initialize;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.app.model.dao.SettingsDAO;
import org.app.model.entity.Settings;
import org.app.model.entity.enums.DefaultLanguage;
import org.app.model.entity.enums.DefaultTheme;

@Singleton
@Startup
public class InitializeSettings {

	@EJB
	SettingsDAO settingsDAO;

	@PostConstruct
	void init() {
		if (settingsDAO.findAll().size() == 0) {

			Settings settings = new Settings();

			settings.setDefaultLanguage(DefaultLanguage.english);
			settings.setDefaultTheme(DefaultTheme.Default);
			settingsDAO.update(settings);
		}
	}
}
