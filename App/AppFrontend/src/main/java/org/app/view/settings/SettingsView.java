package org.app.view.settings;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.SessionService;
import org.app.controler.SettingsService;
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
@CDIView(MainUI.SETTINGS_VIEW)
public class SettingsView extends VerticalLayout implements View {

	@Inject
	SessionService sessionService;

	@Inject
	SettingsService settingsService;

	private Settings mySettings;
	private Set<Settings> settings;


	public SettingsView() {
		setSizeFull();
		setSpacing(true);

		addComponent(new TopMainMenu());
	}

	@PostConstruct
	void init() {
		final Button saveButton = new Button("Save");
		mySettings = new Settings();
		settings = new HashSet<>();
		List<Settings> settings = settingsService.getSettingsDAO().findAll();

		for (Settings entry : settings) {
			mySettings = entry;
		}

		ComboBox<DefaultLanguage> cbxLanguage = new ComboBox<DefaultLanguage>("Language");
		cbxLanguage.setItems(DefaultLanguage.english, DefaultLanguage.german, DefaultLanguage.spanish,
				DefaultLanguage.french);
		cbxLanguage.setValue(mySettings.getDefaultLanguage());
		addComponent(cbxLanguage);

		ComboBox<DefaultTheme> cbxTheme = new ComboBox<DefaultTheme>("Language");
		cbxTheme.setItems(DefaultTheme.Standard, DefaultTheme.Facebook, DefaultTheme.Medju, DefaultTheme.Jugend200);
		cbxTheme.setValue(mySettings.getDefaultTheme());

		CheckBox ckbEdit = new CheckBox("Edit");
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
