package org.app.controler;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import org.app.model.dao.TitleDAO;

/**
 * Managed Bean
 */
@RequestScoped
public class TitleService {

	@EJB
	private TitleDAO titleDAO;

	private boolean isEditing = false;

	public boolean getEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	public void toggleEditing() {
		this.isEditing = !this.isEditing;
	}

	public TitleDAO getTitleDAO() {
		return titleDAO;
	}

}
