package org.app.view.login;

import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.controler.PasswordService;
import org.app.model.entity.Account;
import org.app.view.LanguageSelector;
import org.app.view.MainUI;
import org.app.view.Messages;
import org.app.view.Translatable;

import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@UIScoped
public class LoginView extends VerticalLayout implements View, Translatable {

	@Inject
	private AccountService accountService;
	@Inject
	private PasswordService passwordService;

	private Account account;

	private Label welcomeMessage;
	private TextField username;
	private TextField password;
	private Button loginButton;
	private LanguageSelector languageSelector;

	public LoginView() {
		setSpacing(true);
		setStyleName("login-form");

		welcomeMessage = new Label("Sign In");
		username = new TextField("Username");
		password = new TextField("Password");
		languageSelector = new LanguageSelector();
		languageSelector.setCaption("Language");

		// password.addStyleName("v-password-textfield");

		loginButton = new Button("Login", e -> {
			if (validate(username.getValue(), password.getValue())) {
				((MainUI) UI.getCurrent()).loginSuccessful(accountService.findByUserName(username.getValue()));
				((MainUI) UI.getCurrent()).setLocale(languageSelector.getValue());

				// ((MainUI)
				// UI.getCurrent()).setLoginAccount(accountService.findByUserName(username.getValue()));

			} else {
			}
		});

		addStyleName("v-login-view");
		addComponent(welcomeMessage);
		addComponent(username);
		addComponent(password);
		addComponent(loginButton);
		addComponent(languageSelector);
	}

	private boolean validate(String username, String password) {
		account = new Account();

		try {
			account = accountService.findByUserName(username);
		} catch (Exception e) {
			Notification.show("Username not exist: " + username + " -- " + account.getUsername());
			return false;
		}

		if (passwordService.validatePassword(password, account.getPassword())) {
			return true;
		} else {
			Notification.show("Password not correct");
			return false;
		}
	}

	@Override
	public void updateMessageStrings() {
		final Messages messages = Messages.getInstance();
		username.setCaption(messages.getMessage("auth.username"));
		password.setCaption(messages.getMessage("auth.password"));
		loginButton.setCaption(messages.getMessage("auth.login"));
		languageSelector.setCaption(messages.getMessage("auth.selector"));
	}

}
