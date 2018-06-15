package org.app.controler;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.app.model.dao.AccountDAO;
import org.app.model.entity.Account;

/*
 * Managed Bean
 * In MVC the Controler-Part
 */
@RequestScoped
public class AccountService {

	@EJB
	private AccountDAO accountDAO;

	private Account account;
	private EncryptService encryptService;
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

	public void remove(int id) {
		accountDAO.remove(id);
	}

	public void create(Account account) {
		account.setPassword(encryptService.encodePassword(account.getPassword()));
		accountDAO.create(account);
	}

	public void update(Account account) {
		accountDAO.update(account);
	}

	public List<Account> findAll() {
		List<Account> list = accountDAO.findAll();
		return list;
	}

	public Account findByID(int id) {
		return account = accountDAO.findByID(id);
	}

	public Account findByUserName(String username) {
		return account = accountDAO.findByUserName(username);
	}


}
