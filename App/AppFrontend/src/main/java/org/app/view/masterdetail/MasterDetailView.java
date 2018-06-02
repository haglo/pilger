package org.app.view.masterdetail;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.controler.TitleService;
import org.app.view.MainUI;
import org.app.view.TopMainMenu;

import org.app.view.masterdetail.views.TitleView;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@CDIView(MainUI.MASTER_DETAIL_VIEW)
public class MasterDetailView extends VerticalLayout implements View {

	@Inject
	TitleService titleService;

	@Inject
	AccountService accountService;

	// @Inject
	// LandService landService;

	private HorizontalLayout allView;
	private VerticalLayout masterDetailLeftNavBar;
	private CssLayout masterDetailContent;

	private Button titleButton;
	// private Button landButton;

	public MasterDetailView() {
		setSizeFull();
		setSpacing(true);
		addComponent(new TopMainMenu());
	}

	@PostConstruct
	void init() {
		allView = new HorizontalLayout();
		masterDetailLeftNavBar = new VerticalLayout();
		masterDetailLeftNavBar.addStyleName(ValoTheme.MENU_ROOT);
		masterDetailContent = new CssLayout();

		Label cLabel = new Label("MasterDetailContent");
		Label head = new Label("MasterDetailLeftNavi");
		head.addStyleName(ValoTheme.MENU_TITLE);

		masterDetailLeftNavBar.addComponent(head);
		masterDetailLeftNavBar.addComponent(showTitleView());
		// masterDetailLeftNavBar.addComponent(showLandView());

		masterDetailContent.addComponent(cLabel);

		allView.addComponent(masterDetailLeftNavBar);
		allView.addComponent(masterDetailContent);
		allView.setSizeFull();

		addComponent(allView);

	}

	// public MasterDetailView() {
	// allView = new HorizontalLayout();
	// masterDetailLeftNavBar = new VerticalLayout();
	// masterDetailLeftNavBar.addStyleName(ValoTheme.MENU_ROOT);
	// masterDetailContent = new CssLayout();
	//
	// Label cLabel = new Label("MasterDetailContent");
	// Label head = new Label("MasterDetailLeftNavi");
	// head.addStyleName(ValoTheme.MENU_TITLE);
	//
	// masterDetailLeftNavBar.addComponent(head);
	// masterDetailLeftNavBar.addComponent(showTitleView());
	// masterDetailLeftNavBar.addComponent(showLandView());
	//
	// masterDetailContent.addComponent(cLabel);
	//
	// allView.addComponent(masterDetailLeftNavBar);
	// allView.addComponent(masterDetailContent);
	// allView.setSizeFull();
	//
	// addComponent(new TopMainMenu());
	// addComponent(allView);
	//
	// titleButton.addClickListener(event -> titleButton.setCaption("Title: You
	// pushed it!"));
	// landButton.addClickListener(event -> landButton.setCaption("Land: You pushed
	// it!"));
	// }

	private Button showTitleView() {
		titleButton = new Button("Title", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				masterDetailContent.removeAllComponents();
				masterDetailContent.addComponent(new TitleView(titleService, accountService));
			}
		});
		return titleButton;
	}

	// private Button showLandView() {
	// landButton = new Button("Land", new Button.ClickListener() {
	// @Override
	// public void buttonClick(ClickEvent event) {
	// masterDetailContent.removeAllComponents();
	// masterDetailContent.addComponent(new LandView(landService));
	// }
	// });
	// return landButton;
	// }

}
