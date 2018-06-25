package org.app.view.person;

import java.util.Comparator;
import java.util.List;

import org.app.controler.AccountService;
import org.app.controler.PersonService;
import org.app.controler.TitleService;
import org.app.helper.I18n;
import org.app.model.entity.Account;
import org.app.model.entity.Person;
import org.app.model.entity.Title;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class PersonDetailView extends Window  {

	private static final long serialVersionUID = 1L;

	private I18n i18n;
	private Account createBy;
	private Account modifyBy;
	private TextField txfFirstname;
	private TextField txfLastname;
	private TextArea txaComment;
	private CheckBox ckbEdit;
	private Button saveButton;
	private ComboBox<Account> cbxCreateBy;
	private TextField txfCreateAt;
	private ComboBox<Account> cbxModifyBy;
	private TextField txfModifyAt;
	
	private PersonService personService;
	private AccountService accountService;

	@SuppressWarnings("static-access")
	public PersonDetailView(PersonView personView, Person selectedPerson) {
		i18n = new I18n();
		this.personService = personView.getPersonService();
		this.accountService = personView.getAccountService();
		saveButton = new Button(i18n.BASIC_SAVE);

		this.setCaption(i18n.TITLE_WINDOW_DETAIL_CAPTION);
		VerticalLayout subContent = new VerticalLayout();
		this.setContent(subContent);
		this.center();

		this.setStyleName("point1");
		subContent.setStyleName("point2");

		try {
			List<Account> accountList = accountService.findAll();
			personService.setEditing(false);

			TextField txfID = new TextField("ID", "" + selectedPerson.getId());
			subContent.addComponent(txfID);

			cbxCreateBy = new ComboBox<>(i18n.BASIC_CREATE_BY);
			createBy = selectedPerson.getCreateBy();
			accountList.sort(Comparator.comparing(Account::getAccountGroup));
			cbxCreateBy.setPageLength(8);
			cbxCreateBy.setItemCaptionGenerator(Account::getUsername);
			cbxCreateBy.setItems(accountList);
			cbxCreateBy.setSelectedItem(selectedPerson.getCreateBy());
			cbxCreateBy.addValueChangeListener(event -> {
				this.createBy = event.getValue();
			});
			subContent.addComponent(cbxCreateBy);

			txfCreateAt = new TextField(i18n.BASIC_CREATE_AT, "" + selectedPerson.getCreateAt());
			subContent.addComponent(txfCreateAt);

			cbxModifyBy = new ComboBox<>(i18n.BASIC_MODIFY_BY);
			modifyBy = selectedPerson.getModifyBy();
			accountList.sort(Comparator.comparing(Account::getAccountGroup));
			cbxModifyBy.setPageLength(8);
			cbxModifyBy.setItemCaptionGenerator(Account::getUsername);
			cbxModifyBy.setItems(accountList);
			cbxModifyBy.setSelectedItem(selectedPerson.getModifyBy());
			cbxModifyBy.addValueChangeListener(ev -> {
				this.modifyBy = ev.getValue();
			});
			subContent.addComponent(cbxModifyBy);

			txfModifyAt = new TextField(i18n.BASIC_MODIFY_AT, "" + selectedPerson.getModifyAt());
			subContent.addComponent(txfModifyAt);

			txfFirstname = new TextField(i18n.PERSON_SURNAME, "" + selectedPerson.getFirstName());
			subContent.addComponent(txfFirstname);

			txfLastname = new TextField(i18n.PERSON_LASTNAME, "" + selectedPerson.getLastName());
			subContent.addComponent(txfLastname);

			txaComment = new TextArea(i18n.BASIC_COMMENT, "" + selectedPerson.getComment());
			subContent.addComponent(txaComment);

			ckbEdit = new CheckBox(i18n.BASIC_EDIT);
			ckbEdit.addValueChangeListener(event -> {
				personService.toggleEditing();
				if (event.getValue()) {
					saveButton.setEnabled(true);
				} else {
					saveButton.setEnabled(false);
				}
			});

			subContent.addComponent(ckbEdit);

			saveButton.setEnabled(personService.getEditing());
			subContent.addComponent(saveButton);

			saveButton.addClickListener(event -> {
				selectedPerson.setCreateBy(createBy);
				selectedPerson.setModifyBy(modifyBy);
				selectedPerson.setFirstName(txfFirstname.getValue());
				selectedPerson.setLastName(txfLastname.getValue());
				selectedPerson.setComment(txaComment.getValue());
				personView.updateRow(selectedPerson);
				personView.refreshGrid();
				getUI().getCurrent().removeWindow(this);
			});

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
