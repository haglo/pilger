package org.app.view.login;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.controler.AuthService;
import org.app.model.entity.Account;
import org.app.view.LanguageSelector;
import org.app.view.MainUI;
import org.app.view.I18nManager;
import org.app.view.Translatable;

import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@UIScoped
public class LoginView extends VerticalLayout implements View, Translatable {

	@Inject
	private AuthService authService;

	private Label welcomeMessage;
	private TextField username;
	private PasswordField password;
	private Button loginButton;
	private CheckBox rememberMe;
	private LanguageSelector languageSelector;

	public LoginView() {
		setSpacing(true);
		setStyleName("login-form");

		welcomeMessage = new Label("Sign In");
		username = new TextField("Username");
		password = new PasswordField("Password");
		rememberMe = new CheckBox();
		rememberMe.setCaption("remember Me");
		languageSelector = new LanguageSelector();
		languageSelector.setCaption("Language");

		// password.addStyleName("v-password-textfield");

		loginButton = new Button("Login", e -> {
			if (authService.validateAccount(username.getValue(), password.getValue(), rememberMe.getValue())) {
				((MainUI) UI.getCurrent()).loginSuccessful();
				((MainUI) UI.getCurrent()).setLocale(languageSelector.getValue());
			} else {
				Notification.show(authService.getMessageForAuthentication());
			}
		});

		// loginButton = new Button("Login", e -> {
		// if (validate(username.getValue(), password.getValue())) {
		// ((MainUI)
		// UI.getCurrent()).loginSuccessful(accountService.findByUserName(username.getValue()));
		// ((MainUI) UI.getCurrent()).setLocale(languageSelector.getValue());
		// } else {
		// }
		// });

//		addStyleName("v-login-view");
//		addComponent(welcomeMessage);
//		addComponent(username);
//		addComponent(password);
//		addComponent(loginButton);
//		addComponent(rememberMe);
//		addComponent(languageSelector);
	}

	@PostConstruct
	void init() {
		loginButton = new Button("Login", e -> {
			if (authService.validateAccount(username.getValue(), password.getValue(), rememberMe.getValue())) {
				((MainUI) UI.getCurrent()).loginSuccessful();
				((MainUI) UI.getCurrent()).setLocale(languageSelector.getValue());
			} else {
				Notification.show(authService.getMessageForAuthentication());
			}
		});
		
		addStyleName("v-login-view");
		addComponent(welcomeMessage);
		addComponent(username);
		addComponent(password);
		addComponent(loginButton);
		addComponent(rememberMe);
		addComponent(languageSelector);
	}

	// private boolean validate(String username, String password) {
	// account = new Account();
	//
	// try {
	// account = accountService.findByUserName(username);
	// } catch (Exception e) {
	// Notification.show("Username not exist: " + username + " -- " +
	// account.getUsername());
	// return false;
	// }
	//
	// if (passwordService.validatePassword(password, account.getPassword())) {
	// return true;
	// } else {
	// Notification.show("Password not correct");
	// return false;
	// }
	// }

	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		username.setCaption(i18n.getMessage("auth.username"));
		password.setCaption(i18n.getMessage("auth.password"));
		loginButton.setCaption(i18n.getMessage("auth.login"));
		rememberMe.setCaption(i18n.getMessage("auth.rememberMe"));
		languageSelector.setCaption(i18n.getMessage("auth.selector"));
	}

}
