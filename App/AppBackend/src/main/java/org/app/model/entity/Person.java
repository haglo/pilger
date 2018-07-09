package org.app.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({ @NamedQuery(name = Person.QUERY_GET_ALL, query = "SELECT c FROM Person c") })

public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String QUERY_GET_ALL = "Person.GetAll";

	private Timestamp createAT;
	private Timestamp modifyAT;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String uuid;

	/**
	 * Einbinden: Entity Account über ComboBox
	 */
	@ManyToOne()
	@JoinColumn(name = "CREATEBY_ID")
	private Account createBy;

	/**
	 * Einbinden: Entity Account über ComboBox
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MODIFYBY_ID")
	private Account modifyBy;
	
	@NotNull
	@Size(min = 0, max = 50)
	private String firstName;

	@NotNull
	@Size(min = 0, max = 100)
	private String lastName;

	private String comment;

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Address> addresses = new HashSet<Address>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public void addAddress(Address address) {
		addresses.add(address);
		address.setPerson(this);
	}

	public void removeAddress(Address address) {
		addresses.remove(address);
		address.setPerson(null);
	}
	
	@PrePersist
	protected void onCreate() {
		createAT = new Timestamp(System.currentTimeMillis());
	}

	@PreUpdate
	protected void onUpdate() {
		modifyAT = new Timestamp(System.currentTimeMillis());
	}

	public Timestamp getCreateAt() {
		return createAT;
	}

	public Timestamp getModifyAt() {
		return modifyAT;
	}

	public Account getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Account createBy) {
		this.createBy = createBy;
	}

	public Account getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(Account modifyBy) {
		this.modifyBy = modifyBy;
	}
	
	public void prePersist() {
		if (getUuid() == null) {
			setUuid(UUID.randomUUID().toString());
		}
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (uuid != null ? uuid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Person)) {
			return false;
		}
		Person other = (Person) object;
		if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Person[uuid=" + uuid + "]";
	}

}
