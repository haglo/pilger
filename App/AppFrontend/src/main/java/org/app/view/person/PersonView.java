package org.app.view.person;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.app.model.dao.PersonDAO;
import org.app.model.entity.Person;

import com.vaadin.cdi.UIScoped;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.View;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@UIScoped
public class PersonView extends VerticalLayout implements View {

	@EJB
	private PersonDAO personDAO;

	final private String firstName = "Vorname";
	final private String lastName = "Nachname";

	private AddressView addressView;
	private Person person;
	private Person selectedPerson;
	private Set<Person> selectedPersons;

	public PersonView() {
		setSizeFull();
		setSpacing(true);
	}

	@PostConstruct
	void init() {
		selectedPersons = new HashSet<>();
		List<Person> personList = personDAO.findAll();
		personList.sort(Comparator.comparing(Person::getLastName));

		DataProvider<Person, ?> dataProvider = DataProvider.ofCollection(personList);
		Grid<Person> personGrid = new Grid<Person>();
		personGrid.setSelectionMode(SelectionMode.MULTI);
		personGrid.addSelectionListener(event -> {
			selectedPersons = event.getAllSelectedItems();
			person = new Person();
			person = personDAO.findByID(1);
			showAddresses(personDAO, person);
			
//			person = exactOneSelected(selectedPersons);
//			System.out.println("Person " + person.getFirstName());
//			if (person != null) {
//				showAddresses(personDAO, selectedPerson);
//			}
		});

		personGrid.getEditor().setEnabled(true);

		personGrid.setDataProvider(dataProvider);
		personGrid.addColumn(Person::getFirstName).setCaption(firstName);
		personGrid.addColumn(Person::getLastName).setCaption(lastName);

		addComponent(personGrid);
	}

	private void showAddresses(PersonDAO personDAO, Person selectedPerson) {

		try {
			if (addressView.isAttached()) {
				removeComponent(addressView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		addressView = new AddressView(personDAO, selectedPerson);
		addComponent(addressView);
	}

	private Person getSelectedPerson(Set<Person> selectedPersons) {
		if (selectedPersons.size() > 1) {
			Notification.show("Only one Item can be selected");
			return null;
		}
		if (selectedPersons.size() < 1) {
			Notification.show("One Item must be selected");
			return null;
		}
		if (selectedPersons.size() == 1) {
			for (Person person : selectedPersons) {
				selectedPerson = person;
			}
		}
		return selectedPerson;
		
	}
}
