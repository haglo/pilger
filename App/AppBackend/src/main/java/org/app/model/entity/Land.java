package org.app.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "LAND")
@SuppressWarnings("all")
@NamedQueries(
		{ @NamedQuery(name = Land.QUERY_GET_ALL, query = "SELECT c FROM Land c"),
		  @NamedQuery(name = Land.QUERY_GET_BY_PRIORITY, query = "SELECT c FROM Land c WHERE c.listPrio =  :listPrio") 
		}
)
public class Land implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String QUERY_GET_ALL = "Land.GetAll";
	public static final String QUERY_GET_BY_PRIORITY = "Land.GetByPriority";

	private Timestamp createAT;
	private Timestamp modifyAT;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	private int listPrio;

	@Column(name = "value", unique = true, nullable = false)
	private String value;

	@Size(max = 5)
	@Column(name = "isoCode", unique = true, nullable = true)
	private String isoCode;

	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getListPrio() {
		return listPrio;
	}

	public void setListPrio(int listPrio) {
		this.listPrio = listPrio;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String land) {
		this.value = land;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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


}
