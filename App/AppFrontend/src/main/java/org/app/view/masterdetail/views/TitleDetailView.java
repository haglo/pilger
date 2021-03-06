package org.app.view.masterdetail.views;

import java.util.Comparator;
import java.util.List;

import org.app.controler.AccountService;
import org.app.controler.TitleService;
import org.app.helper.I18n;
import org.app.model.entity.Account;
import org.app.model.entity.Title;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TitleDetailView extends Window  {

	private static final long serialVersionUID = 1L;

	private I18n i18n;
	private Account createBy;
	private Account modifyBy;
	private TextField txfListPrio;
	private TextField txfValue;
	private TextArea txaComment;
	private CheckBox ckbEdit;
	private Button saveButton;
	private ComboBox<Account> cbxCreateBy;
	private TextField txfCreateAt;
	private ComboBox<Account> cbxModifyBy;
	private TextField txfModifyAt;
	
	private TitleService titleService;
	private AccountService accountService;

	@SuppressWarnings("static-access")
	public TitleDetailView(TitleView titleView, Title selectedTitle) {
		i18n = new I18n();
		this.titleService = titleView.getTitleService();
		this.accountService = titleView.getAccountService();
		saveButton = new Button(i18n.BASIC_SAVE);

		this.setCaption(i18n.TITLE_WINDOW_DETAIL_CAPTION);
		VerticalLayout subContent = new VerticalLayout();
		this.setContent(subContent);
		this.center();


		try {
			List<Account> accountList = accountService.findAll();
			titleService.setEditing(false);

			TextField txfID = new TextField("ID", "" + selectedTitle.getId());
			subContent.addComponent(txfID);

			cbxCreateBy = new ComboBox<>(i18n.BASIC_CREATE_BY);
			createBy = selectedTitle.getCreateBy();
			accountList.sort(Comparator.comparing(Account::getAccountGroup));
			cbxCreateBy.setPageLength(8);
			cbxCreateBy.setItemCaptionGenerator(Account::getUsername);
			cbxCreateBy.setItems(accountList);
			cbxCreateBy.setSelectedItem(selectedTitle.getCreateBy());
			cbxCreateBy.addValueChangeListener(event -> {
				this.createBy = event.getValue();
			});
			subContent.addComponent(cbxCreateBy);

			txfCreateAt = new TextField(i18n.BASIC_CREATE_AT, "" + selectedTitle.getCreateAt());
			subContent.addComponent(txfCreateAt);

			cbxModifyBy = new ComboBox<>(i18n.BASIC_MODIFY_BY);
			modifyBy = selectedTitle.getModifyBy();
			accountList.sort(Comparator.comparing(Account::getAccountGroup));
			cbxModifyBy.setPageLength(8);
			cbxModifyBy.setItemCaptionGenerator(Account::getUsername);
			cbxModifyBy.setItems(accountList);
			cbxModifyBy.setSelectedItem(selectedTitle.getModifyBy());
			cbxModifyBy.addValueChangeListener(ev -> {
				this.modifyBy = ev.getValue();
			});
			subContent.addComponent(cbxModifyBy);

			txfModifyAt = new TextField(i18n.BASIC_MODIFY_AT, "" + selectedTitle.getModifyAt());
			subContent.addComponent(txfModifyAt);

			txfListPrio = new TextField(i18n.BASIC_LIST_PRIO, "" + selectedTitle.getListPrio());
			subContent.addComponent(txfListPrio);

			txfValue = new TextField(i18n.TITLE_VALUE, "" + selectedTitle.getTitleValue());
			subContent.addComponent(txfValue);

			txaComment = new TextArea(i18n.BASIC_COMMENT, "" + selectedTitle.getComment());
			subContent.addComponent(txaComment);

			ckbEdit = new CheckBox(i18n.BASIC_EDIT);
			ckbEdit.addValueChangeListener(event -> {
				titleService.toggleEditing();
				if (event.getValue()) {
					saveButton.setEnabled(true);
				} else {
					saveButton.setEnabled(false);
				}
			});

			subContent.addComponent(ckbEdit);

			saveButton.setEnabled(titleService.getEditing());
			subContent.addComponent(saveButton);

			saveButton.addClickListener(event -> {
				selectedTitle.setCreateBy(createBy);
				selectedTitle.setModifyBy(modifyBy);
				selectedTitle.setListPrio(Integer.valueOf(txfListPrio.getValue()));
				selectedTitle.setTitleValue(txfValue.getValue());
				selectedTitle.setComment(txaComment.getValue());
				titleView.updateRow(selectedTitle);
				titleView.refreshGrid();
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
