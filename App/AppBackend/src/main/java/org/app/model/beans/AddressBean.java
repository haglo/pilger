package org.app.model.beans;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.app.model.dao.AddressDAO;
import org.app.model.entity.Address;

@Stateless
@Remote(AddressDAO.class)
public class AddressBean implements AddressDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Address create(Address address) {
		em.persist(address);

		return address;
	}

	@Override
	public Address update(Address address) {
		return em.merge(address);
	}

	@Override
	public void remove(Integer id) {
		Address toBeDeleted = findByID(id);
		em.remove(toBeDeleted);
	}

	@Override
	public Address findByID(Integer id) {
		return em.find(Address.class, id);
	}

	@Override
	public List<Address> findAll() {
		return em.createNamedQuery(Address.QUERY_GET_ALL, Address.class).getResultList();
	}

}
