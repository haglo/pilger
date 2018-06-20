package org.app.view.person;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.PersonService;
import org.app.helper.Constants;
import org.app.helper.I18nManager;
import org.app.helper.Translatable;
import org.app.model.dao.PersonDAO;
import org.app.model.entity.Person;
import org.app.view.TopMainMenu;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(Constants.PERSON_VIEW)
@UIScoped
public class PersonView extends VerticalLayout implements View, Translatable {

	@Inject
	PersonService personService;
	
	
	private Constants c;
	private AddressView addressView;
	private Person person;
	private Person selectedPerson;
	private Set<Person> selectedPersons;

	private TextField txfFirstName = new TextField();
	private TextField txfLastName = new TextField();
	private TextField txfComment = new TextField();
	private Grid<Person> personGrid = new Grid<Person>();

	public PersonView() {
		setSizeFull();
		setSpacing(true);
		addComponent(new TopMainMenu());
	}

	@PostConstruct
	void init() {
		selectedPersons = new HashSet<>();
		List<Person> personList = personService.getPersonDAO().findAll();
		personList.sort(Comparator.comparing(Person::getLastName));

		DataProvider<Person, ?> dataProvider = DataProvider.ofCollection(personList);
		personGrid.setSelectionMode(SelectionMode.MULTI);

		personGrid.addSelectionListener(event -> {
			selectedPersons = event.getAllSelectedItems();
			person = new Person();
			person = getTheSelectedPerson(selectedPersons);
			if (person != null) {
				showAddresses(personService.getPersonDAO(), person);
			}

		});

		personGrid.getEditor().setEnabled(true);
		personGrid.getEditor().addSaveListener(event -> {
			person = event.getBean();
			updateRow(personService, person, personGrid);
		});

		personGrid.setDataProvider(dataProvider);

		personGrid.addColumn(Person::getFirstName).setCaption(FIRST_NAME)
				.setEditorComponent(txfFirstName, Person::setFirstName).setId(FIRST_NAME);
		personGrid.addColumn(Person::getLastName).setCaption(LAST_NAME)
				.setEditorComponent(txfLastName, Person::setLastName).setId(LAST_NAME);
		personGrid.addColumn(Person::getComment).setCaption(COMMENT).setEditorComponent(txfComment, Person::setComment)
				.setId(COMMENT);

		Button add = new Button("+");
		add.addClickListener(event -> addRow(personService, personGrid));

		Button delete = new Button("-");
		delete.addClickListener(event -> deleteRow(personService, selectedPersons, personGrid));

		HorizontalLayout tb = new HorizontalLayout(add, delete);

		addComponent(personGrid);
		addComponent(tb);
		updateMessageStrings();
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

	private Person getTheSelectedPerson(Set<Person> selectedPersons) {
		selectedPerson = new Person();
		if (selectedPersons.size() > 1) {
			Notification.show(c.NOTIFICATION_ONLYONE_ITEM);
			return null;
		}
		if (selectedPersons.size() < 1) {
			Notification.show(c.NOTIFICATION_ONE_ITEM);
			return null;
		}
		if (selectedPersons.size() == 1) {
			for (Person person : selectedPersons) {
				selectedPerson = person;
			}
		}
		return selectedPerson;

	}

	private void addRow(PersonService personService, Grid<Person> personGrid) {
		List<Person> list = personService.getPersonDAO().findAll();
		person = new Person();

		person.setFirstName("");
		person.setLastName("");
		person.setComment("");
		list.add(person);
		personGrid.setItems(list);

		personGrid.getEditor().editRow(list.size() - 1);
		txfFirstName.focus();

		personService.getPersonDAO().create(person);
		refreshGrid(personGrid, personService);
	}

	private void deleteRow(PersonService personService, Set<Person> selectedPersons, Grid<Person> personGrid) {
		if (selectedPersons.size() == 0) {
			Notification.show(c.NOTIFICATION_NO_ITEM);
			return;
		}
		for (Person person : selectedPersons) {
			personService.getPersonDAO().remove(person.getId());
		}
		refreshGrid(personGrid, personService);
	}

	private void updateRow(PersonService personService, Person person, Grid<Person> personGrid) {
		personService.getPersonDAO().update(person);
		refreshGrid(personGrid, personService);
	}

	private void refreshGrid(Grid<Person> personGrid, PersonService personService) {
		List<Person> personList = personService.getPersonDAO().findAll();
		personGrid.setItems(personList);
	}

	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		c = new Constants();
		personGrid.getColumn(FIRST_NAME).setCaption(i18n.getMessage("person.surname"));
		personGrid.getColumn(LAST_NAME).setCaption(i18n.getMessage("person.lastname"));
		personGrid.getColumn(COMMENT).setCaption(i18n.getMessage("basic.comment"));

		c.NOTIFICATION_NO_ITEM = i18n.getMessage("notification.noItem");
		c.NOTIFICATION_ONE_ITEM = i18n.getMessage("notification.oneItem");
		c.NOTIFICATION_ONLYONE_ITEM = i18n.getMessage("notification.onlyOneItem");

	}
}
