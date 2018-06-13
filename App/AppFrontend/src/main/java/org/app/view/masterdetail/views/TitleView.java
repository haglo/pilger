package org.app.view.masterdetail.views;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.app.controler.AccountService;
import org.app.controler.TitleService;
import org.app.model.entity.Account;
import org.app.model.entity.Title;
import org.app.view.MainUI;
import org.app.view.TopMainMenu;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CheckBoxGroup;
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
@CDIView(MainUI.TITLE_VIEW)
public class TitleView extends VerticalLayout implements View {
	
	final private String listPriority = "List Priority";
	final private String value = "Titel";
	final private String comment = "Kommentar";

	private ListDataProvider<Title> dataProvider;
	private Title entry;
	private Title selectedEntry;
	private Title prevEntry;
	private Title nextEntry;
	private Set<Title> selected;

	private TextField newValueField = new TextField();
	private TextField newCommentField = new TextField();
	
	private Account selectedCreateBy;
	private Account selectedModifyBy;

	
	public TitleView(TitleService service, AccountService accountService) {
		List<Account> accountList = accountService.findAll();			

		final List<Title> list = service.getTitleDAO().findAll();
		selected = new HashSet<>();
		dataProvider = DataProvider.ofCollection(list);
		dataProvider.setSortOrder(Title::getListPrio, SortDirection.ASCENDING);

		Grid<Title> grid = new Grid<Title>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addSelectionListener(event -> {
			selected = event.getAllSelectedItems();
		});
		grid.getEditor().setEnabled(true);
		grid.getEditor().addSaveListener(event -> {
			entry = event.getBean();
			updateRow(service, entry, grid);
		});

		grid.setDataProvider(dataProvider);
		grid.addColumn(Title::getListPrio).setCaption(listPriority).setId(listPriority);
		grid.addColumn(title -> title.getTitleValue()).setCaption(value).setEditorComponent(newValueField,
				Title::setTitleValue);
		grid.addColumn(Title::getComment).setCaption(comment).setEditorComponent(newCommentField,
				Title::setComment);

		Button add = new Button("+");
		add.addClickListener(event -> addRow(service, grid));

		Button delete = new Button("-");
		delete.addClickListener(event -> deleteRow(service, selected, grid));

		Button up = new Button("up");
		up.addClickListener(event -> upRow(service, selected, grid));

		Button top = new Button("top");
		top.addClickListener(event -> topRow(service, selected, grid));

		Button down = new Button("down");
		down.addClickListener(event -> downRow(service, selected, grid));

		Button bottom = new Button("bottom");
		bottom.addClickListener(event -> bottomRow(service, selected, grid));

		Button details = new Button("details");
		details.addClickListener(event -> showDetails(service, selected, grid, accountList));

		HorizontalLayout tb = new HorizontalLayout(add, delete, up, top, down, bottom, details);
		
		addComponent(grid);
		addComponent(tb);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}

	private void addRow(TitleService service, Grid<Title> grid) {
		entry = new Title();
		List<Title> list = service.getTitleDAO().findAll();

		entry.setCreateBy(getSession().getAttribute(Account.class));
		entry.setListPrio(getMaxListPrio(service) + 1);
		entry.setTitleValue("");
		list.add(entry);
		grid.setItems(list);

		grid.getEditor().editRow(list.size() - 1);
		newValueField.focus();

		service.getTitleDAO().create(entry);
		refreshGrid(grid, service);
	}

	private void deleteRow(TitleService service, Set<Title> selected, Grid<Title> grid) {
		if (selected.size() == 0) {
			Notification.show("Nothing selected");
			return;
		}
		for (Title entry : selected) {
			service.getTitleDAO().remove(entry.getId());
		}
		refreshGrid(grid, service);
	}

	private void upRow(TitleService service, Set<Title> selected, Grid<Title> grid) {
		selectedEntry = new Title();
		prevEntry = new Title();

		if (!onlyOneSelected(selected)) {
			return;
		}

		for (Title entry : selected) {
			selectedEntry = entry;
			if (selectedEntry.getListPrio() == 0) {
				Notification.show("Item is on the Top");
				return;
			}
			prevEntry = (Title) service.getTitleDAO().findByPriority(entry.getListPrio() - 1).get(0);
			selectedEntry.setListPrio(selectedEntry.getListPrio() - 1);
			prevEntry.setListPrio(prevEntry.getListPrio() + 1);
			service.getTitleDAO().update(selectedEntry);
			service.getTitleDAO().update(prevEntry);
		}
		refreshGrid(grid, service);
	}

	private void topRow(TitleService service, Set<Title> selected, Grid<Title> grid) {
		selectedEntry = new Title();

		if (!onlyOneSelected(selected)) {
			return;
		}

		for (Title entry : selected) {
			selectedEntry = entry;
		}

		if (selectedEntry.getListPrio() == 0) {
			Notification.show("Item is on the Top");
			return;
		}

		List<Title> list = service.getTitleDAO().findAll();
		for (Title tmp : list) {
			if (tmp.getListPrio() < selectedEntry.getListPrio()) {
				tmp.setListPrio(tmp.getListPrio() + 1);
			}
			service.getTitleDAO().update(tmp);
		}
		selectedEntry.setListPrio(0);
		service.getTitleDAO().update(selectedEntry);
		refreshGrid(grid, service);
	}

	private void downRow(TitleService service, Set<Title> selected, Grid<Title> grid) {
		selectedEntry = new Title();
		nextEntry = new Title();

		if (!onlyOneSelected(selected)) {
			return;
		}

		for (Title entry : selected) {
			selectedEntry = entry;
		}

		if (selectedEntry.getListPrio() == getMaxListPrio(service)) {
			Notification.show("Item is on the Bottom");
			return;
		}
		nextEntry = (Title) service.getTitleDAO().findByPriority(selectedEntry.getListPrio() + 1).get(0);
		selectedEntry.setListPrio(selectedEntry.getListPrio() + 1);
		nextEntry.setListPrio(nextEntry.getListPrio() - 1);
		service.getTitleDAO().update(selectedEntry);
		service.getTitleDAO().update(nextEntry);
		refreshGrid(grid, service);
	}

	private void bottomRow(TitleService service, Set<Title> selected, Grid<Title> grid) {
		selectedEntry = new Title();
		int maxListPrio = 0;

		if (!onlyOneSelected(selected)) {
			return;
		}

		for (Title entry : selected) {
			selectedEntry = entry;
		}

		maxListPrio = getMaxListPrio(service);
		if (selectedEntry.getListPrio() == maxListPrio) {
			Notification.show("Item is on the Bottom");
			return;
		}

		List<Title> list = service.getTitleDAO().findAll();
		for (Title tmp : list) {
			if (tmp.getListPrio() > selectedEntry.getListPrio()) {
				tmp.setListPrio(tmp.getListPrio() - 1);
			}
			service.getTitleDAO().update(tmp);
		}
		selectedEntry.setListPrio(maxListPrio);
		service.getTitleDAO().update(selectedEntry);
		refreshGrid(grid, service);
	}

	private void updateRow(TitleService service, Title title, Grid<Title> grid) {
		title.setModifyBy(getSession().getAttribute(Account.class));
		service.getTitleDAO().update(title);
		refreshGrid(grid, service);
	}

	private void showDetails(TitleService service, Set<Title> selected, Grid<Title> grid, List<Account> accountList) {
		final Button save = new Button("Save");
		selectedEntry = new Title();

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

			for (Title entry : selected) {
				selectedEntry = entry;
			}

			TextField txfID = new TextField("ID", "" + selectedEntry.getId());
			subContent.addComponent(txfID);
			
			
			ComboBox<Account> cbxCreatBy = new ComboBox<>("Create By");
			accountList.sort(Comparator.comparing(Account::getAccountGroup));
			cbxCreatBy.setPageLength(8);
			cbxCreatBy.setItemCaptionGenerator(Account::getUsername);
			cbxCreatBy.setItems(accountList);
			cbxCreatBy.setSelectedItem(selectedEntry.getCreateBy());
			subContent.addComponent(cbxCreatBy);
			cbxCreatBy.addValueChangeListener(event -> {
				selectedCreateBy = event.getValue();
			});

			TextField txfCreateAt = new TextField("Create At", "" + selectedEntry.getCreateAt());
			subContent.addComponent(txfCreateAt);

			ComboBox<Account> cbxModifyBy = new ComboBox<>("Modify By");
			accountList.sort(Comparator.comparing(Account::getAccountGroup));
			cbxModifyBy.setPageLength(8);
			cbxModifyBy.setItemCaptionGenerator(Account::getUsername);
			cbxModifyBy.setItems(accountList);
			cbxModifyBy.setSelectedItem(selectedEntry.getCreateBy());
			subContent.addComponent(cbxModifyBy);
			cbxModifyBy.addValueChangeListener(event -> {
				selectedModifyBy = event.getValue();
			});

			TextField txfModifyAt = new TextField("Modify At", "" + selectedEntry.getModifyAt());
			subContent.addComponent(txfModifyAt);

			TextField txfListPrio = new TextField(listPriority, "" + selectedEntry.getListPrio());
			subContent.addComponent(txfListPrio);

			TextField txfValue = new TextField(value, "" + selectedEntry.getTitleValue());
			subContent.addComponent(txfValue);

			TextArea txaComment = new TextArea(comment, "" + selectedEntry.getComment());
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
				selectedEntry.setCreateBy(selectedCreateBy);
				selectedEntry.setModifyBy(selectedModifyBy);
				selectedEntry.setListPrio(Integer.valueOf(txfListPrio.getValue()));
				selectedEntry.setTitleValue(txfValue.getValue());
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

	private void refreshGrid(Grid<Title> grid, TitleService service) {
		List<Title> list = service.getTitleDAO().findAll();
		grid.sort(listPriority, SortDirection.ASCENDING);
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
}