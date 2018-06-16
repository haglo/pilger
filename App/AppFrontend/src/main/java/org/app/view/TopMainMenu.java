package org.app.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class TopMainMenu extends CustomComponent {

	public TopMainMenu() {
		HorizontalLayout layout = new HorizontalLayout();
		personViewButton.setIcon(VaadinIcons.FAMILY);
		masterDetailViewButton.setIcon(VaadinIcons.NEWSPAPER);
		accountViewButton.setIcon(VaadinIcons.USER);
		helpViewButton.setIcon(VaadinIcons.QUESTION);
		settingsViewButton.setIcon(VaadinIcons.COGS);
		
		layout.addComponent(personViewButton);
		layout.addComponent(masterDetailViewButton);
		layout.addComponent(accountViewButton);
		layout.addComponent(settingsViewButton);
		layout.addComponent(helpViewButton);
		layout.addComponent(logoutButton());

		layout.setSizeUndefined();
		layout.setSpacing(true);
		setSizeUndefined();
		setCompositionRoot(layout);
		addStyleName("v-top-navigation-bar");
	}
	
	
	Button personViewButton = new Button("",
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.PERSON_VIEW));
		
	Button masterDetailViewButton = new Button("",
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.MASTER_DETAIL_VIEW));
		
	Button accountViewButton = new Button("",
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.ACCOUNT_VIEW));

	Button helpViewButton = new Button("",
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.HELP_VIEW));
		
	Button settingsViewButton = new Button("",
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.SETTINGS_VIEW));

	private Button logoutButton() {
		Button button = new Button("", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getSession().close();
//				getUI().getPage().setLocation(getLogoutPath());
				getUI().getPage().reload();
			}
		});
		button.setIcon(VaadinIcons.SIGN_OUT);
		return button;
	}
	
//	private String getLogoutPath() {
//		return getUI().getPage().getLocation().getPath();
//	}

}
