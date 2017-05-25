package com.gstar.security.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

public class CookieSecurityContext extends SecurityContextImpl {

	private static final long serialVersionUID = -5869019008099883979L;
	
	public CookieSecurityContext(UserDetails userDetails){
		setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword()));
	}

}
