package org.app.model.dao;

import java.util.List;

import org.app.model.entity.Account;

public interface AccountDAO {

	public Account create(Account account);

	public Account update(Account account);

	public void remove(int id);

	public Account findByID(int id);

	public Account findByUserName(String username);

	public List<Account> findAll();

}