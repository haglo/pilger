package org.app.view.masterdetail.views;

import java.util.Comparator;
import java.util.List;

import org.app.controler.AccountService;
import org.app.controler.TitleService;
import org.app.helper.Constants;
import org.app.helper.I18nManager;
import org.app.helper.Translatable;
import org.app.model.entity.Account;
import org.app.model.entity.Title;

import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TitleDetailView extends Window implements Translatable {

	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "Details - Title";

	private Constants c;
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
		this.titleService = titleView.getTitleService();
		this.accountService = titleView.getAccountService();
		saveButton = new Button("Save");

		this.setCaption(VIEW_NAME);
		VerticalLayout subContent = new VerticalLayout();
		this.setContent(subContent);
		this.center();


		try {
			List<Account> accountList = accountService.findAll();
			titleService.setEditing(false);

			TextField txfID = new TextField("ID", "" + selectedTitle.getId());
			subContent.addComponent(txfID);

			cbxCreateBy = new ComboBox<>("CreateBy");
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

			txfCreateAt = new TextField("Create At", "" + selectedTitle.getCreateAt());
			subContent.addComponent(txfCreateAt);

			cbxModifyBy = new ComboBox<>("Modify By");
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

			txfModifyAt = new TextField("Modify At", "" + selectedTitle.getModifyAt());
			subContent.addComponent(txfModifyAt);

			txfListPrio = new TextField(LISTPRIO, "" + selectedTitle.getListPrio());
			subContent.addComponent(txfListPrio);

			txfValue = new TextField(VALUE, "" + selectedTitle.getTitleValue());
			subContent.addComponent(txfValue);

			txaComment = new TextArea(COMMENT, "" + selectedTitle.getComment());
			subContent.addComponent(txaComment);

			ckbEdit = new CheckBox("Edit");
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
			updateMessageStrings();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		c = new Constants();
		this.setCaption(i18n.getMessage("title.windowcaption"));
		cbxCreateBy.setCaption(i18n.getMessage("basic.createby"));
		txfCreateAt.setCaption(i18n.getMessage("basic.createat"));
		cbxModifyBy.setCaption(i18n.getMessage("basic.modifyby"));
		txfModifyAt.setCaption(i18n.getMessage("basic.modifyat"));
		txfListPrio.setCaption(i18n.getMessage("basic.listprio"));
		txfValue.setCaption(i18n.getMessage("title.value"));
		txaComment.setCaption(i18n.getMessage("basic.comment"));
		ckbEdit.setCaption(i18n.getMessage("basic.edit"));
		saveButton.setCaption(i18n.getMessage("basic.save"));
	}

}
