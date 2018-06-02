package org.app.model.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Entity implementation class for Entity: Address
 *
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name = Address.QUERY_GET_ALL, query = "SELECT c FROM Address c"), 
	@NamedQuery(name = Address.QUERY_GET_BY_PERSONID, query = "SELECT a FROM Address a WHERE a.person.id = :personID")	
})
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String QUERY_GET_ALL = "Address.GetAll";

	public static final String QUERY_GET_BY_PERSONID = "Address.GetByPersonID";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String street;

	private String zip;

	private String city;

	/**
	 * Ohne Person darf keine Adresse angelegt werden
	 * Kontrolliert die Verknüpfung
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH} , optional = false)
	private Person person;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "app.Address[id=" + id + "]";
    }


}
