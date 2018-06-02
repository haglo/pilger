package org.app.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NamedQuery(name = Communication.QUERY_GET_ALL, query = "SELECT c FROM Communication c")
public class Communication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String QUERY_GET_ALL = "Communication.GetAll";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@Size(max = 100)
	private String value;

	@NotNull
	@Size(max = 20)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMMUNICATIONTYPE_ID")
	private CommunicationType communicationType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KIND_ID")
	private Kind kind;

	private String comment;
	
	@ManyToOne
	private Person person;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
