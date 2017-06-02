package com.gstar.security.security;

import java.util.List;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.gstar.security.model.Account;
import com.gstar.security.repository.AccountRepository;

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	final String type;
	final AccountRepository accountRepository;
	
	public OAuth2SuccessHandler(String type, AccountRepository accountRepositoty){
		this.type = type;
		this.accountRepository = accountRepositoty;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
			Authentication auth) throws IOException, ServletException {
		String fbId = auth.getName();
		Account account = accountRepository.findByAuthTypeAndAuthId(type, fbId);
		
		if (account != null) {
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken
			(
				new UserDetailsImpl(account), null, UserDetailsImpl.authorities(account))
			);
			res.sendRedirect("/");
		} else {
			account = new Account(fbId, "fb_"+fbId, auth.getAuthorities(), "facebook", fbId, false);
			accountRepository.save(account);
		}
	}
}
