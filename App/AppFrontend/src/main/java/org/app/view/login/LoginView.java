package org.app.view.login;

import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.controler.PasswordService;
import org.app.model.entity.Account;
import org.app.view.MainUI;

import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@UIScoped
public class LoginView extends VerticalLayout implements View {

	@Inject
	private AccountService accountService;
	@Inject
	private PasswordService passwordService;
	
	private Account account;

	private TextField username;
	private TextField password;
	private Button loginButton;
	

	public LoginView() {
		setSpacing(true);

		Label label = new Label("Enter your information below to log in.");
		username = new TextField("Username");
		password = new TextField("Password");

		loginButton = new Button("Login", e -> {
			if (validate(username.getValue(), password.getValue())) {
				((MainUI) UI.getCurrent()).loginSuccessful();
			} else {}
		});

		addComponent(label);
		addComponent(username);
		addComponent(password);
		addComponent(loginButton);
	}

	public void enter(ViewChangeEvent event) {
		Notification.show("Welcome! Please log in.");
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

}
