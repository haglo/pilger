package org.app.view.help;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.SessionService;
import org.app.model.entity.Account;
import org.app.view.I18nManager;
import org.app.view.MainUI;
import org.app.view.TopMainMenu;
import org.app.view.Translatable;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(MainUI.HELP_VIEW)
@UIScoped
public class HelpView extends VerticalLayout implements View, Translatable {
	
	@Inject
	SessionService sessionService;
	
	private Label welcomeMessageLabel = new Label();
	private Label langMesssageLabel = new Label();


	public HelpView() {
		setSizeFull();
		setSpacing(true);
		addComponent(new TopMainMenu());
	}
	
	@PostConstruct
	void init(){
		Label username = new Label(sessionService.getCurrentUser().getUsername());
		addComponent(langMesssageLabel);
		addComponent(welcomeMessageLabel);
		addComponent(username);
		addComponent(headingLabel());
		addComponent(someText());
		updateMessageStrings();		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("Showing view: Help!");
	}

	private Label headingLabel() {
		return new Label("Help without HTML");
	}

	private Label someText() {
		Label label = new Label("Help <b>with HTML</b>");
		label.setContentMode(ContentMode.HTML);
		return label;
	}
	
	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		welcomeMessageLabel.setCaption(i18n.getMessage("person.welcomemessage"));
		langMesssageLabel.setCaption(
				i18n.getMessage("person.language", getLoc().getCountry(), getLoc().getLanguage(), getLoc().toString()));
	}

	public Locale getLoc() {
		return ((MainUI) UI.getCurrent()).getLocale();

	}


}
