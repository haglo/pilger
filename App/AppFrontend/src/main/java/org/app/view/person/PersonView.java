package org.app.view.person;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.inject.Inject;

import org.app.view.MainUI;
import org.app.view.I18nManager;
import org.app.view.TopMainMenu;
import org.app.view.Translatable;
import org.app.controler.PersonService;
import org.app.model.dao.PersonDAO;
import org.app.model.entity.Person;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.cdi.ViewScoped;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(MainUI.PERSON_VIEW)
@UIScoped
public class PersonView extends VerticalLayout implements View, Translatable {

	@Inject
	PersonService personService;

	final private String firstName = "Vorname";
	final private String lastName = "Nachname";
	final private String comment = "Kommentar";

	private AddressView addressView;
	private Person person;
	private Person selectedPerson;
	private Set<Person> selectedPersons;

	private TextField txfFirstName = new TextField();
	private TextField txfLastName = new TextField();
	private TextField txfComment = new TextField();
	private Label welcomeMessageLabel = new Label();
	private Label langMesssageLabel = new Label();
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

			// person = personDAO.findByID(1);
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

		personGrid.addColumn(Person::getFirstName).setCaption(firstName)
				.setEditorComponent(txfFirstName, Person::setFirstName).setId(firstName);
		personGrid.addColumn(Person::getLastName).setCaption(lastName)
				.setEditorComponent(txfLastName, Person::setLastName).setId(lastName);
		personGrid.addColumn(Person::getComment).setCaption(comment).setEditorComponent(txfComment, Person::setComment)
				.setId(comment);

		Button add = new Button("+");
		add.addClickListener(event -> addRow(personService, personGrid));

		Button delete = new Button("-");
		delete.addClickListener(event -> deleteRow(personService, selectedPersons, personGrid));

		HorizontalLayout tb = new HorizontalLayout(add, delete);

		addComponent(langMesssageLabel);
		addComponent(welcomeMessageLabel);
		addComponent(personGrid);
		addComponent(tb);
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
			Notification.show("Nothing selected");
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
		welcomeMessageLabel.setCaption(i18n.getMessage("person.welcomemessage"));
		langMesssageLabel.setCaption(
				i18n.getMessage("person.language", getLoc().getCountry(), getLoc().getLanguage(), getLoc().toString()));
		personGrid.getColumn(firstName).setCaption(i18n.getMessage("person.surname"));
		personGrid.getColumn(lastName).setCaption(i18n.getMessage("person.lastname"));
		personGrid.getColumn(comment).setCaption(i18n.getMessage("basic.comment"));

	}

	public Locale getLoc() {
		return ((MainUI) UI.getCurrent()).getLocale();

	}

}
