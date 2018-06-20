package org.app.view.help;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.SessionService;
import org.app.helper.Constants;
import org.app.helper.I18nManager;
import org.app.helper.Translatable;
import org.app.view.MainUI;
import org.app.view.TopMainMenu;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@CDIView(Constants.HELP_VIEW)
@UIScoped
public class HelpView extends VerticalLayout implements View, Translatable {

	@Inject
	SessionService sessionService;

	@Inject
	AboutSubView aboutView;

	private Label welcomeMessageLabel = new Label();
	private Label langMesssageLabel = new Label();
	private Button about;

	public HelpView() {
		setSizeFull();
		setSpacing(true);
		addComponent(new TopMainMenu());
	}

	@PostConstruct
	void init() {
		final CssLayout navigationBar = new CssLayout();
		navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		navigationBar.addComponent(createNavigationButton("Person View (Default)", Constants.PERSON_VIEW));
		navigationBar.addComponent(createNavigationButton("Master-Detail-View", Constants.MASTER_DETAIL_VIEW));

		Label username = new Label(sessionService.getCurrentUser().getUsername());
		about = new Button(ABOUT, ev -> getUI().addWindow(aboutView));
		about.addStyleName("link");

		addComponent(langMesssageLabel);
		addComponent(welcomeMessageLabel);
		addComponent(username);
		addComponent(headingLabel());
		addComponent(someText());
		addComponent(navigationBar);
		addComponent(about);
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

	public Locale getLoc() {
		return ((MainUI) UI.getCurrent()).getLocale();

	}

	private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}

	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		this.about.setCaption(i18n.getMessage("basic.about"));
		welcomeMessageLabel.setCaption(i18n.getMessage("person.welcomemessage"));
		langMesssageLabel.setCaption(
				i18n.getMessage("help.language", getLoc().getCountry(), getLoc().getLanguage(), getLoc().toString()));
		
	}

}
