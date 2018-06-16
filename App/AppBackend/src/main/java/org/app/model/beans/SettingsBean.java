package org.app.model.beans;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.app.model.dao.SettingsDAO;
import org.app.model.entity.Settings;

@Stateless
@Remote(SettingsDAO.class)
public class SettingsBean implements SettingsDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Settings update(Settings settings) {
		return em.merge(settings);
	}

	@Override
	public List<Settings> findAll() {
		return em.createNamedQuery(Settings.QUERY_FIND_ALL, Settings.class).getResultList();
	}




}
