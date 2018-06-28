package org.app.view.login;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.AuthService;
import org.app.controler.SessionService;
import org.app.controler.SettingsService;
import org.app.helper.I18n;
import org.app.helper.I18nManager;
import org.app.helper.LanguageSelector;
import org.app.helper.Translatable;
import org.app.model.entity.Account;
import org.app.view.MainUI;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;




@SuppressWarnings("serial")
@CDIView(I18n.LOGIN_VIEW)
@UIScoped
public class LoginView extends VerticalLayout implements View, Translatable {

	@Inject
	private AuthService authService;

	@Inject
	private SessionService sessionService;

	@Inject
	private SettingsService settingsService;

	private Button loginButton;
	private CheckBox rememberMe;
	private LanguageSelector languageSelector;
	private VerticalLayout centeringLayout;

	public LoginView() {
		setSpacing(true);
		setStyleName("pilger-login-view");
		// getSession().setAttribute(I18n.LOCALE, settingsService.getMyLocale());

	}

	@PostConstruct
	void init() {
		sessionService.setCurrentLocale(settingsService.getMyLocale());
		sessionService.setCurrentTheme(settingsService.getMyTheme());
		// getSession().setAttribute(I18n.LOCALE, settingsService.getMyLocale());
		// getSession().setAttribute(I18n.LOCALE, "Hallo");

		// getUI().setLocale((Locale) sessionService.getCurrentLocale());

		Component loginForm = buildLoginForm();
		centeringLayout = new VerticalLayout();
		centeringLayout.addComponent(loginForm);
		// centeringLayout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

		addComponent(centeringLayout);
	}


	/**
	 * Special case - Must be called by MainUI via Interface Translatable
	 */
	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		rememberMe.setCaption(i18n.getMessage("auth.rememberme"));
		loginButton.setCaption(i18n.getMessage("auth.login"));
	}

	private Component buildLoginForm() {
		FormLayout loginForm = new FormLayout();
		loginForm.setSizeUndefined();
		loginForm.setMargin(false);

		Label welcomeMessage = new Label("Sign In");
		TextField username = new TextField("");
		PasswordField password = new PasswordField("");
		rememberMe = new CheckBox();
		rememberMe.setCaption("Remember Me");

		// languageSelector = new LanguageSelector(settingsService.getMyLocale());
		languageSelector = new LanguageSelector(sessionService.getCurrentLocale());

		loginButton = new Button("Login", e -> {
			if (authService.validateAccount(username.getValue(), password.getValue(), rememberMe.getValue())) {
				getSession().setAttribute(Account.class, authService.getAccount());
				((MainUI) UI.getCurrent()).loginSuccessful();
				((MainUI) UI.getCurrent()).setTheme(settingsService.getMyTheme());
				((MainUI) UI.getCurrent()).setLocale(languageSelector.getValue());
			} else {
				Notification.show(authService.getMessageForAuthentication());
			}
		});
		loginButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);

		loginForm.addComponent(welcomeMessage);
		loginForm.addComponent(username);
		loginForm.addComponent(password);
		loginForm.addComponent(languageSelector);
		loginForm.addComponent(loginButton);
		loginForm.addComponent(rememberMe);
		if (getSession() == null) {
			loginForm.addComponent(new Label(sessionService.getCurrentLocale().toString()));
		} else {
			loginForm.addComponent(new Label(getSession().getAttribute(I18n.LOCALE).toString()));
		}

		return loginForm;
	}

	public void setSettingsService(SettingsService settingsService) {
		this.settingsService = settingsService;
	}

}