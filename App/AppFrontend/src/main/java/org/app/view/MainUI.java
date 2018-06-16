package org.app.view;

import java.util.Locale;
import java.util.Optional;

import javax.inject.Inject;

import org.app.controler.SessionService;
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
@Theme("appui")
@CDIUI("")
public class MainUI extends UI {
	@Inject
	CDIViewProvider viewProvider;

	@Inject
	LoginView loginView;
	
	@Inject
	SessionService sessionService;

	private Navigator navigator;

	public static final String PERSON_VIEW = "Person";
	public static final String MASTER_DETAIL_VIEW = "MasterDetail";
	public static final String ACCOUNT_VIEW = "Account";
	public static final String HELP_VIEW = "Help";
	public static final String TITLE_VIEW = "Title";
	public static final String SETTINGS_VIEW = "Settings";
	
//	private Locale initialLocale;

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
		final CssLayout menuView = new CssLayout();
		final CssLayout contentView = new CssLayout();

		mainLayout.addComponent(menuView);
		mainLayout.addComponent(contentView);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		setContent(mainLayout);

		navigator = new Navigator(this, contentView);
		navigator.addProvider(viewProvider);
		navigator.setErrorView(loginView);

		String initialState = Optional.ofNullable(navigator.getState()).filter(state -> !state.trim().isEmpty())
				.orElse(PERSON_VIEW);
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
	
	
//	public Locale getInitialLocale() {
////		initialLocale = settingsService.getMyLocale();
////		return initialLocale;
//		return Locale.ENGLISH;
//	}

	private void updateMessageStrings(Component component) {
		if (component instanceof Translatable) {
			((Translatable) component).updateMessageStrings();
		}
		if (component instanceof HasComponents) {
			((HasComponents) component).iterator().forEachRemaining(this::updateMessageStrings);
		}
	}

}