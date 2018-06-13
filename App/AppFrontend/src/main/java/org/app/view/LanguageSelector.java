package org.app.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

public class LanguageSelector extends ComboBox<Locale> implements Translatable {

	private static final long serialVersionUID = 1L;

	private static final Locale SWEDISH = new Locale("sv");
	private static final Locale FINNISH = new Locale("fi");

	public LanguageSelector() {
		setItems(getLanguageList());
		setValue(Locale.ENGLISH);
		setEmptySelectionAllowed(false);

		addValueChangeListener(e -> {
//			getUI().setLocale((Locale) getValue());
			((MainUI) UI.getCurrent()).setLocale((Locale) getValue());

		});
	}

	@Override
	public void updateMessageStrings() {
		setValue(getLocale());
		setItemCaptionGenerator(Locale::getLanguage);
		setItems(getLanguageList());
	}

	private List<Locale> getLanguageList() {
		List<Locale> languageList = new ArrayList<>();
		languageList.add(Locale.ENGLISH);
		languageList.add(Locale.GERMAN);
		languageList.add(SWEDISH);
		languageList.add(FINNISH);
		return languageList;
	}
}