package org.app.view.staff;

import javax.annotation.PostConstruct;

import org.app.helper.I18n;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("serial")
@CDIView(I18n.SPLIT_VIEW)
@UIScoped
public class SplitView extends VerticalLayout implements View {

	public SplitView() {
//		setSplitPosition(70, Sizeable.UNITS_PERCENTAGE);
		setSizeFull();
	}

	@PostConstruct
	void init() {
//		Panel panel = new Panel("Split Panels Inside This Panel");

		VerticalSplitPanel vsplit = new VerticalSplitPanel();
		vsplit.setSplitPosition(75, Unit.PERCENTAGE);
		vsplit.setSizeFull();

		VerticalLayout contentA = new VerticalLayout();
		contentA.addComponent(new Label("Hallo Welt, oben"));
		
		HorizontalLayout contentB = new HorizontalLayout();
		contentB.addComponent(new Label("Hallo Welt, links"));
		contentB.addComponent(new Label("Hallo Welt, rechts"));

		vsplit.setFirstComponent(contentA);
		vsplit.setSecondComponent(contentB);

//		panel.setContent(vsplit);
//		panel.setSizeFull();
		
//		addComponent(panel);
		addComponent(vsplit);
		
		
//		SplitLayout layout = new SplitLayout(
//		        new Label("First content component"),
//		        new Label("Second content component"));

	}


}
