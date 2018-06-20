package org.app.view.person;

import java.util.ArrayList;
import java.util.List;

import org.app.helper.I18nManager;
import org.app.helper.Translatable;
import org.app.model.dao.PersonDAO;
import org.app.model.entity.Address;
import org.app.model.entity.Person;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class AddressView extends VerticalLayout implements View, Translatable {

	private Grid<Address> grid;

	private ListDataProvider<Address> addressDataProvider;

	private List<Address> addressList;

	public AddressView() {
		grid = new Grid<Address>();
		addComponent(grid);
	}

	public AddressView(PersonDAO personDAO, Person selectedPerson) {
		addressList = new ArrayList<Address>();
		addressList = personDAO.findAddresses(selectedPerson);
		
		addressDataProvider = DataProvider.ofCollection(addressList);
		grid = new Grid<Address>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setDataProvider(addressDataProvider);
		grid.addColumn(address -> address.getStreet()).setCaption(STREET).setId(STREET);
		grid.addColumn(address -> address.getPostbox()).setCaption(POSTBOX).setId(POSTBOX);
		grid.addColumn(address -> address.getZip()).setCaption(ZIPCODE).setId(ZIPCODE);
		grid.addColumn(address -> address.getCity()).setCaption(CITY).setId(CITY);

		addComponent(grid);
		updateMessageStrings();
	}
	
	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		grid.getColumn(STREET).setCaption(i18n.getMessage("person.street"));
		grid.getColumn(POSTBOX).setCaption(i18n.getMessage("person.postbox"));
		grid.getColumn(ZIPCODE).setCaption(i18n.getMessage("person.zipcode"));
		grid.getColumn(CITY).setCaption(i18n.getMessage("person.city"));

	}


}