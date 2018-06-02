package org.app.view.help;

import org.app.view.MainUI;
import org.app.view.TopMainMenu;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(MainUI.HELP_VIEW)
public class HelpView extends VerticalLayout implements View {

	public HelpView() {
		setSizeFull();
		setSpacing(true);
		addComponent(new TopMainMenu());
		addComponent(headingLabel());
		addComponent(someText());
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
