package org.app.model.beans;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.app.model.dao.TitleDAO;
import org.app.model.entity.Title;

@Stateless
@Remote(TitleDAO.class)
public class TitleBean implements TitleDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Title create(Title title) {
		em.persist(title);
		return title;
	}

	@Override
	public Title update(Title title) {
		return em.merge(title);
	}

	@Override
	public void remove(int id) {
		Title toBeDeleted = findByID(id);
		em.remove(toBeDeleted);
	}

	@Override
	public List<Title> findAll() {
		return em.createNamedQuery(Title.QUERY_FIND_ALL, Title.class).getResultList();
	}

	@Override
	public Title findByID(int id) {
		return em.find(Title.class, id);
	}

	@Override
	public List<Title> findByPriority(int listPrio) {
		return em.createNamedQuery(Title.QUERY_FIND_BY_PRIORITY, Title.class).setParameter("listPrio", listPrio)
				.getResultList();
	}

}
