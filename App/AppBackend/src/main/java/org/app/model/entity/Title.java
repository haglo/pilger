package org.app.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TITLE")
@NamedQueries({ @NamedQuery(name = Title.QUERY_FIND_ALL, query = "SELECT c FROM Title c"),
		@NamedQuery(name = Title.QUERY_FIND_BY_PRIORITY, query = "SELECT c FROM Title c WHERE c.listPrio =  :listPrio") })
public class Title implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String QUERY_FIND_ALL = "Title.FindAll";
	public static final String QUERY_FIND_BY_PRIORITY = "Title.FindByPriority";

	private Timestamp createAT;
	private Timestamp modifyAT;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * Einbinden: Entity Account über ComboBox
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CREATEBY_ID")
	private Account createBy;

	/**
	 * Einbinden: Entity Account über ComboBox
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MODIFYBY_ID")
	private Account modifyBy;

	@NotNull
	private int listPrio;

	@Column(unique = true, nullable = false)
	private String titleValue;

	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getListPrio() {
		return listPrio;
	}

	public void setListPrio(int listPrio) {
		this.listPrio = listPrio;
	}

	public String getTitleValue() {
		return titleValue;
	}

	public void setTitleValue(String title) {
		this.titleValue = title;
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
