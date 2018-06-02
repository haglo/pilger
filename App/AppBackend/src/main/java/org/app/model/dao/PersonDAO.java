package org.app.model.dao;

import java.util.List;

import org.app.model.entity.Address;
import org.app.model.entity.Person;

public interface PersonDAO {

	public Person create(Person person);

	public Person update(Person person);

	public void remove(int id);

	public Person findByID(int id);

	public List<Person> findAll();

	public void addAddress(Address address, Person person);

	public List<Address> findAddresses(Person person);

	public void removeAddress(Address toBeRemoved);

}