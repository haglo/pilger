package org.app.view.login;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.AuthService;
import org.app.controler.SettingsService;
import org.app.helper.I18nManager;
import org.app.helper.LanguageSelector;
import org.app.helper.Translatable;
import org.app.view.MainUI;

import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
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
@UIScoped
public class LoginView extends VerticalLayout implements View, Translatable {

	@Inject
	private AuthService authService;

	@Inject
	private SettingsService settingsService;

	private Label welcomeMessage;
	private TextField username;
	private PasswordField password;
	private Button loginButton;
	private CheckBox rememberMe;
	private LanguageSelector languageSelector;

	public LoginView() {
		setSpacing(true);
		setStyleName("pilger-login-view");

		welcomeMessage = new Label("Sign In");
		username = new TextField("Username");
		password = new PasswordField("Password");
		rememberMe = new CheckBox();
		rememberMe.setCaption("remember Me");
		loginButton = new Button("Login", e -> {
			if (authService.validateAccount(username.getValue(), password.getValue(), rememberMe.getValue())) {
				((MainUI) UI.getCurrent()).loginSuccessful();
				((MainUI) UI.getCurrent()).setTheme(settingsService.getMyTheme());
				;
				((MainUI) UI.getCurrent()).setLocale(languageSelector.getValue());
			} else {
				Notification.show(authService.getMessageForAuthentication());
			}
		});
		loginButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//		updateMessageStrings();
	}

//	@PostConstruct
//	void init() {
//		Locale li = settingsService.getMyLocale();
//		languageSelector = new LanguageSelector(li);
//		
////		addStyleName("v-login-view");
//		addComponent(welcomeMessage);
//		addComponent(username);
//		addComponent(password);
//		addComponent(loginButton);
//		addComponent(rememberMe);
//		addComponent(languageSelector);
//		// setStyleName("v-login-view");
//		// setStyleName(ValoTheme.LAY);
//	}
	
	@PostConstruct
	void init() {
//		Locale li = settingsService.getMyLocale();
//		languageSelector = new LanguageSelector(li);
		
		// login form, centered in the available part of the screen
        Component loginForm = buildLoginForm();
        
        VerticalLayout centeringLayout = new VerticalLayout();
//        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent(loginForm);
//        centeringLayout.addComponent(languageSelector);
        
        centeringLayout.setComponentAlignment(loginForm,
                Alignment.MIDDLE_CENTER);

		
//		addStyleName("v-login-view");
		addComponent(centeringLayout);
//		addComponent(welcomeMessage);
//		addComponent(username);
//		addComponent(password);
//		addComponent(loginButton);
//		addComponent(rememberMe);
//		addComponent(languageSelector);
		// setStyleName("v-login-view");
		// setStyleName(ValoTheme.LAY);
	}

	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		welcomeMessage.setCaption(i18n.getMessage("auth.signin"));
		username.setCaption(i18n.getMessage("auth.username"));
		password.setCaption(i18n.getMessage("auth.password"));
		loginButton.setCaption(i18n.getMessage("auth.login"));
		rememberMe.setCaption(i18n.getMessage("auth.rememberMe"));
		languageSelector.setCaption(i18n.getMessage("auth.selector"));
	}

	private Component buildLoginForm() {
		FormLayout loginForm = new FormLayout();
		loginForm.setSizeUndefined();
		loginForm.setMargin(false);
		
		welcomeMessage = new Label("Sign In");
		username = new TextField("Username");
		password = new PasswordField("Password");
		rememberMe = new CheckBox();
		rememberMe.setCaption("remember Me");
		Locale li = settingsService.getMyLocale();
		languageSelector = new LanguageSelector(li);
		
		loginForm.addComponent(welcomeMessage);
		loginForm.addComponent(username);
		loginForm.addComponent(password);
		loginForm.addComponent(languageSelector);
		loginForm.addComponent(loginButton);
		loginForm.addComponent(rememberMe);

		return loginForm;
	}

}
