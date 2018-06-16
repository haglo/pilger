package org.app.view.account;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.model.entity.Account;
import org.app.model.entity.enums.AccountGroup;
import org.app.view.MainUI;
import org.app.view.TopMainMenu;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@CDIView(MainUI.ACCOUNT_VIEW)
public class AccountView extends VerticalLayout implements View {

	@Inject
	AccountService accountService;


	private Account entry;
	private Set<Account> selectedAccounts;
	private Account selectedEntry;
	private TextField firstEntryField = new TextField();
	private TextField txfMailAddress = new TextField();
	private TextField txfPassword = new TextField();
	private TextField txfComment = new TextField();
	private ComboBox <AccountGroup>cbxAccountGroup = new ComboBox<>();

	public AccountView() {
		setSizeFull();
		setSpacing(true);
		addComponent(new TopMainMenu());
		addComponent(new Label("Account View"));
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("Showing view: Account View");
	}

	@PostConstruct
	void init() {
		selectedAccounts = new HashSet<>();
		List<Account> accountList = accountService.findAll();
		accountList.sort(Comparator.comparing(Account::getUsername));
		
		DataProvider<Account, ?> dataProvider = DataProvider.ofCollection(accountList);
		Grid<Account> grid = new Grid<Account>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addSelectionListener(event -> {
			selectedAccounts = event.getAllSelectedItems();
		});

		grid.getEditor().setEnabled(true);
		grid.getEditor().addSaveListener(event -> {
			entry = event.getBean();
			updateRow(accountService, entry, grid);
		});

		cbxAccountGroup.setPageLength(8);
		cbxAccountGroup.setEmptySelectionAllowed(false);
		cbxAccountGroup.setItems(AccountGroup.System, AccountGroup.PowerUser, AccountGroup.Administrators, AccountGroup.Users);
		
		grid.setDataProvider(dataProvider);
		grid.addColumn(Account::getUsername).setCaption("Users Name").setEditorComponent(firstEntryField,
				Account::setUsername);
		grid.addColumn(Account::getMailaddress).setCaption("Email Address").setEditorComponent(txfMailAddress,
				Account::setMailaddress);
		grid.addColumn(Account::getPassword).setCaption("Password").setEditorComponent(txfPassword,
				Account::setPassword);
		grid.addColumn(Account::getAccountGroup).setCaption("Account Group").setEditorComponent(cbxAccountGroup,
				Account::setAccountGroup);
		grid.addColumn(Account::getComment).setCaption("Comment").setEditorComponent(txfComment,
				Account::setComment);

		Button add = new Button("+");
		add.addClickListener(event -> addRow(accountService, grid));

		Button delete = new Button("-");
		delete.addClickListener(event -> deleteRow(accountService, selectedAccounts, grid));

		Button details = new Button("details");
		details.addClickListener(event -> showDetails(accountService, selectedAccounts, grid));

		HorizontalLayout tb = new HorizontalLayout(add, delete, details);
		addComponent(grid);
		addComponent(tb);
	}

	private void addRow(AccountService service, Grid<Account> grid) {
		List<Account> list = service.findAll();
		entry = new Account();

		entry.setUsername("");
		entry.setMailaddress("");
		entry.setPassword("");
		entry.setComment("");
		list.add(entry);
		grid.setItems(list);

		grid.getEditor().editRow(list.size() - 1);
		firstEntryField.focus();

		service.create(entry);
		refreshGrid(grid, service);
	}

	private void deleteRow(AccountService service, Set<Account> selected, Grid<Account> grid) {
		if (selected.size() == 0) {
			Notification.show("Nothing selected");
			return;
		}
		for (Account entry : selected) {
			service.remove(entry.getId());
		}
		refreshGrid(grid, service);
	}

	private void updateRow(AccountService service, Account entry, Grid<Account> grid) {
		service.update(entry);
		refreshGrid(grid, service);
	}

	private void showDetails(AccountService service, Set<Account> selected, Grid<Account> grid) {
		final Button save = new Button("Save");
		selectedEntry = new Account();

		try {
			if (!onlyOneSelected(selected)) {
				return;
			}
			service.setEditing(false);

			// Create a sub-window and set the content
			Window subWindow = new Window("Details");
			VerticalLayout subContent = new VerticalLayout();
			subWindow.setContent(subContent);
			subWindow.center();

			for (Account entry : selected) {
				selectedEntry = entry;
			}

			TextField txfID = new TextField("ID", "" + selectedEntry.getId());
			subContent.addComponent(txfID);

			TextField txfCreateAt = new TextField("Create At", "" + selectedEntry.getCreateAt());
			subContent.addComponent(txfCreateAt);

			TextField txfModifyAt = new TextField("Modify At", "" + selectedEntry.getModifyAt());
			subContent.addComponent(txfModifyAt);

			TextField txfUsername = new TextField("Users Name", "" + selectedEntry.getUsername());
			subContent.addComponent(txfUsername);

			TextField txfMailaddress = new TextField("Email Address", "" + selectedEntry.getMailaddress());
			subContent.addComponent(txfMailaddress);

			TextField txfPassword = new TextField("Password", "" + selectedEntry.getPassword());
			subContent.addComponent(txfPassword);

			ComboBox<AccountGroup> cbxGroup = new ComboBox<AccountGroup>("Gruppe");
			cbxGroup.setItems(AccountGroup.System, AccountGroup.PowerUser, AccountGroup.Administrators, AccountGroup.Users);
			cbxGroup.setValue(selectedEntry.getAccountGroup());
			subContent.addComponent(cbxGroup);

			TextArea txaComment = new TextArea("Kommentar", "" + selectedEntry.getComment());
			subContent.addComponent(txaComment);

			CheckBox ckbEdit = new CheckBox("Edit");
			ckbEdit.addValueChangeListener(event -> {
				service.toggleEditing();
				if (event.getValue()) {
					save.setEnabled(true);
				} else {
					save.setEnabled(false);
				}
			});

			subContent.addComponent(ckbEdit);

			save.setEnabled(service.getEditing());
			subContent.addComponent(save);

			save.addClickListener(event -> {
				selectedEntry.setUsername(txfUsername.getValue());
				selectedEntry.setMailaddress(txfMailaddress.getValue());
				selectedEntry.setPassword(txfPassword.getValue());
				selectedEntry.setAccountGroup(cbxGroup.getSelectedItem().get());
				selectedEntry.setComment(txaComment.getValue());
				updateRow(service, selectedEntry, grid);
			});

			getUI().addWindow(subWindow);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refreshGrid(Grid<Account> grid, AccountService service) {
		List<Account> list = service.findAll();
		grid.setItems(list);
	}

	private boolean onlyOneSelected(Set<Account> selected) {
		boolean isCorrect = true;
		if (selected.size() > 1) {
			Notification.show("Only one Item can be selected");
			isCorrect = false;
		}
		if (selected.size() < 1) {
			Notification.show("One Item must be selected");
			isCorrect = false;
		}
		return isCorrect;

	}
	
}
