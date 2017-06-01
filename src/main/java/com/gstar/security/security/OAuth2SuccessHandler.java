package com.gstar.security.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.gstar.security.repository.AccountRepository;

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	final String type;
	final AccountRepository accountRepository;
	
	public OAuth2SuccessHandler(String type, AccountRepository accountRepositoty){
		this.type = type;
		this.accountRepository = accountRepositoty;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
	}

}
