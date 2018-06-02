package org.app.model.beans;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.app.model.dao.AccountDAO;
import org.app.model.entity.Account;

@Stateless
@Remote(AccountDAO.class)
public class AccountBean implements AccountDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Account create(Account account) {
		em.persist(account);

		return account;
	}

	@Override
	public Account update(Account account) {
		return em.merge(account);
	}

	@Override
	public void remove(int id) {
		Account toBeDeleted = findByID(id);
		em.remove(toBeDeleted);
	}

	@Override
	public List<Account> findAll() {
		return em.createNamedQuery(Account.QUERY_FIND_ALL, Account.class).getResultList();
	}

	@Override
	public Account findByID(int id) {
		return em.find(Account.class, id);
	}

	@Override
	public Account findByUserName(String username) {
		return em.createNamedQuery(Account.QUERY_FIND_BY_USERNAME, Account.class ).setParameter("username", username).getSingleResult();
	}


}
