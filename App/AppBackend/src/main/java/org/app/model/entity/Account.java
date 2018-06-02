package org.app.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.app.model.entity.enums.AccountGroup;

@Entity
@Table(name = "ACCOUNT")
@NamedQueries(
		{ 	@NamedQuery(name = Account.QUERY_FIND_ALL, query = "SELECT c FROM Account c"),
			@NamedQuery(name = Account.QUERY_FIND_BY_USERNAME, query = "SELECT c FROM Account c WHERE c.username =  :username") 
		})
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String QUERY_FIND_ALL = "Account.FindAll";
	public static final String QUERY_FIND_BY_USERNAME = "Account.FindByUserName";

	private Timestamp createAT;
	private Timestamp modifyAT;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true, nullable = false, length = 45)
	private String username;

	private String mailaddress;

	@Column(nullable = false, length = 60)
	private String password;

	private String comment;

	/**
	 * Einbinden: Enum AccountGroup über ComboBox
	 */
	@Enumerated(EnumType.STRING)
	private AccountGroup accountGroup;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AccountGroup getAccountGroup() {
		return accountGroup;
	}

	public void setAccountGroup(AccountGroup accountGroup) {
		this.accountGroup = accountGroup;
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
