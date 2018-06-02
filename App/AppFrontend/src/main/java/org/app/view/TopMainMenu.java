package org.app.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class TopMainMenu extends CustomComponent {

	public TopMainMenu() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(personViewButton);
		layout.addComponent(masterDetailViewButton);
		layout.addComponent(accountViewButton);
		layout.addComponent(helpViewButton);
		
		layout.addComponent(logoutButton());
		layout.setSizeUndefined();
		layout.setSpacing(true);
		setSizeUndefined();
		setCompositionRoot(layout);
	}
	
	
	Button personViewButton = new Button(MainUI.PERSON_VIEW,
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.PERSON_VIEW));
		
	Button masterDetailViewButton = new Button(MainUI.MASTER_DETAIL_VIEW,
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.MASTER_DETAIL_VIEW));
		
	Button accountViewButton = new Button(MainUI.ACCOUNT_VIEW,
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.ACCOUNT_VIEW));

	Button helpViewButton = new Button(MainUI.HELP_VIEW,
			e -> UI.getCurrent().getNavigator().navigateTo(MainUI.HELP_VIEW));
		

	private Button logoutButton() {
		Button button = new Button("Logout", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getSession().close();
//				getUI().getPage().setLocation(getLogoutPath());
				getUI().getPage().reload();
			}
		});
		return button;
	}
	
//	private String getLogoutPath() {
//		return getUI().getPage().getLocation().getPath();
//	}

}
