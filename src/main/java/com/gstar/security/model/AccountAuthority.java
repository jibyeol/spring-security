package com.gstar.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class AccountAuthority implements Serializable {

	private static final long serialVersionUID = 726192372896037304L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="account_id")
	private int accountId;
	private String role;

	public AccountAuthority(){}
	public AccountAuthority(Long id, String role) {
		this.id = id;
		this.role = role;
	}
	
}
