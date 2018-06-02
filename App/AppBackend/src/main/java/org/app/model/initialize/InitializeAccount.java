package org.app.model.initialize;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.app.model.dao.AccountDAO;
import org.app.model.entity.Account;
import org.app.model.entity.enums.AccountGroup;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Singleton
@Startup
public class InitializeAccount {

	@EJB
	AccountDAO accountDAO;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	private String encode(String password) {
		return encoder.encode(password);
	}

	@PostConstruct
	void init() {
		if (accountDAO.findAll().size() == 0) {

			Account account = new Account();

			account.setUsername("Admin1");
			account.setMailaddress("admin1@google.de");
			account.setPassword(encode("secret1"));
			account.setAccountGroup(AccountGroup.System);
			accountDAO.create(account);

			account.setUsername("Admin2");
			account.setMailaddress("admin2@google.de");
			account.setPassword(encode("secret2"));
			account.setAccountGroup(AccountGroup.PowerUser);
			accountDAO.create(account);

			account.setUsername("Admin3");
			account.setMailaddress("admin3@google.de");
			account.setPassword(encode("secret3"));
			account.setAccountGroup(AccountGroup.Administrators);
			accountDAO.create(account);

			account.setUsername("Admin4");
			account.setMailaddress("admin4@google.de");
			account.setPassword(encode("secret4"));
			account.setAccountGroup(AccountGroup.Users);
			accountDAO.create(account);
		}
	}
}
