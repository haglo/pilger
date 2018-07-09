package org.app.model.dao;

import java.util.List;

import org.app.model.entity.Address;

public interface AddressDAO {

	public Address create(Address address);

	public Address update(Address address);

	public void remove(Integer id);

	public Address findByID(Integer id);

	public List<Address> findAll();
}