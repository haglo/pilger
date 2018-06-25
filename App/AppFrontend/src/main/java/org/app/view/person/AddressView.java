package org.app.view.person;

import java.util.ArrayList;
import java.util.List;

import org.app.helper.I18n;
import org.app.helper.I18nManager;
import org.app.helper.Translatable;
import org.app.model.dao.PersonDAO;
import org.app.model.entity.Address;
import org.app.model.entity.Person;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class AddressView extends VerticalLayout implements View {

	private I18n i18n;
	private Grid<Address> grid;
	private ListDataProvider<Address> addressDataProvider;
	private List<Address> addressList;

	public AddressView(PersonDAO personDAO, Person selectedPerson) {
		//top - right - bottom - left
		setMargin(new MarginInfo(false, true, false, false));
		setStyleName("pilger-address-view");
		//setWidth("45%");
		setSizeFull();

		HorizontalLayout addressNavBar;
		i18n = new I18n();
		addressList = new ArrayList<Address>();
		addressList = personDAO.findAddresses(selectedPerson);
		
		addressDataProvider = DataProvider.ofCollection(addressList);
		grid = new Grid<Address>();
		grid.setStyleName("pilger-address-view-grid");
		//grid.setWidth("95%");
		grid.setSizeFull();

		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setDataProvider(addressDataProvider);

		grid.addColumn(address -> address.getStreet()).setCaption(i18n.PERSON_STREET);
		grid.addColumn(address -> address.getPostbox()).setCaption(i18n.PERSON_POSTBOX);
		grid.addColumn(address -> address.getZip()).setCaption(i18n.PERSON_ZIPCODE);
		grid.addColumn(address -> address.getCity()).setCaption(i18n.PERSON_CITY);
		
		
		Button add = new Button("+");
		Button delete = new Button("-");
		
		addressNavBar = new HorizontalLayout(add, delete);
		addComponent(grid);
		addComponent(addressNavBar);
	}
}