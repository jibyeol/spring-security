package com.gstar.security.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import com.gstar.security.service.CookieService;

public class CookieAuthenticationFilter extends GenericFilterBean {
	
	UserDetailsService userDetailsService;
	CookieService cookieService;
	
	public CookieAuthenticationFilter(UserDetailsService userDetailsService, CookieService cookieService){
		this.userDetailsService = userDetailsService;
		this.cookieService = cookieService;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		SecurityContext context = loadSecurityContext(req);
		
		try {
			SecurityContextHolder.setContext(context);
			chain.doFilter(req, res);
		} finally {
			SecurityContextHolder.clearContext();
		}
	}

	private SecurityContext loadSecurityContext(HttpServletRequest req) {
		final String username = cookieService.extractUsername(req.getCookies());
		
		return username != null
				? new CookieSecurityContext(userDetailsService.loadUserByUsername(username))
				: SecurityContextHolder.createEmptyContext();
	}

}
