package org.app.model.initialize;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.app.model.dao.AddressDAO;
import org.app.model.dao.PersonDAO;
import org.app.model.entity.Address;
import org.app.model.entity.Person;

@Singleton
@Startup
public class InitializePerson {

	@EJB
	PersonDAO personDAO;

	@EJB
	AddressDAO addressDAO;

	@PostConstruct
	void init() {
		if (personDAO.findAll().size() == 0) {

			Person person;
			Address address;
			

			/**
			 * Person: Hans-Georg Gloeckler
			 */
			person = new Person();
			person.setFirstName("Hans-Georg2");
			person.setLastName("Gloeckler");

			address = new Address();
			address.setStreet("St.-Afra-Weg 1");
			address.setZip("11111");
			address.setCity("Pfaffenhofen");
			address.prePersist();
			address.setPerson(person);
			person.getAddresses().add(address);

			address = new Address();
			address.setStreet("St.-Afra-Weg 2");
			address.setZip("22222");
			address.setCity("Pfaffenhofen");
			address.setPerson(person);
			address.prePersist();
			person.getAddresses().add(address);

			person.prePersist();
			personDAO.update(person);

			/**
			 * Person: Viktor Maier
			 */
			person = new Person();
			person.setFirstName("Viktor2");
			person.setLastName("Maier");

			address = new Address();
			address.setStreet("St.-Afra-Weg 3");
			address.setZip("33333");
			address.setCity("Pfaffenhofen");
			address.setPerson(person);
			address.prePersist();
			person.getAddresses().add(address);

			address = new Address();
			address.setStreet("St.-Afra-Weg 4");
			address.setZip("44444");
			address.setCity("Pfaffenhofen");
			address.setPerson(person);
			address.prePersist();
			person.getAddresses().add(address);
			
			person.prePersist();			
			personDAO.update(person);
			
//			personHelper = new PersonHelper();
//			personHelper.findAddresses(1);

//			personDAO.removeAddress(address);
			
//			System.out.println("====================");
//			System.out.println(person.getFirstName());
//			System.out.println(address.getStreet());
//			System.out.println("====================");
		}
	}
}
