package org.app.view.help;

import com.vaadin.cdi.ViewScoped;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@ViewScoped
public class AboutSubView extends Window {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "About";

	    public AboutSubView() {
	        VerticalLayout subContent = new VerticalLayout();
			this.setContent(subContent);
			this.center();
			
			subContent.addComponent(new TextArea());
			subContent.addComponent(new TextField("hallot"));
			subContent.addComponent(new TextField("hallot"));
			subContent.addComponent(new TextField("hallot"));
	    }
}
