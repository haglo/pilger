package org.app.view.masterdetail.views;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.app.controler.AccountService;
import org.app.controler.TitleService;
import org.app.helper.Constants;
import org.app.helper.I18nManager;
import org.app.helper.Translatable;
import org.app.model.entity.Account;
import org.app.model.entity.Title;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@CDIView(Constants.TITLE_VIEW)
public class TitleView extends VerticalLayout implements View, Translatable {

	private ListDataProvider<Title> dataProvider;
	private Title entry;
	private Title selectedTitle;
	private Title prevEntry;
	private Title nextEntry;
	private Set<Title> selectedTitles;

	private TextField newValueField = new TextField();
	private TextField newCommentField = new TextField();

	private TitleService titleService;
	private AccountService accountService;
	private Grid<Title> grid;

	public TitleView(TitleService tService, AccountService aService) {
		this.titleService = tService;
		this.accountService = aService;
		this.grid = new Grid<Title>();

		final List<Title> list = titleService.getTitleDAO().findAll();
		selectedTitles = new HashSet<>();
		dataProvider = DataProvider.ofCollection(list);
		dataProvider.setSortOrder(Title::getListPrio, SortDirection.ASCENDING);

		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addSelectionListener(event -> {
			selectedTitles = event.getAllSelectedItems();
		});
		grid.getEditor().setEnabled(true);
		grid.getEditor().addSaveListener(event -> {
			selectedTitle = event.getBean();
			updateRow(selectedTitle);
		});

		grid.setDataProvider(dataProvider);
		grid.addColumn(Title::getListPrio).setCaption(LISTPRIO).setId(LISTPRIO);
		grid.addColumn(title -> title.getTitleValue()).setCaption(VALUE).setEditorComponent(newValueField,
				Title::setTitleValue).setId(VALUE);;
		grid.addColumn(Title::getComment).setCaption(COMMENT).setEditorComponent(newCommentField, Title::setComment).setId(COMMENT);;

		Button edit = new Button("Edit");
		edit.setIcon(VaadinIcons.EDIT);

		Button add = new Button("+");
		add.addClickListener(event -> addRow());

		Button delete = new Button("-");
		delete.addClickListener(event -> deleteRow());

		Button up = new Button();
		up.setIcon(VaadinIcons.ARROW_UP);
		up.addClickListener(event -> upRow());

		Button top = new Button();
		top.setIcon(VaadinIcons.UPLOAD_ALT);
		top.addClickListener(event -> topRow());

		Button down = new Button();
		down.setIcon(VaadinIcons.ARROW_DOWN);
		down.addClickListener(event -> downRow());

		Button bottom = new Button();
		bottom.setIcon(VaadinIcons.DOWNLOAD_ALT);
		bottom.addClickListener(event -> bottomRow());

		Button detail = new Button("detailsView", ev -> {
			if (onlyOneSelected(selectedTitles)) {
				for (Title entry : selectedTitles) {
					selectedTitle = entry;
					getUI().addWindow(new TitleDetailView(this, selectedTitle));
				}
				refreshGrid();
			}
		});
		detail.setIcon(VaadinIcons.PENCIL);

		HorizontalLayout tb = new HorizontalLayout(add, delete, up, top, down, bottom, detail);
		// tb.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		// tb.addStyleName("grid-nav-bar");

		addStyleName("pilger-grid");
		addComponent(grid);
		addComponent(tb);
	}

	private void addRow() {
		entry = new Title();
		List<Title> list = titleService.getTitleDAO().findAll();

		entry.setCreateBy(getSession().getAttribute(Account.class));
		entry.setListPrio(getMaxListPrio(titleService) + 1);
		entry.setTitleValue("");
		list.add(entry);
		grid.setItems(list);

		grid.getEditor().editRow(list.size() - 1);
		newValueField.focus();

		titleService.getTitleDAO().create(entry);
		refreshGrid();
	}

	private void deleteRow() {
		if (selectedTitles.size() == 0) {
			Notification.show("Nothing selected");
			return;
		}
		for (Title entry : selectedTitles) {
			titleService.getTitleDAO().remove(entry.getId());
		}
		refreshGrid();
	}

	private void upRow() {
		prevEntry = new Title();

		if (!onlyOneSelected(selectedTitles)) {
			return;
		}

		for (Title entry : selectedTitles) {
			selectedTitle = entry;
			if (selectedTitle.getListPrio() == 0) {
				Notification.show("Item is on the Top");
				return;
			}
			prevEntry = (Title) titleService.getTitleDAO().findByPriority(entry.getListPrio() - 1).get(0);
			selectedTitle.setListPrio(selectedTitle.getListPrio() - 1);
			prevEntry.setListPrio(prevEntry.getListPrio() + 1);
			titleService.getTitleDAO().update(selectedTitle);
			titleService.getTitleDAO().update(prevEntry);
		}
		refreshGrid();
	}

	private void topRow() {
		if (!onlyOneSelected(selectedTitles)) {
			return;
		}

		for (Title entry : selectedTitles) {
			selectedTitle = entry;
		}

		if (selectedTitle.getListPrio() == 0) {
			Notification.show("Item is on the Top");
			return;
		}

		List<Title> list = titleService.getTitleDAO().findAll();
		for (Title tmp : list) {
			if (tmp.getListPrio() < selectedTitle.getListPrio()) {
				tmp.setListPrio(tmp.getListPrio() + 1);
			}
			titleService.getTitleDAO().update(tmp);
		}
		selectedTitle.setListPrio(0);
		titleService.getTitleDAO().update(selectedTitle);
		refreshGrid();
	}

	private void downRow() {
		nextEntry = new Title();

		if (!onlyOneSelected(selectedTitles)) {
			return;
		}

		for (Title entry : selectedTitles) {
			selectedTitle = entry;
		}

		if (selectedTitle.getListPrio() == getMaxListPrio(titleService)) {
			Notification.show("Item is on the Bottom");
			return;
		}
		nextEntry = (Title) titleService.getTitleDAO().findByPriority(selectedTitle.getListPrio() + 1).get(0);
		selectedTitle.setListPrio(selectedTitle.getListPrio() + 1);
		nextEntry.setListPrio(nextEntry.getListPrio() - 1);
		titleService.getTitleDAO().update(selectedTitle);
		titleService.getTitleDAO().update(nextEntry);
		refreshGrid();
	}

	private void bottomRow() {
		int maxListPrio = 0;

		if (!onlyOneSelected(selectedTitles)) {
			return;
		}

		for (Title entry : selectedTitles) {
			selectedTitle = entry;
		}

		maxListPrio = getMaxListPrio(titleService);
		if (selectedTitle.getListPrio() == maxListPrio) {
			Notification.show("Item is on the Bottom");
			return;
		}

		List<Title> list = titleService.getTitleDAO().findAll();
		for (Title tmp : list) {
			if (tmp.getListPrio() > selectedTitle.getListPrio()) {
				tmp.setListPrio(tmp.getListPrio() - 1);
			}
			titleService.getTitleDAO().update(tmp);
		}
		selectedTitle.setListPrio(maxListPrio);
		titleService.getTitleDAO().update(selectedTitle);
		refreshGrid();
	}

	public void updateRow(Title title) {
		titleService.getTitleDAO().update(title);
		refreshGrid();
	}

	public void refreshGrid() {
		List<Title> list = titleService.getTitleDAO().findAll();
		grid.sort(LISTPRIO, SortDirection.ASCENDING);
		grid.setItems(list);
	}
	
	private int getMaxListPrio(TitleService service) {
		int maxListPrio;
		List<Title> list = service.getTitleDAO().findAll();
		maxListPrio = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getListPrio() > maxListPrio) {
				maxListPrio = list.get(i).getListPrio();
			}
		}
		return maxListPrio;
	}

	private boolean onlyOneSelected(Set<Title> selected) {
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
	
	@Override
	public void updateMessageStrings() {
		final I18nManager i18n = I18nManager.getInstance();
		this.setCaption(i18n.getMessage("title.windowcaption"));
		grid.getColumn(LISTPRIO).setCaption(i18n.getMessage("basic.listprio"));
		grid.getColumn(VALUE).setCaption(i18n.getMessage("person.lastname"));
		grid.getColumn(COMMENT).setCaption(i18n.getMessage("title.value"));
	}

	public TitleService getTitleService() {
		return titleService;
	}
	
	public AccountService getAccountService() {
		return accountService;
	}


}