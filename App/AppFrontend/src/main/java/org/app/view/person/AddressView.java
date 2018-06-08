package org.app.view.person;

import java.util.ArrayList;
import java.util.List;

import org.app.model.dao.PersonDAO;
import org.app.model.entity.Address;
import org.app.model.entity.Person;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class AddressView extends VerticalLayout implements View {

	final private String street = "Straße";
	final private String zip = "PLZ";
	final private String city = "Stadt";

	private Grid<Address> grid;

	private ListDataProvider<Address> addressDataProvider;

	private List<Address> addressList;

	public AddressView() {
		grid = new Grid<Address>();
		addComponent(grid);
	}

	public AddressView(PersonDAO personDAO, Person selectedPerson) {
//		addressList = person.getAddresses();
//		addressDataProvider = DataProvider.ofCollection(addressList);
//
//		grid = new Grid<Address>();
//		grid.setSelectionMode(SelectionMode.MULTI);
//		grid.setDataProvider(addressDataProvider);
//		grid.addColumn(address -> address.getStreet()).setCaption(street);
//		grid.addColumn(address -> address.getZip()).setCaption(zip);
//		grid.addColumn(address -> address.getCity()).setCaption(city);
		
		addressList = new ArrayList<Address>();
		addressList = personDAO.findAddresses(selectedPerson);
		
		addressDataProvider = DataProvider.ofCollection(addressList);
		grid = new Grid<Address>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setDataProvider(addressDataProvider);
		grid.addColumn(address -> address.getStreet()).setCaption(street);
		grid.addColumn(address -> address.getZip()).setCaption(zip);
		grid.addColumn(address -> address.getCity()).setCaption(city);

		addComponent(new Label("Hallo Address View"));
		addComponent(grid);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}

}