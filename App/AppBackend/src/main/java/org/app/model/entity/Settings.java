package org.app.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.app.model.entity.enums.DefaultLanguage;

@Entity
@Table(name = "SETTINGS")

public class Settings implements Serializable {

	private static final long serialVersionUID = 1L;


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
	@ManyToOne()
	@JoinColumn(name = "MODIFYBY_ID")
	private Account modifyBy;

	/**
	 * Einbinden: Enum Languages über ComboBox
	 */
	@Enumerated(EnumType.STRING)
	private DefaultLanguage defaultLanguage;
	
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


	public DefaultLanguage getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(DefaultLanguage defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
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
