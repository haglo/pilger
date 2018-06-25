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

public class TitleNewView extends Window {

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
	private Title newTitle;
	private AccountService accountService;

	@SuppressWarnings("static-access")
	public TitleNewView(TitleView titleView) {
		i18n = new I18n();
		this.titleService = titleView.getTitleService();
		this.newTitle = new Title();
		this.accountService = titleView.getAccountService();
		saveButton = new Button(i18n.BASIC_SAVE);
		saveButton.setEnabled(true);

		this.setCaption(i18n.TITLE_WINDOW_NEW_CAPTION);
		VerticalLayout subContent = new VerticalLayout();
		this.setContent(subContent);
		this.center();

		try {
			List<Account> accountList = accountService.findAll();

			txfValue = new TextField(i18n.TITLE_VALUE);
			subContent.addComponent(txfValue);

			txaComment = new TextArea(i18n.BASIC_COMMENT);
			subContent.addComponent(txaComment);

			subContent.addComponent(saveButton);

			saveButton.addClickListener(event -> {
				newTitle.setCreateBy(getSession().getAttribute(Account.class));
				newTitle.setListPrio(titleView.getMaxListPrio() + 1);
				newTitle.setTitleValue(txfValue.getValue());
				newTitle.setComment(txaComment.getValue());
				titleService.getTitleDAO().create(newTitle);
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
