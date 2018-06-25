package org.app.view.account;

import java.util.EnumSet;

import org.app.controler.AccountService;
import org.app.helper.I18n;
import org.app.helper.I18nManager;
import org.app.helper.Translatable;
import org.app.model.entity.Account;
import org.app.model.entity.enums.AccountGroup;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


public class AccountDetailView extends Window {

	private static final long serialVersionUID = 1L;

	private I18n i18n;
	private TextField txfUsername;
	private TextField txfMailaddress;
	private TextField txfPassword;
	private TextArea txaComment;
	private CheckBox ckbEdit;
	private Button saveButton;
	private TextField txfCreateAt;
	private TextField txfModifyAt;
	private ComboBox<AccountGroup> cbxGroup;

	private AccountService accountService;
	
	@SuppressWarnings("static-access")
	public AccountDetailView(AccountView accountView, Account selectedAccount) {
		i18n = new I18n();
		this.accountService = accountView.getAccountService();
		saveButton = new Button(i18n.BASIC_SAVE);

		this.setCaption(i18n.ACCOUNT_WINDOW_DETAIL_CAPTION);
		VerticalLayout subContent = new VerticalLayout();
		this.setContent(subContent);
		this.center();

		try {
			accountService.setEditing(false);

			TextField txfID = new TextField("ID", "" + selectedAccount.getId());
			subContent.addComponent(txfID);

			txfCreateAt = new TextField(i18n.BASIC_CREATE_AT, "" + selectedAccount.getCreateAt());
			subContent.addComponent(txfCreateAt);

			txfModifyAt = new TextField(i18n.BASIC_MODIFY_AT, "" + selectedAccount.getModifyAt());
			subContent.addComponent(txfModifyAt);

			txfUsername = new TextField(i18n.ACCOUNT_USERNAME, "" + selectedAccount.getUsername());
			subContent.addComponent(txfUsername);

			txfMailaddress = new TextField(i18n.BASIC_EMAIL, "" + selectedAccount.getMailaddress());
			subContent.addComponent(txfMailaddress);

			txfPassword = new TextField(i18n.ACCOUNT_PASSWORD, "" + selectedAccount.getPassword());
			subContent.addComponent(txfPassword);

			cbxGroup = new ComboBox<AccountGroup>(i18n.ACCOUNT_GROUP);
			cbxGroup.setItems(EnumSet.allOf(AccountGroup.class));
			cbxGroup.setValue(selectedAccount.getAccountGroup());
			subContent.addComponent(cbxGroup);

			txaComment = new TextArea(i18n.BASIC_COMMENT, "" + selectedAccount.getComment());
			subContent.addComponent(txaComment);

			ckbEdit = new CheckBox(i18n.BASIC_EDIT);
			ckbEdit.addValueChangeListener(event -> {
				accountService.toggleEditing();
				if (event.getValue()) {
					saveButton.setEnabled(true);
				} else {
					saveButton.setEnabled(false);
				}
			});

			subContent.addComponent(ckbEdit);

			saveButton.setEnabled(accountService.getEditing());
			subContent.addComponent(saveButton);

			saveButton.addClickListener(event -> {
				selectedAccount.setUsername(txfUsername.getValue());
				selectedAccount.setMailaddress(txfMailaddress.getValue());
				selectedAccount.setPassword(txfPassword.getValue());
				selectedAccount.setAccountGroup(cbxGroup.getSelectedItem().get());
				selectedAccount.setComment(txaComment.getValue());
				accountView.updateRow(selectedAccount);
				accountView.refreshGrid();
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
