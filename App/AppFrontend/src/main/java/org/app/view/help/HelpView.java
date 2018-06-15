package org.app.view.help;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.SessionService;
import org.app.model.entity.Account;
import org.app.view.MainUI;
import org.app.view.TopMainMenu;

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
public class HelpView extends VerticalLayout implements View {
	
	@Inject
	SessionService sessionService;

	public HelpView() {
		setSizeFull();
		setSpacing(true);

		addComponent(new TopMainMenu());
		addComponent(headingLabel());
		addComponent(someText());

	}
	
	@PostConstruct
	void init(){
		Label username = new Label(sessionService.getCurrentUser().getUsername());
		addComponent(username);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("Showing view: Help!");
	}

	private Label headingLabel() {
		return new Label("Help");
	}

	private Label someText() {
		Label label = new Label("Hallo Welt");
		label.setContentMode(ContentMode.HTML);
		return label;
	}

}
