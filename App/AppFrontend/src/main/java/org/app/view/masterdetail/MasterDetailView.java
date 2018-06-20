package org.app.view.masterdetail;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.controler.TitleService;
import org.app.helper.Constants;
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
@CDIView(Constants.MASTER_DETAIL_VIEW)
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
//		masterDetailLeftNavBar.addStyleName(ValoTheme.MENU_ROOT);
		masterDetailContent = new CssLayout();

		Label head = new Label("Master Detail");
//		head.addStyleName(ValoTheme.MENU_ROOT);

		masterDetailLeftNavBar.addComponent(head);
		masterDetailLeftNavBar.addComponent(showTitleView());
		// masterDetailLeftNavBar.addComponent(showLandView());

		allView.addComponent(masterDetailLeftNavBar);
		allView.addComponent(masterDetailContent);
		allView.setSizeFull();

		addStyleName("navigation-bar");
		addComponent(allView);

	}


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

}
