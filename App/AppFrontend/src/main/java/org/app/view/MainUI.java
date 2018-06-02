package org.app.view;

import javax.inject.Inject;

import org.app.view.person.PersonView;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("appui")
@CDIUI("")
public class MainUI extends UI {
	@Inject
	PersonView personView;

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout mainLayout = new VerticalLayout();
		final CssLayout menuView = new CssLayout();
		final CssLayout contentView = new CssLayout();

		mainLayout.addComponent(menuView);
		mainLayout.addComponent(contentView);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		setContent(mainLayout);
		setContent(personView);

	}

}