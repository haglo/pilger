package org.app.view.person;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.security.DenyAll;
import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.controler.PersonService;
import org.app.helper.I18n;
import org.app.model.entity.Account;
import org.app.model.entity.Person;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.PERSON_VIEW)
@UIScoped
public class PersonView extends VerticalLayout implements View {

	@Inject
	PersonService personService;

	@Inject
	AccountService accountService;

	private I18n i18n;
	private Person selectedPerson;
	private Person newPerson;
	private Set<Person> selectedPersons;

	private SaveModus saveModus;
	private TextField txfFirstName = new TextField();
	private TextField txfLastName = new TextField();
	private TextField txfComment = new TextField();

	private VerticalLayout mainContent;
	private VerticalLayout personContent;
	private Grid<Person> personGrid;
	private HorizontalLayout personNavBar;
	private HorizontalLayout addressCommunicationContent;

	private AddressView addressView;
	private CommunicationView communicationView;

	public PersonView() {
		i18n = new I18n();
		setSizeFull();
		//setSpacing(false);
		setStyleName("pilger-person-main");
		setMargin(new MarginInfo(false, true, true, true));
		//setWidth("1000px");
	}

	@PostConstruct
	void init() {
		mainContent = new VerticalLayout();
		
		personContent = new VerticalLayout();
		personGrid = new Grid<Person>();
		personNavBar = new HorizontalLayout();
		addressCommunicationContent = new HorizontalLayout();

		saveModus = SaveModus.UPDATE;
		selectedPersons = new HashSet<Person>();
		selectedPerson = new Person();
		newPerson = new Person();

		//personContent.setSpacing(false);
		
		mainContent.setStyleName("pilger-person-main");
		personContent.setStyleName("pilger-person-person-content");
		personGrid.setStyleName("pilger-person-person-grid");
		personNavBar.setStyleName("pilger-person-person-nav-bar");
		addressCommunicationContent.setStyleName("pilger-person-address-communication-content");
		
		personGrid.setWidth("100%");
		//personGrid.setSizeFull();
//		personGrid.setHeightMode(HeightMode.UNDEFINED);
		//personGrid.setHeight("100%");
		mainContent.setMargin(false);
//		mainContent.setSizeFull();
		personContent.setMargin(false);
//		personContent.setSizeFull();

		//addressCommunicationContent.setMargin(false);
		addressCommunicationContent.setWidth("100%");
		//addressCommunicationContent.setSizeFull();
		//addressCommunicationContent.setHeight("30%");

		List<Person> personList = personService.getPersonDAO().findAll();
		personList.sort(Comparator.comparing(Person::getLastName));

		DataProvider<Person, ?> dataProvider = DataProvider.ofCollection(personList);
		personGrid.setSelectionMode(SelectionMode.MULTI);

		personGrid.addSelectionListener(event -> {
			selectedPerson = new Person();
			selectedPersons = new HashSet<Person>();
			selectedPersons = event.getAllSelectedItems();
			if (selectedPersons.size() == 0) {
				addressCommunicationContent.removeComponent(addressView);
				addressCommunicationContent.removeComponent(communicationView);
				mainContent.removeComponent(addressCommunicationContent);
				addressCommunicationContent.removeStyleName("visible");
			} else {
				selectedPerson = getTheSelectedPerson(selectedPersons);
				if (selectedPerson != null) {
					showAddresses(selectedPerson);
					showCommunication(selectedPerson);
					mainContent.addComponent(addressCommunicationContent);
					addressCommunicationContent.addStyleName("visible");
				}
			}
		});

		personGrid.getEditor().setEnabled(true);
		personGrid.getEditor().addSaveListener(event -> {
			selectedPerson = event.getBean();
			if (saveModus == SaveModus.UPDATE) {
				selectedPerson = new Person();
				selectedPerson.setModifyBy(getSession().getAttribute(Account.class));
				updateRow(selectedPerson);
			} else {
				newPerson = new Person();
				newPerson.setCreateBy(getSession().getAttribute(Account.class));
				createPerson(newPerson);
			}
		});

		personGrid.getEditor().addCancelListener(event -> {
			if (saveModus == SaveModus.UPDATE) {
				selectedPerson = new Person();
				selectedPerson = event.getBean();
			} else {
				newPerson = new Person();
				newPerson = event.getBean();
				deletePerson(newPerson);
			}

		});

		personGrid.setDataProvider(dataProvider);

		personGrid.addColumn(Person::getFirstName).setCaption(i18n.PERSON_SURNAME).setEditorComponent(txfFirstName,
				Person::setFirstName);
		personGrid.addColumn(Person::getLastName).setCaption(i18n.PERSON_LASTNAME).setEditorComponent(txfLastName,
				Person::setLastName);
		personGrid.addColumn(Person::getComment).setCaption(i18n.BASIC_COMMENT).setEditorComponent(txfComment,
				Person::setComment);

		Button add = new Button("+");
		add.addClickListener(event -> {
			saveModus = SaveModus.NEW;
			addRow();
		});

		Button delete = new Button("-");
		delete.addClickListener(event -> deleteRow(selectedPersons));

		Button detail = new Button("", ev -> {
			if (getTheSelectedPerson(selectedPersons) != null) {
				getUI().addWindow(new PersonDetailView(this, getTheSelectedPerson(selectedPersons)));
			}
			refreshGrid();

		});
		detail.setIcon(VaadinIcons.PENCIL);

		CssLayout personNavBar = new CssLayout(add, delete, detail);
		personNavBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		personContent.addComponent(personGrid);
		personContent.addComponent(personNavBar);

		mainContent.addComponent(personContent);
		mainContent.addComponent(addressCommunicationContent);

		mainContent.setExpandRatio(personContent, 0.7f);
		mainContent.setExpandRatio(addressCommunicationContent, 0.3f);

		addComponent(mainContent);
	}

	private void showAddresses(Person selectedPerson) {

		try {
			if (addressView.isAttached()) {
				removeComponent(addressView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		addressView = new AddressView(personService.getPersonDAO(), selectedPerson);
		addressCommunicationContent.addComponent(addressView);
	}

	private void showCommunication(Person selectedPerson) {

		try {
			if (communicationView.isAttached()) {
				removeComponent(addressView);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		communicationView = new CommunicationView(personService.getPersonDAO(), selectedPerson);
		addressCommunicationContent.addComponent(communicationView);
	}

	private Person getTheSelectedPerson(Set<Person> selectedPersons) {
		selectedPerson = new Person();
		if (selectedPersons.size() > 1) {
			Notification.show(i18n.NOTIFICATION_ONLY_ONE_ITEM);
			return null;
		}
		if (selectedPersons.size() < 1) {
			Notification.show(i18n.NOTIFICATION_EXACT_ONE_ITEM);
			return null;
		}
		if (selectedPersons.size() == 1) {
			for (Person person : selectedPersons) {
				selectedPerson = person;
			}
		}
		return selectedPerson;

	}

	private void addRow() {
		List<Person> list = personService.getPersonDAO().findAll();
		newPerson = new Person();

		newPerson.setFirstName("");
		newPerson.setLastName("");
		newPerson.setComment("");
		list.add(newPerson);
		personGrid.setItems(list);

		personGrid.getEditor().editRow(list.size() - 1);
		txfFirstName.focus();

		createPerson(newPerson);
	}

	private void deleteRow(Set<Person> selectedPersons) {
		if (selectedPersons.size() == 0) {
			Notification.show(i18n.NOTIFICATION_NO_ITEM);
			return;
		}
		for (Person person : selectedPersons) {
			personService.getPersonDAO().remove(person.getId());
		}
		refreshGrid();
	}

	public void updateRow(Person person) {
		personService.getPersonDAO().update(person);
		refreshGrid();
	}

	public void createPerson(Person person) {

		try {
			personService.getPersonDAO().create(person);
			refreshGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			saveModus = SaveModus.UPDATE;
		}
	}

	public void deletePerson(Person person) {
		try {
			personService.getPersonDAO().remove(person.getId());
			refreshGrid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			saveModus = SaveModus.UPDATE;
		}
	}

	public void refreshGrid() {
		List<Person> personList = personService.getPersonDAO().findAll();
		personGrid.setItems(personList);
	}

	private enum SaveModus {
		NEW, UPDATE
	}

	public PersonService getPersonService() {
		return personService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

}