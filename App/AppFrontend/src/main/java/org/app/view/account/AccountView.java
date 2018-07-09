package org.app.view.account;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.controler.SettingsService;
import org.app.helper.I18n;
import org.app.model.entity.Account;
import org.app.model.entity.enums.AccountGroup;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(I18n.ACCOUNT_VIEW)
public class AccountView extends HorizontalLayout implements View {

	@Inject
	AccountService accountService;
	
	@Inject
	SettingsService settings;


	private I18n i18n;
	private Account account;
	private Set<Account> selectedAccounts;
	private Account selectedAccount;
	private TextField firstEntryField = new TextField();
	private TextField txfMailAddress = new TextField();
	private TextField txfPassword = new TextField();
	private TextField txfComment = new TextField();
	private ComboBox<AccountGroup> cbxAccountGroup = new ComboBox<>();
	private Grid<Account> grid;

	public AccountView() {
		i18n = new I18n();
		setMargin(new MarginInfo(false, true, true, true));
	}

	@PostConstruct
	void init() {
		setSizeFull();
		setWidth(settings.getAppWindowWidth());

		VerticalLayout content = new VerticalLayout();
		selectedAccounts = new HashSet<>();
		List<Account> accountList = accountService.findAll();
		accountList.sort(Comparator.comparing(Account::getUsername));

		DataProvider<Account, ?> dataProvider = DataProvider.ofCollection(accountList);
		grid = new Grid<Account>();
		grid.setSizeFull();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addSelectionListener(event -> {
			selectedAccounts = event.getAllSelectedItems();
		});

		grid.getEditor().setEnabled(true);
		grid.getEditor().addSaveListener(event -> {
			account = event.getBean();
			updateRow(account);
		});

		cbxAccountGroup.setPageLength(8);
		cbxAccountGroup.setEmptySelectionAllowed(false);
		cbxAccountGroup.setItems(EnumSet.allOf(AccountGroup.class));

		grid.setDataProvider(dataProvider);
		grid.addColumn(Account::getUsername).setCaption(i18n.ACCOUNT_USERNAME)
				.setEditorComponent(firstEntryField, Account::setUsername).setId(i18n.ACCOUNT_USERNAME);
		grid.addColumn(Account::getMailaddress).setCaption(i18n.BASIC_EMAIL).setEditorComponent(txfMailAddress,
				Account::setMailaddress);
		grid.addColumn(Account::getPassword).setCaption(i18n.ACCOUNT_PASSWORD).setEditorComponent(txfPassword,
				Account::setPassword);
		grid.addColumn(Account::getAccountGroup).setCaption(i18n.ACCOUNT_GROUP).setEditorComponent(cbxAccountGroup,
				Account::setAccountGroup);
		grid.addColumn(Account::getComment).setCaption(i18n.BASIC_COMMENT).setEditorComponent(txfComment,
				Account::setComment);

		Button add = new Button("+");
		add.addClickListener(event -> {
			getUI().addWindow(new AccountNewView(this));
		});

		Button delete = new Button("-");
		delete.addClickListener(event -> deleteRow());

		Button detail = new Button("", ev -> {
			if (onlyOneSelected(selectedAccounts)) {
				for (Account entry : selectedAccounts) {
					selectedAccount = entry;
					getUI().addWindow(new AccountDetailView(this, selectedAccount));
				}
			}
		});
		detail.setIcon(VaadinIcons.PENCIL);

		CssLayout accountNavBar = new CssLayout(add, delete, detail);
		accountNavBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		content.addComponent(grid);
		content.addComponent(accountNavBar);
		addComponent(content);
		setDefaultComponentAlignment(Alignment.TOP_CENTER);
	}

//	private void addRow() {
//		try {
//			account = new Account();
//			List<Account> list = accountService.findAll();
//
//			account.setUsername("");
//			account.setMailaddress("");
//			account.setPassword("");
//			account.setComment("");
//			list.add(account);
//			grid.setItems(list);
//
//			grid.getEditor().editRow(list.size() - 1);
//			firstEntryField.focus();
//
//			accountService.create(account);
//			refreshGrid();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//		}
//	}

	private void deleteRow() {
		if (selectedAccounts.size() == 0) {
			Notification.show(i18n.NOTIFICATION_NO_ITEM);
			return;
		}
		for (Account entry : selectedAccounts) {
			accountService.remove(entry.getId());
		}
		refreshGrid();
	}

	public void updateRow(Account account) {
		accountService.update(account);
		refreshGrid();
	}

	public void refreshGrid() {
		List<Account> list = accountService.findAll();
		grid.sort(i18n.ACCOUNT_USERNAME, SortDirection.ASCENDING);
		grid.setItems(list);
	}

	private boolean onlyOneSelected(Set<Account> selected) {
		boolean isCorrect = true;
		if (selected.size() > 1) {
			Notification.show(i18n.NOTIFICATION_ONLY_ONE_ITEM);
			isCorrect = false;
		}
		if (selected.size() < 1) {
			Notification.show(i18n.NOTIFICATION_EXACT_ONE_ITEM);
			isCorrect = false;
		}
		return isCorrect;

	}

	public AccountService getAccountService() {
		return accountService;
	}

}
