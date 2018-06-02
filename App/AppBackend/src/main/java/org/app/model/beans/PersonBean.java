package org.app.model.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.app.model.dao.AddressDAO;
import org.app.model.dao.PersonDAO;
import org.app.model.entity.Address;
import org.app.model.entity.Person;

@Stateless
@Remote(PersonDAO.class)
public class PersonBean implements PersonDAO {

	@PersistenceContext
	private EntityManager em;

	@EJB
	AddressDAO addressDAO;

	@Override
	public Person create(Person person) {
		em.persist(person);
		em.flush();

		return person;
	}

	@Override
	public Person update(Person person) {

		try {
			return em.merge(person);
		} finally {
			em.flush();
		}

	}

	@Override
	public void remove(int id) {
		Person toBeDeleted = findByID(id);
		em.remove(toBeDeleted);
		em.flush();
	}

	@Override
	public Person findByID(int id) {
		return em.find(Person.class, id);
	}

	@Override
	public List<Person> findAll() {
		return em.createNamedQuery(Person.QUERY_GET_ALL, Person.class).getResultList();
	}

	@Override
	public void addAddress(Address address, Person person) {
		address.setPerson(person);
		person.getAddresses().add(address);
		addressDAO.update(address);
	}

	@Override
	public List<Address> findAddresses(Person person) {
//		List<Address> addresses = new ArrayList<Address>();
//		for (Address address : person.getAddresses()){
//			address.setPerson(person);
//			addresses.add(address);
//		}
		return em.createNamedQuery(Address.QUERY_GET_BY_PERSONID, Address.class).setParameter("personID", person.getId()).getResultList();
	}

	public void removeAddress(Address toBeRemoved) {
//		System.out.println("====================");
//		System.out.println("Firstname of Person: " + toBeRemoved.getPerson().getFirstName());
//		System.out.println("ID of Address: " + toBeRemoved.getId());
//		System.out.println("Street of Address: " + toBeRemoved.getStreet());
//		System.out.println("====================");

		// toBeRemoved.setPerson(toBeRemoved.getPerson());
		// toBeRemoved.getPerson().getAddresses().remove(toBeRemoved);
		// addressDAO.remove(toBeRemoved.getId());

	}

	// public void removeAddress(Address toBeRemoved) {
	// Address merged = em.find(Address.class, toBeRemoved.getId());
	// toBeRemoved.setPerson(null);
	// em.remove(toBeRemoved);
	// em.flush();
	// toBeRemoved.getPerson();
	// em.remove(toBeRemoved);
	// person.getAddresses().remove(toBeRemoved);
	// addressDAO.update(toBeRemoved);
	// }
	
	

}
