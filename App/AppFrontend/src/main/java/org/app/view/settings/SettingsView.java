package org.app.view.settings;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.SettingsService;
import org.app.helper.I18n;
import org.app.model.entity.Settings;
import org.app.model.entity.enums.DefaultLanguage;
import org.app.model.entity.enums.DefaultTheme;
import org.app.view.TopMainMenu;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings({ "serial", "unused" })
@CDIView(I18n.SETTINGS_VIEW)
public class SettingsView extends VerticalLayout implements View {


	@Inject
	SettingsService settingsService;
	
	private I18n i18n;
	private Settings mySettings;
	private Set<Settings> settings;
	private Button saveButton;
	private ComboBox<DefaultLanguage> cbxLanguage;
	private ComboBox<DefaultTheme> cbxTheme;
	private CheckBox ckbEdit;


	public SettingsView() {
		setSizeFull();
		setSpacing(true);
		i18n = new I18n();
	}

	@PostConstruct
	void init() {
		saveButton = new Button(i18n.BASIC_SAVE);
		mySettings = new Settings();
		settings = new HashSet<>();
		List<Settings> settings = settingsService.getSettingsDAO().findAll();

		for (Settings entry : settings) {
			mySettings = entry;
		}

		cbxLanguage = new ComboBox<DefaultLanguage>(i18n.SETTINGS_LANGUAGE);
		cbxLanguage.setItems(EnumSet.allOf(DefaultLanguage.class));
		cbxLanguage.setValue(mySettings.getDefaultLanguage());
		addComponent(cbxLanguage);

		cbxTheme = new ComboBox<DefaultTheme>(i18n.SETTINGS_THEME);
		cbxTheme.setItems(EnumSet.allOf(DefaultTheme.class));
		cbxTheme.setValue(mySettings.getDefaultTheme());

		ckbEdit = new CheckBox(i18n.BASIC_EDIT);
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
	}

}
