package org.app.model.initialize;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.app.model.dao.AccountDAO;
import org.app.model.dao.TitleDAO;
import org.app.model.entity.Title;

@Singleton
@Startup
public class InitializeTitle {

	@EJB
	TitleDAO titleDAO;

	@EJB
	AccountDAO accountDAO;
	
	@PostConstruct
	void init() {
		if (titleDAO.findAll().size() == 0) {

			Title title = new Title();

			title.setCreateBy(accountDAO.findByID(1));;
			title.setListPrio(0);
			title.setTitleValue("Prof.");
			titleDAO.create(title);

			title.setCreateBy(accountDAO.findByID(1));;
			title.setListPrio(1);
			title.setTitleValue("PD");
			titleDAO.create(title);

			title.setCreateBy(accountDAO.findByID(1));;
			title.setListPrio(2);
			title.setTitleValue("Dr.");
			titleDAO.create(title);

			title.setCreateBy(accountDAO.findByID(2));;
			title.setListPrio(3);
			title.setTitleValue("Graf");
			titleDAO.create(title);

			title.setCreateBy(accountDAO.findByID(2));;
			title.setListPrio(4);
			title.setTitleValue("Freiherr");
			titleDAO.create(title);

		}
	}
}
