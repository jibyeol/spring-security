package com.gstar.security.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Account implements Serializable {

	private static final long serialVersionUID = -2763708357702354291L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique=true)
	private String userId;
	private String password;
	private String nick;
	
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="member_id")
	List<AccountAuthority> roles;
	
	String mail;
	String auth;
	String authType;
	String authId;

	public void addRole(String role) {
		if(roles == null){
			roles = new ArrayList<AccountAuthority>();
		}
		roles.add(new AccountAuthority(id, role));
	}

	public Account(String userId, String nick, Collection<? extends GrantedAuthority> authorities, String authType,
			String authId, boolean noPrefix) {
		this.userId = userId;
		this.nick = nick;
		for(GrantedAuthority role : authorities){
			addRole("ROLE_" + role.getAuthority());
		}
		this.authType = authType;
		this.authId = authId;
	}
}
