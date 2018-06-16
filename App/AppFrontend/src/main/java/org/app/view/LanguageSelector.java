package org.app.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.SettingsService;

import com.vaadin.cdi.ViewScoped;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

@ViewScoped
public class LanguageSelector extends ComboBox<Locale> implements Translatable {

	private static final long serialVersionUID = 1L;

	private static final Locale SWEDISH = new Locale("sv");
	private static final Locale FINNISH = new Locale("fi");
	private static final Locale SPANISH = new Locale("es");
	
//	public LanguageSelector() {
//		setCaption("Language");
//		setItems(getLanguageList());
//		setValue(Locale.ENGLISH);
////		setValue(settings.getDefaultLanguage());
//		setEmptySelectionAllowed(false);
//		addValueChangeListener(e -> {
//			getUI().setLocale((Locale) getValue());
////			((MainUI) UI.getCurrent()).setLocale((Locale) getValue());
//
//		});
//	}
	
	public LanguageSelector(Locale initialLoc) {
		setCaption("Language");
		setItems(getLanguageList());
		setValue(initialLoc);
		setEmptySelectionAllowed(false);
		addValueChangeListener(e -> {
			getUI().setLocale((Locale) getValue());
//			((MainUI) UI.getCurrent()).setLocale((Locale) getValue());

		});
	}
	
//	@PostConstruct
//	void init() {
//		setCaption("Language");
//		setItems(getLanguageList());
//		Locale initialLoc = ((MainUI) UI.getCurrent()).getInitialLocale();
//		setValue(initialLoc);
//		
//		setEmptySelectionAllowed(false);
//		addValueChangeListener(e -> {
//			getUI().setLocale((Locale) getValue());
////			((MainUI) UI.getCurrent()).setLocale((Locale) getValue());
//
//		});
//		
//	}
	
	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		setValue(getLocale());
		setItemCaptionGenerator(Locale::getLanguage);
		setItems(getLanguageList());
		setCaption(i18n.getMessage("auth.selector"));
	}

	private List<Locale> getLanguageList() {
		List<Locale> languageList = new ArrayList<>();
		languageList.add(Locale.ENGLISH);
		languageList.add(Locale.GERMAN);
		languageList.add(SWEDISH);
		languageList.add(FINNISH);
		languageList.add(SPANISH);
		return languageList;
	}
	

}