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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.HorizontalLayout;
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
		final HorizontalLayout menuView = new HorizontalLayout();
		final HorizontalLayout contentView = new HorizontalLayout();
		contentView.setSizeFull();
		contentView.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		
		TopMainMenu topNavBar = new TopMainMenu();
		menuView.addComponent(topNavBar);

		mainLayout.addComponent(menuView);
		mainLayout.addComponent(contentView);

		mainLayout.setComponentAlignment(menuView, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(contentView, Alignment.TOP_CENTER);
		
		mainLayout.setExpandRatio(menuView, 0.10f);
		mainLayout.setExpandRatio(contentView, 0.90f);

		mainLayout.setSizeFull();

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