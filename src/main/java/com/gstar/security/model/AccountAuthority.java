package com.gstar.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class AccountAuthority implements Serializable {

	private static final long serialVersionUID = 726192372896037304L;
	
	@Id
	@Column(name="account_id")
	private Long id;
	@Id
	private String role;

	public AccountAuthority(){}
	public AccountAuthority(Long id, String role) {
		this.id = id;
		this.role = role;
	}
	
}
