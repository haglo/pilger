package org.app.controler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

import org.app.model.dao.PersonDAO;
import org.app.model.entity.Account;
import org.app.model.entity.Address;
import org.app.model.entity.Person;

/*
 * Managed Bean
 * MVC: Controler-Part
 * Repräsentiert den kpmpletten Lebenszyklus eines Personendatensatz &
 * Bietet zusätzliche Funktionalität für diesen Personendatensatz an
 */
@SuppressWarnings("serial")
@RequestScoped
public class PersonService implements Serializable {

	@EJB
	private PersonDAO personDAO;

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

	public PersonDAO getPersonDAO() {
		return personDAO;
	}

}
