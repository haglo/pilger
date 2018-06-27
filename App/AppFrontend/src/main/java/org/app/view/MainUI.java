package org.app.view;

import java.util.Locale;
import java.util.Optional;

import javax.inject.Inject;

import org.app.controler.SettingsService;
import org.app.helper.I18n;
import org.app.helper.Translatable;
import org.app.model.entity.Account;
import org.app.view.login.LoginView;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("default")
@CDIUI("")
public class MainUI extends UI {
	@Inject
	CDIViewProvider viewProvider;

	@Inject
	LoginView loginView;
	
	@Inject
	SettingsService settingsService;
	
	private Navigator navigator;

	@Override
	protected void init(VaadinRequest request) {
		getSession().setAttribute(I18n.LOCALE, settingsService.getMyLocale());
		if (isLoggedIn()) {
			setupMainLayout();
		} else {
			setContent(loginView);
		}
	}

	private boolean isLoggedIn() {
		return getSession().getAttribute(Account.class.getName()) != null;
	}

	public void loginSuccessful() {
		setupMainLayout();
	}

	private void setupMainLayout() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final CssLayout menuView = new CssLayout();
		final CssLayout contentView = new CssLayout();

		
		TopMainMenu topNavBar = new TopMainMenu();
		menuView.addComponent(topNavBar);
		
		
		mainLayout.addComponent(menuView);
		mainLayout.addComponent(contentView);
		//mainLayout.setMargin(true);
		//mainLayout.setSpacing(true);

//		this.setStyleName("point3");
//		mainLayout.setStyleName("point4");
		menuView.setStyleName("point5");
		contentView.setStyleName("point6");
		
		//mainLayout.setSizeFull();
		//mainLayout.setHeight("100%");
		contentView.setSizeFull();
		
		//mainLayout.setComponentAlignment(menuView, Alignment.MIDDLE_CENTER);
		//mainLayout.setComponentAlignment(contentView, Alignment.MIDDLE_CENTER);

		setContent(mainLayout);

		navigator = new Navigator(this, contentView);
		navigator.addProvider(viewProvider);
		navigator.setErrorView(loginView);

		String initialState = Optional.ofNullable(navigator.getState()).filter(state -> !state.trim().isEmpty())
				.orElse(I18n.PERSON_VIEW);
		navigator.navigateTo(initialState);
	}

	@Override
	public void setLocale(Locale locale) {
		super.setLocale(locale);
		updateMessageStrings(getContent());
	}
	
	public void setTheme(String theme) {
		super.setTheme(theme);
	}

	@Override
	public Locale getLocale() {
		return super.getLocale();
	}
	
	private void updateMessageStrings(Component component) {
		if (component instanceof Translatable) {
			((Translatable) component).updateMessageStrings();
		}
		if (component instanceof HasComponents) {
			((HasComponents) component).iterator().forEachRemaining(this::updateMessageStrings);
		}
	}
	
}