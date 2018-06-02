package org.app.view;

import java.util.Optional;

import javax.inject.Inject;

import org.app.model.entity.Account;
import org.app.view.login.LoginView;
import org.app.view.person.PersonView;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("appui")
@CDIUI("")
public class MainUI extends UI {
//	@Inject
//	PersonView personView;
	
	@Inject
    CDIViewProvider viewProvider;
	
	@Inject
	LoginView loginView;
	
	private Navigator navigator;
	private Account loginAccount;
	
	public static final String PERSON_VIEW = "Person";
	public static final String MASTER_DETAIL_VIEW = "MasterDetail";
	public static final String ACCOUNT_VIEW = "Account";
	public static final String HELP_VIEW = "Help";
	public static final String TITLE_VIEW = "Title";
	

//	@Override
//	protected void init(VaadinRequest request) {
//		final VerticalLayout mainLayout = new VerticalLayout();
//		final CssLayout menuView = new CssLayout();
//		final CssLayout contentView = new CssLayout();
//
//		mainLayout.addComponent(menuView);
//		mainLayout.addComponent(contentView);
//		mainLayout.setMargin(true);
//		mainLayout.setSpacing(true);
//		setContent(mainLayout);
//		setContent(personView);
//
//	}
	
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
		// Store somewhere logged user state
		// This is only an example, do it in a better way :D
		getSession().setAttribute(Account.class, new Account());
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

		// No need to add views manually with CDIViewProvider
		// All @CDIView annotated classes will be automatically found
		//navigator.addView(PERSON_VIEW, PersonView.class);
		navigator.setErrorView(loginView);

		// If there is no state in URL and there is no a default view (state = "")
		// navigate to a known view
		String initialState = Optional.ofNullable(navigator.getState())
				.filter(state -> !state.trim().isEmpty())
				.orElse(PERSON_VIEW);
		navigator.navigateTo(initialState);
	}


	public Account getLoginAccount() {
		return loginAccount;
	}


	public void setLoginAccount(Account loginAccount) {
		this.loginAccount = loginAccount;
	}
	
	

}