package com.gstar.security.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.gstar.security.model.Account;

import lombok.Data;

@Data
public class UserDetailsImpl extends User {

	private static final long serialVersionUID = 182120913160070524L;
	
	private String nick;
	
	public UserDetailsImpl(Account account) {
		super(account.getUserId(), account.getPassword(), authorities(account));
		this.nick = account.getNick();
	}
	
	private static Collection<? extends GrantedAuthority> authorities(Account account) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(account.getRole()));
		return authorities;
	}

}
