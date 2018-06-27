package org.app.view;

import org.app.helper.I18nManager;
import org.app.helper.I18n;
import org.app.helper.Translatable;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class TopMainMenu extends CustomComponent implements Translatable {
	
	private Button logOutButton;

	public TopMainMenu() {
		HorizontalLayout layout = new HorizontalLayout();
		personViewButton.setIcon(VaadinIcons.FAMILY);
		personViewButton.addStyleName("icon-align-top");
		masterDetailViewButton.setIcon(VaadinIcons.NEWSPAPER);
		masterDetailViewButton.addStyleName("icon-align-top");
		accountViewButton.setIcon(VaadinIcons.USER);
		accountViewButton.addStyleName("icon-align-top");	
		helpViewButton.setIcon(VaadinIcons.QUESTION);
		helpViewButton.addStyleName("icon-align-top");
		settingsViewButton.setIcon(VaadinIcons.COGS);
		settingsViewButton.addStyleName("icon-align-top");
		splitViewButton.setIcon(VaadinIcons.ACCORDION_MENU);
		splitViewButton.addStyleName("icon-align-top");
		
		layout.addComponent(personViewButton);
		layout.addComponent(masterDetailViewButton);
		layout.addComponent(accountViewButton);
		layout.addComponent(settingsViewButton);
		layout.addComponent(helpViewButton);
		layout.addComponent(splitViewButton);
		layout.addComponent(logoutButton());

//		layout.setSizeUndefined();
//		layout.setSpacing(true);
//		setSizeUndefined();
		setCompositionRoot(layout);
		addStyleName("pilger-top-nav-bar");
		updateMessageStrings();
	}
	
	
	Button personViewButton = new Button("Addresses",
			e -> UI.getCurrent().getNavigator().navigateTo(I18n.PERSON_VIEW));
		
	Button masterDetailViewButton = new Button("Master Detail",
			e -> UI.getCurrent().getNavigator().navigateTo(I18n.MASTER_DETAIL_VIEW));
		
	Button accountViewButton = new Button("Accounts",
			e -> UI.getCurrent().getNavigator().navigateTo(I18n.ACCOUNT_VIEW));

	Button helpViewButton = new Button("Help",
			e -> UI.getCurrent().getNavigator().navigateTo(I18n.HELP_VIEW));
		
	Button settingsViewButton = new Button("Settings",
			e -> UI.getCurrent().getNavigator().navigateTo(I18n.SETTINGS_VIEW));
	
	Button splitViewButton = new Button("Split",
			e -> UI.getCurrent().getNavigator().navigateTo(I18n.SPLIT_VIEW));
	

	private Button logoutButton() {
		logOutButton = new Button("Logout", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getSession().close();
				getUI().getPage().reload();
			}
		});
		logOutButton.setIcon(VaadinIcons.SIGN_OUT);
		logOutButton.addStyleName("icon-align-top");
		return logOutButton;
	}
	
	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		personViewButton.setCaption(i18n.getMessage("navi.persons"));
		masterDetailViewButton.setCaption(i18n.getMessage("navi.masterdetail"));
		accountViewButton.setCaption(i18n.getMessage("navi.accounts"));
		helpViewButton.setCaption(i18n.getMessage("navi.help"));
		settingsViewButton.setCaption(i18n.getMessage("navi.settings"));
		splitViewButton.setCaption(i18n.getMessage("navi.settings"));
		logOutButton.setCaption(i18n.getMessage("navi.logout"));
	}
	


}
