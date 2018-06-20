package org.app.view.settings;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.SessionService;
import org.app.controler.SettingsService;
import org.app.helper.Constants;
import org.app.helper.I18nManager;
import org.app.helper.Translatable;
import org.app.model.entity.Settings;
import org.app.model.entity.enums.DefaultLanguage;
import org.app.model.entity.enums.DefaultTheme;
import org.app.view.MainUI;
import org.app.view.TopMainMenu;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings({ "serial", "unused" })
@CDIView(Constants.SETTINGS_VIEW)
public class SettingsView extends VerticalLayout implements View, Translatable {

	@Inject
	SessionService sessionService;

	@Inject
	SettingsService settingsService;

	private Settings mySettings;
	private Set<Settings> settings;
	private Button saveButton;
	private ComboBox<DefaultLanguage> cbxLanguage;
	private ComboBox<DefaultTheme> cbxTheme;
	private CheckBox ckbEdit;


	public SettingsView() {
		setSizeFull();
		setSpacing(true);

		addComponent(new TopMainMenu());
	}

	@PostConstruct
	void init() {
		saveButton = new Button("Save");
		mySettings = new Settings();
		settings = new HashSet<>();
		List<Settings> settings = settingsService.getSettingsDAO().findAll();

		for (Settings entry : settings) {
			mySettings = entry;
		}

		cbxLanguage = new ComboBox<DefaultLanguage>("Language");
		cbxLanguage.setItems(EnumSet.allOf(DefaultLanguage.class));
		cbxLanguage.setValue(mySettings.getDefaultLanguage());
		addComponent(cbxLanguage);

		cbxTheme = new ComboBox<DefaultTheme>("Theme");
		cbxTheme.setItems(EnumSet.allOf(DefaultTheme.class));
		cbxTheme.setValue(mySettings.getDefaultTheme());

		ckbEdit = new CheckBox("Edit");
		ckbEdit.addValueChangeListener(event -> {
			settingsService.toggleEditing();
			if (event.getValue()) {
				saveButton.setEnabled(true);
			} else {
				saveButton.setEnabled(false);
			}
		});

		saveButton.setEnabled(settingsService.getEditing());

		saveButton.addClickListener(event -> {
			mySettings.setDefaultLanguage(cbxLanguage.getSelectedItem().get());
			mySettings.setDefaultTheme(cbxTheme.getSelectedItem().get());
			settingsService.getSettingsDAO().update(mySettings);
		});

		addComponent(cbxTheme);
		addComponent(cbxLanguage);
		addComponent(ckbEdit);
		addComponent(saveButton);
		updateMessageStrings();
	}

	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		saveButton.setCaption(i18n.getMessage("basic.save"));
		cbxLanguage.setCaption(i18n.getMessage("settings.language"));
		cbxTheme.setCaption(i18n.getMessage("settings.theme"));
		ckbEdit.setCaption(i18n.getMessage("basic.edit"));
	}
}
